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
import androidx.collection.LruCache
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.action.SetContextInternal
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.mockdata.createViewForContext
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.testutil.getPrivateField
import br.com.zup.beagle.android.testutil.setPrivateField
import br.com.zup.beagle.android.utils.Observer
import br.com.zup.beagle.android.utils.getContextData
import br.com.zup.beagle.android.utils.setContextBinding
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import io.mockk.mockk
import io.mockk.verifyOrder
import io.mockk.spyk
import io.mockk.slot
import io.mockk.mockkObject
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

private val CONTEXT_ID = RandomData.string()

class ContextDataManagerTest : BaseTest() {

    private lateinit var contextDataManager: ContextDataManager
    private lateinit var contexts: MutableMap<Int, ContextBinding>
    private lateinit var viewBinding: MutableMap<View, MutableSet<Binding<*>>>

    private val viewContext = createViewForContext()

    override fun setUp() {
        super.setUp()

        mockkObject(BeagleMessageLogs)
        mockkObject(GlobalContext)

        every { BeagleMessageLogs.errorWhileTryingToNotifyContextChanges(any()) } just Runs
        every { BeagleMessageLogs.errorWhileTryingToChangeContext(any()) } just Runs
        every { BeagleMessageLogs.errorWhileTryingToAccessContext(any()) } just Runs
        every { GlobalContext.set(any(), any()) } just Runs

        contextDataManager = ContextDataManager()

        contexts = contextDataManager.getPrivateField("contexts")
        viewBinding = contextDataManager.getPrivateField("viewBinding")
    }

    @Test
    fun init_should_add_observer_to_GlobalContext() {
        // Given
        every { GlobalContext.observeGlobalContextChange(any()) } just Runs

        // When
        val contextDataManager = ContextDataManager()

        // Then
        val contexts = contextDataManager.getPrivateField<Map<Int, ContextBinding>>("contexts")
        assertNotNull(contexts[Int.MAX_VALUE])
        verify { GlobalContext.observeGlobalContextChange(any()) }
    }

    @Test
    fun addContext_should_add_new_context() {
        // Given
        val contextData = ContextData(CONTEXT_ID, true)

        // When
        contextDataManager.addContext(viewContext, contextData)

        // Then
        val contextBinding = contexts[viewContext.id]
        assertNotNull(contextBinding)
        assertEquals(contextBinding?.context, contextData)
        assertEquals(0, contextBinding?.bindings?.size)
        assertEquals(contextData, viewContext.getContextData())
    }

    @Test
    fun addContext_should_not_add_global_context() {
        // Given
        val contextData = ContextData("global", true)
        every { BeagleMessageLogs.globalKeywordIsReservedForGlobalContext() } just Runs

        // When
        contextDataManager.addContext(viewContext, contextData)

        // Then
        verify(exactly = once()) { BeagleMessageLogs.globalKeywordIsReservedForGlobalContext() }
    }

    @Test
    fun addContext_should_not_add_new_context_when_context_already_exists() {
        // Given
        val contextData1 = ContextData(CONTEXT_ID, true)
        val contextData2 = ContextData(CONTEXT_ID, false)

        // When
        contextDataManager.addContext(viewContext, contextData1)
        contextDataManager.addContext(viewContext, contextData2)

        // Then
        assertEquals(contextData1, contexts[viewContext.id]?.context)
        assertEquals(contextData1, viewContext.getContextData())
    }

    @Test
    fun addContext_should_clear_bindings_when_context_already_exists() {
        // Given
        val contextData = ContextData(CONTEXT_ID, true)
        contexts[viewContext.id] = ContextBinding(
            context = contextData,
            bindings = mutableSetOf(Binding<Boolean>(
                observer = mockk(),
                bind = mockk()
            ))
        )

        // When
        contextDataManager.addContext(viewContext, contextData)

        // Then
        assertTrue { contexts[viewContext.id]?.bindings?.isEmpty() ?: false }
    }

    @Test
    fun addBinding_should_add_bind_to_context_to_viewBinding() {
        // Given
        val viewWithBind = mockk<View>()
        val bind = Bind.Expression(listOf(), "@{$CONTEXT_ID[0]}", type = Boolean::class.java)
        val contextData = ContextData(CONTEXT_ID, listOf(true))
        val observer = mockk<Observer<Boolean?>>()
        contextDataManager.addContext(viewContext, contextData)

        // When
        contextDataManager.addBinding(viewWithBind, bind, observer)

        // Then
        val binding = viewBinding[viewWithBind]?.first()
        assertEquals(bind, binding?.bind)
        assertEquals(observer, binding?.observer)
    }

