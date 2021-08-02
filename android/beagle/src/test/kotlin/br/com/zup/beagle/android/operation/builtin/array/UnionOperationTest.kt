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
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a Union Operation")
internal class UnionOperationTest {

    val unionOperation = UnionOperation()

    @DisplayName("When union two non empty arrays")
    @Nested
    inner class UniteTwoNonEmptyArrays {

        @Test
        @DisplayName("Then the resulting array should contain the two arrays concatenated")
        fun concatArrays(){
            //GIVEN
            val listOne = OperationType.TypeJsonArray(JSONArray(listOf("One", "Two", "Three")))
            val listtwo = OperationType.TypeJsonArray(JSONArray(listOf("Four", "Five", "Six")))

            //WHEN
            val result = unionOperation.execute(listOne, listtwo)

            //THEN
            val expected = OperationType.TypeJsonArray(JSONArray(listOf("One", "Two", "Three", "Four", "Five", "Six")))
            assertEquals(expected.toString(), result.toString())
        }
    }

    @DisplayName("When union a non empty array with an empty array")
    @Nested
    inner class UniteNonEmptyArrayWithEmptyArray {

        @Test
        @DisplayName("Then the resulting array should contain the two arrays concatenated")
        fun concatEmptyArrayToNonEmptyArray(){
            //GIVEN
            val listOne = OperationType.TypeJsonArray(JSONArray(listOf("One", "Two", "Three")))
            val listtwo = OperationType.TypeJsonArray(JSONArray(listOf<String>()))

            //WHEN
            val result = unionOperation.execute(listOne, listtwo)

            //THEN
            val expected = OperationType.TypeJsonArray(JSONArray(listOf("One", "Two", "Three")))
            assertEquals(expected.toString(), result.toString())
        }

        @Test
        @DisplayName("Then the resulting array should contain the two arrays concatenated")
        fun concatNonEmptyArrayToEmptyArray(){
            //GIVEN
            val listOne = OperationType.TypeJsonArray(JSONArray(listOf<String>()))
            val listtwo = OperationType.TypeJsonArray(JSONArray(listOf("Four", "Five", "Six")))

            //WHEN
            val result = unionOperation.execute(listOne, listtwo)

            //THEN
            val expected = OperationType.TypeJsonArray(JSONArray(listOf("Four", "Five", "Six")))
            assertEquals(expected.toString(), result.toString())
        }
    }

    @DisplayName("When union two empty arrays")
    @Nested
    inner class UniteTwoEmptyArrays {

        @Test
        @DisplayName("Then the resulting array should be an empty array")
        fun concatArrays(){
            //GIVEN
            val listOne = OperationType.TypeJsonArray(JSONArray(listOf<String>()))
            val listtwo = OperationType.TypeJsonArray(JSONArray(listOf<String>()))

            //WHEN
            val result = unionOperation.execute(listOne, listtwo)

            //THEN
            assertTrue((result.value as JSONArray).length() == 0)
        }
    }
}