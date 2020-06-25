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

import androidx.appcompat.app.AppCompatActivity
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.data.serializer.BeagleMoshi
import br.com.zup.beagle.android.engine.renderer.ActivityRootView
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.utils.ViewModelProviderFactory
import br.com.zup.beagle.android.view.viewmodel.ScreenContextViewModel
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import io.mockk.verifySequence
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Test

import kotlin.test.assertEquals

data class PersonTest(val name: String)

private const val NAME = "name"

class ContextActionExecutorTest : BaseTest() {

    private val rootView = mockk<ActivityRootView>()
    private val viewModel = mockk<ScreenContextViewModel>(relaxed = true)
    private val sender = mockk<Action>()
    private val action = mockk<Action>()

    private lateinit var contextActionExecutor: ContextActionExecutor

    private val contextDataSlot = slot<ContextData>()

    override fun setUp() {
        super.setUp()

        mockkObject(ViewModelProviderFactory)

        contextActionExecutor = ContextActionExecutor()

        every { action.execute(any()) } just Runs
        every { rootView.activity } returns mockk()

        every {
            ViewModelProviderFactory.of(any<AppCompatActivity>())[ScreenContextViewModel::class.java]
        } returns viewModel
        every { viewModel.addImplicitContext(capture(contextDataSlot), any(), any()) } just Runs
    }

    @Test
    fun executeActions_should_create_implicit_context() {
        // Given
        val eventId = "onChange"
        val value = RandomData.string()

        // When
        contextActionExecutor.executeActions(rootView, sender, listOf(action), eventId, value)

        // Then
        verifySequence {
            viewModel.addImplicitContext(contextDataSlot.captured, sender, listOf(action))
            action.execute(rootView)
        }
    }

    @Test
    fun executeActions_should_parse_primitive_value_to_JSONObject() {
        // Given
        val eventId = "onChange"
        val value = RandomData.string()

        // When
        contextActionExecutor.executeActions(rootView, sender, listOf(action), eventId, value)

        // Then
        assertEquals(eventId, contextDataSlot.captured.id)
        assertEquals("{\"value\":\"$value\"}", contextDataSlot.captured.value.toString())
    }

    @Test
    fun executeActions_should_parse_object_value_to_JSONObject() {
        // Given
        val eventId = "onChange"
        val value = PersonTest(name = NAME)

        // When
        contextActionExecutor.executeActions(rootView, sender, listOf(action), eventId, value)

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
        contextActionExecutor.executeActions(rootView, sender, listOf(action), eventId, value)

        // Then
        assertEquals(eventId, contextDataSlot.captured.id)
        val expected = JSONArray()
            .put(
                JSONObject().put(NAME, NAME)
            ).toString()
        assertEquals(expected, contextDataSlot.captured.value.toString())
    }

    @Test
    fun executeActions_should_not_create_implicit_context_when_value_is_null() {
        // Given
        val eventId = "onChange"
        val value = null

        // When
        contextActionExecutor.executeActions(rootView, sender, listOf(action), eventId, value)

        // Then
        verify(exactly = 0) { viewModel.addImplicitContext(any(), any(), any()) }
        verify(exactly = once()) { action.execute(rootView) }
    }
}