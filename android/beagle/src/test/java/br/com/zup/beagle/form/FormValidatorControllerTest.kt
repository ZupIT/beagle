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

package br.com.zup.beagle.form

import android.view.View
import br.com.zup.beagle.engine.renderer.layout.FormInputValidator
import br.com.zup.beagle.widget.form.FormInput
import br.com.zup.beagle.widget.form.FormSubmit
import br.com.zup.beagle.widget.form.InputWidget
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FormValidatorControllerTest {

    @MockK
    lateinit var validatorHandler: ValidatorHandler
    @MockK
    private lateinit var submitView: View
    @MockK
    private lateinit var formSubmit: FormSubmit
    @MockK
    private lateinit var inputWidget: InputWidget
    @MockK
    private lateinit var formInput: FormInput
    @MockK
    lateinit var formInputValidator: FormInputValidator

    private val submitViewEnabledSlot = slot<Boolean>()

    @InjectMockKs
    private lateinit var formValidatorController: FormValidatorController

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        formValidatorController.formSubmitView = submitView

        every { submitView.tag } returns formSubmit
        every { submitView.isEnabled = capture(submitViewEnabledSlot) } just Runs
    }

    @Test
    fun configFormSubmit_when_form_is_valid_and_submit_is_disabled() {
        // GIVEN
        val result = true
        every { formSubmit.enabled } returns false

        // WHEN
        formValidatorController.configFormSubmit()

        // THEN
        assertEquals(result, submitViewEnabledSlot.captured)
    }

    @Test
    fun checkFormFieldsValidate_when_one_view_is_invalid_should_return_false() {
        // GIVEN
        val result = false
        every { formInputValidator.isValid } returns false
        every { formSubmit.enabled } returns false
        formValidatorController.formInputValidatorList.add(formInputValidator)


        // WHEN
        formValidatorController.configFormSubmit()

        // THEN
        assertEquals(result, submitViewEnabledSlot.captured)
    }

    @Test
    fun configFormInputList_should_increment_list_and_call_subscribeOnValidState() {
        // GIVEN
        val result = 1
        every { formInput.validator } returns null
        every { formInput.child } returns inputWidget

        // WHEN
        formValidatorController.configFormInputList(formInput)

        // THEN
        assertTrue { result == formValidatorController.formInputValidatorList.size }
    }
}