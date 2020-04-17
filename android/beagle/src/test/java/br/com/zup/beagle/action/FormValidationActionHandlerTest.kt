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

package br.com.zup.beagle.action

import android.content.Context
import android.view.View
import br.com.zup.beagle.extensions.once
import br.com.zup.beagle.logger.BeagleLogger
import br.com.zup.beagle.testutil.RandomData
import br.com.zup.beagle.widget.form.FormInput
import br.com.zup.beagle.widget.form.InputWidget
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class FormValidationActionHandlerTest {

    @MockK
    private lateinit var context: Context

    private lateinit var formValidationActionHandler: FormValidationActionHandler

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        formValidationActionHandler = FormValidationActionHandler()

        mockkObject(BeagleLogger)
    }

    @After
    fun tearDown() {
        unmockkObject(BeagleLogger)
    }

    @Test
    fun handle_should_iterate_over_errors_and_call_ValidationErrorListener_of_view_with_name() {
        // Given
        val view = mockk<View>(relaxed = true)
        val formInput = mockk<FormInput>(relaxed = true)
        val inputWidget = mockk<InputWidget>(relaxed = true)
        val inputName = RandomData.string()
        val validationMessage = RandomData.string()
        val formValidation = FormValidation(
            errors = listOf(
                FieldError(inputName, validationMessage)
            )
        )
        every { view.tag } returns formInput
        every { formInput.name } returns inputName
        every { formInput.child } returns inputWidget
        formValidationActionHandler.formInputs = listOf(formInput)

        // When
        formValidationActionHandler.handle(context, formValidation)

        // Then
        verify(exactly = once()) { inputWidget.onErrorMessage(validationMessage) }
    }

    @Test
    fun handle_should_iterate_over_errors_and_log_validationListener_not_found() {
        // Given
        val inputName = RandomData.string()
        val formValidation = FormValidation(
            errors = listOf(
                FieldError(inputName, RandomData.string())
            )
        )
        every { BeagleLogger.warning(any()) } just Runs

        // When
        formValidationActionHandler.handle(context, formValidation)

        // Then
        val logMessage = "Input name with name $inputName does not implement ValidationErrorListener"
        verify(exactly = once()) { BeagleLogger.warning(logMessage) }
    }
}