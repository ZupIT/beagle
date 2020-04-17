/*
 * Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package br.com.zup.beagle.cache

import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.data.BeagleApi
import br.com.zup.beagle.data.serializer.BeagleSerializer
import br.com.zup.beagle.extensions.once
import br.com.zup.beagle.networking.ResponseData
import br.com.zup.beagle.setup.BeagleEnvironment
import br.com.zup.beagle.store.StoreHandler
import br.com.zup.beagle.store.StoreType
import br.com.zup.beagle.testutil.RandomData
import br.com.zup.beagle.view.ScreenRequest
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

private val URL = RandomData.string()
private val BEAGLE_HASH_KEY = "$URL#hash"
private val BEAGLE_JSON_KEY = "$URL#json"
private val BEAGLE_HASH_VALUE = RandomData.string()
private val SCREEN_REQUEST = ScreenRequest(URL)
private val RESPONSE_BODY = RandomData.string()
private const val BEAGLE_HASH = "beagle-hash"
private const val INVALIDATION_TIME: Long = 0

class CacheManagerTest {

    private val storeHandlerDataSlot = slot<Map<String, String>>()
    private val cacheKeySlot = slot<String>()
    private val maxTimeSlot = slot<Long>()
    private val responseBodySlot = slot<String>()
    private val timerCacheSlot = slot<TimerCache>()

    @MockK
    private lateinit var storeHandler: StoreHandler
    @MockK
    private lateinit var timerCacheStore: LruCacheStore
    @MockK
    private lateinit var beagleEnvironment: BeagleEnvironment
    @MockK
    private lateinit var responseData: ResponseData

    private lateinit var cacheManager: CacheManager

    @Before
    fun setUp() {
        mockkObject(BeagleEnvironment)

        every { BeagleEnvironment.beagleSdk.config.cache.memoryMaximumCapacity } returns 15

        MockKAnnotations.init(this)

        cacheManager = CacheManager(
            storeHandler,
            timerCacheStore,
            beagleEnvironment
        )

        every { beagleEnvironment.beagleSdk.config.cache.enabled } returns true
        every { beagleEnvironment.beagleSdk.config.cache.maxAge } returns INVALIDATION_TIME
        every { timerCacheStore.restore(any()) } returns null
        every { storeHandler.restore(StoreType.DATABASE, any(), any()) } returns mapOf()
        every { responseData.statusCode } returns 200
        every { responseData.data } returns byteArrayOf()
        every { responseData.headers } returns mapOf()
        every { storeHandler.save(any(), capture(storeHandlerDataSlot)) } just Runs
        every { responseData.headers } returns mapOf(BEAGLE_HASH to BEAGLE_HASH_VALUE)
        every { responseData.data } returns RESPONSE_BODY.toByteArray()
        every { timerCacheStore.save(
            cacheKey = capture(cacheKeySlot),
            timerCache = capture(timerCacheSlot)
        ) } just Runs
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun restoreBeagleCacheForUrl_should_return_null_when_cache_is_disabled() {
        // Given
        every { beagleEnvironment.beagleSdk.config.cache.enabled } returns false

        // When
        val actual = cacheManager.restoreBeagleCacheForUrl(URL)

        // Then
        verify(exactly = 0) { timerCacheStore.restore(any()) }
        assertNull(actual)
    }

    @Test
    fun screenRequestWithHash() {
    }

    @Test
    fun handleResponseData() {
    }
}