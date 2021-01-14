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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.context.valueOf
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.utils.evaluateExpression
import br.com.zup.beagle.android.utils.handleEvent
import br.com.zup.beagle.android.view.viewmodel.ActionRequestViewModel
import br.com.zup.beagle.android.view.viewmodel.FetchViewState
import br.com.zup.beagle.android.view.viewmodel.Response
import br.com.zup.beagle.core.ServerDrivenComponent
import io.mockk.*
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

data class DataTest(val email: String, val password: String)

class SendRequestTest : BaseAsyncActionTest() {

    private val viewModel: ActionRequestViewModel = mockk()
    private val liveData: MutableLiveData<FetchViewState> = mockk()
    private val observerSlot = slot<Observer<FetchViewState>>()
    private val responseData: Response = mockk()
    private val view: View = mockk()
    private val contextDataSlot = slot<ContextData>()

    @BeforeEach
    override fun setUp() {
        super.setUp()

        prepareViewModelMock(viewModel)
        mockkStatic("br.com.zup.beagle.android.utils.ActionExtensionsKt")

        every { viewModel.fetch(any()) } returns liveData
        every { liveData.observe(rootView.getLifecycleOwner(), capture(observerSlot)) } just Runs
    }

    @Test
    fun `should execute with success action when handle action`() {
        // Given
        val onSuccessAction: Action = mockk()
        val onErrorAction: Action = mockk()
        val onFinishAction: Action = mockk()
        val requestAction = createSendRequest(onSuccess = listOf(onSuccessAction),
            onError = listOf(onErrorAction), onFinish = listOf(onFinishAction))

        // When
        requestAction.execute(rootView, view)
        val result = FetchViewState.Success(responseData)
        observerSlot.captured.onChanged(result)

        // Then
        verifyOrder {
            requestAction.handleEvent(
                rootView,
                view,
                listOf(onFinishAction),
                analyticsValue = "onFinish"
            )
            requestAction.handleEvent(
                rootView,
                view,
                listOf(onSuccessAction),
                any<ContextData>(),
                analyticsValue = "onSuccess"
            )
        }

        assertEquals("onSuccess", contextDataSlot.captured.id)
    }

    @Test
    fun `should execute with fail action when handle action`() {
        // Given
        val onSuccessAction: Action = mockk()
        val onErrorAction: Action = mockk()
        val onFinishAction: Action = mockk()
        val requestAction = createSendRequest(onSuccess = listOf(onSuccessAction),
            onError = listOf(onErrorAction), onFinish = listOf(onFinishAction))

        // When
        requestAction.execute(rootView, view)
        val result = FetchViewState.Error(responseData)
        observerSlot.captured.onChanged(result)

        // Then
        verifyOrder {
            requestAction.handleEvent(
                rootView,
                view,
                listOf(onFinishAction),
                analyticsValue = "onFinish"
            )
            requestAction.handleEvent(
                rootView,
                view,
                listOf(onErrorAction),
                any<ContextData>(),
                analyticsValue = "onError"
            )
        }

        assertEquals("onError", contextDataSlot.captured.id)
    }

    @Test
    fun `should not send action success when handle action`() {
        // Given
        val onErrorAction: Action = mockk()
        val onFinishAction: Action = mockk()
        val requestAction = createSendRequest(onSuccess = null,
            onError = listOf(onErrorAction), onFinish = listOf(onFinishAction))

        // When
        requestAction.execute(rootView, view)
        val result = FetchViewState.Success(mockk())
        observerSlot.captured.onChanged(result)

        // Then
        verify(exactly = once()) {
            requestAction.handleEvent(
                rootView,
                view,
                listOf(onFinishAction),
                analyticsValue = "onFinish"
            )
        }
    }

    @Test
    fun `should not send any action when handle action`() {
        // Given
        val requestAction = createSendRequest(onSuccess = null,
            onError = null, onFinish = null)

        // When
        requestAction.execute(rootView, view)
        val result = FetchViewState.Success(mockk())
        observerSlot.captured.onChanged(result)

        // Then
        verify(exactly = 0) {
            requestAction.handleEvent(any(), any(), any<List<Action>>(), analyticsValue = any())
        }
    }

    @Test
    fun `GIVEN a RequestAction WHEN finish execution THEN should call onActionFinished`() {
        // Given
        val requestAction = createSendRequest(onSuccess = null, onError = null, onFinish = null)

        // When
        requestAction.status.observeForever(observer)
        requestAction.execute(rootView, view)
        val result = FetchViewState.Success(mockk())
        observerSlot.captured.onChanged(result)

        // Then
        assert(onActionFinishedWasCalled())
    }

    @Test
    fun `should send only action finish when handle action with success`() {
        // Given
        val onFinishAction: Action = mockk()
        val requestAction = createSendRequest(onSuccess = null,
            onError = null, onFinish = listOf(onFinishAction))

        // When
        requestAction.execute(rootView, view)
        val result = FetchViewState.Success(mockk())
        observerSlot.captured.onChanged(result)

        // Then
        verify(exactly = once()) {
            requestAction.handleEvent(
                rootView,
                view,
                listOf(onFinishAction),
                analyticsValue = "onFinish"
            )
        }
    }

    @Test
    fun `should send only action finish when handle action with error`() {
        // Given
        val onFinishAction: Action = mockk()
        val requestAction = createSendRequest(onSuccess = null,
            onError = null, onFinish = listOf(onFinishAction))

        // When
        requestAction.execute(rootView, view)
        val result = FetchViewState.Error(mockk())
        observerSlot.captured.onChanged(result)

        // Then
        verify(exactly = once()) {
            requestAction.handleEvent(
                rootView,
                view,
                listOf(onFinishAction),
                analyticsValue = "onFinish"
            )
        }
    }

    private fun createSendRequest(
        onSuccess: List<Action>? = null,
        onError: List<Action>? = null,
        onFinish: List<Action>? = null,
        data: Any? = null
    ): SendRequest {
        return SendRequest(
            url = valueOf(""),
            onSuccess = onSuccess,
            onError = onError,
            onFinish = onFinish,
            data = data
        ).apply {
            every { evaluateExpression(rootView, view, any<Any>()) } returns ""
            every { handleEvent(rootView, view, any<List<Action>>(), capture(contextDataSlot), analyticsValue = any()) } just Runs
            every { handleEvent(rootView, view, any<List<Action>>(), analyticsValue = any()) } just Runs
        }
    }

    @Test
    fun `Data sent should be an JSON object`() {
        // Given
        val dataSlot = slot<Any>()
        val requestAction = createSendRequest(data = DataTest("name@email.com", "123456"))
        val mockedSendRequestInternal = slot<SendRequestInternal>()
        every { viewModel.fetch(capture(mockedSendRequestInternal)) } returns liveData
        every { requestAction.evaluateExpression(any(), any(), capture(dataSlot)) } answers {
            dataSlot.captured
        }

        // When
        requestAction.execute(rootView, view)

        // Then
        assertTrue(mockedSendRequestInternal.captured.data is JSONObject)
    }
}