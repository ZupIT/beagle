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

import br.com.zup.beagle.android.action.SetContextInternal
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.jsonpath.JsonPathFinder
import br.com.zup.beagle.android.jsonpath.JsonPathReplacer
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.testutil.getPrivateField
import com.squareup.moshi.Moshi
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import org.json.JSONArray
import org.json.JSONObject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private val CONTEXT_ID = RandomData.string()

data class Model(val a: String, val b: Boolean)

class ContextDataManagerTest {

    private lateinit var contextDataManager: ContextDataManager

    private lateinit var contexts: MutableMap<String, ContextBinding>

    @MockK
    private lateinit var jsonPathFinder: JsonPathFinder
    @MockK
    private lateinit var jsonPathReplacer: JsonPathReplacer
    @MockK
    private lateinit var contextPathResolver: ContextPathResolver
    @MockK
    private lateinit var moshi: Moshi

    @MockK
    private lateinit var bindModel: Bind.Expression<Model>
    @MockK
    private lateinit var model: Model

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { bindModel.type } returns Model::class.java
        every { bindModel.value } returns "@{$CONTEXT_ID}"
        every { bindModel.notifyChange(any()) } just Runs

        mockkObject(BeagleMessageLogs)
        every { BeagleMessageLogs.errorWhileTryingToNotifyContextChanges(any()) } just Runs
        every { BeagleMessageLogs.errorWhileTryingToChangeContext(any()) } just Runs
        every { BeagleMessageLogs.errorWhileTryingToAccessContext(any()) } just Runs
        every { contextPathResolver.getKeysFromPath(any(), any()) } returns LinkedList()
        every { jsonPathFinder.find(any(), any()) } returns mockk()
        every { moshi.adapter<Any>(any<Class<*>>()).fromJson(any<String>()) } returns bindModel

        contextDataManager = ContextDataManager(
            jsonPathFinder,
            jsonPathReplacer,
            contextPathResolver,
            moshi
        )

        contexts = contextDataManager.getPrivateField("contexts")

        contexts.clear()

        val contextData = ContextData(CONTEXT_ID, model)
        contexts[CONTEXT_ID] = ContextBinding(contextData, mutableListOf(bindModel))
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun addContext_should_add_new_context() {
        // Given
        val contextData = ContextData(CONTEXT_ID, true)

        // When
        contextDataManager.addContext(contextData)

        // Then
        val contextBinding = contexts[contextData.id]
        assertNotNull(contextBinding)
        assertEquals(contextBinding?.context, contextData)
        assertEquals(0, contextBinding?.bindings?.size)
    }

    @Test
    fun addBindingToContext_should_add_binding_to_context_on_stack() {
        // Given
        val bind = Bind.Expression("@{$CONTEXT_ID.a}", type = Model::class.java)
        val contextData = ContextData(CONTEXT_ID, true)
        contextDataManager.addContext(contextData)

        // When
        contextDataManager.addBindingToContext(bind)

        // Then
        assertEquals(bind, contexts[CONTEXT_ID]?.bindings?.get(0))
    }

    @Test
    fun addBindingToContext_should_add_binding_to_context_on_top_of_stack() {
        // Given
        val bind = Bind.Expression("@{$CONTEXT_ID.a}", type = Model::class.java)
        val contextData = ContextData(CONTEXT_ID, true)
        contextDataManager.addContext(contextData)

        // When
        contextDataManager.addBindingToContext(bind)

        // Then
        assertEquals(bind, contexts[CONTEXT_ID]?.bindings?.get(0))
    }

    @Test
    fun updateContext_should_update_context_data_with_context_id() {
        // Given
        val json = JSONObject().apply {
            put("a", true)
        }
        val contextData = ContextData(CONTEXT_ID, json)
        val updateContext = SetContextInternal(CONTEXT_ID, false, "a")
        contexts[contextData.id] = ContextBinding(contextData, mutableListOf())
        every { jsonPathReplacer.replace(any(), any(), any()) } returns true

        // When
        val result = contextDataManager.updateContext(updateContext)

        // Then
        assertTrue { result }
    }

    @Test
    fun updateContext_should_log_error_when_jsonPathReplacer_throws_exception() {
        // Given
        val contextData = ContextData(CONTEXT_ID, true)
        val updateContext = SetContextInternal(CONTEXT_ID, false, "a")
        contexts[contextData.id] = ContextBinding(contextData, mutableListOf())
        every { jsonPathReplacer.replace(any(), any(), any()) } throws IllegalStateException()

        // When
        val result = contextDataManager.updateContext(updateContext)

        // Then
        assertFalse { result }
    }

    @Test
    fun updateContext_should_set_value_on_context_root() {
        // Given
        val contextData = ContextData(CONTEXT_ID, true)
        val updateContext = SetContextInternal(CONTEXT_ID, false, null)
        contexts[contextData.id] = ContextBinding(contextData, mutableListOf())

        // When
        val result = contextDataManager.updateContext(updateContext)

        // Then
        assertTrue { result }
        assertEquals(updateContext.contextId, contexts[contextData.id]?.context?.id)
        assertEquals(updateContext.value, contexts[contextData.id]?.context?.value)
    }

