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
import br.com.zup.beagle.analytics2.ActionAnalyticsConfig
import br.com.zup.beagle.android.components.form.FormInput
import br.com.zup.beagle.android.components.form.InputWidget
import br.com.zup.beagle.android.components.form.core.Constants
import br.com.zup.beagle.android.widget.RootView

/**
 * Configures the error messages returned by a service external to the application.
 * For example, when checking the registration status of a CPF in the recipe,
 * we can return the API error message to the application using FormValidation.
 *
 * @param errors list of errors.
 *
 */
@Deprecated(Constants.FORM_DEPRECATED_MESSAGE)
internal class FormValidation(
    val errors: List<FieldError>,
    override var analytics: ActionAnalyticsConfig? = null
) : ActionAnalytics() {

    @Transient
    var formInputs: List<FormInput>? = null

    override fun execute(rootView: RootView, origin: View) {
        errors.forEach { error ->
            val formInput = formInputs?.find {
                it.name == error.inputName
            }
            val childInputWidget: InputWidget? = formInput?.child

            childInputWidget?.onErrorMessage(error.message)
        }
    }
}

/**
 * Class to define error.
 *
 * @param inputName component name to which this error refers.
 * @param message The error message displayed.
 *
 */
data class FieldError(
    val inputName: String,
    val message: String
)
