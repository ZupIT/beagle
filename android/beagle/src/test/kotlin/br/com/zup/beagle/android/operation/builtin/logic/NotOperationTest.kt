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

package br.com.zup.beagle.android.operation.builtin.logic

import br.com.zup.beagle.android.operation.OperationType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@DisplayName("Given Not Operation")
internal class NotOperationTest {

    val notOperation = NotOperation()

    @DisplayName("When passing parameters")
    @Nested
    inner class Condition {

        @Test
        @DisplayName("Then should return false")
        fun checkOperationNotReturnFalse() {
            // GIVEN
            val input = OperationType.TypeBoolean(true)

            // WHEN
            val result = notOperation.execute(input)

            // THEN
            val expected = OperationType.TypeBoolean(false)

            assertEquals(expected, result)
        }

        @Test
        @DisplayName("Then should return true")
        fun checkOperationNotReturnTrue() {
            // GIVEN
            val input = OperationType.TypeBoolean(false)

            // WHEN
            val result = notOperation.execute(input)

            // THEN
            val expected = OperationType.TypeBoolean(true)

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
                notOperation.execute()
            }
        }

    }
}