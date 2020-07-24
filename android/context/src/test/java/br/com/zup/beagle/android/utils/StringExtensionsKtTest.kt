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


import org.junit.Test
import kotlin.test.assertEquals

class StringExtensionsKtTest {

    @Test
    fun getExpressions_should_return_one_expression_present_on_string() {
        // Given
        val text = "@{exp1}"

        // When
        val expressions = text.getExpressions()

        // Then
        assertEquals(1, expressions.size)
        assertEquals("exp1", expressions[0])
    }

    @Test
    fun getExpressions_should_return_one_expression_present_on_text_string() {
        // Given
        val text = "I have this @{exp1}"

        // When
        val expressions = text.getExpressions()

        // Then
        assertEquals(1, expressions.size)
        assertEquals("exp1", expressions[0])
    }

    @Test
    fun getExpressions_should_return_two_expressions_present_on_text_string() {
        // Given
        val text = "I have this @{exp1} and this one @{exp2}"

        // When
        val expressions = text.getExpressions()

        // Then
        assertEquals(2, expressions.size)
        assertEquals("exp1", expressions[0])
        assertEquals("exp2", expressions[1])
    }

    @Test
    fun getExpressions_should_return_two_expressions_present_on_beginning_of_text_string() {
        // Given
        val text = "@{exp1} and this one @{exp2}"

        // When
        val expressions = text.getExpressions()

        // Then
        assertEquals(2, expressions.size)
        assertEquals("exp1", expressions[0])
        assertEquals("exp2", expressions[1])
    }
}
