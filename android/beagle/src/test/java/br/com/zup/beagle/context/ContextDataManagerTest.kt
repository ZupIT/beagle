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

package br.com.zup.beagle.context

import br.com.zup.beagle.setup.BeagleEnvironment
import br.com.zup.beagle.testutil.RandomData
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.json.JSONObject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

data class Model(val a: String, val b: Boolean)

class ContextDataManagerTest {

    private val contexts = mutableMapOf<String, ContextBinding>()

    @Before
    fun setUp() {
        mockkObject(BeagleEnvironment)

        every { BeagleEnvironment.beagleSdk.registeredWidgets() } returns listOf()

        contexts.clear()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun addContext_should_add_new_context() {
        // Given
        val contextData = ContextData(RandomData.string(), true)

        // When
        ContextDataManager.pushContext(contextData)

        // Then
        val contextBinding = contexts[contextData.id]
        assertNotNull(contextBinding)
        assertEquals(contextBinding?.context, contextData)
        assertEquals(0, contextBinding?.bindings?.size)
    }

    @Test
    fun evaluateContextBindings_should_notify_all_bindings() {
        // Given
        val contextId = RandomData.string()
        val bind = Bind.Expression("@{$contextId}", type = Model::class.java)
        val model = JSONObject().apply {
            put("a", RandomData.string())
            put("b", RandomData.boolean())
        }
        val contextData = ContextData(contextId, model)
        ContextDataManager.pushContext(contextData)
        ContextDataManager.addBindingToContext(bind)

        // When Then
        bind.observe {
            assertNotNull(it)
        }
        ContextDataManager.evaluateContextBindings()
    }
}