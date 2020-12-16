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

package br.com.zup.beagle.android.context

import android.view.View
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.action.AsyncAction
import br.com.zup.beagle.android.action.BaseAsyncActionTest
import br.com.zup.beagle.android.action.SendRequest
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.view.viewmodel.AsyncActionViewModel
import br.com.zup.beagle.android.view.viewmodel.ScreenContextViewModel
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifySequence
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach

data class PersonTest(val name: String)

private const val NAME = "name"

class ContextActionExecutorTest : BaseAsyncActionTest() {

    private val viewModel = mockk<ScreenContextViewModel>(relaxed = true)
    private val sender = mockk<Action>()
    private val action = mockk<Action>()
    private val view: View = mockk()

    private val contextActionExecutor = ContextActionExecutor

    private val contextDataSlot = slot<ContextData>()

    @BeforeEach
    override fun setUp() {
        super.setUp()

        every { action.execute(any(), view) } just Runs

        prepareViewModelMock(viewModel)

        every { viewModel.addImplicitContext(capture(contextDataSlot), any(), any()) } just Runs
    }

    @Test
    fun executeActions_should_create_implicit_context() {
        // Given
        val eventId = "onChange"
        val value = RandomData.string()

        // When
        contextActionExecutor.executeActions(rootView, view, sender, listOf(action), ContextData(eventId, value))

        // Then
        verifySequence {
            viewModel.addImplicitContext(contextDataSlot.captured, sender, listOf(action))
            action.execute(rootView, view)
        }
    }

    @Test
    fun executeActions_should_parse_primitive_value_to_JSONObject() {
        // Given
        val eventId = "onChange"
        val value = RandomData.string()

        // When
        contextActionExecutor.executeActions(rootView, view, sender, listOf(action), ContextData(eventId, value))

        // Then
        assertEquals(eventId, contextDataSlot.captured.id)
        assertEquals(value, contextDataSlot.captured.value.toString())
    }

    @Test
    fun executeActions_should_parse_object_value_to_JSONObject() {
        // Given
        val eventId = "onChange"
        val value = PersonTest(name = NAME)

        // When
        contextActionExecutor.executeActions(rootView, view, sender, listOf(action), ContextData(eventId, value))

        // Then
        assertEquals(eventId, contextDataSlot.captured.id)
        val expected = JSONObject()
            .put(NAME, NAME)
            .toString()
        assertEquals(expected, contextDataSlot.captured.value.toString())
    }

    @Test
    fun executeActions_should_parse_list_of_object_value_to_JSONArray() {
        // Given
        val eventId = "onChange"
        val value = arrayListOf(PersonTest(name = NAME))

        // When
        contextActionExecutor.executeActions(rootView, view, sender, listOf(action), ContextData(eventId, value))

        // Then
        assertEquals(eventId, contextDataSlot.captured.id)
        val expected = JSONArray()
            .put(
                JSONObject().put(NAME, NAME)
            ).toString()
        assertEquals(expected, contextDataSlot.captured.value.toString())
    }

    @Test
    fun executeActions_should_not_create_implicit_context_when_context_is_null() {
        // Given
        val context = null

        // When
        contextActionExecutor.executeActions(rootView, view, sender, listOf(action), context)

        // Then
        verify(exactly = 0) { viewModel.addImplicitContext(any(), any(), any()) }
        verify(exactly = once()) { action.execute(rootView, view) }
    }

    @Test
    fun `GIVEN an AsyncAction WHEN executed THEN should call onActionStarted and onAsyncActionExecuted`() {
        // Given
        val context = null
        val asyncActionViewModel = mockk<AsyncActionViewModel>()
        prepareViewModelMock(asyncActionViewModel)
        val asyncActionSlot = slot<AsyncActionData>()
        val asyncAction = SendRequest("http://www.test.com")
        asyncAction.status.observeForever(observer)
        every { asyncActionViewModel.onAsyncActionExecuted(capture(asyncActionSlot)) } just Runs

        // When
        contextActionExecutor.executeActions(rootView, view, sender, listOf(asyncAction), context)

        // Then
        assertEquals(asyncActionSlot.captured.asyncAction, asyncAction)
        assertEquals(asyncActionSlot.captured.origin, view)
        verify(exactly = 1) { asyncActionViewModel.onAsyncActionExecuted(asyncActionSlot.captured) }
        verify(exactly = 1) { asyncAction.onActionStarted() }
        assert(onActionStartedWasCalled())
    }
}
