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

@DisplayName("Given Condition Operation")
internal class ConditionOperationTest {

    val conditionOperation = ConditionOperation()

    @DisplayName("When passing parameters")
    @Nested
    inner class Condition {

        private val returnIfTrue = OperationType.TypeString("is true")
        private val returnIfFalse = OperationType.TypeString("is false")

        @Test
        @DisplayName("Then should return true")
        fun checkInputAndReturnTrueCondition() {
            // GIVEN
            val input = OperationType.TypeBoolean(true)

            // WHEN
            val result = conditionOperation.execute(input, returnIfTrue, returnIfFalse)

            // THEN
            assertEquals(returnIfTrue, result)
        }

        @Test
        @DisplayName("Then should return false")
        fun checkInputAndReturnFalseCondition() {
            // GIVEN
            val input = OperationType.TypeBoolean(false)

            // WHEN
            val result = conditionOperation.execute(input, returnIfTrue, returnIfFalse)

            // THEN
            assertEquals(returnIfFalse, result)
        }

        @Test
        @DisplayName("Then should return correct object")
        fun checkInputAndReturnCorrectTextC() {
            // GIVEN
            val input = OperationType.TypeBoolean(false)
            val textNull = OperationType.Null

            // WHEN
            val result = conditionOperation.execute(input, returnIfTrue, textNull)

            // THEN
            assertEquals(textNull, result)
        }
    }

    @DisplayName("When execute operation with empty parameters")
    @Nested
    inner class NullOperation {

        @Test
        @DisplayName("Then should return null")
        fun checkNull() {
            // WHEN THEN
            // WHEN
            val result = conditionOperation.execute()

            // THEN
            val expected = OperationType.Null
            assertEquals(expected, result)
        }

    }
}