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
import br.com.zup.beagle.android.action.Action
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ImplicitContextManagerTest {

    private val action = mockk<Action>()
    private val anotherAction = mockk<Action>()
    private val contextData = mockk<ContextData>()
    private val sender = mockk<View>()
    private val implicitContextManager = ImplicitContextManager()

    @Test
    fun addImplicitContext_should_not_add_context_for_repeated_senders(){
        // When
        implicitContextManager.addImplicitContext(contextData, sender, listOf(action))
        implicitContextManager.addImplicitContext(contextData, sender, listOf(action))
        val contexts = implicitContextManager.getImplicitContextForBind(action)

        // Then
        assertEquals(contextData, contexts[0])
        assertTrue(contexts.size == 1)
    }

    @Test
    fun getImplicitContextForBind_should_returns_contexts_if_same_action_reference(){
        // Given
        implicitContextManager.addImplicitContext(contextData, sender, listOf(action))

        // When
        val contexts = implicitContextManager.getImplicitContextForBind(action)

        // Then
        assertEquals(contextData, contexts[0])
    }

    @Test
    fun getImplicitContextForBind_should_be_empty_if_actions_are_different(){
        // Given
        implicitContextManager.addImplicitContext(contextData, sender, listOf(action))

        // When
        val contexts = implicitContextManager.getImplicitContextForBind(anotherAction)

        // Then
        assertTrue(contexts.isEmpty())
    }
}
