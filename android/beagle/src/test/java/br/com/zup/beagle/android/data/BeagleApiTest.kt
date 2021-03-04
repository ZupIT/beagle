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

import br.com.zup.beagle.android.exception.BeagleApiException
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.networking.HttpClient
import br.com.zup.beagle.android.networking.RequestCall
import br.com.zup.beagle.android.networking.RequestData
import br.com.zup.beagle.android.networking.ResponseData
import br.com.zup.beagle.android.networking.urlbuilder.UrlBuilder
import br.com.zup.beagle.android.networking.urlbuilder.UrlBuilderFactory
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.testutil.RandomData
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.URI
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows

private val FINAL_URL = RandomData.string()
private val REQUEST_DATA = RequestData(URI(""), url = FINAL_URL)

@DisplayName("Given a Beagle Api")
@ExperimentalCoroutinesApi
class BeagleApiTest {

    private val httpClient: HttpClient = mockk()
    private val urlBuilder: UrlBuilder = mockk()
    private val requestCall: RequestCall = mockk()
    private val responseData: ResponseData = mockk()

    private val requestDataSlot = mutableListOf<RequestData>()
    private val onSuccessSlot = slot<(responseData: ResponseData) -> Unit>()
    private val onErrorSlot = slot<(responseData: ResponseData) -> Unit>()

    private lateinit var beagleApi: BeagleApi

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)

        mockkObject(BeagleMessageLogs)
        mockkConstructor(UrlBuilderFactory::class)

        mockkStatic("br.com.zup.beagle.android.data.StringExtensionsKt")

        beagleApi = BeagleApi(httpClient)

        mockListenersAndExecuteHttpClient()

        every { any<String>().formatUrl(any(), any()) } returns FINAL_URL
        every { anyConstructed<UrlBuilderFactory>().make() } returns urlBuilder

        every { BeagleMessageLogs.logHttpRequestData(any()) } just Runs
        every { BeagleMessageLogs.logHttpResponseData(any()) } just Runs
        every { BeagleMessageLogs.logUnknownHttpError(any()) } just Runs
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @DisplayName("When fetch data")
    @Nested
    inner class FetchDataTest {

        @DisplayName("Then should call log http response")
        @Test
        fun testCallLogHttpResponse() = runBlockingTest {
            // Given, When
            val data = beagleApi.fetchData(REQUEST_DATA)

            // Then
            verify(exactly = once()) { BeagleMessageLogs.logHttpResponseData(responseData) }
            assertEquals(data, responseData)
        }

        @DisplayName("Then should add fixed headers")
        @Test
        fun testHasFixedHeader() = runBlockingTest {
            // Given
            mockListenersAndExecuteHttpClient()

            // When
            beagleApi.fetchData(REQUEST_DATA)

            // Then
            checkFixedHeaders(requestDataSlot[0])
        }

        @DisplayName("Then should add fixed headers to existing ones with different keys")
        @Test
        fun testDifferentKeysWasAddInHeader() = runBlockingTest {
            // Given
            val headers = mapOf("a" to RandomData.string())
            mockListenersAndExecuteHttpClient()

            // When
            beagleApi.fetchData(REQUEST_DATA.copy(headers = headers))

            // Then
            checkFixedHeaders(requestDataSlot[0])
            assertEquals(headers["a"], requestDataSlot[0].headers["a"])
        }

        @DisplayName("Then should replace header with same keys as fixed ones")
        @Test
        fun testReplaceHeaderWithSameKey() = runBlockingTest {
            // Given
            val headers = mapOf(BeagleApi.CONTENT_TYPE to RandomData.string())
            mockListenersAndExecuteHttpClient()

            // When
            beagleApi.fetchData(REQUEST_DATA.copy(headers = headers))

            // Then
            checkFixedHeaders(requestDataSlot[0])
        }

    }

    @DisplayName("When fetch data with error")
    @Nested
    inner class FetchDataExceptionTest {

        @DisplayName("Then should return an exception")
        @Test
        fun testExceptionReturnedInHttpCall() = runBlockingTest {
            // Given
            val responseData: ResponseData = mockk()
            val message = "fetchData error for url ${REQUEST_DATA.url}"
            val expectedException = BeagleApiException(responseData, REQUEST_DATA, message)
            mockListenersAndExecuteHttpClient { onErrorSlot.captured(responseData) }

            // When
            val exceptionThrown = assertThrows<BeagleApiException>(message) {
                beagleApi.fetchData(REQUEST_DATA)
            }

            // Then
            assertEquals(expectedException.message, exceptionThrown.message)
            verify(exactly = once()) { BeagleMessageLogs.logUnknownHttpError(expectedException) }
        }

    }

    private fun mockListenersAndExecuteHttpClient(executionLambda: (() -> Unit)? = null) {
        every {
            httpClient.execute(
                capture(requestDataSlot),
                onSuccess = capture(onSuccessSlot),
                onError = capture(onErrorSlot)
            )
        } answers {
            if (executionLambda != null) {
                executionLambda()
            } else {
                onSuccessSlot.captured(responseData)
            }
            requestCall
        }
    }

    private fun checkFixedHeaders(requestData: RequestData) {
        assertEquals(BeagleApi.APP_JSON, requestData.headers[BeagleApi.CONTENT_TYPE])
        assertEquals(BeagleApi.BEAGLE_PLATFORM_HEADER_VALUE, requestData.headers[BeagleApi.BEAGLE_PLATFORM_HEADER_KEY])
        assertEquals(BeagleApi.APP_JSON, requestData.httpAdditionalData.headers!![BeagleApi.CONTENT_TYPE])
        assertEquals(BeagleApi.BEAGLE_PLATFORM_HEADER_VALUE, requestData.httpAdditionalData.headers!![BeagleApi.BEAGLE_PLATFORM_HEADER_KEY])
    }
}
