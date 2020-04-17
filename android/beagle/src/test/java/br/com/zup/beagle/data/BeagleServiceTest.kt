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

import br.com.zup.beagle.action.Action
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.data.cache.BeagleCacheHelper
import br.com.zup.beagle.data.serializer.BeagleSerializer
import br.com.zup.beagle.data.serializer.makeScreenJson
import br.com.zup.beagle.exception.BeagleException
import br.com.zup.beagle.extensions.once
import br.com.zup.beagle.logger.BeagleMessageLogs
import br.com.zup.beagle.networking.HttpClient
import br.com.zup.beagle.networking.RequestCall
import br.com.zup.beagle.networking.ResponseData
import br.com.zup.beagle.networking.urlbuilder.UrlBuilder
import br.com.zup.beagle.setup.BeagleEnvironment
import br.com.zup.beagle.testutil.CoroutineTestRule
import br.com.zup.beagle.testutil.RandomData
import br.com.zup.beagle.view.ScreenRequest
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

private val URL = RandomData.httpUrl()
private val JSON_SUCCESS = makeScreenJson()
private const val JSON_ERROR = ""
private val screenRequest = ScreenRequest(URL)
private val screenError = ScreenRequest(JSON_ERROR)

@ExperimentalCoroutinesApi
class BeagleServiceTest {

    private val onSuccessSlot = slot<(responseData: ResponseData) -> Unit>()
    private val onErrorSlot = slot<(throwable: Throwable) -> Unit>()

    @MockK
    private lateinit var serializer: BeagleSerializer
    @MockK
    private lateinit var urlBuilder: UrlBuilder
    @MockK
    private lateinit var httpClient: HttpClient
    @MockK
    private lateinit var component: ServerDrivenComponent
    @MockK
    private lateinit var action: Action
    @MockK
    private lateinit var requestCall: RequestCall
    @MockK
    private lateinit var responseData: ResponseData

    @get:Rule
    val scope = CoroutineTestRule()

    @InjectMockKs
    private lateinit var beagleService: BeagleService

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        mockkObject(BeagleMessageLogs)
        mockkObject(BeagleEnvironment)
        mockkObject(BeagleCacheHelper)

        mockListenerExecution { onSuccessSlot.captured(responseData) }
        every { BeagleEnvironment.beagleSdk.config.baseUrl } returns RandomData.httpUrl()
        every { BeagleMessageLogs.logHttpRequestData(any()) } just Runs
        every { BeagleMessageLogs.logHttpResponseData(any()) } just Runs
        every { BeagleMessageLogs.logUnknownHttpError(any()) } just Runs
        every { serializer.deserializeComponent(any()) } returns component
        every { BeagleCacheHelper.getFromCache(any()) } returns null
        every { BeagleCacheHelper.cache(any(), any()) } returns component
        every { serializer.deserializeAction(any()) } returns action
        every { responseData.data } returns JSON_SUCCESS.toByteArray()
        every { urlBuilder.format(any(), any()) } returns URL
    }

    @After
    fun tearDown() {
        unmockkObject(BeagleCacheHelper)
        unmockkObject(BeagleMessageLogs)
        unmockkObject(BeagleEnvironment)
    }

    private fun mockListenerExecution(executionLambda: () -> Unit) {
        every {
            httpClient.execute(
                any(),
                onSuccess = capture(onSuccessSlot),
                onError = capture(onErrorSlot)
            )
        } answers {
            executionLambda()
            requestCall
        }
    }

    @Test
    fun fetch_should_deserialize_a_component_response() = runBlockingTest {
        val result = beagleService.fetchComponent(screenRequest)

        verify(exactly = once()) { serializer.deserializeComponent(JSON_SUCCESS) }
        assertEquals(component, result)
    }

    @Test
    fun fetch_should_return_a_exception_when_some_http_call_fails() = runBlockingTest {
        // Given
        val message = RandomData.string()
        val expectedException = BeagleException(message)
        mockListenerExecution { onErrorSlot.captured(expectedException) }

        // When
        val exceptionResponse = assertFails(message) {
            beagleService.fetchComponent(screenError)
        }

        // Then
        assertEquals(expectedException.message, exceptionResponse.message)
    }

    @Test
    fun fetch_should_return_a_exception_when_http_throws_a_exception() = runBlockingTest {
        // Given
        val exception = RuntimeException()
        every { httpClient.execute(any(), any(), any()) } throws exception

        // When
        val exceptionResponse = assertFails {
            beagleService.fetchComponent(screenError)
        }

        // Then
        assertTrue(exceptionResponse is BeagleException)
    }

    @Test
    fun fetch_should_return_a_exception_moshi_deserialization_fails() = runBlockingTest {
        // Given
        val exception = BeagleException(RandomData.string())
        every { serializer.deserializeComponent(any()) } throws exception

        // When
        val exceptionResponse =
            assertFails("Widget deserializer error with respective json: $JSON_ERROR") {
                beagleService.fetchComponent(screenError)
            }

        // Then
        assertTrue(exceptionResponse is BeagleException)
    }

    @Test
    fun fetchAction_should_deserialize_a_action_response() = runBlockingTest {
        val actionResult = beagleService.fetchAction(URL)

        verify(exactly = once()) { serializer.deserializeAction(JSON_SUCCESS) }
        assertEquals(action, actionResult)
    }

    @Test
    fun fetchAction_should_return_a_exception_moshi_deserialization_fails() = runBlockingTest {
        // Given
        val exception = BeagleException(RandomData.string())
        every { serializer.deserializeAction(any()) } throws exception

        // When
        val exceptionResponse =
            assertFails("Action deserializer error with respective json: $JSON_ERROR") {
                beagleService.fetchAction(JSON_ERROR)
            }

        // Then
        assertTrue(exceptionResponse is BeagleException)
    }
}