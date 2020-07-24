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

package br.com.zup.beagle.android.components.form.core

import android.view.View
import br.com.zup.beagle.android.components.form.FormInput
import br.com.zup.beagle.android.components.form.FormSubmit
import br.com.zup.beagle.android.components.form.InputWidget
import br.com.zup.beagle.android.components.utils.beagleComponent
import br.com.zup.beagle.android.extensions.once
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class FormValidatorControllerTest {

    @MockK
    lateinit var validatorHandler: ValidatorHandler

    @MockK
    private lateinit var submitView: View

    @MockK
    private lateinit var formSubmit: FormSubmit

    @RelaxedMockK
    private lateinit var inputWidget: InputWidget

    @MockK
    private lateinit var formInput: FormInput

    @MockK
    lateinit var formInputValidator: FormInputValidator

    @RelaxedMockK
    lateinit var validator: Validator<Any, Any>

    @RelaxedMockK
    lateinit var formInputValidatorList: MutableList<FormInputValidator>

    private val submitViewEnabledSlot = slot<Boolean>()

    @InjectMockKs
    private lateinit var formValidatorController: FormValidatorController

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        formValidatorController.formSubmitView = submitView

        every { submitView.getTag(any()) } returns formSubmit
        every { submitView.isEnabled = capture(submitViewEnabledSlot) } just Runs
        every { submitView.beagleComponent = any() } just Runs
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
        val iterator = mockk<MutableIterator<FormInputValidator>>()
        every { formInputValidatorList.iterator() } returns iterator
        every { iterator.hasNext() } returns true
        every { iterator.next() } returns formInputValidator

        // WHEN
        formValidatorController.configFormSubmit()

        // THEN
        assertEquals(result, submitViewEnabledSlot.captured)
    }

    @Test
    fun configFormInputList_should_increment_list_and_call_subscribeOnValidState() {
        // GIVEN
        every { formInput.validator } returns "stub"
        every { formInput.child } returns inputWidget
        every { formInput.required } returns true
        every { validatorHandler.getValidator(any()) } returns validator

        // WHEN
        formValidatorController.configFormInputList(formInput)

        // THEN
        verify(exactly = once()) { formInputValidatorList.add(any()) }
        verify(exactly = once()) { validator.isValid(any(), any()) }
    }

    @Test
    fun configFormInputList_should_set_isValid_by_required() {
        // GIVEN
        val required = true
        every { formInput.validator } returns null
        every { formInput.child } returns inputWidget
        every { formInput.required } returns required
        every { validatorHandler.getValidator(any()) } returns validator
        val slot = slot<FormInputValidator>()
        every { formInputValidatorList.add(capture(slot)) } returns true

        // WHEN
        formValidatorController.configFormInputList(formInput)

        // THEN
        verify(exactly = 0) { validator.isValid(any(), any()) }
        assertFalse { slot.captured.isValid }
    }
}