    @Test
    fun addBinding_should_add_binding_to_context_on_top_of_stack() {
        // Given
        val viewWithBind = createViewForContext(viewContext)
        val bind = expressionOf<Boolean>("@{$CONTEXT_ID}")
        val observer = mockk<Observer<Boolean?>>(relaxed = true)
        val contextData = ContextData(CONTEXT_ID, true)
        contextDataManager.addContext(viewContext, contextData)

        // When
        contextDataManager.addBinding(viewWithBind, bind, observer)
        contextDataManager.linkBindingToContextAndEvaluateThem(viewWithBind)

        // Then
        val contextBinding = contexts[viewContext.id]?.bindings?.first()
        assertEquals(bind, contextBinding?.bind)
        assertEquals(observer, contextBinding?.observer)
        assertTrue { viewBinding.isEmpty() }
    }

    @Test
    fun addBinding_should_add_binding_to_global_context() {
        // Given
        val viewWithBind = createViewForContext()
        val bind = expressionOf<Boolean>("@{global}")
        val observer = mockk<Observer<Boolean?>>(relaxed = true)
        contextDataManager.addBinding(viewWithBind, bind, observer)

        // When
        contextDataManager.linkBindingToContextAndEvaluateThem(viewWithBind)

        // Then
        val contextBinding = contexts[Int.MAX_VALUE]?.bindings?.first()
        assertEquals(bind, contextBinding?.bind)
        assertEquals(observer, contextBinding?.observer)
    }

    @Test
    fun updateContext_should_update_context_data_with_context_id() {
        // Given
        val json = JSONObject().apply {
            put("a", true)
        }
        val contextData = ContextData(CONTEXT_ID, json)
        val updateContext = SetContextInternal(CONTEXT_ID, false, "a")
        contextDataManager.addContext(viewContext, contextData)

        // When
        contextDataManager.updateContext(viewContext, updateContext)

        // Then
        assertFalse { json.getBoolean("a") }
    }

    @Test
    fun updateContext_should_set_value_on_context_root() {
        // Given
        val contextData = ContextData(CONTEXT_ID, true)
        val updateContext = SetContextInternal(CONTEXT_ID, false, null)
        contextDataManager.addContext(viewContext, contextData)

        // When
        contextDataManager.updateContext(viewContext, updateContext)

        // Then
        val contextBinding = contexts[viewContext.id]?.context
        assertEquals(updateContext.contextId, contextBinding?.id)
        assertEquals(updateContext.value, contextBinding?.value)
    }

    @Test
    fun updateContext_should_call_global_context_when_id_is_global() {
        // Given
        val updateContext = SetContextInternal("global", false, null)

        // When
        contextDataManager.updateContext(viewContext, updateContext)

        // Then
        verify(exactly = once()) { GlobalContext.set(updateContext.value, updateContext.path) }
    }

    @Test
    fun getContextsFromBind_should_filter_all_contexts_from_view_hierarchy() {
        // Given
        val contextId1 = RandomData.string()
        val contextId2 = RandomData.string()
        val bind = expressionOf<String>("@{$contextId1} @{$contextId2}")
        val viewContext1 = createViewForContext()
        viewContext1.setContextBinding(ContextBinding(
            ContextData(
                id = contextId1,
                value = RandomData.string()
            ))
        )
        val viewContext2 = createViewForContext(viewContext1)
        viewContext2.setContextBinding(ContextBinding(
            ContextData(
                id = contextId2,
                value = RandomData.string()
            ))
        )

        // When
        val contexts = contextDataManager.getContextsFromBind(viewContext2, bind)

        // Then
        assertEquals(2, contexts.size)
        assertEquals(contextId2, contexts[0].id)
        assertEquals(contextId1, contexts[1].id)
    }

    @Test
    fun getContextsFromBind_should_return_globalContext() {
        // Given
        val bind = expressionOf<String>("@{global}")
        val viewContext = createViewForContext()

        // When
        val contexts = contextDataManager.getContextsFromBind(viewContext, bind)

        // Then
        assertEquals("global", contexts.first().id)
    }

