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

package br.com.zup.beagle.android.utils

import br.com.zup.beagle.android.BaseTest
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BeagleMoshiExtensionsTest : BaseTest() {

    @Test
    fun getMoshiDataFormatted_evaluate_string() {
        // Given
        val value = "hello"

        // When
        val result = value.tryToDeserialize()

        // Then
        assertEquals("hello", result)
    }

    @Test
    fun getMoshiDataFormatted_evaluate_int() {
        // Given
        val value = "20"

        // When
        val result = value.tryToDeserialize()

        // Then
        assertEquals(20, result)
    }

    @Test
    fun getMoshiDataFormatted_evaluate_double() {
        // Given
        val value = "20.5"

        // When
        val result = value.tryToDeserialize()

        // Then
        assertEquals(20.5, result)
    }

    @Test
    fun getMoshiDataFormatted_evaluate_JSONObject_with_string() {
        // Given
        val value = "{\"value\":\"hello\"}"

        // When
        val result = value.tryToDeserialize()

        // Then
        assertTrue(result is JSONObject)
        assertEquals(value, result.toString())
    }

    @Test
    fun getMoshiDataFormatted_evaluate_JSONArray_with_int() {
        // Given
        val value = "[{\"value\":2}]"

        // When
        val result = value.tryToDeserialize()

        // Then
        assertTrue(result is JSONArray)
        assertEquals(value, result.toString())
    }

    @Test
    fun getMoshiDataFormatted_evaluate_JSONArray_with_double() {
        // Given
        val value = "[{\"value\":2.5}]"

        // When
        val result = value.tryToDeserialize()

        // Then
        assertTrue(result is JSONArray)
        assertEquals(value, result.toString())
    }

    @Test
    fun getMoshiDataFormatted_evaluate_JSONObject_with_int() {
        // Given
        val value = "{\"value\":2}"

        // When
        val result = value.tryToDeserialize()

        // Then
        assertTrue(result is JSONObject)
        assertEquals(value, result.toString())
    }

    @Test
    fun getMoshiDataFormatted_evaluate_json_with_double() {
        // Given
        val value = "{\"value\":2.5}"

        // When
        val result = value.tryToDeserialize()

        // Then
        assertTrue(result is JSONObject)
        assertEquals(value, result.toString())
    }
}