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

import br.com.zup.beagle.platform.BeaglePlatform
import br.com.zup.beagle.serialization.jackson.BeagleSerializationUtil
import br.com.zup.beagle.widget.ui.Button
import com.google.common.hash.Hashing
import com.google.common.net.HttpHeaders
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import org.junit.jupiter.api.Test
import java.net.HttpURLConnection
import java.nio.charset.Charset
import java.time.Duration
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class BeagleCacheHandlerTest {

    private val allEndpoints = listOf("/.*")
    private val imageEndpoints = listOf("/image", "/image2")

    companion object {
        private const val HOME_ENDPOINT = "/home"
        private const val IMAGE_ENDPOINT = "/image"
        private const val DURATION = 15L

        private val BUTTON_JSON = BeagleSerializationUtil.beagleObjectMapper().writeValueAsString(Button("test"))
        private val BUTTON_JSON_HASH = Hashing.sha512().hashString(this.BUTTON_JSON, Charset.defaultCharset()).toString()
        private val TTL = mapOf(HOME_ENDPOINT to Duration.ofSeconds(DURATION))
    }

    @Test
    fun `cacheHandler should return true for isEndpointExcluded for image endpoint when all included and images excluded`() {
        assertTrue {
            BeagleCacheHandler(excludeEndpoints = this.imageEndpoints, includeEndpoints = this.allEndpoints)
                .isEndpointExcluded(IMAGE_ENDPOINT)
        }
    }

    @Test
    fun `cacheHandler should return false for isEndpointExcluded for home endpoint when all included and images excluded`() {
        assertFalse {
            BeagleCacheHandler(excludeEndpoints = this.imageEndpoints, includeEndpoints = this.allEndpoints)
                .isEndpointExcluded(HOME_ENDPOINT)
        }
    }

    @Test
    fun `cacheHandler should return false for isEndpointExcluded for image endpoint when images included`() {
        assertFalse {
            BeagleCacheHandler(includeEndpoints = this.imageEndpoints).isEndpointExcluded(IMAGE_ENDPOINT)
        }
    }

    @Test
    fun `cacheHandler should return true for isEndpointExcluded for home endpoint when images included`() {
        assertTrue {
            BeagleCacheHandler(includeEndpoints = this.imageEndpoints).isEndpointExcluded(HOME_ENDPOINT)
        }
    }

    @Test
    fun `cacheHandler should return same hash for button object when call generateAndAddHash`() {
        assertEquals(
            BeagleCacheHandler().generateAndAddHash(
                endpoint = HOME_ENDPOINT,
                currentPlatform = BeaglePlatform.ALL.name,
                json = BUTTON_JSON
            ),
            BUTTON_JSON_HASH
        )
    }

    @Test
    fun `cacheHandler should return true for isHashIsUpToDate`() {
        val cacheHandler = BeagleCacheHandler()
        cacheHandler.generateAndAddHash(
            endpoint = HOME_ENDPOINT,
            currentPlatform = BeaglePlatform.WEB.name,
            json = BUTTON_JSON
        )
        assertTrue {
            cacheHandler.isHashUpToDate(
                endpoint = HOME_ENDPOINT,
                currentPlatform = BeaglePlatform.WEB.name,
                hash = BUTTON_JSON_HASH
            )
        }
    }

    @Test
    fun `cacheHandler should return false for isHashIsUpToDate`() {
        val cacheHandler = BeagleCacheHandler()
        val cacheHandler2 = BeagleCacheHandler()
        cacheHandler.generateAndAddHash(
            endpoint = HOME_ENDPOINT,
            currentPlatform = BeaglePlatform.ANDROID.name,
            json = BUTTON_JSON
        )
        assertFalse {
            cacheHandler.isHashUpToDate(
                endpoint = HOME_ENDPOINT,
                currentPlatform = BeaglePlatform.ANDROID.name,
                hash = "hash"
            )
        }
        assertFalse {
            cacheHandler2.isHashUpToDate(
                endpoint = HOME_ENDPOINT,
                currentPlatform = BeaglePlatform.ANDROID.name,
                hash = BUTTON_JSON_HASH
            )
        }
    }

    @Test
    fun `Test handleCache when endpoint is excluded`() {
        this.testHandleCache(endpoint = IMAGE_ENDPOINT) { verifySequence { it.createResponseFromController() } }
    }

    @Test
    fun `Test handleCache when endpoint is not included`() {
        this.testHandleCache(endpoint = "") { verifySequence { it.createResponseFromController() } }
    }

    @Test
    fun `Test handleCache when no previous cache`() {
        this.testHandleCache(endpoint = HOME_ENDPOINT) { this.verifySentControllerResponse(it) }
    }

    @Test
    fun `Test handleCache when previous cache is outdated`() {
        this.testHandleCache(endpoint = HOME_ENDPOINT, hash = "", prepare = this::preparePreviousCache) {
            this.verifySentControllerResponse(it)
        }
    }

    @Test
    fun `Test handleCache when previous cache is up to date`() {
        this.testHandleCache(endpoint = HOME_ENDPOINT, hash = BUTTON_JSON_HASH, prepare = this::preparePreviousCache) {
            this.verifySentCachedResponse(it)
        }
    }

    @Test
    fun `Test handleCache when no previous cache and endpoint has TTL`() {
        this.testHandleCache(endpoint = HOME_ENDPOINT, ttl = TTL) { this.verifySentControllerResponse(it, DURATION) }
    }

    @Test
    fun `Test handleCache when previous cache is outdated and endpoint has TTL`() {
        this.testHandleCache(endpoint = HOME_ENDPOINT, hash = "", ttl = TTL, prepare = this::preparePreviousCache) {
            this.verifySentControllerResponse(it, DURATION)
        }
    }

    @Test
    fun `Test handleCache when previous cache is up to date and endpoint has TTL`() {
        this.testHandleCache(
            endpoint = HOME_ENDPOINT,
            hash = BUTTON_JSON_HASH,
            ttl = TTL,
            prepare = this::preparePreviousCache
        ) { this.verifySentCachedResponse(it, DURATION) }
    }

    @Test
    fun `Test legacy handleCache when endpoint is excluded`() {
        this.testLegacyHandleCache(endpoint = IMAGE_ENDPOINT) { verifySequence { it.callController(Response.START) } }
    }

    @Test
    fun `Test legacy handleCache when endpoint is not included`() {
        this.testLegacyHandleCache(endpoint = "") { verifySequence { it.callController(Response.START) } }
    }

    @Test
    fun `Test legacy handleCache when no previous cache`() {
        this.testLegacyHandleCache(endpoint = HOME_ENDPOINT, verify = this::verifySentControllerResponse)
    }

    @Test
    fun `Test legacy handleCache when previous cache is outdated`() {
        this.testLegacyHandleCache(
            endpoint = HOME_ENDPOINT,
            hash = "",
            prepare = this::preparePreviousCache,
            verify = this::verifySentControllerResponse
        )
    }

    @Test
    fun `Test legacy handleCache when previous cache is up to date`() {
        this.testLegacyHandleCache(endpoint = HOME_ENDPOINT, hash = BUTTON_JSON_HASH, prepare = this::preparePreviousCache) {
            verifySequence {
                it.addStatus(Response.START, HttpURLConnection.HTTP_NOT_MODIFIED)
                it.addHashHeader(Response.STATUS, BUTTON_JSON_HASH)
            }
        }
    }

    private fun testHandleCache(
        endpoint: String,
        hash: String? = null,
        ttl: Map<String, Duration> = emptyMap(),
        prepare: (BeagleCacheHandler) -> Unit = {},
        verify: (HttpCacheHandler<Response>) -> Unit
    ) {
        val httpCacheHandler = mockk<HttpCacheHandler<Response>>()
        val cacheHandler = BeagleCacheHandler(
            excludeEndpoints = this.imageEndpoints,
            includeEndpoints = this.allEndpoints,
            ttl = ttl
        )

        prepare(cacheHandler)
        every { httpCacheHandler.createResponseFromController() } returns Response.CONTROLLER
        every { httpCacheHandler.createResponse(this.any()) } returns Response.STATUS
        every { httpCacheHandler.addHeader(this.any(), this.any(), this.any()) } returns Response.HEADER
        every { httpCacheHandler.getBody(this.any()) } returns BUTTON_JSON

        cacheHandler.handleCache(
            endpoint = endpoint,
            receivedHash = hash,
            currentPlatform = BeaglePlatform.IOS.name,
            handler = httpCacheHandler
        )

        verify(httpCacheHandler)
    }

    private fun testLegacyHandleCache(
        endpoint: String,
        hash: String? = null,
        prepare: (BeagleCacheHandler) -> Unit = {},
        verify: (RestCacheHandler<Response>) -> Unit
    ) {
        val restCache = mockk<RestCacheHandler<Response>>()
        val cacheHandler = BeagleCacheHandler(excludeEndpoints = this.imageEndpoints, includeEndpoints = this.allEndpoints)

        prepare(cacheHandler)
        every { restCache.callController(this.any()) } returns Response.CONTROLLER
        every { restCache.addHashHeader(this.any(), this.any()) } returns Response.HEADER
        every { restCache.addStatus(this.any(), this.any()) } returns Response.STATUS
        every { restCache.getBody(this.any()) } returns BUTTON_JSON

        cacheHandler.handleCache(
            endpoint = endpoint,
            receivedHash = hash,
            currentPlatform = BeaglePlatform.IOS.name,
            initialResponse = Response.START,
            restHandler = restCache
        )

        verify(restCache)
    }

    private fun preparePreviousCache(handler: BeagleCacheHandler) {
        handler.generateAndAddHash(
            endpoint = HOME_ENDPOINT,
            currentPlatform = BeaglePlatform.IOS.name,
            json = BUTTON_JSON
        )
    }

    private fun verifySentControllerResponse(handler: RestCacheHandler<Response>) {
        verifySequence {
            handler.callController(Response.START)
            handler.getBody(Response.CONTROLLER)
            handler.addHashHeader(Response.CONTROLLER, BUTTON_JSON_HASH)
        }
    }

    private fun verifySentControllerResponse(handler: HttpCacheHandler<Response>, seconds: Long = 30L) {
        verifySequence {
            handler.createResponseFromController()
            handler.getBody(Response.CONTROLLER)
            handler.addHeader(Response.CONTROLLER, BeagleCacheHandler.CACHE_HEADER, BUTTON_JSON_HASH)
            handler.addHeader(Response.HEADER, HttpHeaders.CACHE_CONTROL, "max-age=$seconds")
        }
    }

    private fun verifySentCachedResponse(handler: HttpCacheHandler<Response>, seconds: Long = 30L) {
        verifySequence {
            handler.createResponse(HttpURLConnection.HTTP_NOT_MODIFIED)
            handler.addHeader(Response.STATUS, BeagleCacheHandler.CACHE_HEADER, BUTTON_JSON_HASH)
            handler.addHeader(Response.HEADER, HttpHeaders.CACHE_CONTROL, "max-age=$seconds")
        }
    }

    private enum class Response { START, CONTROLLER, HEADER, STATUS }
}