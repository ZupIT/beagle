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
import br.com.zup.beagle.core.ServerDrivenComponent
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

private val URL = RandomData.string()
private val REQUEST_DATA = RequestData(url = URL)

@DisplayName("Given a Component Requester")
@ExperimentalCoroutinesApi
class ComponentRequesterTest : BaseTest() {

    private val beagleApi: BeagleApi = mockk()
    private val serializer: BeagleSerializer = mockk()
    private val cacheManager: CacheManager = mockk()
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
    }

    @DisplayName("When beagleCache is not expired")
    @Nested
    inner class BeagleCacheIsNotExpiredTest {

        @DisplayName("Then should deserialize cached json")
        @Test
        fun testDeserializeCachedJson() = runBlockingTest {
            // Given
            val component = mockk<ServerDrivenComponent>()
            val jsonMock = "jsonMock"

            every {
                cacheManager.restoreBeagleCacheForUrl(REQUEST_DATA.url!!)
            } returns beagleCache
            every { beagleCache.isExpired() } returns false
            every { beagleCache.json } returns jsonMock
            every { serializer.deserializeComponent(jsonMock) } returns component

            // When
            val actualComponent = componentRequester.fetchComponent(REQUEST_DATA)

            // Then
            verify { serializer.deserializeComponent(jsonMock) }
            assertEquals(component, actualComponent)
        }

    }

    @DisplayName("When beagleCache is expired")
    @Nested
    inner class BeagleCacheIsExpiredTest {

        @DisplayName("Then sohuld fetch from api and deserialize this new json")
        @Test
        fun testDeserializeCachedJson() = runBlockingTest {
            // Given
            val newRequestDataMock = mockk<RequestData>()
            val requestDataMock = mockk<RequestData>()
            val responseDataMock = mockk<ResponseData>()
            val newJsonMock = "newJsonMock"
            val expected = mockk<ServerDrivenComponent>()

            every { cacheManager.restoreBeagleCacheForUrl(REQUEST_DATA.url!!) } returns beagleCache
            every { beagleCache.isExpired() } returns true
            every { cacheManager.requestDataWithCache(REQUEST_DATA, beagleCache) } returns newRequestDataMock
            coEvery { beagleApi.fetchData(newRequestDataMock) } returns responseDataMock
            every { cacheManager.handleResponseData(REQUEST_DATA.url!!, beagleCache, responseDataMock) } returns newJsonMock
            every { serializer.deserializeComponent(newJsonMock) } returns expected

            // When
            val result = componentRequester.fetchComponent(REQUEST_DATA)

            //Then
            coVerifySequence {
                cacheManager.restoreBeagleCacheForUrl(REQUEST_DATA.url!!)
                cacheManager.requestDataWithCache(REQUEST_DATA, beagleCache)
                beagleApi.fetchData(newRequestDataMock)
                cacheManager.handleResponseData(REQUEST_DATA.url!!, beagleCache, responseDataMock)
                serializer.deserializeComponent(newJsonMock)
            }

            assertEquals(expected, result)
        }

    }

}
