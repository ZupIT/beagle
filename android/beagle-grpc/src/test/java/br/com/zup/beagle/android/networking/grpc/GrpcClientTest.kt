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

package br.com.zup.beagle.android.networking.grpc

import beagle.Messages
import beagle.ScreenControllerGrpcKt
import br.com.zup.beagle.android.networking.HttpAdditionalData
import br.com.zup.beagle.android.networking.HttpClient
import br.com.zup.beagle.android.networking.HttpMethod
import br.com.zup.beagle.android.networking.RequestData
import br.com.zup.beagle.android.networking.ResponseData
import io.grpc.Metadata
import io.grpc.Status
import io.grpc.StatusException
import io.grpc.stub.MetadataUtils
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.net.URI

// TODO: ver como testar envio e recebimento de headers

private const val GRPC_ADDRESS = "http://grpc.test"
private const val REQUEST_HTTP_URL = "http://test"
private val REQUEST_HTTP_DATA = RequestData(URI(""), url = REQUEST_HTTP_URL)
private const val REQUEST_GRPC_URL = "http://grpc.test/someEndpoint?param=paramValue&param2=paramValue2"
private val REQUEST_GRPC_DATA = RequestData(URI(""), url = REQUEST_GRPC_URL)

@DisplayName("Given a GrpcClient")
@ExperimentalCoroutinesApi
class GrpcClientTest {

    private val httpClient: HttpClient = mockk()
    private lateinit var grpcClient: GrpcClient

