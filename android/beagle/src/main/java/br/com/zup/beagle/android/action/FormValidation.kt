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

import br.com.zup.beagle.android.components.form.FormInput
import br.com.zup.beagle.android.components.form.InputWidget
import br.com.zup.beagle.android.widget.RootView

internal class FormValidation(
    val errors: List<FieldError>
) : Action {

    @Transient
    var formInputs: List<FormInput>? = null

    override fun execute(rootView: RootView) {
        errors.forEach { error ->
            val formInput = formInputs?.find {
                it.name == error.inputName
            }
            val childInputWidget : InputWidget? = formInput?.child

            childInputWidget?.onErrorMessage(error.message)
        }
    }
}

data class FieldError(
    val inputName: String,
    val message: String
)
