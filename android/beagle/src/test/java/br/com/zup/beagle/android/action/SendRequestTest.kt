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
import androidx.appcompat.app.AppCompatActivity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.engine.renderer.ActivityRootView
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.view.viewmodel.ActionRequestViewModel
import br.com.zup.beagle.android.view.viewmodel.Response
import br.com.zup.beagle.android.context.valueOf
import br.com.zup.beagle.android.utils.ViewModelProviderFactory
import br.com.zup.beagle.android.utils.evaluateExpression
import br.com.zup.beagle.android.utils.handleEvent
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class SendRequestTest {

    @get:Rule
    var executorRule = InstantTaskExecutorRule()

    private val rootView: ActivityRootView = mockk(relaxed = true)
    private val viewModel: ActionRequestViewModel = mockk()
    private val liveData: MutableLiveData<ActionRequestViewModel.FetchViewState> = mockk()
    private val observerSlot = slot<Observer<ActionRequestViewModel.FetchViewState>>()
    private val responseData: Response = mockk()
    private val contextData: ContextData = mockk()
    private val view: View = mockk()
    private val contextDataSlot = slot<ContextData>()

    @Before
    fun setUp() {
        mockkObject(ViewModelProviderFactory)

        every {
            ViewModelProviderFactory
                .of(any<AppCompatActivity>())
                .get(ActionRequestViewModel::class.java)
        } returns viewModel

        mockkStatic("br.com.zup.beagle.android.utils.ActionExtensionsKt")

        every { viewModel.fetch(any()) } returns liveData
        every { liveData.observe(rootView.getLifecycleOwner(), capture(observerSlot)) } just Runs
    }

    @After
    fun tearDown() {
        unmockkAll()
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
        val result = ActionRequestViewModel.FetchViewState.Success(responseData)
        observerSlot.captured.onChanged(result)

        // Then
        verifyOrder {
            requestAction.handleEvent(rootView, view, listOf(onFinishAction))
            requestAction.handleEvent(rootView, view, listOf(onSuccessAction), any<ContextData>())
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
        val result = ActionRequestViewModel.FetchViewState.Error(responseData)
        observerSlot.captured.onChanged(result)

        // Then
        verifyOrder {
            requestAction.handleEvent(rootView, view, listOf(onFinishAction))
            requestAction.handleEvent(rootView, view, listOf(onErrorAction), any<ContextData>())
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
        val result = ActionRequestViewModel.FetchViewState.Success(mockk())
        observerSlot.captured.onChanged(result)

        // Then
        verify(exactly = once()) {
            requestAction.handleEvent(rootView, view, listOf(onFinishAction))
        }
    }

    @Test
    fun `should not send any action when handle action`() {
        // Given
        val requestAction = createSendRequest(onSuccess = null,
            onError = null, onFinish = null)

        // When
        requestAction.execute(rootView, view)
        val result = ActionRequestViewModel.FetchViewState.Success(mockk())
        observerSlot.captured.onChanged(result)

        // Then
        verify(exactly = 0) {
            requestAction.handleEvent(any(), any(), any<List<Action>>())
        }
    }


    @Test
    fun `should send only action finish when handle action with success`() {
        // Given
        val onFinishAction: Action = mockk()
        val requestAction = createSendRequest(onSuccess = null,
            onError = null, onFinish = listOf(onFinishAction))

        // When
        requestAction.execute(rootView, view)
        val result = ActionRequestViewModel.FetchViewState.Success(mockk())
        observerSlot.captured.onChanged(result)

        // Then
        verify(exactly = once()) {
            requestAction.handleEvent(rootView, view, listOf(onFinishAction))
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
        val result = ActionRequestViewModel.FetchViewState.Error(mockk())
        observerSlot.captured.onChanged(result)

        // Then
        verify(exactly = once()) {
            requestAction.handleEvent(rootView, view, listOf(onFinishAction))
        }
    }

    private fun createSendRequest(
        onSuccess: List<Action>?,
        onError: List<Action>?,
        onFinish: List<Action>?
    ): SendRequest {
        return SendRequest(
            url = valueOf(""),
            onSuccess = onSuccess,
            onError = onError,
            onFinish = onFinish
        ).apply {
            every { evaluateExpression(rootView, any<Any>()) } returns ""
            every { handleEvent(rootView, view, any<List<Action>>(), capture(contextDataSlot)) } just Runs
            every { handleEvent(rootView, view, any<List<Action>>()) } just Runs

        }
    }
}