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

import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.utils.normalize
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

private data class Test(val a: String)

class ContextDataExtensionsKtTest {

    @Test
    fun normalize_should_return_the_actual_contextData_when_value_type_is_double() {
        // Given
        val contextData = ContextData(
            id = RandomData.string(),
            value = RandomData.double()
        )

        // When
        val normalized = contextData.normalize()

        // Then
        assertEquals(normalized, contextData)
    }

    @Test
    fun normalize_should_return_the_actual_contextData_when_value_type_is_string() {
        // Given
        val contextData = ContextData(
            id = RandomData.string(),
            value = RandomData.string()
        )

        // When
        val normalized = contextData.normalize()

        // Then
        assertEquals(normalized, contextData)
    }

    @Test
    fun normalize_should_return_the_actual_contextData_when_value_type_is_boolean() {
        // Given
        val contextData = ContextData(
            id = RandomData.string(),
            value = RandomData.boolean()
        )

        // When
        val normalized = contextData.normalize()

        // Then
        assertEquals(normalized, contextData)
    }

    @Test
    fun normalize_should_return_the_actual_contextData_when_value_type_is_JSONArray() {
        // Given
        val contextData = ContextData(
            id = RandomData.string(),
            value = JSONArray()
        )

        // When
        val normalized = contextData.normalize()

        // Then
        assertEquals(normalized, contextData)
    }

    @Test
    fun normalize_should_return_the_actual_contextData_when_value_type_is_JSONObject() {
        // Given
        val contextData = ContextData(
            id = RandomData.string(),
            value = JSONObject()
        )

        // When
        val normalized = contextData.normalize()

        // Then
        assertEquals(normalized, contextData)
    }

    @Test
    fun normalize_should_return_new_contextData_when_value_type_is_list() {
        // Given
        val contextData = ContextData(
            id = RandomData.string(),
            value = emptyList<ContextData>()
        )

        // When
        val normalized = contextData.normalize()

        // Then
        assertNotEquals(normalized, contextData)
        assertTrue(normalized.value is JSONArray)
    }

    @Test
    fun normalize_should_return_new_contextData_when_value_type_is_object() {
        // Given
        val contextData = ContextData(
            id = RandomData.string(),
            value = Test("")
        )

        // When
        val normalized = contextData.normalize()

        // Then
        assertNotEquals(normalized, contextData)
        assertTrue(normalized.value is JSONObject)
    }
}