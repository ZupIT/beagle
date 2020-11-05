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

package br.com.zup.beagle.android.operation.builtin.other

import br.com.zup.beagle.android.operation.OperationType
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@DisplayName("Given Is Empty Operation")
internal class IsEmptyOperationTest {

    val isEmptyOperation = IsEmptyOperation()

    @DisplayName("When passing string in parameter")
    @Nested
    inner class StringParameter {

        @Test
        @DisplayName("Then should return true")
        fun checkEmptyString() {
            // GIVEN
            val input = OperationType.TypeString("")

            // WHEN
            val result = isEmptyOperation.execute(input)

            // THEN
            val expected = OperationType.TypeBoolean(true)

            assertEquals(expected, result)
        }

        @Test
        @DisplayName("Then should return false")
        fun checkNotEmptyString() {
            // GIVEN
            val input = OperationType.TypeString("a")

            // WHEN
            val result = isEmptyOperation.execute(input)

            // THEN
            val expected = OperationType.TypeBoolean(false)

            assertEquals(expected, result)
        }
    }

    @DisplayName("When passing list in parameter")
    @Nested
    inner class ListParameter {

        @Test
        @DisplayName("Then should return true")
        fun checkEmptyList() {
            // GIVEN
            val input = OperationType.TypeJsonArray(JSONArray())

            // WHEN
            val result = isEmptyOperation.execute(input)

            // THEN
            val expected = OperationType.TypeBoolean(true)

            assertEquals(expected, result)
        }

        @Test
        @DisplayName("Then should return false")
        fun checkNotEmptyList() {
            // GIVEN
            val input = OperationType.TypeJsonArray(JSONArray(listOf(1)))

            // WHEN
            val result = isEmptyOperation.execute(input)

            // THEN
            val expected = OperationType.TypeBoolean(false)

            assertEquals(expected, result)
        }
    }

    @DisplayName("When passing json object in parameter")
    @Nested
    inner class JsonObjectParameter {

        @Test
        @DisplayName("Then should return true")
        fun checkEmptyJsonObject() {
            // GIVEN
            val input = OperationType.TypeJsonObject(JSONObject())

            // WHEN
            val result = isEmptyOperation.execute(input)

            // THEN
            val expected = OperationType.TypeBoolean(true)

            assertEquals(expected, result)
        }

        @Test
        @DisplayName("Then should return false")
        fun checkNotEmptyJsonObject() {
            // GIVEN
            val input = OperationType.TypeJsonObject(
                JSONObject(
                    """
                    {
                    "name": "Test"
                    }
                    
                """
                )
            )

            // WHEN
            val result = isEmptyOperation.execute(input)

            // THEN
            val expected = OperationType.TypeBoolean(false)

            assertEquals(expected, result)
        }
    }

    @DisplayName("When passing null in parameter")
    @Nested
    inner class NullParameter {

        @Test
        @DisplayName("Then should return true")
        fun checkNull() {
            // GIVEN
            val input = null

            // WHEN
            val result = isEmptyOperation.execute(input)

            // THEN
            val expected = OperationType.TypeBoolean(true)

            assertEquals(expected, result)
        }
    }

    @DisplayName("When execute operation with empty parameters")
    @Nested
    inner class NullOperation {

        @Test
        @DisplayName("Then should return true")
        fun checkNull() {
            // WHEN
            val result = isEmptyOperation.execute()

            // THEN
            val expected = OperationType.TypeBoolean(true)

            assertEquals(expected, result)
        }

    }
}