    @BeforeEach
    fun setUp() {
        grpcClient = GrpcClient(GRPC_ADDRESS, httpClient, TestCoroutineDispatcher())

        mockkStatic(MetadataUtils::class)
        mockkConstructor(ScreenControllerGrpcKt.ScreenControllerCoroutineStub::class)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @DisplayName("When execute is called for a non grpc call")
    @Nested
    inner class ExecuteNonGrpcCallTest {

        @DisplayName("Then should redirect the call to HttpClient")
        @Test
        fun testHttpClientCall() = runBlockingTest {
            val onSuccess: (ResponseData) -> Unit = {}
            val onError: (ResponseData) -> Unit = {}

            grpcClient.execute(REQUEST_HTTP_DATA, onSuccess, onError)

            verify(exactly = 1) { httpClient.execute(REQUEST_HTTP_DATA, onSuccess, onError) }
        }

        @DisplayName("Then should call onError")
        @Test
        fun testNullHttpClientCall() = runBlockingTest {
            var errorResponseData: ResponseData? = null
            val onSuccess: (ResponseData) -> Unit = {}
            val onError: (ResponseData) -> Unit = { responseData -> errorResponseData = responseData }

            grpcClient = GrpcClient(GRPC_ADDRESS, null, TestCoroutineDispatcher())
            grpcClient.execute(REQUEST_HTTP_DATA, onSuccess, onError)

            assertEquals(-1, errorResponseData?.statusCode)
            assertEquals(GrpcClient.HTTP_CLIENT_NULL, String(errorResponseData?.data!!))
        }

    }

    @DisplayName("When execute is called for a grpc call")
    @Nested
    inner class ExecuteGrpcCallTest {

        @DisplayName("Then should call onError for empty screen name")
        @Test
        fun testEmptyScreenNameCall() = runBlockingTest {
            val requestData = RequestData(URI(""), url = GRPC_ADDRESS)
            var errorResponseData: ResponseData? = null
            val onSuccess: (ResponseData) -> Unit = {}
            val onError: (ResponseData) -> Unit = { responseData -> errorResponseData = responseData }

            grpcClient.execute(requestData, onSuccess, onError)

            assertEquals(-1, errorResponseData?.statusCode)
            assertEquals(GrpcClient.SCREEN_NAME_NULL, String(errorResponseData?.data!!))

        }

        @DisplayName("Then should add headers to request")
        @Test
        fun testAddRequestHeaders() = runBlockingTest {
            val headerKey = "headerKey"
            val expectedHeaderValue = "headerValue"
            val requestData = RequestData(URI(""), url = REQUEST_GRPC_URL, httpAdditionalData = HttpAdditionalData(headers = mapOf(headerKey to expectedHeaderValue)))

            val stubSlot = slot<ScreenControllerGrpcKt.ScreenControllerCoroutineStub>()
            val headersSlot = slot<Metadata>()
            every { MetadataUtils.attachHeaders(capture(stubSlot), capture(headersSlot)) } answers {
                // TODO: verificar alternativa
                stubSlot.captured.channel
                stubSlot.captured
            }

            grpcClient.execute(requestData, {}, {})

            val metadataKey = Metadata.Key.of(headerKey, Metadata.ASCII_STRING_MARSHALLER)
            assertEquals(expectedHeaderValue, headersSlot.captured.get(metadataKey))
        }

        @DisplayName("Then should add headers interceptor to request")
        @Test
        fun testRequestHeadersInterceptor() = runBlockingTest {
            val requestData = RequestData(URI(""), url = REQUEST_GRPC_URL)
            val interceptorSlot = slot<HeaderClientInterceptor>()
            every { anyConstructed<ScreenControllerGrpcKt.ScreenControllerCoroutineStub>().withInterceptors(capture(interceptorSlot)) } answers {
                this.callOriginal()
            }

            grpcClient.execute(requestData, {}, {})

            assertNotNull(interceptorSlot.captured)
        }

        @DisplayName("Then should send correct request message")
        @Test
        fun testSendCorrectRequestMessage() = runBlockingTest {
            val responseMock: Messages.ViewNode = mockk(relaxed = true)
            val messageSlot = slot<Messages.ScreenRequest>()
            coEvery { anyConstructed<ScreenControllerGrpcKt.ScreenControllerCoroutineStub>().getScreen(capture(messageSlot)) } returns responseMock

            grpcClient.execute(REQUEST_GRPC_DATA, {}, {})

            val expectedScreenName = "someEndpoint"
            val expectedParams = "{\"param\":\"paramValue\",\"param2\":\"paramValue2\"}"
            val screenRequestMessage = messageSlot.captured

            assertEquals(expectedScreenName, screenRequestMessage.name)
            assertEquals(expectedParams, screenRequestMessage.parameters)

        }

        @DisplayName("Then should send correct request params for POST requests")
        @Test
        fun testSendCorrectParamsForPost() = runBlockingTest {
            val requestBody = "{param:paramValue}"
            val additionalData = HttpAdditionalData(method = HttpMethod.POST, body = requestBody)
            val request = RequestData(URI(""), url = REQUEST_GRPC_URL, httpAdditionalData = additionalData)
            val responseMock: Messages.ViewNode = mockk(relaxed = true)
            val messageSlot = slot<Messages.ScreenRequest>()
            coEvery { anyConstructed<ScreenControllerGrpcKt.ScreenControllerCoroutineStub>().getScreen(capture(messageSlot)) } returns responseMock

            grpcClient.execute(request, {}, {})

            val screenRequestMessage = messageSlot.captured

            assertEquals(requestBody, screenRequestMessage.parameters)

        }

        @DisplayName("Then should parse server error response and call onError")
        @Test
        fun testServerErrorCall() = runBlockingTest {
            val requestData = RequestData(URI(""), url = REQUEST_GRPC_URL)
            var errorResponseData: ResponseData? = null
            val onSuccess: (ResponseData) -> Unit = {}
            val onError: (ResponseData) -> Unit = { responseData -> errorResponseData = responseData }
            val serverExceptionDescription = "Cannot find screen"
            val serverExceptionStatusMock = Status.ABORTED.withDescription(serverExceptionDescription)
            val serverExceptionMock = StatusException(serverExceptionStatusMock)
            coEvery { anyConstructed<ScreenControllerGrpcKt.ScreenControllerCoroutineStub>().getScreen(any()) } throws serverExceptionMock


            grpcClient.execute(requestData, onSuccess, onError)

            assertEquals(-1, errorResponseData?.statusCode)
            assertEquals(serverExceptionDescription, String(errorResponseData?.data!!))

        }

        @DisplayName("Then should parse server success response and call onSuccess")
        @Test
        fun testServerSuccessCall() = runBlockingTest {
            val requestData = RequestData(URI(""), url = REQUEST_GRPC_URL)
            var successResponseData: ResponseData? = null
            val onSuccess: (ResponseData) -> Unit = { responseData -> successResponseData = responseData }
            val onError: (ResponseData) -> Unit = { }

            coEvery { anyConstructed<ScreenControllerGrpcKt.ScreenControllerCoroutineStub>().getScreen(any()) } returns viewNode


            grpcClient.execute(requestData, onSuccess, onError)

            assertEquals(200, successResponseData?.statusCode)
            assertEquals(viewNodeString, String(successResponseData?.data!!))

        }

        // TODO: Parse Response - Headers
    }
}