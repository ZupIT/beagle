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

package br.com.zup.beagle.android.cache

import br.com.zup.beagle.android.networking.RequestData
import br.com.zup.beagle.android.networking.ResponseData
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.store.StoreHandler
import br.com.zup.beagle.android.store.StoreType
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.utils.nanoTimeInSeconds
import br.com.zup.beagle.android.view.ScreenRequest
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import java.net.URI

private val URL = RandomData.string()
private val BEAGLE_HASH_KEY = "$URL#hash"
private val BEAGLE_JSON_KEY = "$URL#json"
private val BEAGLE_TIME_KEY = "$URL#time"
private val BEAGLE_HASH_VALUE = RandomData.string()
private val BEAGLE_JSON_VALUE = RandomData.string()
private val SCREEN_REQUEST = RequestData(uri = URI(""), url = URL)
private val RESPONSE_BODY = RandomData.string()
private const val BEAGLE_HASH = "beagle-hash"
private const val INVALIDATION_TIME: Long = 0
private const val MAXIMUM_CAPACITY: Int = 2

class CacheManagerTest {

    private val storeHandlerDataSlot = slot<Map<String, String>>()
    private val cacheKeySlot = slot<String>()
    private val beagleCacheSlot = slot<BeagleCache>()

    @MockK
    private lateinit var storeHandler: StoreHandler

    @MockK
    private lateinit var memoryCacheStore: LruCacheStore

    @MockK
    private lateinit var beagleEnvironment: BeagleEnvironment

    @MockK
    private lateinit var responseData: ResponseData

    @MockK
    private lateinit var memoryCache: BeagleCache

    private lateinit var cacheManager: CacheManager

