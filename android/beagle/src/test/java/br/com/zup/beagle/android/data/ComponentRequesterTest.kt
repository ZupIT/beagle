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
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import java.net.URI

private val URL = RandomData.string()
private val SCREEN_REQUEST = RequestData(url = URL, uri = URI(""))

@ExperimentalCoroutinesApi
class ComponentRequesterTest : BaseTest() {

    @MockK
    private lateinit var beagleApi: BeagleApi

    @MockK
    private lateinit var serializer: BeagleSerializer

    @MockK
    private lateinit var cacheManager: CacheManager

    private val beagleCache = mockk<BeagleCache>()

    private lateinit var componentRequester: ComponentRequester

    @BeforeEach
    override fun setUp() {
        super.setUp()

        componentRequester = ComponentRequester(
            beagleApi,
            serializer,
            cacheManager
        )
        mockkStatic("br.com.zup.beagle.android.view.mapper.ScreenRequestMapperKt")
    }

    @Test
    fun `GIVEN a componentRequest with json in Cache WHEN beagleCache is not expired SHOULD deserialize this cachedJson` () = runBlockingTest {
        // Given
        val component = mockk<ServerDrivenComponent>()
        val jsonMock = "jsonMock"

        every {
            cacheManager.restoreBeagleCacheForUrl(SCREEN_REQUEST.url)
        } returns beagleCache
        every { beagleCache.isExpired() } returns false
        every { beagleCache.json } returns jsonMock
        every { serializer.deserializeComponent(jsonMock) } returns component

        // When
        val actualComponent = componentRequester.fetchComponent(SCREEN_REQUEST)

        // Then
        verify { serializer.deserializeComponent(jsonMock) }
        assertEquals(component, actualComponent)
    }

    @Test
    fun `GIVEN a componentRequest with json in cache WHEN beagleCache is expired SHOULD fetch from api and deserialize this new json`() = runBlockingTest {
        // Given
        val newScreenRequestMock = mockk<RequestData>()
        val requestDataMock = mockk<RequestData>()
        val responseDataMock = mockk<ResponseData>()
        val newJsonMock = "newJsonMock"
        val expected = mockk<ServerDrivenComponent>()

        every { cacheManager.restoreBeagleCacheForUrl(SCREEN_REQUEST.url) } returns beagleCache
        every { beagleCache.isExpired() } returns true
        every { cacheManager.screenRequestWithCache(SCREEN_REQUEST, beagleCache) } returns newScreenRequestMock
        coEvery { beagleApi.fetchData(requestDataMock) } returns responseDataMock
        every { cacheManager.handleResponseData(SCREEN_REQUEST.url, beagleCache, responseDataMock) } returns newJsonMock
        every { serializer.deserializeComponent(newJsonMock) } returns expected

        // When
        val result = componentRequester.fetchComponent(SCREEN_REQUEST)

        //Then
        coVerifySequence {
            cacheManager.restoreBeagleCacheForUrl(SCREEN_REQUEST.url)
            cacheManager.screenRequestWithCache(SCREEN_REQUEST, beagleCache)
            beagleApi.fetchData(requestDataMock)
            cacheManager.handleResponseData(SCREEN_REQUEST.url, beagleCache, responseDataMock)
            serializer.deserializeComponent(newJsonMock)
        }

        assertEquals(expected, result)
    }
}
