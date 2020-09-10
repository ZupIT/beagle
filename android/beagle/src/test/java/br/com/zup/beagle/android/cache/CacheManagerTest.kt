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
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

private val URL = RandomData.string()
private val BEAGLE_HASH_KEY = "$URL#hash"
private val BEAGLE_JSON_KEY = "$URL#json"
private val BEAGLE_TIME_KEY = "$URL#time"
private val BEAGLE_HASH_VALUE = RandomData.string()
private val BEAGLE_JSON_VALUE = RandomData.string()
private val SCREEN_REQUEST = ScreenRequest(URL)
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

    @Before
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

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun cache_should_not_initialize_cache_store_when_cache_is_disabled_and_memoryMaximumCapacity_is_zero() {
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
    fun restoreBeagleCacheForUrl_should_return_null_when_cache_is_disabled() {
        // Given
        every { beagleEnvironment.beagleSdk.config.cache.enabled } returns false

        // When
        val actual = cacheManager.restoreBeagleCacheForUrl(URL)

        // Then
        verify(exactly = 0) { memoryCacheStore.restore(any()) }
        assertNull(actual)
    }

    @Test
    fun restoreBeagleCacheForUrl_should_return_beagleCache_when_timer_is_valid() {
        // Given
        every { memoryCacheStore.restore(any()) } returns memoryCache
        every { memoryCache.isHot() } returns true

        // When
        val actual = cacheManager.restoreBeagleCacheForUrl(URL)

        // Then
        verify(exactly = 1) { memoryCacheStore.restore(BEAGLE_HASH_KEY) }
        assertTrue { actual?.isHot() == true }
        assertEquals(BEAGLE_JSON_VALUE, actual?.json)
        assertEquals(BEAGLE_HASH_VALUE, actual?.hash)
    }

    @Test
    fun restoreBeagleCacheForUrl_should_return_isHot_false_when_timer_is_not_valid() {
        // Given
        every { memoryCacheStore.restore(any()) } returns memoryCache
        every { memoryCache.isHot() } returns false

        // When
        val actual = cacheManager.restoreBeagleCacheForUrl(URL)

        // Then
        verify(exactly = 1) { memoryCacheStore.restore(BEAGLE_HASH_KEY) }
        assertTrue { actual?.isHot() == false }
    }

    @Test
    fun restoreBeagleCacheForUrl_should_return_beagleCache_from_disk_when_timer_is_null_and_data_exists_and_cache_is_valid() {
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
    fun restoreBeagleCacheForUrl_should_return_null_from_disk_when_timer_is_null_and_data_exists_and_cache_is_invalid() {
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
    fun restoreBeagleCacheForUrl_should_return_null_from_disk_when_timer_is_null_and_data_does_not_exists() {
        // Given When
        val actual = cacheManager.restoreBeagleCacheForUrl(URL)

        // Then
        verify(exactly = 1) { storeHandler.restore(StoreType.DATABASE, BEAGLE_HASH_KEY, BEAGLE_JSON_KEY, BEAGLE_TIME_KEY) }
        assertNull(actual)
    }

    @Test
    fun screenRequestWithHash_should_add_beagle_hash_header_when_beagleCache_is_not_null() {
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
    fun screenRequestWithHash_should_not_add_beagle_hash_header_when_beagleCache_is_null() {
        // Given
        val beagleCache: BeagleCache? = null

        // When
        val actualScreenRequest = cacheManager.screenRequestWithCache(SCREEN_REQUEST, beagleCache)

        // Then
        assertNull(actualScreenRequest.headers[BEAGLE_HASH])
    }

    @Test
    fun handleResponseData_should_return_cached_json_when_statusCode_is_304_and_cache_is_not_null() {
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
    fun handleResponseData_should_return_responseBody_from_http_when_statusCode_is_not_304() {
        // Given
        val beagleCache: BeagleCache? = null
        every { responseData.statusCode } returns 200


        // When
        val responseBody = cacheManager.handleResponseData(URL, beagleCache, responseData)

        // Then
        assertEquals(RESPONSE_BODY, responseBody)
    }

    @Test
    fun handleResponseData_should_return_responseBody_from_http_when_statusCode_is_304_but_beagleCache_is_null() {
        // Given
        val beagleCache: BeagleCache? = null
        every { responseData.statusCode } returns 304


        // When
        val responseBody = cacheManager.handleResponseData(URL, beagleCache, responseData)

        // Then
        assertEquals(RESPONSE_BODY, responseBody)
    }

    @Test
    fun handleResponseData_should_not_call_store_cache_if_is_disabled() {
        // Given
        every { beagleEnvironment.beagleSdk.config.cache.enabled } returns false

        // When
        cacheManager.handleResponseData(URL, null, responseData)

        // Then
        verify(exactly = 0) { storeHandler.save(StoreType.DATABASE, any()) }
        verify(exactly = 0) { memoryCacheStore.save(any(), any()) }
    }

    @Test
    fun handleResponseData_should_not_call_store_cache_if_beagleCache_header_is_not_present() {
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
    fun handleResponseData_should_not_call_store_cache_if_responseData_does_not_have_beagleHash_in_headers() {
        // Given When
        cacheManager.handleResponseData(URL, null, responseData)

        // Then
        verify(exactly = 0) { storeHandler.save(StoreType.DATABASE, any()) }
    }

    @Test
    fun handleResponseData_should_call_store_cache_if_responseData_have_beagleHash_in_headers() {
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
    fun handleResponseData_should_catch_CacheControl_header_if_responseData_have_beagleHash_in_headers() {
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
    fun handleResponseData_should_catch_CacheControl_header_maxAge_value_only_if_responseData_have_beagleHash_in_headers() {
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