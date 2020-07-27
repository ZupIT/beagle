/*
 * Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.zup.beagle.cache

import com.google.common.hash.Hashing
import com.google.common.net.HttpHeaders
import java.net.HttpURLConnection
import java.nio.charset.Charset
import java.time.Duration

class BeagleCacheHandler(properties: BeagleCacheProperties) {
    companion object {
        const val CACHE_HEADER = "beagle-hash"
        internal const val MAX_AGE_HEADER = "max-age"
        internal val DEFAULT_TTL = Duration.ofSeconds(30L)
    }

    constructor(
        excludeEndpoints: List<String> = listOf(),
        includeEndpoints: List<String> = listOf(),
        ttl: Map<String, Duration> = mapOf()
    ) : this(
        object : BeagleCacheProperties {
            override val exclude: List<String> = excludeEndpoints
            override val include: List<String> = includeEndpoints
            override val ttl: Map<String, Duration> = ttl
        }
    )

    private val endpointHashMap = mutableMapOf<String, String>()
    private val excludePatterns = properties.exclude.filter { it.isNotBlank() }.map(::Regex)
    private val includePatterns = properties.include.filter { it.isNotBlank() }.map(::Regex)
    private val ttl = properties.ttl.filterKeys { it.isNotBlank() }

    private fun generateHashForJson(json: String) =
        Hashing.sha512().hashString(json, Charset.defaultCharset()).toString()

    private fun getCacheKey(endpoint: String, currentPlatform: String?) =
        currentPlatform?.plus("_")?.plus(endpoint) ?: endpoint

    internal fun isEndpointExcluded(endpoint: String) =
        this.includePatterns.none { it matches endpoint } || this.excludePatterns.any { it matches endpoint }

    internal fun isHashUpToDate(endpoint: String, currentPlatform: String?, hash: String) =
        this.endpointHashMap[this.getCacheKey(endpoint, currentPlatform)] == hash

    internal fun generateAndAddHash(endpoint: String, currentPlatform: String?, json: String) =
        this.endpointHashMap.computeIfAbsent(this.getCacheKey(endpoint, currentPlatform)) {
            this.generateHashForJson(json)
        }

    @Deprecated("Please use the new signature.")
    fun <T> handleCache(
        endpoint: String,
        receivedHash: String?,
        currentPlatform: String?,
        initialResponse: T,
        restHandler: RestCacheHandler<T>
    ) = this.handleCache(
        endpoint,
        receivedHash,
        currentPlatform,
        object : HttpCacheHandler<T> {
            override fun createResponseFromController() = restHandler.callController(initialResponse)

            override fun createResponse(status: Int) = restHandler.addStatus(initialResponse, status)

            override fun getBody(response: T) = restHandler.getBody(response)

            override fun addHeader(response: T, key: String, value: String) =
                if (key == CACHE_HEADER) restHandler.addHashHeader(response, value) else response
        }
    )

    fun <T> handleCache(
        endpoint: String,
        receivedHash: String?,
        currentPlatform: String?,
        handler: HttpCacheHandler<T>
    ) = when {
        this.isEndpointExcluded(endpoint) -> handler.createResponseFromController()
        receivedHash != null && this.isHashUpToDate(
            endpoint = endpoint,
            currentPlatform = currentPlatform,
            hash = receivedHash
        ) ->
            handler.createResponse(HttpURLConnection.HTTP_NOT_MODIFIED)
                .let { handler.addHeader(it, CACHE_HEADER, receivedHash) }
                .let { this.addTtlHeader(it, endpoint, handler) }
        else ->
            handler.createResponseFromController()
                .let {
                    handler.addHeader(
                        it,
                        CACHE_HEADER,
                        this.generateAndAddHash(
                            endpoint = endpoint,
                            currentPlatform = currentPlatform,
                            json = handler.getBody(it)
                        )
                    )
                }.let { this.addTtlHeader(it, endpoint, handler) }
    }

    private fun <T> addTtlHeader(response: T, endpoint: String, handler: HttpCacheHandler<T>) = handler.addHeader(
        response = response,
        key = HttpHeaders.CACHE_CONTROL,
        value = "$MAX_AGE_HEADER=${(this.ttl[endpoint] ?: DEFAULT_TTL).seconds}"
    )
}