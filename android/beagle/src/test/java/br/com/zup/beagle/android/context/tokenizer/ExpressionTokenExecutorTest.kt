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

package br.com.zup.beagle.android.context.tokenizer

import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.testutil.RandomData
import org.junit.Assert.assertNull
import org.junit.Test
import kotlin.test.assertEquals

class ExpressionTokenExecutorTest {

    private val expressionTokenExecutor = ExpressionTokenExecutor()
    private val tokenParser = TokenParser()

    @Test
    fun execute_should_execute_all_functions() {
        // Given
        val expression = tokenParser.parse("condition(gt(sum(6, 3), 5), null, 'notNull')")

        // When
        val result = executeExpression(expression)

        // Then
        assertNull(result)
    }

    @Test
    fun execute_should_evaluate_binding() {
        // Given
        val context = ContextData(
            id = "context",
            value = RandomData.string()
        )
        val expression = tokenParser.parse("context")

        // When
        val result = expressionTokenExecutor.execute(listOf(context), expression) { _, _ ->
            return@execute context.value
        }

        // Then
        assertEquals(context.value, result)
    }

    @Test
    fun execute_should_return_null_when_expression_is_invalid() {
        // Given
        val expression = ExpressionToken(value = "", token = InvalidToken())

        // When
        val result = executeExpression(expression)

        // Then
        assertNull(result)
    }

    private fun executeExpression(expressionToken: ExpressionToken): Any? {
        return expressionTokenExecutor.execute(listOf(), expressionToken) { _, _ -> }
    }
}