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
import br.com.zup.beagle.android.utils.getContextBinding
import br.com.zup.beagle.android.utils.getContextData
import br.com.zup.beagle.android.utils.setContextBinding
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyOrder
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

private val CONTEXT_ID = RandomData.string()

@DisplayName("Given a ContextDataManager")
class ContextDataManagerTest : BaseTest() {

    private lateinit var contextDataManager: ContextDataManager
    private lateinit var contexts: MutableMap<Int, ContextBinding>
    private lateinit var viewBinding: MutableMap<View, MutableSet<Binding<*>>>

    private val viewContext = createViewForContext()

    @BeforeEach
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

    @DisplayName("When create the ContextDataManager")
    @Nested
    inner class Init {

        @DisplayName("Then should add observer to GlobalContext")
        @Test
        fun globalContext() {
            // Given
            every { GlobalContext.observeGlobalContextChange(any()) } just Runs

            // When
            val contextDataManager = ContextDataManager()

            // Then
            val contexts = contextDataManager.getPrivateField<Map<Int, ContextBinding>>("contexts")
            assertNotNull(contexts[Int.MAX_VALUE])
            verify { GlobalContext.observeGlobalContextChange(any()) }
        }

        @DisplayName("Then should add observer to GlobalContext and validate the updateGlobalContext method")
        @Test
        fun globalContextMethod() {
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

    @DisplayName("When addContext is called")
    @Nested
    inner class AddContext {

        @DisplayName("Then should add new context")
        @Test
        fun addNewContext() {
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

        @DisplayName("Then should not add global context")
        @Test
        fun notAddGlobalContext() {
            // Given
            val contextData = ContextData("global", true)
            every { BeagleMessageLogs.globalKeywordIsReservedForGlobalContext() } just Runs

            // When
            contextDataManager.addContext(viewContext, contextData)

            // Then
            verify(exactly = once()) { BeagleMessageLogs.globalKeywordIsReservedForGlobalContext() }
        }

        @DisplayName("Then should not add context if already exists")
        @Test
        fun notAddContextTwice() {
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

        @DisplayName("Then should override context only if shouldOverrideExistingContext")
        @Test
        fun shouldOverrideExistingContext() {
            // Given
            val contextData1 = ContextData(CONTEXT_ID, true)
            val contextData2 = ContextData(CONTEXT_ID, false)
            val shouldOverrideExistingContext = true
            contextDataManager.addContext(viewContext, contextData1)

            // When
            contextDataManager.addContext(viewContext, contextData2, shouldOverrideExistingContext)

            // Then
            assertEquals(contextData2, contexts[viewContext.id]?.context)
            assertEquals(contextData2, viewContext.getContextData())
        }

        @DisplayName("Then should clear bindings if already exists")
        @Test
        fun clearBindings() {
            // Given
            val contextData = ContextData(CONTEXT_ID, true)
            contexts[viewContext.id] = ContextBinding(
                context = contextData,
                bindings = mutableSetOf(Binding<Boolean>(observer = mockk(), bind = mockk()))
            )

            // When
            contextDataManager.addContext(viewContext, contextData)

            // Then
            assertTrue { contexts[viewContext.id]?.bindings?.isEmpty() ?: false }
        }
    }

    @DisplayName("When addBinding is called")
    @Nested
    inner class AddBinding {

        @DisplayName("Then should add binding to context to viewBinding")
        @Test
        fun viewBinding() {
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

        @DisplayName("Then should add binding to context on top of stack")
        @Test
        fun topOfStack() {
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

        @DisplayName("Then should add binding to global context")
        @Test
        fun globalContext() {
            // Given
            val viewWithBind = createViewForContext()
            val bind = expressionOf<Boolean>("@{global}")
            val observer = mockk<Observer<Boolean?>>(relaxed = true)

            // When
            contextDataManager.addBinding(viewWithBind, bind, observer)
            contextDataManager.linkBindingToContextAndEvaluateThem(viewWithBind)

            // Then
            val contextBinding = contexts[Int.MAX_VALUE]?.bindings?.first()
            assertEquals(bind, contextBinding?.bind)
            assertEquals(observer, contextBinding?.observer)
        }
    }

    @DisplayName("When linkBindingToContextAndEvaluateThem is called")
    @Nested
    inner class EvaluateContexts {

        @DisplayName("Then should call notifyBindingChanges if view is not in viewBinding")
        @Test
        fun notifyBindingChanges() {
            // Given
            val viewWithBind = createViewForContext()
            val contextBinding = mockk<ContextBinding>(relaxed = true)
            viewWithBind.setContextBinding(contextBinding)

            // When
            contextDataManager.linkBindingToContextAndEvaluateThem(viewWithBind)

            // Then
            verify(exactly = once()) { contextDataManager.notifyBindingChanges(contextBinding) }
        }

        @DisplayName("Then should get value from evaluation")
        @Test
        fun getValueEvaluation() {
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

        @DisplayName("Then should get value from operation")
        @Test
        fun getValueOperation() {
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

        @DisplayName("Then should get null value from evaluation")
        @Test
        fun getNullValueEvaluation() {
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

        @DisplayName("Then should get value from different type")
        @Test
        fun getNullValueEvaluationDifferentType() {
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
    }

    @DisplayName("When updateContext is called")
    @Nested
    inner class UpdateContext {

        @DisplayName("Then should update context data with context id")
        @Test
        fun updateContextData() {
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

        @DisplayName("Then should set value on context root")
        @Test
        fun contextRoot() {
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

        @DisplayName("Then should call global context to id global")
        @Test
        fun globalContext() {
            // Given
            val updateContext = SetContextInternal("global", false, null)

            // When
            contextDataManager.updateContext(viewContext, updateContext)

            // Then
            verify(exactly = once()) { GlobalContext.set(updateContext.value, updateContext.path) }
        }
    }

    @DisplayName("When getContextsFromBind is called")
    @Nested
    inner class GetContexts {

        @DisplayName("Should filter all contexts from view hierarchy")
        @Test
        fun filterContexts() {
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

        @DisplayName("Should return globalContext")
        @Test
        fun globalContext() {
            // Given
            val bind = expressionOf<String>("@{global}")
            val viewContext = createViewForContext()

            // When
            val contexts = contextDataManager.getContextsFromBind(viewContext, bind)

            // Then
            assertEquals("global", contexts.first().id)
        }
    }

    @DisplayName("When clearContexts is called")
    @Nested
    inner class ClearContexts {

        @DisplayName("Should clear viewBindings, contexts and contextsWithoutId")
        @Test
        fun clearAllData() {
            // Given
            val bind = mockk<Bind.Expression<Boolean>>()
            val observer = mockk<Observer<Boolean?>>()
            val context = ContextData(id = RandomData.string(), value = RandomData.string())
            val contextDataManager = ContextDataManager()
            val viewWithoutId = mockk<View>(relaxed = true) {
                every { id } returns View.NO_ID
                every { getContextBinding() } returns mockk(relaxed = true)
            }
            contextDataManager.addContext(viewWithoutId, context)
            contextDataManager.addContext(viewContext, context)
            contextDataManager.addBinding(viewContext, bind, observer)
            val contexts: Map<Int, ContextBinding> = contextDataManager.getPrivateField("contexts")
            val contextsWithoutId: Map<View, ContextBinding> = contextDataManager.getPrivateField("contextsWithoutId")
            val viewBinding: Map<View, MutableSet<Binding<*>>> = contextDataManager.getPrivateField("viewBinding")
            val contextsWithoutIdSizeBefore = contextsWithoutId.size
            val contextsSizeBefore = contexts.size
            val viewBindingSizeBefore = viewBinding.size
            every { GlobalContext.clearObserverGlobalContext(any()) } just Runs

            // When
            contextDataManager.clearContexts()

            // Then
            assertNotEquals(contextsWithoutIdSizeBefore, contextsWithoutId.size)
            assertNotEquals(contextsSizeBefore, contexts.size)
            assertNotEquals(viewBindingSizeBefore, viewBinding.size)
            assertTrue { contexts.isEmpty() }
            assertTrue { contextsWithoutId.isEmpty() }
            assertTrue { viewBinding.isEmpty() }
            verify(exactly = once()) { GlobalContext.clearObserverGlobalContext(any()) }
        }
    }

    @DisplayName("When getContextData is called")
    @Nested
    inner class GetContext {

        @DisplayName("Then should return context")
        @Test
        fun getContextData() {
            // Given
            val contextData = ContextData(CONTEXT_ID, true)
            contextDataManager.addContext(viewContext, contextData)

            // When
            val result = contextDataManager.getContextData(viewContext)

            // Then
            assertEquals(contextData, result)
        }
    }

    @DisplayName("When restoreContext is called")
    @Nested
    inner class RestoreContext {

        @DisplayName("Then should update view's context")
        @Test
        fun restoreContextData() {
            // Given
            val contextData = ContextData(CONTEXT_ID, true)
            contextDataManager.addContext(viewContext, contextData)

            // When
            contextDataManager.restoreContext(viewContext)

            // Then
            assertEquals(contextData, viewContext.getContextData())
        }
    }

    @DisplayName("When setIdToViewWithContext is called")
    @Nested
    inner class SetIdToView {

        @DisplayName("Then should update view's context")
        @Test
        fun setIdToViewWithContext() {
            // Given
            val contextData1 = ContextData(CONTEXT_ID, true)
            val viewWithoutId = mockk<View>(relaxed = true) {
                every { id } returns View.NO_ID
                every { getContextBinding() } returns mockk(relaxed = true) {
                    every { context } returns contextData1
                }
            }
            contextDataManager.addContext(viewWithoutId, contextData1)

            val contextData2 = ContextData(CONTEXT_ID, false)
            val viewWithId = viewWithoutId.apply {
                every { id } returns 10
                every { getContextBinding() } returns mockk(relaxed = true) {
                    every { context } returns contextData2
                }
            }
            contextDataManager.addContext(viewWithId, contextData2)

            val contextsWithoutId: Map<View, ContextBinding> = contextDataManager.getPrivateField("contextsWithoutId")
            val contextsWithoutIdSizeBefore = contextsWithoutId.size

            // When
            contextDataManager.setIdToViewWithContext(viewWithId)

            // Then
            assertEquals(contextData2, viewWithId.getContextData())
            assertNotEquals(contextsWithoutIdSizeBefore, contextsWithoutId.size)
        }

        @DisplayName("Then should update manager's binding")
        @Test
        fun setIdToViewWithContextManager() {
            // Given
            val contextData = ContextData(CONTEXT_ID, true)
            val viewWithoutId = mockk<View>(relaxed = true) {
                every { id } returns View.NO_ID
                every { getContextBinding() } returns mockk(relaxed = true) {
                    every { context } returns contextData
                }
            }
            contextDataManager.addContext(viewWithoutId, contextData)

            val viewWithId = viewWithoutId.apply { every { id } returns 10 }

            val contexts = contextDataManager.getPrivateField<MutableMap<Int, ContextBinding>>("contexts")
            val contextsWithoutId: Map<View, ContextBinding> = contextDataManager.getPrivateField("contextsWithoutId")
            val contextsWithoutIdSizeBefore = contextsWithoutId.size

            // When
            contextDataManager.setIdToViewWithContext(viewWithId)

            // Then
            assertEquals(contexts[viewWithId.id], viewWithId.getContextBinding())
            assertNotEquals(contextsWithoutIdSizeBefore, contextsWithoutId.size)
        }
    }

    @DisplayName("When onViewIdChanged is called")
    @Nested
    inner class OnViewIdChanged {

        @DisplayName("Then should swipe context to newId to a view with oldId and context")
        @Test
        fun swipeContext() {
            // Given
            val oldId = 0
            val newId = 1
            val contextData = ContextData(CONTEXT_ID, true)
            val viewWithId = mockk<View>(relaxed = true) {
                every { id } returns oldId
                every { getContextBinding() } returns mockk(relaxed = true) {
                    every { context } returns contextData
                }
            }
            contextDataManager.addContext(viewWithId, contextData)

            val contexts = contextDataManager.getPrivateField<MutableMap<Int, ContextBinding>>("contexts")

            // When
            contextDataManager.onViewIdChanged(oldId, newId, viewWithId)

            // Then
            assertEquals(contexts[newId], viewWithId.getContextBinding())
            assertNull(contexts[oldId])
        }

        @DisplayName("Then should update new and remove old to a view with oldId and context with newId")
        @Test
        fun updateNewAndRemoveOld() {
            // Given
            val oldId = 0
            val newId = 1
            val oldContextData = ContextData("old", true)
            val oldViewWithId = mockk<View>(relaxed = true) {
                every { id } returns oldId
                every { getContextBinding() } returns mockk(relaxed = true) {
                    every { context } returns oldContextData
                }
            }
            contextDataManager.addContext(oldViewWithId, oldContextData)

            val newContextData = ContextData("new", true)
            val newViewWithId = mockk<View>(relaxed = true) {
                every { id } returns newId
                every { getContextBinding() } returns mockk(relaxed = true) {
                    every { context } returns newContextData
                }
            }
            contextDataManager.addContext(newViewWithId, newContextData)

            val contexts = contextDataManager.getPrivateField<MutableMap<Int, ContextBinding>>("contexts")

            // When
            contextDataManager.onViewIdChanged(oldId, newId, newViewWithId)

            // Then
            assertEquals(contexts[newId], newViewWithId.getContextBinding())
            assertNull(contexts[oldId])
        }
    }
}
