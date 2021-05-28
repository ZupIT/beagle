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

package br.com.zup.beagle.android.view.mapper

import br.com.zup.beagle.android.networking.HttpAdditionalData
import br.com.zup.beagle.android.networking.HttpMethod
import br.com.zup.beagle.android.networking.RequestData
import br.com.zup.beagle.android.networking.urlbuilder.UrlBuilder
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.view.ScreenMethod
import br.com.zup.beagle.android.view.ScreenRequest
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

private val PATH = RandomData.httpUrl()
private val SCREEN_REQUEST = ScreenRequest(
    url = PATH,
    body = "body",
    headers = mapOf("header" to "teste"),
)
private val EXPECTED_RESULT = RequestData(
    url = PATH,
    httpAdditionalData = HttpAdditionalData(
        headers = mapOf("header" to "teste"),
        body = "body",
    ),
)

@DisplayName("Given a ScreenRequestMapper")
class ScreenRequestMapperTest {

    private val urlBuilder: UrlBuilder = mockk()
    private val environment: BeagleEnvironment = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        every { urlBuilder.format(environment.beagleSdk.config.baseUrl, SCREEN_REQUEST.url) } returns PATH
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Nested
    @DisplayName("When toRequestData is called")
    inner class ToRequestData {

        @Test
        @DisplayName("Then should convert to RequestData via extension function")
        fun requestDataViaExtension() {
            // When
            val requestData = SCREEN_REQUEST.toRequestData(urlBuilder, environment)

            // Then
            assertEquals(EXPECTED_RESULT, requestData)
        }

        @Test
        @DisplayName("Then should return RequestData via mapper")
        fun requestDataViaMapper() {
            // When
            val requestData = ScreenRequestMapper.toRequestData(urlBuilder, environment, SCREEN_REQUEST)

            // Then
            assertEquals(EXPECTED_RESULT, requestData)
        }

        @Test
        @DisplayName("Then should return RequestData with correct method")
        fun requestDataMethod() {
            // Given
            val screenRequests = listOf(
                SCREEN_REQUEST.copy(method = ScreenMethod.GET),
                SCREEN_REQUEST.copy(method = ScreenMethod.POST),
                SCREEN_REQUEST.copy(method = ScreenMethod.PUT),
                SCREEN_REQUEST.copy(method = ScreenMethod.DELETE),
                SCREEN_REQUEST.copy(method = ScreenMethod.HEAD),
                SCREEN_REQUEST.copy(method = ScreenMethod.PATCH)
            )

            // When
            val result = mutableListOf<RequestData>()
            screenRequests.forEach {
                result.add(ScreenRequestMapper.toRequestData(urlBuilder, environment, it))
            }

            // Then
            val expectedResult = screenRequests.map {
                val method = HttpMethod.valueOf(it.method.name)
                val httpAdditionalData = EXPECTED_RESULT.httpAdditionalData.copy(method = method)
                EXPECTED_RESULT.copy(method = method, httpAdditionalData = httpAdditionalData)
            }
            assertEquals(expectedResult, result)
        }
    }
}
