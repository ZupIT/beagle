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

import android.util.LruCache
import br.com.zup.beagle.extensions.once
import br.com.zup.beagle.setup.BeagleEnvironment
import br.com.zup.beagle.testutil.RandomData
import br.com.zup.beagle.testutil.getPrivateField
import br.com.zup.beagle.testutil.setPrivateField
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

private val CACHE_KEY = RandomData.string()
private val CACHE_MAX_SIZE = RandomData.int()

class LruCacheStoreTest {

    @MockK
    private lateinit var cachedData: LruCache<String, TimerCache>
    @MockK
    private lateinit var timerCache: TimerCache

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        mockkObject(BeagleEnvironment)

        every { BeagleEnvironment.beagleSdk.config.cache.memoryMaximumCapacity } returns CACHE_MAX_SIZE

        LruCacheStore.setPrivateField("cache", cachedData)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun lruCacheStore_should_use_maxSize_from_BeagleEnvironment() {
        // Given When
        val actualMaxAge = LruCacheStore.getPrivateField<Int>("maxSize")

        // Then
        assertEquals(CACHE_MAX_SIZE, actualMaxAge)
    }

    @Test
    fun save_should_add_new_beagleHashKey_to_cache() {
        // Given
        val timerCacheSlot = slot<TimerCache>()
        every { cachedData.put(any(), capture(timerCacheSlot)) } returns null

        // When
        LruCacheStore.save(CACHE_KEY, timerCache)

        // Then
        verify(exactly = once()) { cachedData.put(CACHE_KEY, timerCacheSlot.captured) }
        assertEquals(timerCacheSlot.captured, timerCache)
    }

    @Test
    fun restore_should_return_cached_timerCache() {
        // Given
        every { cachedData[CACHE_KEY] } returns timerCache

        // When
        val actualTimerCache = LruCacheStore.restore(CACHE_KEY)

        // Then
        assertEquals(timerCache, actualTimerCache)
    }
}