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


package br.com.zup.beagle.android.data

import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.cache.BeagleCache
import br.com.zup.beagle.android.cache.CacheManager
import br.com.zup.beagle.android.data.serializer.BeagleSerializer
import br.com.zup.beagle.android.networking.RequestData
import br.com.zup.beagle.android.networking.ResponseData
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.view.ScreenRequest
import br.com.zup.beagle.android.view.mapper.toRequestData
import br.com.zup.beagle.core.ServerDrivenComponent
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import kotlin.test.assertEquals

private val URL = RandomData.string()
private val SCREEN_REQUEST = ScreenRequest(URL)
private val RESPONSE_BODY = RandomData.string()

@ExperimentalCoroutinesApi
class ComponentRequesterTest : BaseTest() {

    @MockK
    private lateinit var beagleApi: BeagleApi

    @MockK
    private lateinit var serializer: BeagleSerializer

    @MockK
    private lateinit var cacheManager: CacheManager

    private val beagleCache = mockk<BeagleCache> {
        every { isHot } returns true
        every { json } returns RESPONSE_BODY
    }

    private lateinit var componentRequester: ComponentRequester

    override fun setUp() {
        super.setUp()

        componentRequester = ComponentRequester(
            beagleApi,
            serializer,
            cacheManager
        )
        mockkStatic("br.com.zup.beagle.android.view.mapper.ScreenRequestMapperKt")
    }

    override fun tearDown() {
        super.tearDown()
        unmockkAll()
    }

    @Test
    fun fetchComponent_should_restore_json_from_hot_cache() = runBlockingTest {
        // Given
        val component = mockk<ServerDrivenComponent>()

        every { cacheManager.restoreBeagleCacheForUrl(any()) } returns beagleCache
        every { serializer.deserializeComponent(any()) } returns component

        // When
        val actualComponent = componentRequester.fetchComponent(SCREEN_REQUEST)

        // Then
        verify { serializer.deserializeComponent(RESPONSE_BODY) }
        assertEquals(component, actualComponent)
    }

    @Test
    fun fetchComponent_should_call_api() = runBlockingTest {
        // Given
        val component = mockk<ServerDrivenComponent>()
        val responseData = mockk<ResponseData>()
        val requestData: RequestData = mockk()
        every { beagleCache.isHot } returns false

        every { cacheManager.restoreBeagleCacheForUrl(any()) } returns beagleCache
        every { cacheManager.screenRequestWithCache(any(), any()) } returns SCREEN_REQUEST
        every { any<ScreenRequest>().toRequestData(any(), any()) } returns requestData
        coEvery { beagleApi.fetchData(requestData) } returns responseData
        every { cacheManager.handleResponseData(any(), any(), any()) } returns RESPONSE_BODY
        every { serializer.deserializeComponent(any()) } returns component

        // When
        val actualComponent = componentRequester.fetchComponent(SCREEN_REQUEST)

        // Then
        coVerifySequence {
            cacheManager.restoreBeagleCacheForUrl(URL)
            cacheManager.screenRequestWithCache(SCREEN_REQUEST, beagleCache)
            beagleApi.fetchData(requestData)
            cacheManager.handleResponseData(URL, beagleCache, responseData)
            serializer.deserializeComponent(RESPONSE_BODY)
        }
        assertEquals(component, actualComponent)
    }
}
