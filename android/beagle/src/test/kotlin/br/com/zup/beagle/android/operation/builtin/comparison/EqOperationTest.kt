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

package br.com.zup.beagle.android.operation.builtin.comparison

import br.com.zup.beagle.android.operation.OperationType
import org.json.JSONArray
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@DisplayName("Given Eq Operation")
internal class EqOperationTest {

    val eqOperation = EqOperation()

    @DisplayName("When execute operation with integer numbers")
    @Nested
    inner class IntegerNumbers {

        @Test
        @DisplayName("Then should return true")
        fun checkEqualsInteger() {
            // GIVEN
            val inputOne = OperationType.TypeNumber(1)
            val inputTwo = OperationType.TypeNumber(1)

            // WHEN
            val result = eqOperation.execute(inputOne, inputTwo)

            // THEN
            val expected = OperationType.TypeBoolean(true)
            Assertions.assertEquals(expected, result)
        }

        @Test
        @DisplayName("Then should return false")
        fun checkNotEqualsInteger() {
            // GIVEN
            val inputOne = OperationType.TypeNumber(2)
            val inputTwo = OperationType.TypeNumber(1)

            // WHEN
            val result = eqOperation.execute(inputOne, inputTwo)

            // THEN
            val expected = OperationType.TypeBoolean(false)
            Assertions.assertEquals(expected, result)
        }
    }

    @DisplayName("When execute operation with double numbers")
    @Nested
    inner class DoubleNumbers {

        @Test
        @DisplayName("Then should return true")
        fun checkEqualsDouble() {
            // GIVEN
            val inputOne = OperationType.TypeNumber(1.0)
            val inputTwo = OperationType.TypeNumber(1.0)

            // WHEN
            val result = eqOperation.execute(inputOne, inputTwo)

            // THEN
            val expected = OperationType.TypeBoolean(true)
            Assertions.assertEquals(expected, result)
        }

        @Test
        @DisplayName("Then should return false")
        fun checkNotEqualsDouble() {
            // GIVEN
            val inputOne = OperationType.TypeNumber(2.0)
            val inputTwo = OperationType.TypeNumber(1.0)

            // WHEN
            val result = eqOperation.execute(inputOne, inputTwo)

            // THEN
            val expected = OperationType.TypeBoolean(false)
            Assertions.assertEquals(expected, result)
        }
    }

    @DisplayName("When execute operation with string")
    @Nested
    inner class StringOperation {

        @Test
        @DisplayName("Then should return true")
        fun checkEqualsDouble() {
            // GIVEN
            val inputOne = OperationType.TypeString("a")
            val inputTwo = OperationType.TypeString("a")

            // WHEN
            val result = eqOperation.execute(inputOne, inputTwo)

            // THEN
            val expected = OperationType.TypeBoolean(true)
            Assertions.assertEquals(expected, result)
        }

        @Test
        @DisplayName("Then should return false")
        fun checkNotEqualsDouble() {
            // GIVEN
            val inputOne = OperationType.TypeString("a")
            val inputTwo = OperationType.TypeString("b")

            // WHEN
            val result = eqOperation.execute(inputOne, inputTwo)

            // THEN
            val expected = OperationType.TypeBoolean(false)
            Assertions.assertEquals(expected, result)
        }
    }

    @DisplayName("When execute operation with list")
    @Nested
    inner class ListOperation {

        @Test
        @DisplayName("Then should return true")
        fun checkEqualsList() {
            // GIVEN
            val inputOne = OperationType.TypeJsonArray(JSONArray(listOf(1)))
            val inputTwo = OperationType.TypeJsonArray(JSONArray(listOf(1)))

            // WHEN
            val result = eqOperation.execute(inputOne, inputTwo)

            // THEN
            val expected = OperationType.TypeBoolean(true)
            Assertions.assertEquals(expected, result)
        }

        @Test
        @DisplayName("Then should return false")
        fun checkNotEqualsList() {
            // GIVEN
            val inputOne = OperationType.TypeJsonArray(JSONArray(listOf(1)))
            val inputTwo = OperationType.TypeJsonArray(JSONArray(listOf(2)))

            // WHEN
            val result = eqOperation.execute(inputOne, inputTwo)

            // THEN
            val expected = OperationType.TypeBoolean(false)
            Assertions.assertEquals(expected, result)
        }
    }

    @DisplayName("When execute operation with empty parameters")
    @Nested
    inner class ExceptionOperation {

        @Test
        @DisplayName("Then should throw exception")
        fun checkException() {
            // WHEN THEN
            assertThrows<ArrayIndexOutOfBoundsException> {
                eqOperation.execute()
            }
        }

    }
}