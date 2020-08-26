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
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.testutil.RandomData
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.net.URI
import kotlin.test.assertEquals
import kotlin.test.assertFails

private val PATH = RandomData.httpUrl()
private val REQUEST_DATA = RequestData(URI(PATH))
private val BASE_URL = RandomData.string()
private val FINAL_URL = RandomData.string()

@ExperimentalCoroutinesApi
class BeagleApiTest {

    private val requestDataSlot = mutableListOf<RequestData>()
    private val onSuccessSlot = slot<(responseData: ResponseData) -> Unit>()
    private val onErrorSlot = slot<(responseData: ResponseData) -> Unit>()

    @MockK
    private lateinit var httpClient: HttpClient

    @MockK
    private lateinit var urlBuilder: UrlBuilder

    @MockK
    private lateinit var beagleEnvironment: BeagleEnvironment

    @MockK
    private lateinit var requestCall: RequestCall

    @MockK
    private lateinit var responseData: ResponseData

    private lateinit var beagleApi: BeagleApi

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        mockkObject(BeagleMessageLogs)

        beagleApi = BeagleApi(httpClient)

        mockListenersAndExecuteHttpClient()

        every { beagleEnvironment.beagleSdk.config.baseUrl } returns BASE_URL
        every { urlBuilder.format(any(), any()) } returns FINAL_URL
        every { BeagleMessageLogs.logHttpRequestData(any()) } just Runs
        every { BeagleMessageLogs.logHttpResponseData(any()) } just Runs
        every { BeagleMessageLogs.logUnknownHttpError(any()) } just Runs
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun fetchComponent_should_call_logHttpResponseData_and_return() = runBlockingTest {
        // Given, When
        val data = beagleApi.fetchData(REQUEST_DATA)

        // Then
        verify(exactly = once()) { BeagleMessageLogs.logHttpResponseData(responseData) }
        assertEquals(data, responseData)
    }

    @Test
    fun fetch_should_return_a_exception_when_some_http_call_fails() = runBlockingTest {
        // Given
        val responseData: ResponseData = mockk()
        val message = "fetchData error for url ${REQUEST_DATA.uri}"
        val expectedException = BeagleApiException(responseData, REQUEST_DATA, message)
        mockListenersAndExecuteHttpClient { onErrorSlot.captured(responseData) }

        // When
        val exceptionThrown = assertFails(message) {
            beagleApi.fetchData(REQUEST_DATA)
        }

        // Then
        assertEquals(expectedException.message, exceptionThrown.message)
        verify(exactly = once()) { BeagleMessageLogs.logUnknownHttpError(expectedException) }
    }

    @Test
    fun `fetch should add fixed headers`() = runBlockingTest {
        // Given
        mockListenersAndExecuteHttpClient()

        // When
        beagleApi.fetchData(REQUEST_DATA)

        // Then
        checkFixedHeaders(requestDataSlot[0])
    }

    @Test
    fun `fetch should add fixed headers to existing ones with different keys`() = runBlockingTest {
        // Given
        val headers = mapOf("a" to RandomData.string())
        mockListenersAndExecuteHttpClient()

        // When
        beagleApi.fetchData(REQUEST_DATA.copy(headers = headers))

        // Then
        checkFixedHeaders(requestDataSlot[0])
        assertEquals(headers["a"], requestDataSlot[0].headers["a"])
    }

    @Test
    fun `fetch should replace existing headers with same keys as fixed ones`() = runBlockingTest {
        // Given
        val headers = mapOf(BeagleApi.CONTENT_TYPE to RandomData.string())
        mockListenersAndExecuteHttpClient()

        // When
        beagleApi.fetchData(REQUEST_DATA.copy(headers = headers))

        // Then
        checkFixedHeaders(requestDataSlot[0])
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
    }
}
