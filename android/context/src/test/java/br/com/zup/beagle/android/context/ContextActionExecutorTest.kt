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
import androidx.appcompat.app.AppCompatActivity
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.action.ContextActionExecutor
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.viewmodel.ScreenContextViewModel
import br.com.zup.beagle.android.widget.ActivityRootView
import br.com.zup.beagle.android.widget.ViewModelProviderFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifySequence
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Before
import org.junit.Test

import kotlin.test.assertEquals

data class PersonTest(val name: String)

private const val NAME = "name"

class ContextActionExecutorTest {

    private val rootView = mockk<ActivityRootView>()
    private val viewModel = mockk<ScreenContextViewModel>(relaxed = true)
    private val sender = mockk<Action>()
    private val action = mockk<Action>()
    private val view: View = mockk()
    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private lateinit var contextActionExecutor: ContextActionExecutor

    private val contextDataSlot = slot<ContextData>()


    @Before
    fun setUp() {

        mockkObject(ViewModelProviderFactory)

        contextActionExecutor = ContextActionExecutor()

        every { action.execute(any(), view) } just Runs
        every { rootView.activity } returns mockk()

        ContextConstant.moshi = moshi

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
}