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
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

private val URL = RandomData.string()
private val BEAGLE_HASH_KEY = "$URL#hash"
private val BEAGLE_JSON_KEY = "$URL#json"
private val BEAGLE_TIME_KEY = "$URL#time"
private val BEAGLE_HASH_VALUE = RandomData.string()
private val BEAGLE_JSON_VALUE = RandomData.string()
private val REQUEST_DATA = RequestData(url = URL)
private val RESPONSE_BODY = RandomData.string()
private const val BEAGLE_HASH = "beagle-hash"
private const val INVALIDATION_TIME: Long = 0
private const val MAXIMUM_CAPACITY: Int = 2

@DisplayName("Given a Cache Manager")
class CacheManagerTest {

    private val storeHandler: StoreHandler = mockk()
    private val memoryCacheStore: LruCacheStore = mockk()
    private val beagleEnvironment: BeagleEnvironment = mockk()
    private val responseData: ResponseData = mockk()
    private val memoryCache: BeagleCache = mockk()

    private val storeHandlerDataSlot = slot<Map<String, String>>()
    private val cacheKeySlot = slot<String>()
    private val beagleCacheSlot = slot<BeagleCache>()

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

    @DisplayName("When cache is disabled")
    @Nested
    inner class CacheDisabledTest {

        @DisplayName("Then should not call lru cache")
        @Test
        fun testLruCacheNotCallWhenCacheIsDisabled() {
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

        @DisplayName("Then should not call memory cache restore")
        @Test
        fun testRestoreNotCallWhenCacheIsDisabled() {
            // Given
            every { beagleEnvironment.beagleSdk.config.cache.enabled } returns false

            // When
            val actual = cacheManager.restoreBeagleCacheForUrl(URL)

            // Then
            verify(exactly = 0) { memoryCacheStore.restore(any()) }
            assertNull(actual)
        }

        @DisplayName("Then should not call store cache")
        @Test
        fun testNotCallStoreHandler() {
            // Given
            every { beagleEnvironment.beagleSdk.config.cache.enabled } returns false

            // When
            cacheManager.handleResponseData(URL, null, responseData)

            // Then
            verify(exactly = 0) { storeHandler.save(StoreType.DATABASE, any()) }
            verify(exactly = 0) { memoryCacheStore.save(any(), any()) }
        }
    }

    @DisplayName("When time is valid")
    @Nested
    inner class TimeIsValidTest {

        @DisplayName("Then should return a beagle cache")
        @Test
        fun testReturnABeagleCacheWhenTimeIsValid() {
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
    }

    @DisplayName("When time is not valid")
    @Nested
    inner class TimeIsNotValidTest {

        @DisplayName("Then should return cache is expired")
        @Test
        fun testCacheIsExpiredWhenTimeIsNotValid() {
            // Given
            every { memoryCacheStore.restore(any()) } returns memoryCache
            every { memoryCache.isExpired() } returns true

            // When
            val actual = cacheManager.restoreBeagleCacheForUrl(URL)

            // Then
            verify(exactly = 1) { memoryCacheStore.restore(BEAGLE_HASH_KEY) }
            assertTrue { actual?.isExpired() == true }
        }
    }

    @DisplayName("When timer is null and data exists and cache is valid")
    @Nested
    inner class TimeIsNullAndDataExistAndCacheIsValidTest {

        @DisplayName("Then should return beagleCache")
        @Test
        fun testReturnBeagleCache() {
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
    }

    @DisplayName("When timer is null and data exists and cache is invalid")
    @Nested
    inner class TimeIsNullAndDataExistAndCacheIsInValidTest {

        @DisplayName("Then should return beagle cache null")
        @Test
        fun testBeagleCacheIsNull() {
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
    }

    @DisplayName("When timer is null and data does not exists")
    @Nested
    inner class TimeIsNullAndDataDoesNotExistsTest {

        @DisplayName("Then should return beagle cache null")
        @Test
        fun testBeagleCacheIsNull() {
            // Given When
            val actual = cacheManager.restoreBeagleCacheForUrl(URL)

            // Then
            verify(exactly = 1) { storeHandler.restore(StoreType.DATABASE, BEAGLE_HASH_KEY, BEAGLE_JSON_KEY, BEAGLE_TIME_KEY) }
            assertNull(actual)
        }
    }

    @DisplayName("When beagle cache is not null")
    @Nested
    inner class BeagleCacheIsNotNullTest {

        @DisplayName("Then should add beagle hash header")
        @Test
        fun testReturnBeagleHashHeader() {
            // Given
            val beagleCache = mockk<BeagleCache> {
                every { hash } returns BEAGLE_HASH_VALUE
            }

            // When
            val actualRequestData = cacheManager.requestDataWithCache(REQUEST_DATA, beagleCache)

            // Then
            assertEquals(BEAGLE_HASH_VALUE, actualRequestData.headers[BEAGLE_HASH])
            assertEquals(BEAGLE_HASH_VALUE, actualRequestData.httpAdditionalData.headers!![BEAGLE_HASH])
        }
    }

    @DisplayName("When beagle cache is null")
    @Nested
    inner class BeagleCacheIsNullTest {

        @DisplayName("Then should not add beagle hash header")
        @Test
        fun testNotAddBeagleHashHeader() {
            // Given
            val beagleCache: BeagleCache? = null

            // When
            val actualScreenRequest = cacheManager.requestDataWithCache(REQUEST_DATA, beagleCache)

            // Then
            assertNull(actualScreenRequest.headers[BEAGLE_HASH])
            assertNull(actualScreenRequest.httpAdditionalData.headers!![BEAGLE_HASH])
        }
    }

    @DisplayName("When status code is 304 and cache is not null")
    @Nested
    inner class StatusCode304AndCacheIsNotNullTest {

        @DisplayName("Then should return cached json")
        @Test
        fun testReturnCachedJson() {
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
    }

    @DisplayName("When status code is not 304")
    @Nested
    inner class StatusCodeIsNot304Test {

        @DisplayName("Then should return response body")
        @Test
        fun testReturnResponseBody() {
            // Given
            val beagleCache: BeagleCache? = null
            every { responseData.statusCode } returns 200

            // When
            val responseBody = cacheManager.handleResponseData(URL, beagleCache, responseData)

            // Then
            assertEquals(RESPONSE_BODY, responseBody)
        }
    }

    @DisplayName("When status code is 304 and beagle cache is null")
    @Nested
    inner class StatusCodeIs304AndBeagleCacheIsNullTest {

        @DisplayName("Then should return response body")
        @Test
        fun testReturnResponseBody() {
            // Given
            val beagleCache: BeagleCache? = null
            every { responseData.statusCode } returns 304

            // When
            val responseBody = cacheManager.handleResponseData(URL, beagleCache, responseData)

            // Then
            assertEquals(RESPONSE_BODY, responseBody)
        }
    }

    @DisplayName("When call handle response with storeHandle null and cache enabled")
    @Nested
    inner class CallHandleResponseStoreHandleNullAndCacheEnabledTest {

        @DisplayName("Then should not call cache")
        @Test
        fun testNotCallCache() {
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
    }

    @DisplayName("When call restore beagle with storeHandle null and cache enabled")
    @Nested
    inner class CallRestoreStoreHandleNullAndCacheEnabledTest {

        @DisplayName("Then should not call cache")
        @Test
        fun testNotCallCache() {
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
    }

    @DisplayName("When beagle header is not present")
    @Nested
    inner class BeagleHeaderIsNotPresentTest {

        @DisplayName("Then should not call store cache")
        @Test
        fun testNotCallStoreHandler() {
            // Given
            val headers = mapOf<String, String>()
            every { responseData.headers } returns headers

            // When
            cacheManager.handleResponseData(URL, null, responseData)

            // Then
            verify(exactly = 0) { storeHandler.save(StoreType.DATABASE, any()) }
            verify(exactly = 0) { memoryCacheStore.save(any(), any()) }
        }
    }

    @DisplayName("When beagle hash is not present in headers")
    @Nested
    inner class BeagleHashIsNotPresentTest {

        @DisplayName("Then should not call store cache")
        @Test
        fun testNotCallStoreHandler() {
            // Given When
            cacheManager.handleResponseData(URL, null, responseData)

            // Then
            verify(exactly = 0) { storeHandler.save(StoreType.DATABASE, any()) }
        }
    }

    @DisplayName("When has beagle hash in headers")
    @Nested
    inner class BeagleHashInHeadersTest {

        @DisplayName("Then should call store cache")
        @Test
        fun testCallStoreHandler() {
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

        @DisplayName("Then should catch cache control")
        @Test
        fun testCatchCacheControl() {
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
    }

    @DisplayName("When has beagle hash in headers with multi parameters")
    @Nested
    inner class BeagleHeaderWithMultiParameters {

        @DisplayName("Then should catch cache control")
        @Test
        fun testCatchCacheControl() {
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
}