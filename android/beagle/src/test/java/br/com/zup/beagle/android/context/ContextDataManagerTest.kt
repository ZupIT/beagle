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
import br.com.zup.beagle.android.jsonpath.JsonCreateTree
import br.com.zup.beagle.android.jsonpath.JsonPathFinder
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.mockdata.ComponentModel
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.testutil.getPrivateField
import com.squareup.moshi.Moshi
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
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

class ContextDataManagerTest {

    private lateinit var contextDataManager: ContextDataManager

    private lateinit var contexts: MutableMap<String, ContextBinding>

    @RelaxedMockK
    private lateinit var jsonCreateTree: JsonCreateTree

    @MockK
    private lateinit var contextPathResolver: ContextPathResolver

    @MockK
    private lateinit var contextDataEvaluation: ContextDataEvaluation

    @MockK
    private lateinit var moshi: Moshi

    @MockK
    private lateinit var bindModel: Bind.Expression<ComponentModel>

    @MockK
    private lateinit var model: ComponentModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { bindModel.type } returns ComponentModel::class.java
        every { bindModel.value } returns "@{$CONTEXT_ID}"
        every { bindModel.notifyChange(any()) } just Runs

        mockkObject(BeagleMessageLogs)
        every { BeagleMessageLogs.errorWhileTryingToNotifyContextChanges(any()) } just Runs
        every { BeagleMessageLogs.errorWhileTryingToChangeContext(any()) } just Runs
        every { BeagleMessageLogs.errorWhileTryingToAccessContext(any()) } just Runs
        every { contextPathResolver.getKeysFromPath(any(), any()) } returns LinkedList(listOf(""))
        every { moshi.adapter<Any>(any<Class<*>>()).fromJson(any<String>()) } returns bindModel

        contextDataManager = ContextDataManager(
            jsonCreateTree,
            ContextDataTreeHelper(),
            contextPathResolver,
            contextDataEvaluation
        )

        contexts = contextDataManager.getPrivateField("contexts")

        contexts.clear()

        val contextData = ContextData(CONTEXT_ID, model)
        contexts[CONTEXT_ID] = ContextBinding(contextData, mutableSetOf(bindModel))
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
        val bind = Bind.Expression("@{$CONTEXT_ID[0]}", type = Boolean::class.java)
        val contextData = ContextData(CONTEXT_ID, listOf(true))
        contextDataManager.addContext(contextData)

        // When
        contextDataManager.addBindingToContext(bind)

        // Then
        assertEquals(bind, contexts[CONTEXT_ID]?.bindings?.first())
    }

    @Test
    fun addBindingToContext_should_add_binding_to_context_on_top_of_stack() {
        // Given
        val bind = Bind.Expression("@{$CONTEXT_ID.a}", type = ComponentModel::class.java)
        val contextData = ContextData(CONTEXT_ID, true)
        contextDataManager.addContext(contextData)

        // When
        contextDataManager.addBindingToContext(bind)

        // Then
        assertEquals(bind, contexts[CONTEXT_ID]?.bindings?.first())
    }

    @Test
    fun updateContext_should_update_context_data_with_context_id() {
        // Given
        val json = JSONObject().apply {
            put("a", true)
        }
        val contextData = ContextData(CONTEXT_ID, json)
        val updateContext = SetContextInternal(CONTEXT_ID, false, "a")
        contexts[contextData.id] = ContextBinding(contextData, mutableSetOf())

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
        contexts[contextData.id] = ContextBinding(contextData, mutableSetOf())
        every { jsonCreateTree.walkingTreeAndFindKey(any(), any(), any()) } throws IllegalStateException()

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
        contexts[contextData.id] = ContextBinding(contextData, mutableSetOf())

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
    fun evaluateContextBindings_should_get_value_from_evaluation() {
        // Given
        val value = true
        val contextData = ContextData(CONTEXT_ID, value)
        contexts[CONTEXT_ID] = ContextBinding(
            contextData,
            mutableSetOf(bindModel)
        )
        every { contextDataEvaluation.evaluateBindExpression(contextData, bindModel) } returns model

        // When
        contextDataManager.evaluateContexts()

        // Then
        verify { bindModel.notifyChange(model) }
    }
}