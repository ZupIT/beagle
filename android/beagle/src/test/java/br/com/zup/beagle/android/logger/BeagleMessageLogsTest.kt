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
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a BeagleMessageLogs")
internal class BeagleMessageLogsTest {

    private val beagleLoggerInfoSlot = slot<String>()
    val exception = mockk<java.lang.Exception>()

    @BeforeEach
    fun setUp() {
        mockkObject(BeagleEnvironment)

        every { BeagleEnvironment.beagleSdk.logger } returns null
        every { BeagleEnvironment.beagleSdk.config.isLoggingEnabled } returns true

        mockkObject(BeagleLoggerProxy)

        every { BeagleLoggerProxy.info(capture(beagleLoggerInfoSlot)) } just Runs
        every { BeagleLoggerProxy.warning(any()) } just Runs
        every { BeagleLoggerProxy.error(any()) } just Runs
        every { BeagleLoggerProxy.error(any(), any()) } just Runs
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Nested
    @DisplayName("When receive valid data")
    inner class Succeed {

        @Test
        @DisplayName("Then the log Http RequestData should call BeagleLogger info")
        fun `check message log for RequestData`() {
            // Given
            val requestData = makeRequestData()

            // When
            BeagleMessageLogs.logHttpRequestData(requestData)

            // Then
            assertEquals("""
            *** HTTP REQUEST ***
            Url=${requestData.url}
            Method=${requestData.httpAdditionalData.method}
            Headers=${requestData.httpAdditionalData.headers}
            Body=${requestData.httpAdditionalData.body}
        """.trimIndent(), beagleLoggerInfoSlot.captured)
        }

        @Test
        @DisplayName("Then the log Http ResponseData should call BeagleLogger info")
        fun `check message log for ResponseData`() {
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


    }

    @Nested
    @DisplayName("When some data are not found")
    inner class Warning {

        @Test
        @DisplayName("Then the log to WidgetViewFactory not found should call BeagleLogger info")
        fun `check message log for WidgetViewFactory not found`() {
            // Given
            val widget = mockk<ServerDrivenComponent>()

            // When
            BeagleMessageLogs.logViewFactoryNotFound(widget)

            // Then
            val message = """
            Did you miss to create a WidgetViewFactory for Widget ${widget::class.java.simpleName}
        """.trimIndent()
            verify(exactly = 1) { BeagleLoggerProxy.warning(message) }
        }

        @Test
        @DisplayName("Then the form validation log should call BeagleLogger info")
        fun `check message log for form not found`() {
            // Given
            val validator = RandomData.string()

            // When
            BeagleMessageLogs.logFormValidatorNotFound(validator)

            // Then
            verify(exactly = 1) { BeagleLoggerProxy.warning("Validation with name '$validator' were not found!") }
        }

        @Test
        @DisplayName("Then the log to form input not found should call BeagleLogger info")
        fun `check log for form input not found`() {
            // Given
            val formActionName = RandomData.string()

            // When
            BeagleMessageLogs.logFormInputsNotFound(formActionName)

            // Then
            verify(exactly = 1) {
                BeagleLoggerProxy.warning("Are you missing to declare your FormInput for " +
                    "form action '$formActionName'?")
            }
        }

        @Test
        @DisplayName("Then form submission not found log should call BeagleLogger information")
        fun `check the log for attempting to submit a form`() {
            // Given
            val formActionName = RandomData.string()

            // When
            BeagleMessageLogs.logFormSubmitNotFound(formActionName)

            // Then
            verify(exactly = 1) {
                BeagleLoggerProxy.warning("Are you missing to declare your FormSubmit component for " +
                    "form action '$formActionName'?")
            }
        }

        @Test
        @DisplayName("Then the log to attempt to multiple expressions should call BeagleLogger info")
        fun `check the attempt log to use multiple expressions in a type that is not string`() {
            // When
            BeagleMessageLogs.multipleExpressionsInValueThatIsNotString()

            // Then
            verify(exactly = 1) {
                BeagleLoggerProxy.warning(
                    "You are trying to use multiple expressions in a type that is not string!")
            }
        }

        @Test
        @DisplayName("Then in an attempt to use a reserved keyword in a Global Context the log " +
            "should call BeagleLogger info")
        fun `check the message log to global keyword reserved in a global context`() {
            // When
            BeagleMessageLogs.globalKeywordIsReservedForGlobalContext()

            // Then
            verify(exactly = 1) {
                BeagleLoggerProxy.warning("Context name global is a reserved keyword for Global Context only")
            }
        }

        @Test
        @DisplayName("Then in an attempt to found a nonexistent function")
        fun `check the log to found a nonexistent function`() {
            val functionName = RandomData.string()

            // When
            BeagleMessageLogs.functionWithNameDoesNotExist(functionName)

            // Then
            verify(exactly = 1) {
                BeagleLoggerProxy.warning("Function with named $functionName does not exist.")
            }
        }

        @Test
        @DisplayName("Then in an attempt to add a prefetch in an expression")
        fun `check the log to add a prefetch in an expression`() {
            // When
            BeagleMessageLogs.expressionNotSupportInPreFetch()

            // Then
            verify(exactly = 1) {
                BeagleLoggerProxy.warning("Expression is not support in prefetch")
            }
        }
    }

    @Nested
    @DisplayName("When trying to receive or pass invalid data")
    inner class Error {

        @Test
        @DisplayName("Then the log to invalid http client should call BeagleLogger info")
        fun `check message log for http client`() {
            // Given
            val throwable = mockk<Throwable>()

            // When
            BeagleMessageLogs.logUnknownHttpError(throwable)

            // Then
            verify(exactly = 1) {
                BeagleLoggerProxy.error(
                    "Exception thrown while trying to call http client.", throwable)
            }
        }

        @Test
        @DisplayName("Then the log to invalid deserialization should call BeagleLogger info")
        fun `check message log for invalid deserialization`() {
            // Given
            val json = RandomData.string()

            // When
            BeagleMessageLogs.logDeserializationError(json, exception)

            // Then
            verify(exactly = 1) {
                BeagleLoggerProxy.error(
                    "Exception thrown while trying to deserialize the following json: $json", exception)
            }
        }

        @Test
        @DisplayName("Then the log of support for action bar should call BeagleLogger info")
        fun `check message log for support action bar`() {
            // When
            BeagleMessageLogs.logActionBarAlreadyPresentOnView(exception)

            // Then
            verify(exactly = 1) { BeagleLoggerProxy.error("SupportActionBar is already present", exception) }
        }

        @Test
        @DisplayName("Then the context access log should call BeagleLogger info")
        fun `check log for context access`() {
            // When
            BeagleMessageLogs.errorWhileTryingToAccessContext(exception)

            // Then
            verify(exactly = 1) {
                BeagleLoggerProxy.error(
                    "Error while evaluating expression bindings.", exception)
            }
        }

        @Test
        @DisplayName("Then the context change log should call BeagleLogger info")
        fun `check the log for change context`() {
            // When
            BeagleMessageLogs.errorWhileTryingToChangeContext(exception)

            // Then
            verify(exactly = 1) { BeagleLoggerProxy.error("Error while trying to change context.", exception) }
        }

        @Test
        @DisplayName("Then the log to attempt to notify the change of context should call BeagleLogger info")
        fun `check the message log to context change attempt`() {
            // When
            BeagleMessageLogs.errorWhileTryingToNotifyContextChanges(exception)

            // Then
            verify(exactly = 1) {
                BeagleLoggerProxy.error("Error while trying to notify context changes.", exception)
            }
        }

        @Test
        @DisplayName("Then the log to attempt to evaluate binding should call BeagleLogger info")
        fun `check the evaluate binding attempt log`() {
            // When
            BeagleMessageLogs.errorWhileTryingToEvaluateBinding(exception)

            // Then
            verify(exactly = 1) {
                BeagleLoggerProxy.error("Error while trying to evaluate binding.", exception)
            }
        }

        @Test
        @DisplayName("Then the color parses log should call Beagle Logger info")
        fun `check the message log for attempted parses color`() {
            val color = RandomData.string()

            // When
            BeagleMessageLogs.errorWhenMalformedColorIsProvided(color, exception)

            // Then
            verify(exactly = 1) {
                BeagleLoggerProxy.error(
                    "Could not parses color $color", exception)
            }
        }

        @Test
        @DisplayName("Then the message log to not found value should call BeagleLogger info")
        fun `check the message log to not found value`() {
            val value = RandomData.string()

            // When
            BeagleMessageLogs.errorWhenExpressionEvaluateNullValue(value)

            // Then
            verify(exactly = 1) {
                BeagleLoggerProxy.error("Could not found value for $value")
            }
        }

        @Test
        @DisplayName("Then the message log for the invalid simple form should call BeagleLogger info")
        fun `check the attempt message log found simple form not found`() {
            // When
            BeagleMessageLogs.logNotFoundSimpleForm()

            // Then
            verify(exactly = 1) {
                BeagleLoggerProxy.error("Not found simple form in the parents")
            }
        }

        @Test
        @DisplayName("Then in an attempt to set an invalid image the log should call BeagleLogger info")
        fun `check the message log when an invalid image is set`() {
            val image = RandomData.string()
            // When
            BeagleMessageLogs.errorWhileTryingToSetInvalidImage(image, exception)

            // Then
            verify(exactly = 1) {
                BeagleLoggerProxy.error("Could not find image $image", exception)
            }
        }

        @Test
        @DisplayName("Then in an attempt to parse an expression")
        fun `check the message log to parse an expression`() {
            val expression = RandomData.string()

            // When
            BeagleMessageLogs.errorWhileTryingParseExpressionFunction(expression, exception)

            // Then
            verify(exactly = 1) {
                BeagleLoggerProxy.error("Error while trying to parse expression: $expression", exception)
            }
        }

        @Test
        @DisplayName("Then in an attempt to execute an expression function")
        fun `check the message log to execute an expression function`() {
            // When
            BeagleMessageLogs.errorWhileTryingExecuteExpressionFunction(exception)

            // Then
            verify(exactly = 1) {
                BeagleLoggerProxy.error("Error while trying to execute expression function.", exception)
            }
        }

        @Test
        @DisplayName("Then in an attempt to put a child in a view with a specific id")
        fun `check the log to put a child in a view with a specific id`() {
            val id = RandomData.string()

            // When
            BeagleMessageLogs.errorWhileTryingToAddViewWithAddChildrenAction(id)

            // Then
            verify(exactly = 1) {
                BeagleLoggerProxy.error("The view with id:$id cannot receive children")
            }
        }
    }

    @DisplayName("When cannot get property value")
    @Nested
    inner class CanNotGetPropertyValue {

        @DisplayName("Then should call BeagleLoggerProxy warning")
        @Test
        fun testCanNotGetPropertyValue() {
            //given
            val propertyName = "property"
            val expectedMessage = "Cannot get some attributes of property $propertyName."

            //when
            BeagleMessageLogs.cannotGetPropertyValue(propertyName)

            //then
            verify(exactly = 1) { BeagleLoggerProxy.warning(expectedMessage) }
        }
    }

    @DisplayName("When try to download an invalid image")
    @Nested
    inner class DownloadImage {

        @DisplayName("Then should call BeagleLoggerProxy.error with image and exception")
        @Test
        fun downloadInvalidImage() {
            // Given
            val image = "/image"
            val exceptionMessage = "Error while trying to download image: $image"

            // When
            BeagleMessageLogs.errorWhileTryingToDownloadImage(image, exception)

            // Then
            verify(exactly = 1) { BeagleLoggerProxy.error(exceptionMessage, exception) }
        }
    }
}
