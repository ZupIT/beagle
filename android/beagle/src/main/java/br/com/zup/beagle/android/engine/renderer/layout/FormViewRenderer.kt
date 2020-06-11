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

package br.com.zup.beagle.android.engine.renderer.layout

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import br.com.zup.beagle.android.action.ActionExecutor
import br.com.zup.beagle.action.CustomAction
import br.com.zup.beagle.android.action.FormValidationActionHandler
import br.com.zup.beagle.android.engine.renderer.LayoutViewRenderer
import br.com.zup.beagle.android.engine.renderer.RootView
import br.com.zup.beagle.android.engine.renderer.ViewRendererFactory
import br.com.zup.beagle.android.form.FormResult
import br.com.zup.beagle.android.form.FormSubmitter
import br.com.zup.beagle.android.form.FormValidatorController
import br.com.zup.beagle.android.form.ValidatorHandler
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.utils.hideKeyboard
import br.com.zup.beagle.android.view.BeagleActivity
import br.com.zup.beagle.android.view.ServerDrivenState
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.widget.form.Form
import br.com.zup.beagle.widget.form.FormInput
import br.com.zup.beagle.widget.form.FormInputHidden
import br.com.zup.beagle.widget.form.FormSubmit
import br.com.zup.beagle.widget.form.FormRemoteAction
import br.com.zup.beagle.android.widget.form.InputWidget

@Suppress("LongParameterList")
internal class FormViewRenderer(
    override val component: Form,
    private val validatorHandler: ValidatorHandler? = BeagleEnvironment.beagleSdk.validatorHandler,
    private val formValidationActionHandler: FormValidationActionHandler = FormValidationActionHandler(),
    private val formSubmitter: FormSubmitter = FormSubmitter(),
    private val formValidatorController: FormValidatorController = FormValidatorController(),
    private val actionExecutor: ActionExecutor = ActionExecutor(
        formValidationActionHandler = formValidationActionHandler
    ),
    private val formDataStoreHandler: FormDataStoreHandler = FormDataStoreHandler.shared,
    viewRendererFactory: ViewRendererFactory = ViewRendererFactory(),
    viewFactory: ViewFactory = ViewFactory()
) : LayoutViewRenderer<Form>(viewRendererFactory, viewFactory) {

    private val formInputs = mutableListOf<FormInput>()
    private val formInputHiddenList = mutableListOf<FormInputHidden>()
    private var formSubmitView: View? = null

    override fun buildView(rootView: RootView): View {
        val view = viewRendererFactory.make(component.child).build(rootView)

        if (view is ViewGroup) {
            fetchFormViews(view, rootView)
        }

        if (formInputs.size == 0) {
            BeagleMessageLogs.logFormInputsNotFound(component.onSubmit.toString())
        }

        if (formSubmitView == null) {
            BeagleMessageLogs.logFormSubmitNotFound(component.onSubmit.toString())
        }

        return view
    }

    private fun fetchFormViews(viewGroup: ViewGroup, rootView: RootView) {
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
                    addClickToFormSubmit(childView, rootView)
                    formValidatorController.formSubmitView = childView
                }
            } else if (childView is ViewGroup) {
                fetchFormViews(childView, rootView)
            }
        }

        formValidatorController.configFormSubmit()
    }

    private fun addClickToFormSubmit(formSubmitView: View, rootView: RootView) {
        formSubmitView.setOnClickListener {
            formValidationActionHandler.formInputs = formInputs
            handleFormSubmit(rootView)
        }
    }

    private fun handleFormSubmit(rootView: RootView) {
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
            submitForm(formsValue, rootView)
        }
    }

    private fun updateStoredData(formsValue: MutableMap<String, String>) {
        persistCurrentData(formsValue)
        loadStoredData(formsValue)
    }

    private fun persistCurrentData(formsValue: MutableMap<String, String>) {
        val group = component.group
        if (!component.shouldStoreFields || group == null) return
        for ((key, value) in formsValue) {
            formDataStoreHandler.put(group, key, value)
        }
    }

    private fun loadStoredData(formsValue: MutableMap<String, String>) {
        val group = component.group
        if (component.shouldStoreFields || group == null) return
        val storedValues = formDataStoreHandler.getAllValues(group)
        for( (key, value) in storedValues) {
            if(formsValue[key] == null) {
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
        rootView: RootView
    ) {
        component.onSubmit?.forEach { action ->
            when (action) {
                is FormRemoteAction -> formSubmitter.submitForm(action, formsValue) {
                    (rootView.getContext() as AppCompatActivity).runOnUiThread {
                        handleFormResult(rootView, it)
                    }
                }
                is CustomAction -> actionExecutor.doAction(rootView, CustomAction(
                    name = action.name,
                    data = formsValue.plus(action.data)
                ))
                else -> actionExecutor.doAction(rootView, action)
            }
        }

    }

    private fun handleFormResult(rootView: RootView, formResult: FormResult) {
        when (formResult) {
            is FormResult.Success -> {
                component.group?.let {
                    formDataStoreHandler.clear(it)
                }
                actionExecutor.doAction(rootView, formResult.action)
            }
            is FormResult.Error -> (rootView.getContext() as? BeagleActivity)?.onServerDrivenContainerStateChanged(
                ServerDrivenState.Error(formResult.throwable)
            )
        }
    }
}

data class FormInputValidator(
    val formInput: FormInput,
    var isValid: Boolean
)