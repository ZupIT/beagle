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

package br.com.zup.beagle.android.operation.builtin.string

import br.com.zup.beagle.android.operation.OperationType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@DisplayName("Given Substr Operation")
internal class SubstrOperationTest {

    val substrOperation = SubstrOperation()

    @DisplayName("When execute operation")
    @Nested
    inner class Operation {

        val input = OperationType.TypeString("123456789")

        @Test
        @DisplayName("Then should return correct text")
        fun checkStringOne() {
            // GIVEN
            val start = OperationType.TypeNumber(5)
            val length = OperationType.TypeNumber(0)

            // WHEN
            val result = substrOperation.execute(input, start, length)

            // THEN
            val expected = OperationType.TypeString("")
            assertEquals(expected, result)
        }

        @Test
        @DisplayName("Then should return correct text")
        fun checkStringTwo() {
            // GIVEN
            val start = OperationType.TypeNumber(0)
            val length = OperationType.TypeNumber(5)

            // WHEN
            val result = substrOperation.execute(input, start, length)

            // THEN
            val expected = OperationType.TypeString("12345")
            assertEquals(expected, result)
        }

        @Test
        @DisplayName("Then should return correct text")
        fun checkStringThree() {
            // GIVEN
            val start = OperationType.TypeNumber(3)
            val length = OperationType.TypeNumber(3)

            // WHEN
            val result = substrOperation.execute(input, start, length)

            // THEN
            val expected = OperationType.TypeString("456")
            assertEquals(expected, result)
        }

        @Test
        @DisplayName("Then should return correct text")
        fun checkStringFour() {
            // GIVEN
            val start = OperationType.TypeNumber(5)

            // WHEN
            val result = substrOperation.execute(input, start)

            // THEN
            val expected = OperationType.TypeString("6789")
            assertEquals(expected, result)
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
                substrOperation.execute()
            }
        }

    }
}