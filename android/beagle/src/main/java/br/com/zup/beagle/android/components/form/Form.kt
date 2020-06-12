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

package br.com.zup.beagle.android.components.form

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import br.com.zup.beagle.action.Action
import br.com.zup.beagle.action.CustomAction
import br.com.zup.beagle.action.FormRemoteAction
import br.com.zup.beagle.android.action.ActionExecutor
import br.com.zup.beagle.android.action.FormValidationActionHandler
import br.com.zup.beagle.android.components.utils.hideKeyboard
import br.com.zup.beagle.android.engine.renderer.ViewRendererFactory
import br.com.zup.beagle.android.components.form.core.FormDataStoreHandler
import br.com.zup.beagle.android.components.form.core.FormResult
import br.com.zup.beagle.android.components.form.core.FormSubmitter
import br.com.zup.beagle.android.components.form.core.FormValidatorController
import br.com.zup.beagle.android.components.form.core.ValidatorHandler
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.view.BeagleActivity
import br.com.zup.beagle.android.view.ServerDrivenState
import br.com.zup.beagle.android.widget.ui.RootView
import br.com.zup.beagle.android.widget.ui.WidgetView
import br.com.zup.beagle.core.ServerDrivenComponent

data class Form(
    val action: Action,
    private val child: ServerDrivenComponent,
    val group: String? = null,
    val shouldStoreFields: Boolean = false
) : WidgetView() {

    @Transient
    private val viewRendererFactory: ViewRendererFactory = ViewRendererFactory()

    @Transient
    private val formInputs = mutableListOf<FormInput>()

    @Transient
    private val formInputHiddenList = mutableListOf<FormInputHidden>()

    @Transient
    private var formSubmitView: View? = null

    @Transient
    private val validatorHandler: ValidatorHandler? = BeagleEnvironment.beagleSdk.validatorHandler

    @Transient
    private val formValidationActionHandler: FormValidationActionHandler = FormValidationActionHandler()

    @Transient
    private val formSubmitter: FormSubmitter = FormSubmitter()

    @Transient
    private val formValidatorController: FormValidatorController = FormValidatorController()

    @Transient
    private val actionExecutor: ActionExecutor = ActionExecutor(
        formValidationActionHandler = formValidationActionHandler
    )

    @Transient
    private val formDataStoreHandler: FormDataStoreHandler = FormDataStoreHandler.shared

    override fun buildView(rootView: RootView): View {
        val view = viewRendererFactory.make(child).build(rootView)

        if (view is ViewGroup) {
            fetchFormViews(view)
        }

        if (formInputs.size == 0) {
            BeagleMessageLogs.logFormInputsNotFound(action.toString())
        }

        if (formSubmitView == null) {
            BeagleMessageLogs.logFormSubmitNotFound(action.toString())
        }

        return view
    }

    private fun fetchFormViews(viewGroup: ViewGroup) {
        viewGroup.children.forEach { childView ->
            if (childView.tag != null) {
                val tag = childView.tag
                if (tag is FormInput) {
                    formInputs.add(tag)
                    formValidatorController.configFormInputList(tag)
                } else if (tag is FormInputHidden) {
                    formInputHiddenList.add(tag)
                } else if (childView.tag is FormSubmit && formSubmitView == null) {
                    formSubmitView = childView
                    addClickToFormSubmit(childView)
                    formValidatorController.formSubmitView = childView
                }
            } else if (childView is ViewGroup) {
                fetchFormViews(childView)
            }
        }

        formValidatorController.configFormSubmit()
    }

    private fun addClickToFormSubmit(formSubmitView: View) {
        formSubmitView.setOnClickListener {
            formValidationActionHandler.formInputs = formInputs
            handleFormSubmit(formSubmitView.context)
        }
    }

    private fun handleFormSubmit(context: Context) {
        val formsValue = mutableMapOf<String, String>()

        formInputs.forEach { formInput ->
            val inputWidget: InputWidget = formInput.child as InputWidget
            if (formInput.required == true) {
                validateFormInput(formInput, formsValue)
            } else {
                formsValue[formInput.name] = inputWidget.getValue().toString()
            }
        }

        formInputHiddenList.forEach { formInputHidden ->
            formsValue[formInputHidden.name] = formInputHidden.value
        }
        if (formsValue.size == (formInputs.size + formInputHiddenList.size)) {
            updateStoredData(formsValue)
            formSubmitView?.hideKeyboard()
            submitForm(formsValue, context)
        }
    }

    private fun updateStoredData(formsValue: MutableMap<String, String>) {
        persistCurrentData(formsValue)
        loadStoredData(formsValue)
    }

    private fun persistCurrentData(formsValue: MutableMap<String, String>) {
        val group = group
        if (!shouldStoreFields || group == null) return
        for ((key, value) in formsValue) {
            formDataStoreHandler.put(group, key, value)
        }
    }

    private fun loadStoredData(formsValue: MutableMap<String, String>) {
        val group = group
        if (shouldStoreFields || group == null) return
        val storedValues = formDataStoreHandler.getAllValues(group)
        for ((key, value) in storedValues) {
            if (formsValue[key] == null) {
                formsValue[key] = value
            }
        }
    }

    private fun validateFormInput(
        formInput: FormInput,
        formsValue: MutableMap<String, String>
    ) {
        val validator = formInput.validator ?: return

        validatorHandler?.getValidator(validator)?.let {
            val inputWidget: InputWidget = formInput.child as InputWidget
            val inputValue = inputWidget.getValue()

            if (it.isValid(inputValue, inputWidget)) {
                formsValue[formInput.name] = inputValue.toString()
            } else {
                inputWidget.onErrorMessage(formInput.errorMessage ?: "")
            }
        } ?: run {
            BeagleMessageLogs.logFormValidatorNotFound(validator)
        }
    }

    private fun submitForm(
        formsValue: MutableMap<String, String>,
        context: Context
    ) {
        when (val action = action) {
            is FormRemoteAction -> formSubmitter.submitForm(action, formsValue) {
                (context as AppCompatActivity).runOnUiThread {
                    handleFormResult(context, it)
                }
            }
            is CustomAction -> actionExecutor.doAction(context, CustomAction(
                name = action.name,
                data = formsValue.plus(action.data)
            ))
            else ->
                actionExecutor.doAction(context, action)
        }
    }

    private fun handleFormResult(context: Context, formResult: FormResult) {
        when (formResult) {
            is FormResult.Success -> {
                group?.let {
                    formDataStoreHandler.clear(it)
                }
                actionExecutor.doAction(context, formResult.action)
            }
            is FormResult.Error -> (context as? BeagleActivity)?.onServerDrivenContainerStateChanged(
                ServerDrivenState.Error(formResult.throwable)
            )
        }
    }
}