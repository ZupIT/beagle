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

import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.view.BeagleActivity
import br.com.zup.beagle.android.view.ServerDrivenState
import br.com.zup.beagle.android.widget.RootView
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertEquals

class FormLocalActionTest : BaseTest() {

    @RelaxedMockK
    private lateinit var rootView: RootView
    @MockK
    private lateinit var formLocalActionHandler: FormLocalActionHandler
    @MockK
    private lateinit var activity: BeagleActivity

    private val actionListener = slot<ActionListener>()
    private val activityStates = mutableListOf<ServerDrivenState>()

    override fun setUp() {
        super.setUp()

        every { rootView.getContext() } returns activity
        every { formLocalActionHandler.handle(any(), any(), capture(actionListener)) } just Runs
        every { activity.onServerDrivenContainerStateChanged(capture(activityStates)) } just Runs
    }

    @Test
    fun doAction_should_not_handle_CustomAction_action_when_handler_is_null() {
        // Given
        val formLocalAction = FormLocalAction("Stub", emptyMap())
        val listener = mockk<ActionListener>()

        // When
        formLocalAction.execute(rootView)

        // Then
        verify(exactly = 0) { formLocalActionHandler.handle(any(), any(), listener) }
    }

    @Test
    fun do_customAction_and_listen_onStart() {
        // Given
        val formLocalAction = FormLocalAction("Stub", emptyMap())
        val expectedStates = listOf<ServerDrivenState>(
            ServerDrivenState.Loading(true)
        )

        // When
        formLocalAction.formLocalActionHandler = formLocalActionHandler
        formLocalAction.execute(rootView)
        actionListener.captured.onStart()

        // Then
        verify(exactly = once()) { formLocalActionHandler.handle(activity, any(), actionListener.captured) }
        verify(exactly = once()) { activity.onServerDrivenContainerStateChanged(any()) }
        assertEquals(expectedStates, activityStates)
    }

    @Test
    fun do_customAction_and_listen_onSuccess() {
        // Given
        val formLocalAction = FormLocalAction("Stub", emptyMap())
        val expectedState = listOf<ServerDrivenState>(
            ServerDrivenState.Loading(false)
        )
        val dumbAction = mockk<Action>(relaxed = true)

        // When
        formLocalAction.formLocalActionHandler = formLocalActionHandler
        formLocalAction.execute(rootView)
        actionListener.captured.onSuccess(dumbAction)

        // Then
        verify(exactly = once()) { formLocalActionHandler.handle(activity, any(), actionListener.captured) }
        verify(exactly = once()) { activity.onServerDrivenContainerStateChanged(any()) }
        assertEquals(expectedState, activityStates)
    }

    @Test
    fun do_customAction_and_listen_onError() {
        // Given
        val formLocalAction = FormLocalAction("Stub", emptyMap())
        val error = mockk<Throwable>()
        val expectedState = listOf(
            ServerDrivenState.Loading(false),
            ServerDrivenState.Error(error)
        )

        // When
        formLocalAction.formLocalActionHandler = formLocalActionHandler
        formLocalAction.execute(rootView)
        actionListener.captured.onError(error)

        // Then
        verify(exactly = once()) { formLocalActionHandler.handle(activity, any(), actionListener.captured) }
        verify(exactly = 2) { activity.onServerDrivenContainerStateChanged(any()) }
        assertEquals(expectedState, activityStates)
    }
}
