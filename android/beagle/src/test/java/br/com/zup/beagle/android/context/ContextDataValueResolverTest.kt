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
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class ContextDataValueResolverTest {

    private val contextDataValueResolver = ContextDataValueResolver()

    @Test
    fun parse_should_return_a_JSONArray_when_given_a_list() {
        // Given
        val value = emptyList<String>()

        // When
        val parsedValue = contextDataValueResolver.parse(value)

        // Then
        assertTrue(parsedValue is JSONArray)
    }

    @Test
    fun parse_should_return_a_JSONObject_when_given_a_map() {
        // Given
        val value = emptyMap<String, Any>()

        // When
        val parsedValue = contextDataValueResolver.parse(value)

        // Then
        assertTrue(parsedValue is JSONObject)
    }

    @Test
    fun parse_should_return_a_JSONObject_when_given_a_primitive() {
        // Given
        val value = RandomData.string()

        // When
        val parsedValue = contextDataValueResolver.parse(value)

        // Then
        assertTrue(parsedValue is String)
        assertEquals(value, parsedValue)
    }
}