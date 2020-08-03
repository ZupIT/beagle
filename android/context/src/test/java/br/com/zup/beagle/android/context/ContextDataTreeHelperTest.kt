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

import br.com.zup.beagle.android.fake.createViewForContext
import br.com.zup.beagle.android.jsonpath.JsonCreateTree
import br.com.zup.beagle.android.testutil.RandomData
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import java.util.LinkedList
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

private val CONTEXT_ID = RandomData.string()

class ContextDataTreeHelperTest {

    private val view = createViewForContext()
    private val jsonCreateTree: JsonCreateTree = JsonCreateTree()
    private val contextDataTreeHelper = ContextDataTreeHelper()

    @Before
    fun setUp() {

        ContextConstant.memoryMaximumCapacity = 15

    }
    @Test
    fun `should create a context with new json array tree`() {
        // Given
        val contextData = ContextData(CONTEXT_ID, true)
        val contextBinding = ContextBinding(contextData, mutableSetOf())

        // When
        val result = contextDataTreeHelper.updateContextDataWithTree(
            view,
            contextBinding,
            jsonCreateTree,
            LinkedList(listOf("[0]"))
        )

        // Then
        assertNotEquals(result.value, contextData.value)
        assertTrue(result.value is JSONArray)
    }

    @Test
    fun `should create a context with new json object tree`() {
        // Given
        val contextData = ContextData(CONTEXT_ID, JSONArray())
        val contextBinding = ContextBinding(contextData, mutableSetOf())

        // When
        val result = contextDataTreeHelper.updateContextDataWithTree(
            view,
            contextBinding,
            jsonCreateTree,
            LinkedList(listOf("test"))
        )

        // Then
        assertNotEquals(result.value, contextData.value)
        assertTrue(result.value is JSONObject)
    }

    @Test
    fun `should return the same context when the root tree is the same type`() {
        // Given
        val contextData = ContextData(CONTEXT_ID, JSONArray())
        val contextBinding = ContextBinding(contextData, mutableSetOf())

        // When
        val result = contextDataTreeHelper.updateContextDataWithTree(
            view,
            contextBinding,
            jsonCreateTree,
            LinkedList(listOf("[0]"))
        )

        // Then
        assertEquals(result.value, contextData.value)
        assertTrue(result.value is JSONArray)
    }
}