    @Test
    fun clearContexts_should_clear_viewBindings_and_contexts() {
        // Given
        val bind = mockk<Bind.Expression<Boolean>>()
        val observer = mockk<Observer<Boolean?>>()
        val context = ContextData(id = RandomData.string(), value = RandomData.string())
        val contextDataManager = ContextDataManager()
        contextDataManager.addContext(viewContext, context)
        contextDataManager.addBinding(viewContext, bind, observer)
        val contexts: Map<Int, ContextBinding> = contextDataManager.getPrivateField("contexts")
        val viewBinding: Map<View, MutableSet<Binding<*>>> = contextDataManager.getPrivateField("viewBinding")
        val contextsSizeBefore = contexts.size
        val viewBindingSizeBefore = viewBinding.size
        every { GlobalContext.clearObserverGlobalContext(any()) } just Runs

        // When
        contextDataManager.clearContexts()

        // Then
        assertNotEquals(contextsSizeBefore, contexts.size)
        assertNotEquals(viewBindingSizeBefore, viewBinding.size)
        assertTrue { contexts.isEmpty() }
        assertTrue { viewBinding.isEmpty() }
        verify(exactly = once()) { GlobalContext.clearObserverGlobalContext(any()) }
    }

    @Test
    fun evaluateContexts_should_get_value_from_evaluation() {
        // Given
        val value = true
        val contextData = ContextData(CONTEXT_ID, value)
        val bind = expressionOf<Boolean>("@{$CONTEXT_ID}")
        val observer = mockk<Observer<Boolean?>>(relaxed = true)
        contextDataManager.addContext(viewContext, contextData)
        contextDataManager.addBinding(viewContext, bind, observer)

        // When
        contextDataManager.linkBindingToContextAndEvaluateThem(viewContext)

        // Then
        verify(exactly = once()) { observer(value) }
    }

    @Test
    fun evaluateContexts_should_get_value_from_operation() {
        // Given
        val value = 2
        val contextData = ContextData(CONTEXT_ID, value)
        val bind = expressionOf<Int>("@{sum(1, 1)}")
        val observer = mockk<Observer<Int?>>(relaxed = true)
        contextDataManager.addContext(viewContext, contextData)
        contextDataManager.addBinding(viewContext, bind, observer)

        // When
        contextDataManager.linkBindingToContextAndEvaluateThem(viewContext)

        // Then
        verify(exactly = once()) { observer(value) }
    }

    @Test
    fun evaluateContexts_should_get_null_value_from_evaluation() {
        // Given
        val value = true
        val contextData = ContextData(CONTEXT_ID, value)
        val bind = expressionOf<Boolean>("@{$CONTEXT_ID.a}")
        val observer = mockk<Observer<Boolean?>>(relaxed = true)
        contextDataManager.addContext(viewContext, contextData)
        contextDataManager.addBinding(viewContext, bind, observer)

        // When
        contextDataManager.linkBindingToContextAndEvaluateThem(viewContext)

        // Then
        verify(exactly = once()) { observer(null) }
    }

    @Test
    fun evaluateContexts_should_get_different_value_type_from_context_evaluation() {
        // Given
        val contextData = ContextData(CONTEXT_ID, "value")
        val bind = expressionOf<Boolean>("@{$CONTEXT_ID}")
        contextDataManager.addContext(viewContext, contextData)
        contextDataManager.addBinding(viewContext, bind) {
            // Then
            assertNull(it)
        }
        contextDataManager.linkBindingToContextAndEvaluateThem(viewContext)
    }

    @Test
    fun init_must_add_observer_to_GlobalContext_and_validate_the_updateGlobalContext_method() {
        // Given
        val globalContextObserver = slot<GlobalContextObserver>()
        val contextData = ContextData("global", "")
        val globalContextMock = mockk<ContextBinding>(relaxed = true) {
            every { copy(any(), any(), any()) } returns this
        }
        val cache = mockk<LruCache<String, Any>>(relaxed = true)
        every { globalContextMock.cache } returns cache
        every { GlobalContext.observeGlobalContextChange(capture(globalContextObserver)) } just Runs

        // When
        val contextDataManager = spyk<ContextDataManager>(recordPrivateCalls = true)
        val contexts = contextDataManager.getPrivateField<MutableMap<Int, ContextBinding>>("contexts")
        contextDataManager.setPrivateField("globalContext", globalContextMock)
        globalContextObserver.captured.invoke(contextData)

        // Then
        verifyOrder {
            globalContextMock.copy(context = contextData, cache = any(), bindings = any())
            cache.evictAll()
            contextDataManager.notifyBindingChanges(globalContextMock)
        }
        assertEquals(contexts[Int.MAX_VALUE], globalContextMock)
    }
}