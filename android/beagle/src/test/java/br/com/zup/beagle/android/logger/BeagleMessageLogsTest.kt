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

package br.com.zup.beagle.android.logger

import br.com.zup.beagle.android.mockdata.makeRequestData
import br.com.zup.beagle.android.mockdata.makeResponseData
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.core.ServerDrivenComponent
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.*

@DisplayName("Given a Bleagle Message Logs")
class BeagleMessageLogsTest {

    private val beagleLoggerInfoSlot = slot<String>()

    @BeforeEach
    fun setUp() {
        mockkObject(BeagleEnvironment)

        every { BeagleEnvironment.beagleSdk.logger } returns null
        every { BeagleEnvironment.beagleSdk.config.isLoggingEnabled } returns true

        mockkObject(BeagleLoggerProxy)

        every { BeagleLoggerProxy.info(capture(beagleLoggerInfoSlot)) } just Runs
        every {  BeagleLoggerProxy.warning(any()) } just Runs
        every {  BeagleLoggerProxy.error(any()) } just Runs
        every {  BeagleLoggerProxy.error(any(), any()) } just Runs
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun logHttpRequestData_should_call_BeagleLogger_info() {
        // Given
        val requestData = makeRequestData()

        // When
        BeagleMessageLogs.logHttpRequestData(requestData)

        // Then
        assertEquals("""
            *** HTTP REQUEST ***
            Uri=${requestData.uri}
            Method=${requestData.method}
            Headers=${requestData.headers}
            Body=${requestData.body}
        """.trimIndent(), beagleLoggerInfoSlot.captured)
    }

    @Test
    fun logHttpResponseData_should_call_BeagleLogger_info() {
        // Given
        val responseData = makeResponseData()

        // When
        BeagleMessageLogs.logHttpResponseData(responseData)

        // Then
        assertEquals("""
            *** HTTP RESPONSE ***
            StatusCode=${responseData.statusCode}
            Body=${String(responseData.data)}
            Headers=${responseData.headers}
        """.trimIndent(), beagleLoggerInfoSlot.captured)
    }

    @Test
    fun logUnknownHttpError_should_call_BeagleLogger_error() {
        // Given
        val throwable = mockk<Throwable>()

        // When
        BeagleMessageLogs.logUnknownHttpError(throwable)

        // Then
        verify(exactly = 1) {  BeagleLoggerProxy.error("Exception thrown while trying to call http client.", throwable) }
    }

    @Test
    fun logDeserializationError_should_call_BeagleLogger_error() {
        // Given
        val json = RandomData.string()
        val exception = mockk<Exception>()

        // When
        BeagleMessageLogs.logDeserializationError(json, exception)

        // Then
        verify(exactly = 1) {  BeagleLoggerProxy.error(
            "Exception thrown while trying to deserialize the following json: $json", exception) }
    }

    @Test
    fun logViewFactoryNotFoundForWidget_should_call_BeagleLogger_warning() {
        // Given
        val widget = mockk<ServerDrivenComponent>()

        // When
        BeagleMessageLogs.logViewFactoryNotFound(widget)

        // Then
        val message = """
            Did you miss to create a WidgetViewFactory for Widget ${widget::class.java.simpleName}
        """.trimIndent()
        verify(exactly = 1) {  BeagleLoggerProxy.warning(message) }
    }

    @Test
    fun logActionBarAlreadyPresentOnView_should_call_BeagleLogger_error() {
        // Given
        val exception = mockk<Exception>()

        // When
        BeagleMessageLogs.logActionBarAlreadyPresentOnView(exception)

        // Then
        verify(exactly = 1) {  BeagleLoggerProxy.error("SupportActionBar is already present", exception) }
    }

    @Test
    fun logFormValidatorNotFound_should_call_BeagleLogger_warning() {
        // Given
        val validator = RandomData.string()

        // When
        BeagleMessageLogs.logFormValidatorNotFound(validator)

        // Then
        verify(exactly = 1) {  BeagleLoggerProxy.warning("Validation with name '$validator' were not found!") }
    }

    @Test
    fun logFormInputsNotFound_should_call_BeagleLogger_warning() {
        // Given
        val formActionName = RandomData.string()

        // When
        BeagleMessageLogs.logFormInputsNotFound(formActionName)

        // Then
        verify(exactly = 1) {  BeagleLoggerProxy.warning("Are you missing to declare your FormInput for " +
                "form action '$formActionName'?") }
    }

    @Test
    fun logFormSubmitNotFound_should_call_BeagleLogger_warning() {
        // Given
        val formActionName = RandomData.string()

        // When
        BeagleMessageLogs.logFormSubmitNotFound(formActionName)

        // Then
        verify(exactly = 1) {  BeagleLoggerProxy.warning("Are you missing to declare your FormSubmit component for " +
                "form action '$formActionName'?") }
    }

    @Test
    fun errorWhileTryingToAccessContext_should_call_BeagleLogger_error() {
        // Given
        val exception = mockk<Exception>()

        // When
        BeagleMessageLogs.errorWhileTryingToAccessContext(exception)

        // Then
        verify(exactly = 1) {  BeagleLoggerProxy.error("Error while evaluating expression bindings.", exception) }
    }

    @Test
    fun errorWhileTryingToChangeContext_should_call_BeagleLogger_error() {
        // Given
        val exception = mockk<Exception>()

        // When
        BeagleMessageLogs.errorWhileTryingToChangeContext(exception)

        // Then
        verify(exactly = 1) {  BeagleLoggerProxy.error("Error while trying to change context.", exception) }
    }

    @Test
    fun errorWhileTryingToNotifyContextChanges_should_call_BeagleLogger_error() {
        // Given
        val exception = mockk<Exception>()

        // When
        BeagleMessageLogs.errorWhileTryingToNotifyContextChanges(exception)

        // Then
        verify(exactly = 1) {  BeagleLoggerProxy.error("Error while trying to notify context changes.", exception) }
    }

    @Test
    fun errorWhileTryingToEvaluateBinding_should_call_BeagleLogger_error() {
        // Given
        val exception = mockk<Exception>()

        // When
        BeagleMessageLogs.errorWhileTryingToEvaluateBinding(exception)

        // Then
        verify(exactly = 1) {  BeagleLoggerProxy.error("Error while trying to evaluate binding.", exception) }
    }

    @DisplayName("When analyticsQueueIsFull")
    @Nested
    inner class AnalyticsQueueIsFull{

        @DisplayName("Then Should call BeagleLogger warning")
        @Test
        fun testAnalyticsQueueIsFull(){
            //given
            val expectedMessage = "10 analytics records are queued and waiting for the initial configuration " +
                "of the AnalyticsProvider to conclude. This is probably an error within your analytics provider. Why " +
                "are getConfig() and startSession() not done yet? From now on, some analytics records will be lost. " +
                "If you need to increase the maximum number of items the queue can support, implement " +
                "getMaximumItemsInQueue() in your AnalyticsProvider."

            //when
            BeagleMessageLogs.analyticsQueueIsFull(10)

            //then
            verify(exactly = 1) { BeagleLoggerProxy.warning(expectedMessage) }
        }
    }

    @DisplayName("When can not get property value")
    @Nested
    inner class CanNotGetPropertyValue{

        @DisplayName("Then should call BeagleLoggerProxy warning")
        @Test
        fun testCanNotGetPropertyValue(){
            //given
            val propertyName = "property"
            val expectedMessage = "Can not get some attributes of property $propertyName."

            //when
            BeagleMessageLogs.canNotGetPropertyValue(propertyName)

            //then
            verify(exactly = 1) { BeagleLoggerProxy.warning(expectedMessage) }

        }
    }

}
