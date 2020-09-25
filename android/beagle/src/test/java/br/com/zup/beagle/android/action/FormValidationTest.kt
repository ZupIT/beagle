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

package br.com.zup.beagle.android.action

import android.view.View
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.components.form.FormInput
import br.com.zup.beagle.android.components.form.InputWidget
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.testutil.RandomData
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Test

class FormValidationTest : BaseTest(){

    @RelaxedMockK
    private lateinit var view: View
    @RelaxedMockK
    private lateinit var formInput: FormInput
    @RelaxedMockK
    private lateinit var inputWidget: InputWidget

    @Test
    fun `execute should iterate over errors and call onErrorMessage for errorFields`() {
        // Given
        val inputName = RandomData.string()
        val validationMessage = RandomData.string()
        val formValidation = FormValidation(
            errors = listOf(
                FieldError(inputName, validationMessage)
            )
        )
        every { formInput.name } returns inputName
        every { formInput.child } returns inputWidget
        formValidation.formInputs = listOf(formInput)

        // When
        formValidation.execute(rootView, view)

        // Then
        verify(exactly = once()) { inputWidget.onErrorMessage(validationMessage) }
    }
}
