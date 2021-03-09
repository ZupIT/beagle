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
import org.junit.jupiter.api.AfterEach
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.URI

private val PATH = RandomData.httpUrl()
private val SCREEN_REQUEST = ScreenRequest(
    url = PATH,
    body = "body",
    headers = mapOf("header" to "teste"),
)
private val EXPECTED_RESULT = RequestData(
    uri = URI(""),
    body = "body",
    headers = mapOf("header" to "teste"),
    httpAdditionalData = HttpAdditionalData(
        headers = mapOf("header" to "teste"),
        body = "body",
    ),
)

class ScreenRequestMapperTest {

    private val urlBuilder: UrlBuilder = mockk()
    private val environment: BeagleEnvironment = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        every { urlBuilder.format(environment.beagleSdk.config.baseUrl, SCREEN_REQUEST.url) } returns ""
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `should return request data when using extension function to mapper`() {
        // When
        val requestData = SCREEN_REQUEST.toRequestData(urlBuilder, environment)

        // Then
        assertEquals(EXPECTED_RESULT, requestData)
    }

    @Test
    fun `should return request data when mapper`() {
        // When
        val requestData = ScreenRequestMapper.toRequestData(urlBuilder, environment, SCREEN_REQUEST)

        // Then
        assertEquals(EXPECTED_RESULT, requestData)
    }

    @Test
    fun `should return request data with correct method when mapper`() {
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