    @Test
    fun updateContext_should_return_false_when_contextId_does_not_exist() {
        // Given
        val updateContext = SetContextInternal(RandomData.string(), false, null)

        // When
        val result = contextDataManager.updateContext(updateContext)

        // Then
        assertFalse(result)
    }

    @Test
    fun evaluateContextBindings_should_get_value_from_context_value() {
        // Given
        val value = true
        val contextData = ContextData(CONTEXT_ID, value)
        contexts[CONTEXT_ID] = ContextBinding(
            contextData,
            mutableListOf(bindModel)
        )

        // When
        contextDataManager.evaluateAllContext()

        // Then
        verify { bindModel.notifyChange(value) }
    }

    @Test
    fun evaluateContextBindings_should_get_value_from_context_and_deserialize_JSONOBject() {
        // Given
        val contextData = ContextData("$CONTEXT_ID.a", RandomData.string())
        contexts[CONTEXT_ID] = ContextBinding(contextData, mutableListOf(bindModel))
        val model = mockk<JSONObject>()
        every { jsonPathFinder.find(any(), any()) } returns model

        // When
        contextDataManager.evaluateAllContext()

        // Then
        verify { bindModel.notifyChange(bindModel) }
    }

    @Test
    fun evaluateContextBindings_should_get_value_from_context_and_deserialize_JSONArray() {
        // Given
        val contextData = ContextData("$CONTEXT_ID.a", RandomData.string())
        contexts[CONTEXT_ID] = ContextBinding(contextData, mutableListOf(bindModel))
        val model = mockk<JSONArray>()
        every { jsonPathFinder.find(any(), any()) } returns model

        // When
        contextDataManager.evaluateAllContext()

        // Then
        verify { bindModel.notifyChange(bindModel) }
    }

    @Test
    fun evaluateContextBindings_should_throw_exception_when_moshi_returns_null() {
        // Given
        val contextData = ContextData("$CONTEXT_ID.a", RandomData.string())
        contexts[CONTEXT_ID] = ContextBinding(contextData, mutableListOf(bindModel))
        val model = mockk<JSONArray>()
        every { jsonPathFinder.find(any(), any()) } returns model
        every { moshi.adapter<Any>(any<Class<*>>()).fromJson(any<String>()) } returns null

        // When
        contextDataManager.evaluateAllContext()

        // Then
        verify(exactly = once()) { BeagleMessageLogs.errorWhileTryingToNotifyContextChanges(any()) }
        verify(exactly = 0) { bindModel.notifyChange(any()) }
    }

    @Test
    fun evaluateContextBindings_should_throw_exception_when_jsonPathFinder_returns_null() {
        // Given
        val contextData = ContextData("$CONTEXT_ID.a", RandomData.string())
        contexts[CONTEXT_ID] = ContextBinding(contextData, mutableListOf(bindModel))
        every { moshi.adapter<Any>(any<Class<*>>()).fromJson(any<String>()) } returns null
        every { jsonPathFinder.find(any(), any()) } returns null

        // When
        contextDataManager.evaluateAllContext()

        // Then
        verify(exactly = once()) { BeagleMessageLogs.errorWhileTryingToNotifyContextChanges(any()) }
        verify(exactly = 0) { bindModel.notifyChange(any()) }
    }

    @Test
    fun evaluateContextBindings_should_throw_exception_when_trying_to_call_jsonPathFinder() {
        // Given
        val contextData = ContextData("$CONTEXT_ID.a", RandomData.string())
        contexts[CONTEXT_ID] = ContextBinding(contextData, mutableListOf(bindModel))
        every { jsonPathFinder.find(any(), any()) } throws IllegalStateException()

        // When
        contextDataManager.evaluateAllContext()

        // Then
        verify { BeagleMessageLogs.errorWhileTryingToAccessContext(any()) }
        verify { BeagleMessageLogs.errorWhileTryingToNotifyContextChanges(any()) }
        verify(exactly = 0) { bindModel.notifyChange(any()) }
    }

    @Test
    fun evaluateAllContext_should_evaluate_text_string_text_expression() {
        // Given
        val valueSlot = slot<Any>()
        val bind = mockk<Bind.Expression<String>> {
            every { value } returns "This is an expression @{$CONTEXT_ID.exp1} and this @{$CONTEXT_ID.exp2}"
            every { type } returns String::class.java
        }
        every { jsonPathFinder.find(any(), any()) } returns "hello"
        contexts[CONTEXT_ID]?.bindings?.add(bind)


        // When
        val value = contextDataManager.evaluateBinding(bind)

        // Then
        val expected = "This is an expression hello and this hello"
        assertEquals(expected, value)
    }
}