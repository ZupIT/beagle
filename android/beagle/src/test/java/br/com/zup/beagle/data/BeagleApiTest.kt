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

package br.com.zup.beagle.data

import br.com.zup.beagle.exception.BeagleException
import br.com.zup.beagle.extensions.once
import br.com.zup.beagle.logger.BeagleMessageLogs
import br.com.zup.beagle.networking.*
import br.com.zup.beagle.networking.urlbuilder.UrlBuilder
import br.com.zup.beagle.setup.BeagleEnvironment
import br.com.zup.beagle.testutil.RandomData
import br.com.zup.beagle.view.ScreenMethod
import br.com.zup.beagle.view.ScreenRequest
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

import kotlin.test.assertFails

private val PATH = RandomData.httpUrl()
private val SCREEN_REQUEST = ScreenRequest(PATH)
private val BASE_URL = RandomData.string()
private val FINAL_URL = RandomData.string()

@ExperimentalCoroutinesApi
class BeagleApiTest {

    private val requestDataSlot = mutableListOf<RequestData>()
    private val onSuccessSlot = slot<(responseData: ResponseData) -> Unit>()
    private val onErrorSlot = slot<(throwable: Throwable) -> Unit>()

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

        beagleApi = BeagleApi(httpClient, urlBuilder, beagleEnvironment)

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
        val data = beagleApi.fetchData(SCREEN_REQUEST)

        // Then
        verify(exactly = once()) { BeagleMessageLogs.logHttpResponseData(responseData) }
        assertEquals(data, responseData)
    }

    @Test
    fun fetchComponent_should_create_requestData() = runBlockingTest {
        // Given
        val screenRequest = ScreenRequest(
            url = PATH,
            method = ScreenMethod.POST,
            body = RandomData.string()
        )

        // When
        beagleApi.fetchData(screenRequest)

        // Then
        val requestData = requestDataSlot[0]
        assertEquals(FINAL_URL, requestData.uri.toString())
        assertEquals(HttpMethod.POST, requestData.method)
        assertEquals(screenRequest.body, requestData.body)
        assertEquals(1, requestData.headers.size)
        assertEquals("application/json", requestData.headers["Content-Type"])
    }

    @Test
    fun fetchComponent_should_create_requestData_for_each_HttpMethod() = runBlockingTest {
        // Given
        val screenRequest = listOf(
            SCREEN_REQUEST.copy(method = ScreenMethod.GET),
            SCREEN_REQUEST.copy(method = ScreenMethod.POST),
            SCREEN_REQUEST.copy(method = ScreenMethod.PUT),
            SCREEN_REQUEST.copy(method = ScreenMethod.DELETE),
            SCREEN_REQUEST.copy(method = ScreenMethod.HEAD),
            SCREEN_REQUEST.copy(method = ScreenMethod.PATCH)
        )

        // When
        screenRequest.forEach {
            beagleApi.fetchData(it)
        }

        // Then
        assertEquals(HttpMethod.GET, requestDataSlot[0].method)
        assertEquals(HttpMethod.POST, requestDataSlot[1].method)
        assertEquals(HttpMethod.PUT, requestDataSlot[2].method)
        assertEquals(HttpMethod.DELETE, requestDataSlot[3].method)
        assertEquals(HttpMethod.HEAD, requestDataSlot[4].method)
        assertEquals(HttpMethod.PATCH, requestDataSlot[5].method)
    }

    @Test
    fun fetch_should_return_a_exception_when_some_http_call_fails() = runBlockingTest {
        // Given
        val message = RandomData.string()
        val expectedException = BeagleException(message)
        mockListenersAndExecuteHttpClient { onErrorSlot.captured(expectedException) }

        // When
        val exceptionThrown = assertFails(message) {
            beagleApi.fetchData(SCREEN_REQUEST)
        }

        // Then
        assertEquals(expectedException.message, exceptionThrown.message)
        verify(exactly = once()) { BeagleMessageLogs.logUnknownHttpError(expectedException) }
    }

    @Test
    fun fetch_should_return_a_exception_when_http_throws_a_exception() = runBlockingTest {
        // Given
        val exception = RuntimeException()
        every { httpClient.execute(any(), any(), any()) } throws exception

        // When
        val exceptionResponse = assertFails {
            beagleApi.fetchData(SCREEN_REQUEST)
        }

        // Then
        kotlin.test.assertTrue(exceptionResponse is BeagleException)
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
}