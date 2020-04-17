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

import android.content.Context
import br.com.zup.beagle.extensions.once
import br.com.zup.beagle.setup.BeagleEnvironment
import br.com.zup.beagle.view.BeagleActivity
import br.com.zup.beagle.view.ServerDrivenState
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ActionExecutorTest {

    @MockK
    private lateinit var customActionHandler: CustomActionHandler
    @MockK
    private lateinit var navigationActionHandler: NavigationActionHandler
    @MockK
    private lateinit var showNativeDialogActionHandler: ShowNativeDialogActionHandler
    @MockK
    private lateinit var formValidationActionHandler: DefaultActionHandler<FormValidation>
    @MockK
    private lateinit var context: Context
    @MockK
    private lateinit var customAction: CustomAction
    @MockK
    private lateinit var activity: BeagleActivity

    private val actionListener = slot<ActionListener>()
    private val activityStates = mutableListOf<ServerDrivenState>()

    @InjectMockKs
    private lateinit var actionExecutor: ActionExecutor

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        mockkObject(BeagleEnvironment)

        every { BeagleEnvironment.beagleSdk } returns mockk(relaxed = true)
        every { customActionHandler.handle(activity, customAction, capture(actionListener)) } just Runs
        every { activity.onServerDrivenContainerStateChanged(capture(activityStates)) } just Runs
    }

    @After
    fun tearDown() {
        unmockkObject(BeagleEnvironment)
    }

    @Test
    fun doAction_should_handle_Navigate_action() {
        // Given
        val action = mockk<Navigate>()
        every { navigationActionHandler.handle(any(), any()) } just Runs

        // When
        actionExecutor.doAction(context, action)

        // Then
        verify(exactly = once()) { navigationActionHandler.handle(context, action) }
    }

    @Test
    fun doAction_should_handle_ShowNativeDialog_action() {
        // Given
        val action = mockk<ShowNativeDialog>()
        every { showNativeDialogActionHandler.handle(any(), any()) } just Runs

        // When
        actionExecutor.doAction(context, action)

        // Then
        verify(exactly = once()) { showNativeDialogActionHandler.handle(context, action) }
    }

    @Test
    fun doAction_should_handle_FormValidation_action() {
        // Given
        val action = mockk<FormValidation>()
        every { formValidationActionHandler.handle(any(), any()) } just Runs

        // When
        actionExecutor.doAction(context, action)

        // Then
        verify(exactly = once()) { formValidationActionHandler.handle(context, action) }
    }

    @Test
    fun doAction_should_not_handle_FormValidation_action_when_handler_is_null() {
        // Given
        val actionExecutor = ActionExecutor()
        val action = mockk<FormValidation>()
        every { formValidationActionHandler.handle(any(), any()) } just Runs

        // When
        actionExecutor.doAction(context, action)

        // Then
        verify(exactly = 0) { formValidationActionHandler.handle(context, action) }
    }

    @Test
    fun doAction_should_not_handle_CustomAction_action_when_handler_is_null() {
        // Given
        val actionExecutor = ActionExecutor(customActionHandler = null)
        val listener = mockk<ActionListener>()
        val action = mockk<CustomAction>()

        // When
        actionExecutor.doAction(context, action)

        // Then
        verify(exactly = 0) { customActionHandler.handle(context, action, listener) }
    }

    @Test
    fun do_customAction_and_listen_onStart() {
        // Given
        val executor = ActionExecutor(customActionHandler)
        val expectedStates = listOf<ServerDrivenState>(
            ServerDrivenState.Loading(true)
        )

        // When
        executor.doAction(activity, customAction)
        actionListener.captured.onStart()

        // Then
        verify(exactly = once()) { customActionHandler.handle(activity, customAction, actionListener.captured) }
        verify(exactly = once()) { activity.onServerDrivenContainerStateChanged(any()) }
        assertEquals(expectedStates, activityStates)
    }

    @Test
    fun do_customAction_and_listen_onSuccess() {
        // Given
        val executor = ActionExecutor(customActionHandler)
        val expectedState = listOf<ServerDrivenState>(
            ServerDrivenState.Loading(false)
        )
        val dumbAction = mockk<Action>()

        // When
        executor.doAction(activity, customAction)
        actionListener.captured.onSuccess(dumbAction)

        // Then
        verify(exactly = once()) { customActionHandler.handle(activity, customAction, actionListener.captured) }
        verify(exactly = once()) { activity.onServerDrivenContainerStateChanged(any()) }
        assertEquals(expectedState, activityStates)
    }

    @Test
    fun do_customAction_and_listen_onError() {
        // Given
        val executor = ActionExecutor(customActionHandler)
        val error = mockk<Throwable>()
        val expectedState = listOf(
            ServerDrivenState.Loading(false),
            ServerDrivenState.Error(error)
        )

        // When
        executor.doAction(activity, customAction)
        actionListener.captured.onError(error)

        // Then
        verify(exactly = once()) { customActionHandler.handle(activity, customAction, actionListener.captured) }
        verify(exactly = 2) { activity.onServerDrivenContainerStateChanged(any()) }
        assertEquals(expectedState, activityStates)
    }
}