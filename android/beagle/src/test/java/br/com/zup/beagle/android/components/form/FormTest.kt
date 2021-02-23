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

import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.action.FormRemoteAction
import br.com.zup.beagle.android.action.FormValidation
import br.com.zup.beagle.android.action.Navigate
import br.com.zup.beagle.android.action.ResultListener
import br.com.zup.beagle.android.components.BaseComponentTest
import br.com.zup.beagle.android.components.form.core.Constants
import br.com.zup.beagle.android.components.form.core.FormDataStoreHandler
import br.com.zup.beagle.android.components.form.core.FormResult
import br.com.zup.beagle.android.components.form.core.FormValidatorController
import br.com.zup.beagle.android.components.form.core.Validator
import br.com.zup.beagle.android.components.form.core.ValidatorHandler
import br.com.zup.beagle.android.components.utils.beagleComponent
import br.com.zup.beagle.android.components.utils.hideKeyboard
import br.com.zup.beagle.android.engine.renderer.ViewRendererFactory
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.testutil.InstantExecutorExtension
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.testutil.getPrivateField
import br.com.zup.beagle.android.utils.handleEvent
import br.com.zup.beagle.android.view.BeagleActivity
import br.com.zup.beagle.android.view.ServerDrivenState
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

private const val FORM_INPUT_VIEWS_FIELD_NAME = "formInputs"
private const val FORM_SUBMIT_VIEW_FIELD_NAME = "formSubmitView"
private val INPUT_VALUE = RandomData.string()
private const val INPUT_NAME = "INPUT_NAME"
private const val FORM_GROUP_VALUE = "GROUP"
private const val ADDITIONAL_DATA_KEY = "dataKey"
private const val ADDITIONAL_DATA_VALUE = "dataValue"

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class FormTest : BaseComponentTest() {

    private val formInput: FormInput = mockk(relaxed = true)
    private val formSubmit: FormSubmit = mockk(relaxed = true)

    private val validator: Validator<Any, Any> = mockk(relaxed = true, relaxUnitFun = true)
    private val beagleActivity: BeagleActivity = mockk()
    private val inputMethodManager: InputMethodManager = mockk()
    private val formInputView: View = mockk(relaxed = true, relaxUnitFun = true)
    private val formSubmitView: View = mockk(relaxed = true, relaxUnitFun = true)
    private val viewGroup: ViewGroup = mockk(relaxed = true)
    private val inputWidget: InputWidget = mockk(relaxed = true)
    private val remoteAction: FormRemoteAction = mockk(relaxed = true, relaxUnitFun = true)
    private val navigateAction: Navigate = mockk(relaxed = true)
    private val onClickListenerSlot = slot<View.OnClickListener>()
    private val runnableSlot = slot<Runnable>()
    private val formDataStoreHandler: FormDataStoreHandler = mockk()
    private val resultListenerSlot = slot<ResultListener>()

    private lateinit var form: Form

    @BeforeEach
    override fun setUp() {
        super.setUp()

        mockkStatic("br.com.zup.beagle.android.components.utils.ViewExtensionsKt")
        mockkStatic("br.com.zup.beagle.android.utils.WidgetExtensionsKt")
        mockkObject(BeagleMessageLogs)
        mockkConstructor(FormValidatorController::class)
        mockkConstructor(FormValidation::class)

        Constants.shared = formDataStoreHandler
        every { BeagleMessageLogs.logFormInputsNotFound(any()) } just Runs
        every { BeagleMessageLogs.logFormSubmitNotFound(any()) } just Runs
        every { formDataStoreHandler.getAllValues(any()) } returns HashMap()
        every { formDataStoreHandler.put(any(), any(), any()) } just Runs
        every { rootView.getContext() } returns beagleActivity
        every { formDataStoreHandler.clear(any()) } just Runs
        every { formInput.required } returns false
        every { viewRender.build(rootView) } returns viewGroup
        every { formInput.name } returns INPUT_NAME
        every { inputWidget.getValue() } returns INPUT_VALUE
        every { formInput.child } returns inputWidget
        every { formInputView.context } returns beagleActivity
        every { formInputView.beagleComponent } returns formInput
        every { formInputView.getTag(any()) } returns formInput
        every { formSubmitView.hideKeyboard() } just Runs
        every { formSubmitView.beagleComponent } returns formSubmit
        every { formSubmitView.getTag(any()) } returns formSubmit
        every { formSubmitView.context } returns beagleActivity
        every { formSubmitView.setOnClickListener(capture(onClickListenerSlot)) } just Runs
        every { viewGroup.childCount } returns 2
        every { viewGroup.getChildAt(0) } returns formInputView
        every { viewGroup.getChildAt(1) } returns formSubmitView
        every { beagleActivity.getSystemService(any()) } returns inputMethodManager
        every { beagleActivity.runOnUiThread(capture(runnableSlot)) } just Runs
        every { anyConstructed<ViewRendererFactory>().make(any()).build(any()) } returns viewGroup

        val validatorHandler: ValidatorHandler = mockk()
        every { validatorHandler.getValidator(any()) } returns validator
        every { beagleSdk.validatorHandler } returns validatorHandler
        every { anyConstructed<FormValidatorController>().configFormInputList(any()) } just Runs
        every { remoteAction.resultListener = capture(resultListenerSlot) } just Runs

        form = Form(onSubmit = listOf(mockk(relaxed = true)), child = mockk())

        every { form.handleEvent(any(), any(), any<Action>(), analyticsValue = any()) } just Runs
    }

    @Test
    fun build_should_not_try_to_iterate_over_children_if_is_not_a_ViewGroup() {

        val viewNotViewGroup = mockk<View>()
        // Given
        every { anyConstructed<ViewRendererFactory>().make(any()).build(any()) } returns viewNotViewGroup

        // When
        val actual = form.buildView(rootView)

        // Then
        assertEquals(viewNotViewGroup, actual)
        verify(exactly = 0) { viewGroup.childCount }
    }

    @Test
    fun build_should_try_to_iterate_over_all_viewGroups() {
        // Given
        val childViewGroup = mockk<ViewGroup>()
        every { childViewGroup.childCount } returns 0
        every { childViewGroup.beagleComponent } returns null
        every { viewGroup.childCount } returns 1
        every { viewGroup.getChildAt(any()) } returns childViewGroup

        // When
        form.buildView(rootView)

        // Then
        verify(exactly = 1) { childViewGroup.childCount }
    }

    @Test
    fun build_should_try_to_iterate_over_all_viewGroups_that_is_the_formInput() {
        // Given
        val childViewGroup = mockk<ViewGroup>()
        every { childViewGroup.childCount } returns 0
        every { childViewGroup.getTag(any()) } returns mockk<FormInput>(relaxed = true)
        every { viewGroup.childCount } returns 1
        every { viewGroup.getChildAt(any()) } returns childViewGroup

        // When
        form.buildView(rootView)

        // Then
        val views = form.getPrivateField<List<View>>(FORM_INPUT_VIEWS_FIELD_NAME)
        assertEquals(1, views.size)
    }

    @Test
    fun build_should_group_formInput_views() {
        form.buildView(rootView)

        val formInputs =
            form.getPrivateField<List<FormInput>>(FORM_INPUT_VIEWS_FIELD_NAME)
        assertEquals(1, formInputs.size)
        assertEquals(formInput, formInputs[0])
        verify { anyConstructed<FormValidatorController>().formSubmitView = formSubmitView }
    }

    @Test
    fun build_should_find_formSubmitView() {
        form.buildView(rootView)

        val actual = form.getPrivateField<View>(FORM_SUBMIT_VIEW_FIELD_NAME)
        assertEquals(formSubmitView, actual)
        verify(exactly = once()) { formSubmitView.setOnClickListener(any()) }
        verify { anyConstructed<FormValidatorController>().configFormSubmit() }
    }

    @Test
    fun build_should_call_configFormSubmit_on_fetchForms() {
        form.buildView(rootView)

        verify { anyConstructed<FormValidatorController>().configFormSubmit() }
    }

    @Test
    fun onClick_of_formSubmit_should_set_formInputViews_on_formValidationActionHandler() {
        // Given
        form = form.copy(onSubmit = listOf(remoteAction))
        val formResult = FormResult.Success(FormValidation(emptyList()))

        // When
        executeFormSubmitOnClickListener()
        resultListenerSlot.captured(formResult)
        runnableSlot.captured.run()

        // Then
        val views = form.getPrivateField<List<FormInput>>(FORM_INPUT_VIEWS_FIELD_NAME)
        verify(exactly = once()) { anyConstructed<FormValidation>().formInputs = views }
    }

    @Test
    fun onClick_of_formSubmit_should_submit_remote_form() {
        // Given
        form = form.copy(onSubmit = listOf(remoteAction))

        // When
        executeFormSubmitOnClickListener()

        // Then
        verify(exactly = once()) { formSubmitView.hideKeyboard() }
        verify(exactly = once()) { form.handleEvent(rootView, formSubmitView, remoteAction, analyticsValue = "onSubmit") }
    }

    @Test
    fun onClick_of_formSubmit_should_trigger_navigate_action() {
        // Given
        form = form.copy(onSubmit = listOf(navigateAction))

        // When
        executeFormSubmitOnClickListener()

        // Then
        verify(exactly = once()) { formSubmitView.hideKeyboard() }
        verify(exactly = once()) { form.handleEvent(rootView, formSubmitView, navigateAction, analyticsValue = "onSubmit") }
    }

    @Test
    fun onClick_of_formSubmit_should_trigger_list_action() {
        // Given
        form = form.copy(onSubmit = listOf(navigateAction, remoteAction))
        val formResult = FormResult.Success(mockk(relaxed = true))

        // When
        executeFormSubmitOnClickListener()
        resultListenerSlot.captured(formResult)
        runnableSlot.captured.run()

        // Then
        verify(exactly = once()) { formSubmitView.hideKeyboard() }
        verifyOrder {
            navigateAction.execute(rootView, formSubmitView)
            formResult.action.execute(rootView, formSubmitView)
        }
    }

    @Test
    fun onClick_of_formSubmit_should_validate_formField_that_is_required_and_is_valid() {
        // Given
        every { formInput.required } returns true
        every { validator.isValid(any(), any()) } returns true

        // When
        executeFormSubmitOnClickListener()

        // Then
        verify(exactly = once()) { validator.isValid(any(), any()) }
    }

    @Test
    fun onClick_of_formSubmit_should_validate_formField_that_is_required_and_that_not_is_valid() {
        // Given
        every { formInput.required } returns true
        every { validator.isValid(any(), any()) } returns false

        // When
        executeFormSubmitOnClickListener()

        // Then
        verify(exactly = once()) { inputWidget.onErrorMessage(any()) }
    }

    @Test
    fun onClick_of_formSubmit_should_handleFormSubmit_and_call_actionExecutor() {
        // Given
        form = form.copy(onSubmit = listOf(remoteAction))
        val formResult = FormResult.Success(mockk(relaxed = true))

        // When
        executeFormSubmitOnClickListener()
        resultListenerSlot.captured(formResult)
        runnableSlot.captured.run()

        // Then
        verify { form.handleEvent(rootView, formSubmitView, remoteAction, analyticsValue = "onSubmit") }
    }

    @Test
    fun onClick_of_formSubmit_should_trigger_action_and_call_showError() {
        // Given
        form = form.copy(onSubmit = listOf(remoteAction))

        every { beagleActivity.onServerDrivenContainerStateChanged(any()) } just Runs
        val formResult = FormResult.Error(mockk())

        // When
        executeFormSubmitOnClickListener()
        resultListenerSlot.captured(formResult)
        runnableSlot.captured.run()

        // Then
        verify(exactly = once()) {
            beagleActivity.onServerDrivenContainerStateChanged(any<ServerDrivenState.FormError>())
        }
    }

    private fun executeFormSubmitOnClickListener() {
        form.buildView(rootView)
        onClickListenerSlot.captured.onClick(formSubmitView)
    }

    private fun createSimpleAdditionalData(): Map<String, String> {
        val additionalData = HashMap<String, String>()
        additionalData[ADDITIONAL_DATA_KEY] = ADDITIONAL_DATA_VALUE
        return additionalData
    }

    @Test
    fun on_form_submit_should_save_data_locally_if_flag_enabled() {
        // Given
        form = form.copy(shouldStoreFields = true, group = FORM_GROUP_VALUE)

        // When
        executeFormSubmitOnClickListener()

        // Then
        verify {
            formDataStoreHandler.put(
                eq(FORM_GROUP_VALUE),
                eq(INPUT_NAME),
                eq(INPUT_VALUE))
        }
    }

    @Test
    fun on_form_submit_should_not_save_data_locally_if_flag_disabled() {
        // Given
        form = form.copy(shouldStoreFields = false)

        // When
        executeFormSubmitOnClickListener()

        // Then
        verify(exactly = 0) {
            formDataStoreHandler.put(any(), any(), any())
        }
    }

    @Test
    fun on_form_submit_should_send_saved_data() {
        // Given
        val savedKey = "savedKey"
        val savedValue = "savedValue"
        val savedMap = HashMap<String, String>()
        savedMap[savedKey] = savedValue
        val formsValuesSlot = slot<Map<String, String>>()
        every { remoteAction.formsValue = capture(formsValuesSlot) } just Runs

        form = form.copy(shouldStoreFields = false, group = FORM_GROUP_VALUE, onSubmit = listOf(remoteAction))

        every { formDataStoreHandler.getAllValues(FORM_GROUP_VALUE) } returns savedMap

        // When
        executeFormSubmitOnClickListener()

        // Then

        assertEquals(savedValue, formsValuesSlot.captured[savedKey])
    }

    @Test
    fun when_form_submit_succeeds_should_clear_saved_data() {
        // Given
        form = form.copy(shouldStoreFields = false, group = FORM_GROUP_VALUE, onSubmit = listOf(remoteAction))

        // When
        executeFormSubmitOnClickListener()
        resultListenerSlot.captured(FormResult.Success(remoteAction))
        runnableSlot.captured.run()

        // Then
        verify {
            formDataStoreHandler.clear(eq(FORM_GROUP_VALUE))
        }
    }

    @Test
    fun on_form_submit_should_save_additional_data_if_shouldStoreFields_flag_enabled() {
        // Given
        form = form.copy(shouldStoreFields = true, group = FORM_GROUP_VALUE,
            additionalData = createSimpleAdditionalData())

        // When
        executeFormSubmitOnClickListener()

        // Then
        verify {
            formDataStoreHandler.put(
                eq(FORM_GROUP_VALUE),
                eq(ADDITIONAL_DATA_KEY),
                eq(ADDITIONAL_DATA_VALUE))
        }
    }

    @Test
    fun on_form_submit_should_send_additional_data_if_shouldStoreFields_flag_disabled() {
        // Given
        val formsValuesSlot = slot<Map<String, String>>()
        every { remoteAction.formsValue = capture(formsValuesSlot) } just Runs

        form = form.copy(shouldStoreFields = false, group = FORM_GROUP_VALUE,
            onSubmit = listOf(remoteAction), additionalData = createSimpleAdditionalData())

        // When
        executeFormSubmitOnClickListener()

        // Then
        assertEquals(ADDITIONAL_DATA_VALUE, formsValuesSlot.captured[ADDITIONAL_DATA_KEY])
    }

    @Test
    fun onClick_of_formSubmit_should_trigger_action_and_call_showError_retry() {
        // Given
        form = form.copy(onSubmit = listOf(remoteAction))
        val slotFormError = slot<ServerDrivenState>()

        every { beagleActivity.onServerDrivenContainerStateChanged(capture(slotFormError)) } just Runs
        val formResult = FormResult.Error(mockk())

        // When
        executeFormSubmitOnClickListener()
        resultListenerSlot.captured(formResult)
        runnableSlot.captured.run()
        (slotFormError.captured as ServerDrivenState.FormError).retry.invoke()

        // Then
        verify(exactly = 2) { form.handleEvent(any(), any(), any<Action>(), analyticsValue = "onSubmit") }
    }
}