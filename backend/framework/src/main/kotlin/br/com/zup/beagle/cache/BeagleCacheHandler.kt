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

abstract class BeagleCacheHandler(properties: BeagleCacheProperties) {
    companion object {
        const val CACHE_HEADER = "beagle-hash"
        internal const val MAX_AGE_HEADER = "max-age"
        internal val DEFAULT_TTL = Duration.ofSeconds(30L)
    }

    private val endpointHashMap = mutableMapOf<String, String>()
    private val excludePatterns = properties.exclude.filter { it.isNotBlank() }.map(::Regex)
    private val includePatterns = properties.include.filter { it.isNotBlank() }.map(::Regex)
    private val ttl = properties.ttl.filterKeys { it.isNotBlank() }

    abstract fun createResponseFromController(modifyResponse: (response: Any) -> Any = { it }): Any

    abstract fun createResponse(status: Int): Any

    abstract fun getBody(response: Any): String

    abstract fun addHeader(response: Any, key: String, value: String)

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

    fun handleCache(
        endpoint: String,
        receivedHash: String?,
        currentPlatform: String?
    ) = when {
        this.isEndpointExcluded(endpoint) -> createResponseFromController()
        receivedHash != null && this.isHashUpToDate(
            endpoint = endpoint,
            currentPlatform = currentPlatform,
            hash = receivedHash
        ) ->
            createResponse(HttpURLConnection.HTTP_NOT_MODIFIED)
                .let {
                    addHeader(it, CACHE_HEADER, receivedHash)
                    addTtlHeader(it, endpoint)
                }
        else ->
            createResponseFromController {
                generateAndAddCacheToResponse(
                    endpoint = endpoint,
                    currentPlatform = currentPlatform,
                    response = it
                )
            }
    }

    private fun generateAndAddCacheToResponse(
        endpoint: String,
        currentPlatform: String?,
        response: Any
    ) = response.let {
        addHeader(
            it,
            CACHE_HEADER,
            this.generateAndAddHash(
                endpoint = endpoint,
                currentPlatform = currentPlatform,
                json = getBody(it)
            )
        )
        addTtlHeader(it, endpoint)
    }

    private fun addTtlHeader(response: Any, endpoint: String) = addHeader(
        response = response,
        key = HttpHeaders.CACHE_CONTROL,
        value = "$MAX_AGE_HEADER=${(this.ttl[endpoint] ?: DEFAULT_TTL).seconds}"
    )
}