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

import android.util.LruCache
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.testutil.RandomData
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

private val CACHE_KEY = RandomData.string()

class LruCacheStoreTest {

    @MockK
    private lateinit var cachedData: LruCache<String, BeagleCache>

    private lateinit var cacheStore: LruCacheStore

    @MockK
    private lateinit var beagleCache: BeagleCache

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)

        mockkObject(BeagleEnvironment)

        cacheStore = LruCacheStore(cache = cachedData)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun save_should_add_new_beagleHashKey_to_cache() {
        // Given
        val timerCacheSlot = slot<BeagleCache>()
        every { cachedData.put(any(), capture(timerCacheSlot)) } returns null

        // When
        cacheStore.save(CACHE_KEY, beagleCache)

        // Then
        verify(exactly = once()) { cachedData.put(CACHE_KEY, timerCacheSlot.captured) }
        assertEquals(timerCacheSlot.captured, beagleCache)
    }

    @Test
    fun restore_should_return_cached_timerCache() {
        // Given
        every { cachedData[CACHE_KEY] } returns beagleCache

        // When
        val actualTimerCache = cacheStore.restore(CACHE_KEY)

        // Then
        assertEquals(beagleCache, actualTimerCache)
    }
}