    @BeforeEach
    fun setUp() {
        mockkObject(BeagleEnvironment)

        every { BeagleEnvironment.beagleSdk.config.cache.memoryMaximumCapacity } returns 15
        every { BeagleEnvironment.beagleSdk.config.cache.size } returns 15

        MockKAnnotations.init(this)

        cacheManager = CacheManager(
            storeHandler,
            beagleEnvironment,
            memoryCacheStore
        )

        every { memoryCache.json } returns BEAGLE_JSON_VALUE
        every { memoryCache.hash } returns BEAGLE_HASH_VALUE
        every { beagleEnvironment.beagleSdk.config.cache.enabled } returns true
        every { beagleEnvironment.beagleSdk.config.cache.maxAge } returns INVALIDATION_TIME
        every { beagleEnvironment.beagleSdk.config.cache.memoryMaximumCapacity } returns MAXIMUM_CAPACITY
        every { beagleEnvironment.beagleSdk.config.cache.size } returns MAXIMUM_CAPACITY
        every { memoryCacheStore.restore(any()) } returns null
        every { storeHandler.restore(StoreType.DATABASE, any(), any(), any()) } returns mapOf()
        every { storeHandler.getAll(StoreType.DATABASE) } returns mapOf()
        every { storeHandler.delete(StoreType.DATABASE, any()) } just Runs
        every { responseData.statusCode } returns 200
        every { responseData.data } returns byteArrayOf()
        every { responseData.headers } returns mapOf()
        every { storeHandler.save(any(), capture(storeHandlerDataSlot)) } just Runs
        every { responseData.data } returns RESPONSE_BODY.toByteArray()
        every {
            memoryCacheStore.save(
                cacheKey = capture(cacheKeySlot),
                beagleCache = capture(beagleCacheSlot)
            )
        } just Runs
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `cache should not initialize cache store when cache is disabled and memoryMaximumCapacity is zero`() {
        // Given
        every { beagleEnvironment.beagleSdk.config.cache.enabled } returns false
        every { beagleEnvironment.beagleSdk.config.cache.memoryMaximumCapacity } returns 0
        every { beagleEnvironment.beagleSdk.config.cache.size } returns 0

        // When
        CacheManager(
            storeHandler,
            beagleEnvironment
        )

        // Then
        verify(exactly = 0) { LruCacheStore.instance }
    }

    @Test
    fun `restoreBeagleCacheForUrl should return null when cache is disabled`() {
        // Given
        every { beagleEnvironment.beagleSdk.config.cache.enabled } returns false

        // When
        val actual = cacheManager.restoreBeagleCacheForUrl(URL)

        // Then
        verify(exactly = 0) { memoryCacheStore.restore(any()) }
        assertNull(actual)
    }

    @Test
    fun `restoreBeagleCacheForUrl should return beagleCache when timer is valid`() {
        // Given
        every { memoryCacheStore.restore(any()) } returns memoryCache
        every { memoryCache.isExpired() } returns false

        // When
        val actual = cacheManager.restoreBeagleCacheForUrl(URL)

        // Then
        verify(exactly = 1) { memoryCacheStore.restore(BEAGLE_HASH_KEY) }
        assertTrue { actual?.isExpired() == false }
        assertEquals(BEAGLE_JSON_VALUE, actual?.json)
        assertEquals(BEAGLE_HASH_VALUE, actual?.hash)
    }

    @Test
    fun `restoreBeagleCacheForUrl should return isExpired true when timer is not valid`() {
        // Given
        every { memoryCacheStore.restore(any()) } returns memoryCache
        every { memoryCache.isExpired() } returns true

        // When
        val actual = cacheManager.restoreBeagleCacheForUrl(URL)

        // Then
        verify(exactly = 1) { memoryCacheStore.restore(BEAGLE_HASH_KEY) }
        assertTrue { actual?.isExpired() == true }
    }

    @Test
    fun `restoreBeagleCacheForUrl should return beagleCache from disk when timer is null and data exists and cache is valid`() {
        // Given
        every { storeHandler.restore(StoreType.DATABASE, any(), any(), any()) } returns mapOf(
            BEAGLE_HASH_KEY to BEAGLE_HASH_VALUE,
            BEAGLE_JSON_KEY to BEAGLE_JSON_VALUE,
            BEAGLE_TIME_KEY to Long.MAX_VALUE.toString()
        )

        mockkStatic("br.com.zup.beagle.android.utils.SystemUtilsKt")
        every { nanoTimeInSeconds() } returns 0

        // When
        val actual = cacheManager.restoreBeagleCacheForUrl(URL)

        // Then
        verify(exactly = 1) {
            storeHandler.restore(StoreType.DATABASE, BEAGLE_HASH_KEY, BEAGLE_JSON_KEY,
                BEAGLE_TIME_KEY)
        }
        assertEquals(BEAGLE_JSON_VALUE, actual?.json)
        assertEquals(BEAGLE_HASH_VALUE, actual?.hash)
    }

    @Test
    fun `restoreBeagleCacheForUrl should return null from disk when timer is null and data exists and cache is invalid`() {
        // Given
        every { storeHandler.restore(StoreType.DATABASE, any(), any(), any()) } returns mapOf(
            BEAGLE_HASH_KEY to BEAGLE_HASH_VALUE,
            BEAGLE_JSON_KEY to BEAGLE_JSON_VALUE,
            BEAGLE_TIME_KEY to "0"
        )

        mockkStatic("br.com.zup.beagle.android.utils.SystemUtilsKt")
        every { nanoTimeInSeconds() } returns Long.MAX_VALUE

        // When
        val actual = cacheManager.restoreBeagleCacheForUrl(URL)

        // Then
        verify(exactly = 1) {
            storeHandler.restore(StoreType.DATABASE, BEAGLE_HASH_KEY, BEAGLE_JSON_KEY,
                BEAGLE_TIME_KEY)
        }
        assertNull(actual?.json)
        assertNull(actual?.hash)
    }

    @Test
    fun `restoreBeagleCacheForUrl should return null from disk when timer is null and data does not exists`() {
        // Given When
        val actual = cacheManager.restoreBeagleCacheForUrl(URL)

        // Then
        verify(exactly = 1) { storeHandler.restore(StoreType.DATABASE, BEAGLE_HASH_KEY, BEAGLE_JSON_KEY, BEAGLE_TIME_KEY) }
        assertNull(actual)
    }

    @Test
    fun `screenRequestWithHash should add beagle hash header when beagleCache is not null`() {
        // Given
        val beagleCache = mockk<BeagleCache> {
            every { hash } returns BEAGLE_HASH_VALUE
        }

        // When
        val actualScreenRequest = cacheManager.screenRequestWithCache(SCREEN_REQUEST, beagleCache)

        // Then
        assertEquals(BEAGLE_HASH_VALUE, actualScreenRequest.headers[BEAGLE_HASH])
    }

    @Test
    fun `screenRequestWithHash should not add beagle hash header when beagleCache is null`() {
        // Given
        val beagleCache: BeagleCache? = null

        // When
        val actualScreenRequest = cacheManager.screenRequestWithCache(SCREEN_REQUEST, beagleCache)

        // Then
        assertNull(actualScreenRequest.headers[BEAGLE_HASH])
    }

    @Test
    fun `handleResponseData should return cached json when statusCode is 304 and cache is not null`() {
        // Given
        val beagleCache = mockk<BeagleCache> {
            every { json } returns BEAGLE_JSON_VALUE
            every { hash } returns BEAGLE_HASH_VALUE
        }
        every { responseData.statusCode } returns 304


        // When
        val responseBody = cacheManager.handleResponseData(URL, beagleCache, responseData)

        // Then
        assertEquals(BEAGLE_JSON_VALUE, responseBody)
    }

    @Test
    fun `handleResponseData should return responseBody from http when statusCode is not 304`() {
        // Given
        val beagleCache: BeagleCache? = null
        every { responseData.statusCode } returns 200


        // When
        val responseBody = cacheManager.handleResponseData(URL, beagleCache, responseData)

        // Then
        assertEquals(RESPONSE_BODY, responseBody)
    }

    @Test
    fun `handleResponseData should return responseBody from http when statusCode is 304 but beagleCache is null`() {
        // Given
        val beagleCache: BeagleCache? = null
        every { responseData.statusCode } returns 304


        // When
        val responseBody = cacheManager.handleResponseData(URL, beagleCache, responseData)

        // Then
        assertEquals(RESPONSE_BODY, responseBody)
    }

    @Test
    fun `handleResponseData should not call store cache if is disabled`() {
        // Given
        every { beagleEnvironment.beagleSdk.config.cache.enabled } returns false

        // When
        cacheManager.handleResponseData(URL, null, responseData)

        // Then
        verify(exactly = 0) { storeHandler.save(StoreType.DATABASE, any()) }
        verify(exactly = 0) { memoryCacheStore.save(any(), any()) }
    }

    @Test
    fun `GIVEN cache manager WHEN call handle response with storeHandle null and cache enabled THEN should not call cache`() {
        // Given
        every { beagleEnvironment.beagleSdk.config.cache.enabled } returns true
        cacheManager = CacheManager(
            null,
            beagleEnvironment,
            memoryCacheStore
        )

        // When
        cacheManager.handleResponseData(URL, null, responseData)


        // Then
        verify(exactly = 0) { storeHandler.save(StoreType.DATABASE, any()) }
        verify(exactly = 0) { memoryCacheStore.save(any(), any()) }
    }


    @Test
    fun `GIVEN cache manager WHEN call restore beagle with storeHandle null and cache enabled THEN should not call cache`() {
        // Given
        every { beagleEnvironment.beagleSdk.config.cache.enabled } returns true
        cacheManager = CacheManager(
            null,
            beagleEnvironment,
            memoryCacheStore
        )

        // When
        val actual = cacheManager.restoreBeagleCacheForUrl(URL)


        // Then
        verify(exactly = 0) { memoryCacheStore.restore(any()) }
        assertNull(actual)
    }


    @Test
    fun `handleResponseData should not call store cache if beagleCache header is not present`() {
        // Given
        val headers = mapOf<String, String>()
        every { responseData.headers } returns headers

        // When
        cacheManager.handleResponseData(URL, null, responseData)

        // Then
        verify(exactly = 0) { storeHandler.save(StoreType.DATABASE, any()) }
        verify(exactly = 0) { memoryCacheStore.save(any(), any()) }
    }

    @Test
    fun `handleResponseData should not call store cache if responseData does not have beagleHash in headers`() {
        // Given When
        cacheManager.handleResponseData(URL, null, responseData)

        // Then
        verify(exactly = 0) { storeHandler.save(StoreType.DATABASE, any()) }
    }

    @Test
    fun `handleResponseData should call store cache if responseData have beagleHash in headers`() {
        // Given
        every { responseData.headers } returns mapOf(
            BEAGLE_HASH to BEAGLE_HASH_VALUE
        )

        // When
        cacheManager.handleResponseData(URL, null, responseData)

        // Then
        verifySequence {
            storeHandler.save(StoreType.DATABASE, any())
            storeHandler.getAll(StoreType.DATABASE)
            memoryCacheStore.save(BEAGLE_HASH_KEY, any())
        }
        val diskData = storeHandlerDataSlot.captured
        assertEquals(BEAGLE_HASH_VALUE, diskData[BEAGLE_HASH_KEY])
        assertEquals(RESPONSE_BODY, diskData[BEAGLE_JSON_KEY])
        val timerCache = beagleCacheSlot.captured
        assertEquals(BEAGLE_HASH_KEY, cacheKeySlot.captured)
        assertEquals(INVALIDATION_TIME, timerCache.maxTime)
        assertEquals(BEAGLE_HASH_VALUE, timerCache.hash)
        assertEquals(RESPONSE_BODY, timerCache.json)
        assertTrue(timerCache.cachedTime > 0)
    }

    @Test
    fun `handleResponseData should catch CacheControl header if responseData have beagleHash in headers`() {
        // Given
        val maxAge = RandomData.int().toLong()
        every { responseData.headers } returns mapOf(
            BEAGLE_HASH to BEAGLE_HASH_VALUE,
            "cache-control" to "max-age=$maxAge"
        )

        // When
        cacheManager.handleResponseData(URL, null, responseData)

        // Then
        assertEquals(maxAge, beagleCacheSlot.captured.maxTime)
    }

    @Test
    fun `handleResponseData should catch CacheControl header maxAge value only if responseData have beagleHash in headers`() {
        // Given
        val maxAge = RandomData.int().toLong()
        every { responseData.headers } returns mapOf(
            BEAGLE_HASH to BEAGLE_HASH_VALUE,
            "cache-control" to "no-transform, max-age=$maxAge"
        )

        // When
        cacheManager.handleResponseData(URL, null, responseData)

        // Then
        assertEquals(maxAge, beagleCacheSlot.captured.maxTime)
    }
}