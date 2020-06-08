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

package br.com.zup.beagle.action

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.extensions.once
import br.com.zup.beagle.networking.ResponseData
import br.com.zup.beagle.view.viewmodel.ActionRequestViewModel
import br.com.zup.beagle.widget.core.Action
import io.mockk.Called
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Rule
import org.junit.Test


class SendRequestActionHandlerTest {

    @get:Rule
    var executorRule = InstantTaskExecutorRule()

    private val rootView: RootView = mockk(relaxed = true)
    private val viewModel: ActionRequestViewModel = mockk()
    private val listener: SendRequestListener = mockk(relaxed = true)
    private val liveData: MutableLiveData<ActionRequestViewModel.FetchViewState> = mockk()
    private val observerSlot = slot<Observer<ActionRequestViewModel.FetchViewState>>()
    private val responseData: ResponseData = mockk()

    @Test
    fun `should execute with success action when handle action`() {
        // Given
        val onSuccessAction: Action = mockk()
        val onErrorAction: Action = mockk()
        val onFinishAction: Action = mockk()
        val requestAction = SendRequestAction(url = "", onSuccess = onSuccessAction,
            onError = onErrorAction, onFinish = onFinishAction)
        every { viewModel.fetch(requestAction) } returns liveData
        every { liveData.observe(rootView.getLifecycleOwner(), capture(observerSlot)) } just Runs

        // When
        SendRequestActionHandler().handle(rootView, requestAction, viewModel, listener)
        val result = ActionRequestViewModel.FetchViewState.Success(responseData)
        observerSlot.captured.onChanged(result)

        // Then
        verify(exactly = once()) { listener.invoke(listOf(onFinishAction, onSuccessAction)) }
    }

    @Test
    fun `should execute with fail action when handle action`() {
        // Given
        val onSuccessAction: Action = mockk()
        val onErrorAction: Action = mockk()
        val onFinishAction: Action = mockk()
        val responseData: ResponseData = mockk()
        val requestAction = SendRequestAction(url = "", onSuccess = onSuccessAction,
            onError = onErrorAction, onFinish = onFinishAction)
        every { viewModel.fetch(requestAction) } returns liveData
        every { liveData.observe(rootView.getLifecycleOwner(), capture(observerSlot)) } just Runs

        // When
        SendRequestActionHandler().handle(rootView, requestAction, viewModel, listener)
        val result = ActionRequestViewModel.FetchViewState.Error(responseData)
        observerSlot.captured.onChanged(result)

        // Then
        verify(exactly = once()) { listener.invoke(listOf(onFinishAction, onErrorAction)) }
    }

    @Test
    fun `should not send action success when handle action`() {
        // Given
        val onErrorAction: Action = mockk()
        val onFinishAction: Action = mockk()
        val requestAction = SendRequestAction(url = "", onSuccess = null,
            onError = onErrorAction, onFinish = onFinishAction)
        every { viewModel.fetch(requestAction) } returns liveData
        every { liveData.observe(rootView.getLifecycleOwner(), capture(observerSlot)) } just Runs

        // When
        SendRequestActionHandler().handle(rootView, requestAction, viewModel, listener)
        val result = ActionRequestViewModel.FetchViewState.Success(mockk())
        observerSlot.captured.onChanged(result)

        // Then
        verify(exactly = once()) { listener.invoke(listOf(onFinishAction)) }
    }

    @Test
    fun `should not send any action when handle action`() {
        // Given
        val requestAction = SendRequestAction(url = "", onSuccess = null,
            onError = null, onFinish = null)
        every { viewModel.fetch(requestAction) } returns liveData
        every { liveData.observe(rootView.getLifecycleOwner(), capture(observerSlot)) } just Runs

        // When
        SendRequestActionHandler().handle(rootView, requestAction, viewModel, listener)
        val result = ActionRequestViewModel.FetchViewState.Success(mockk())
        observerSlot.captured.onChanged(result)

        // Then
        verify { listener.invoke(any()) wasNot Called }
    }


    @Test
    fun `should send only action finish when handle action with success`() {
        // Given
        val onFinishAction: Action = mockk()
        val requestAction = SendRequestAction(url = "", onSuccess = null,
            onError = null, onFinish = onFinishAction)
        every { viewModel.fetch(requestAction) } returns liveData
        every { liveData.observe(rootView.getLifecycleOwner(), capture(observerSlot)) } just Runs

        // When
        SendRequestActionHandler().handle(rootView, requestAction, viewModel, listener)
        val result = ActionRequestViewModel.FetchViewState.Success(mockk())
        observerSlot.captured.onChanged(result)

        // Then
        verify { listener.invoke(any()) wasNot Called }
    }

    @Test
    fun `should send only action finish when handle action with error`() {
        // Given
        val onFinishAction: Action = mockk()
        val requestAction = SendRequestAction(url = "", onSuccess = null,
            onError = null, onFinish = onFinishAction)
        every { viewModel.fetch(requestAction) } returns liveData
        every { liveData.observe(rootView.getLifecycleOwner(), capture(observerSlot)) } just Runs

        // When
        SendRequestActionHandler().handle(rootView, requestAction, viewModel, listener)
        val result = ActionRequestViewModel.FetchViewState.Error(mockk())
        observerSlot.captured.onChanged(result)

        // Then
        verify { listener.invoke(any()) wasNot Called }
    }
}