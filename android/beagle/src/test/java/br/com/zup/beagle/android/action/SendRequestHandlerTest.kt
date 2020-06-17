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

import androidx.appcompat.app.AppCompatActivity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import br.com.zup.beagle.android.context.ContextActionExecutor
import br.com.zup.beagle.android.engine.renderer.ActivityRootView
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.utils.ViewModelProviderFactory
import br.com.zup.beagle.android.utils.contextActionExecutor
import br.com.zup.beagle.android.view.viewmodel.ActionRequestViewModel
import br.com.zup.beagle.android.view.viewmodel.Response
import br.com.zup.beagle.android.context.Bind.Companion.valueOf
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class SendRequestHandlerTest {

    @get:Rule
    var executorRule = InstantTaskExecutorRule()

    private val contextActionExecutorMock = mockk<ContextActionExecutor>()
    private val rootView: ActivityRootView = mockk(relaxed = true)
    private val viewModel: ActionRequestViewModel = mockk()
    private val liveData: MutableLiveData<ActionRequestViewModel.FetchViewState> = mockk()
    private val observerSlot = slot<Observer<ActionRequestViewModel.FetchViewState>>()
    private val responseData: Response = mockk()

    @Before
    fun setUp() {
        mockkObject(ViewModelProviderFactory)

        contextActionExecutor = contextActionExecutorMock

        every {
            ViewModelProviderFactory
                .of(any<AppCompatActivity>())
                .get(ActionRequestViewModel::class.java)
        } returns viewModel

        every { contextActionExecutorMock.executeAction(any(), any(), any(), any()) } just Runs
    }

    @Test
    fun `should execute with success action when handle action`() {
        // Given
        val onSuccessAction: Action = mockk()
        val onErrorAction: Action = mockk()
        val onFinishAction: Action = mockk()
        val requestAction = SendRequest(url = "", onSuccess = onSuccessAction,
            onError = onErrorAction, onFinish = onFinishAction)
        every { viewModel.fetch(any()) } returns liveData
        every { liveData.observe(rootView.getLifecycleOwner(), capture(observerSlot)) } just Runs

        // When
        requestAction.execute(rootView)
        val result = ActionRequestViewModel.FetchViewState.Success(responseData)
        observerSlot.captured.onChanged(result)

        // Then
        verifyOrder {
            contextActionExecutor.executeAction(rootView, onFinishAction, "onFinish")
            contextActionExecutor.executeAction(rootView, onSuccessAction, "onSuccess", any())
        }
    }

    @Test
    fun `should execute with fail action when handle action`() {
        // Given
        val onSuccessAction: Action = mockk()
        val onErrorAction: Action = mockk()
        val onFinishAction: Action = mockk()
        val requestAction = SendRequest(url = valueOf(""), onSuccess = onSuccessAction,
            onError = onErrorAction, onFinish = onFinishAction)
        every { viewModel.fetch(any()) } returns liveData
        every { liveData.observe(rootView.getLifecycleOwner(), capture(observerSlot)) } just Runs

        // When
        requestAction.execute(rootView)
        val result = ActionRequestViewModel.FetchViewState.Error(responseData)
        observerSlot.captured.onChanged(result)

        // Then
        verifyOrder {
            contextActionExecutor.executeAction(rootView, onFinishAction, "onFinish", null)
            contextActionExecutor.executeAction(rootView, onErrorAction, "onError", any())
        }
    }

    @Test
    fun `should not send action success when handle action`() {
        // Given
        val onErrorAction: Action = mockk()
        val onFinishAction: Action = mockk()
        val requestAction = SendRequest(url = valueOf(""), onSuccess = null,
            onError = onErrorAction, onFinish = onFinishAction)
        every { viewModel.fetch(any()) } returns liveData
        every { liveData.observe(rootView.getLifecycleOwner(), capture(observerSlot)) } just Runs

        // When
        requestAction.execute(rootView)
        val result = ActionRequestViewModel.FetchViewState.Success(mockk())
        observerSlot.captured.onChanged(result)

        // Then
        verify(exactly = once()) { contextActionExecutorMock.executeAction(rootView, any(), "onFinish") }
    }

    @Test
    fun `should not send any action when handle action`() {
        // Given
        val requestAction = SendRequest(url = valueOf(""), onSuccess = null,
            onError = null, onFinish = null)
        every { viewModel.fetch(any()) } returns liveData
        every { liveData.observe(rootView.getLifecycleOwner(), capture(observerSlot)) } just Runs

        // When
        requestAction.execute(rootView)
        val result = ActionRequestViewModel.FetchViewState.Success(mockk())
        observerSlot.captured.onChanged(result)

        // Then
        verify(exactly = 0) { contextActionExecutor.executeAction(any(), any(), any(), any()) }
    }


    @Test
    fun `should send only action finish when handle action with success`() {
        // Given
        val onFinishAction: Action = mockk()
        val requestAction = SendRequest(url = valueOf(""), onSuccess = null,
            onError = null, onFinish = onFinishAction)
        every { viewModel.fetch(any()) } returns liveData
        every { liveData.observe(rootView.getLifecycleOwner(), capture(observerSlot)) } just Runs

        // When
        requestAction.execute(rootView)
        val result = ActionRequestViewModel.FetchViewState.Success(mockk())
        observerSlot.captured.onChanged(result)

        // Then
        verify(exactly = once()) { contextActionExecutor.executeAction(rootView, onFinishAction, "onFinish") }
    }

    @Test
    fun `should send only action finish when handle action with error`() {
        // Given
        val onFinishAction: Action = mockk()
        val requestAction = SendRequest(url = valueOf(""), onSuccess = null,
            onError = null, onFinish = onFinishAction)
        every { viewModel.fetch(any()) } returns liveData
        every { liveData.observe(rootView.getLifecycleOwner(), capture(observerSlot)) } just Runs

        // When
        requestAction.execute(rootView)
        val result = ActionRequestViewModel.FetchViewState.Error(mockk())
        observerSlot.captured.onChanged(result)

        // Then
        verify(exactly = once()) { contextActionExecutor.executeAction(rootView, onFinishAction, "onFinish") }
    }
}