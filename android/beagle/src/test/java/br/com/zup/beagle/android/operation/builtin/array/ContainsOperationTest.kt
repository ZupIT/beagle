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

package br.com.zup.beagle.android.operation.builtin.array

import br.com.zup.beagle.android.operation.OperationType
import org.json.JSONArray
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given Contains Operation")
internal class ContainsOperationTest {

    val containsOperation = ContainsOperation()

    @DisplayName("When check list")
    @Nested
    inner class Contains {
        private val list = OperationType.TypeJsonArray(JSONArray(listOf(1, 2, 3)))

        @Test
        @DisplayName("Then should found the item")
        fun containsItem() {
            // GIVEN
            val item = OperationType.TypeNumber(2)

            // WHEN
            val result = containsOperation.execute(list, item)

            // THEN
            val expected = OperationType.TypeBoolean(true)
            assertEquals(expected, result)
        }

        @Test
        @DisplayName("Then should not found the item")
        fun notContainsItem() {
            // GIVEN
            val item = OperationType.TypeNumber(4)

            // WHEN
            val result = containsOperation.execute(list, item)

            // THEN
            val expected = OperationType.TypeBoolean(false)
            assertEquals(expected, result)
        }
    }
}