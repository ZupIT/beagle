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
import br.com.zup.beagle.setup.BeagleEnvironment
import br.com.zup.beagle.widget.form.FormInput
import br.com.zup.beagle.widget.form.FormSubmit
import br.com.zup.beagle.widget.form.InputWidget
import br.com.zup.beagle.widget.interfaces.Observer
import br.com.zup.beagle.widget.interfaces.WidgetState
import br.com.zup.beagle.widget.state.Observable

class FormValidatorController(
    private val validatorHandler: ValidatorHandler? = BeagleEnvironment.beagleSdk.validatorHandler,
    private val formInputValidatorList: MutableList<FormInputValidator> = mutableListOf()
) {

    var formSubmitView: View? = null

    private fun subscribeOnValidState(formInput: FormInput) {
        val inputWidget: InputWidget = formInput.child
        inputWidget.getState().addObserver(object : Observer<WidgetState> {
            override fun update(o: Observable<WidgetState>, arg: WidgetState) {
                getValidator(formInput.validator)?.let {
                    formInputValidatorList.find { formInputValidator ->
                        formInputValidator.formInput == formInput
                    }?.isValid = it.isValid(arg.value, formInput.child)
                }
                configFormSubmit()
            }
        })
    }

    fun configFormSubmit() {
        if ((formSubmitView?.tag as? FormSubmit)?.enabled == false) {
            formSubmitView?.isEnabled = checkFormFieldsValidate()
        }
    }

    private fun checkFormFieldsValidate(): Boolean {
        formInputValidatorList.forEach {
            if (!it.isValid)
                return false
        }
        return true
    }

    private fun getValidator(validator: String?) = if (validator != null) {
        validatorHandler?.getValidator(validator)
    } else {
        null
    }

    fun configFormInputList(formInput: FormInput) {
        val inputWidget: InputWidget = formInput.child
        var isValid = !(formInput.required ?: false)
        getValidator(formInput.validator)?.let {
            isValid = it.isValid(inputWidget.getValue(), formInput.child)
        }
        formInputValidatorList.add(FormInputValidator(formInput, isValid))
        subscribeOnValidState(formInput)
    }
}