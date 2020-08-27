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

package br.com.zup.beagle.android.context

import androidx.collection.LruCache
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.context.tokenizer.ExpressionTokenExecutor
import br.com.zup.beagle.android.context.tokenizer.function.FunctionResolver
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.mockdata.ComponentModel
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.utils.getExpressions
import com.squareup.moshi.Moshi
import io.mockk.*
import org.json.JSONArray
import org.json.JSONObject
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

private val CONTEXT_ID = RandomData.string()
private val CONTEXT_DATA = ContextData(CONTEXT_ID, JSONObject().apply {
    put("a", "a")
    put("b", true)
})

internal class ContextDataEvaluationTest : BaseTest() {

    private lateinit var contextDataEvaluation: ContextDataEvaluation

    override fun setUp() {
        super.setUp()

        contextDataEvaluation = ContextDataEvaluation()

        mockkObject(BeagleMessageLogs)

        every { BeagleMessageLogs.errorWhileTryingToNotifyContextChanges(any()) } just Runs
        every { BeagleMessageLogs.errorWhileTryingToAccessContext(any()) } just Runs
    }

    @Test
    fun evaluateContextBindings_should_get_string_value_from_root_of_context() {
        // Given
        val contextData = ContextData(
            id = CONTEXT_ID,
            value = RandomData.string()
        )
        val bind = expressionOf<String>("@{$CONTEXT_ID}")

        // When
        val actualValue = contextDataEvaluation.evaluateBindExpression(listOf(contextData), bind)

        // Then
        assertEquals(contextData.value, actualValue)
    }

    @Test
    fun evaluateContextBindings_should_get_double_value_from_root_of_context() {
        // Given
        val contextData = ContextData(
            id = CONTEXT_ID,
            value = RandomData.double()
        )
        val bind = expressionOf<Double>("@{$CONTEXT_ID}")

        // When
        val actualValue = contextDataEvaluation.evaluateBindExpression(listOf(contextData), bind)

        // Then
        assertEquals(contextData.value, actualValue)
    }

    @Test
    fun evaluateContextBindings_should_get_float_value_from_root_of_context() {
        // Given
        val contextData = ContextData(
            id = CONTEXT_ID,
            value = RandomData.float()
        )
        val bind = expressionOf<Float>("@{$CONTEXT_ID}")

        // When
        val actualValue = contextDataEvaluation.evaluateBindExpression(listOf(contextData), bind)

        // Then
        assertEquals(contextData.value, actualValue)
    }

    @Test
    fun evaluateContextBindings_should_get_value_from_context_and_deserialize_JSONObject() {
        // Given
        val bind = expressionOf<ComponentModel>("@{$CONTEXT_ID}")

        // When
        val value = contextDataEvaluation.evaluateBindExpression(listOf(CONTEXT_DATA), bind)

        // Then
        assertTrue { value is ComponentModel }
        val componentModel = value as ComponentModel
        assertEquals("a", componentModel.a)
        assertEquals(true, componentModel.b)
    }

    @Test
    fun evaluateContextBindings_should_get_value_from_context_and_deserialize_JSONArray() {
        // Given
        val bind = expressionOf<List<ComponentModel>>("@{$CONTEXT_ID}")
        val contextData = ContextData(
            id = CONTEXT_ID,
            value = JSONArray()
        )

        // When
        val value = contextDataEvaluation.evaluateBindExpression(listOf(contextData), bind)

        // Then
        assertTrue { value is ArrayList<*> }
    }

    @org.junit.Test
    fun evaluateContextBindings_should_show_error_when_moshi_returns_null() {
        // Given
        val bind = expressionOf<ComponentModel>("@{$CONTEXT_ID}")
        val moshi = mockk<Moshi> {
            every { adapter<Any>(bind.type).fromJson(any<String>()) } returns null
        }
        val contextDataEvaluation = ContextDataEvaluation(moshi = moshi)

        // When
        val value = contextDataEvaluation.evaluateBindExpression(listOf(CONTEXT_DATA), bind)

        // Then
        assertNull(value)
        verify(exactly = once()) { BeagleMessageLogs.errorWhenExpressionEvaluateNullValue(any()) }
    }

    @Test
    fun evaluateAllContext_should_evaluate_text_string_text_expression() {
        // Given
        val hello = "hello"
        val bind = expressionOf<String>("This is an expression @{$CONTEXT_ID.a} and this @{$CONTEXT_ID.b}")

        // When
        val value = contextDataEvaluation.evaluateBindExpression(listOf(CONTEXT_DATA), bind)

        // Then
        val expected = "This is an expression a and this true"
        assertEquals(expected, value)
    }

    @Test
    fun evaluateAllContext_should_not_evaluate_multiple_expressions_that_is_not_text() {
        // Given
        val bind = expressionOf<Int>("This is an expression @{$CONTEXT_ID.a} and this @{$CONTEXT_ID.b}")
        every { BeagleMessageLogs.multipleExpressionsInValueThatIsNotString() } just Runs

        // When
        val value = contextDataEvaluation.evaluateBindExpression(listOf(CONTEXT_DATA), bind)

        // Then
        assertNull(value)
        verify(exactly = once()) { BeagleMessageLogs.multipleExpressionsInValueThatIsNotString() }
    }

    @Test
    fun evaluateAllContext_should_evaluate_empty_string_in_multiple_expressions_with_null_bind_value() {
        // Given
        val bind = expressionOf<String>("This is an expression @{$CONTEXT_ID.exp1} and this @{$CONTEXT_ID.exp2}")
//        every { contextDataManipulator.get(any(), any()) } returns null

        // When
        val value = contextDataEvaluation.evaluateBindExpression(listOf(CONTEXT_DATA), bind)

        // Then
        val expected = "This is an expression  and this "
        assertEquals(expected, value)
    }

    @Test
    fun evaluateAllContext_should_return_empty_in_expressions_with_null_bind_value_in_string_type() {
        // Given
        val bind = expressionOf<String>("@{$CONTEXT_ID.exp1}")
//        every { contextDataManipulator.get(any(), any()) } returns null

        // When
        val value = contextDataEvaluation.evaluateBindExpression(listOf(CONTEXT_DATA), bind)

        // Then
        assertEquals("", value)
    }

    @Test
    fun evaluateAllContext_should_get_value_from_cache() {
        // Given
        val cachedValue = RandomData.string()
        val cache = LruCache<String, Any>(5).apply {
            put("context.b.c", cachedValue)
        }
        val bind = expressionOf<String>("@{context.b.c}")
        val context = ContextData(
            id = "context",
            value = JSONObject().apply {
                put("a", JSONObject().apply {
                    put("c", "not cached")
                })
            }
        )

        // When
        val value = contextDataEvaluation.evaluateBindExpression(context, cache, bind, mutableMapOf())

        // Then
        assertEquals(cachedValue, value)
    }

    @Test
    fun evaluateAllContext_should_get_context2_value_from_evaluatedBindings() {
        // Given
        val contextValue = 1
        val contextData = ContextData(
            id = "context1",
            value = contextValue
        )
        val evaluatedBindings = mutableMapOf<String, Any>(
            "context2" to contextValue
        )
        val bind = expressionOf<Int>("@{sum(context1, context2)}")

        // When
        val value = contextDataEvaluation.evaluateBindExpression(
            contextData,
            LruCache(5),
            bind,
            evaluatedBindings
        )

        // Then
        assertEquals(2, value)
    }

    @Test
    fun evaluateAllContext_with_operation_sum_with_hardcoded_values() {
        // Given
        val bind = expressionOf<Int>("@{sum(1, 2)}")

        // When
        val value = contextDataEvaluation.evaluateBindExpression(listOf(CONTEXT_DATA), bind)

        // Then
        assertEquals(3, value)
    }

    @Test
    fun evaluateAllContext_with_operation_insert_with_binding() {
        // Given
        val context = ContextData(
            id = "binding",
            value = listOf(1, 2, 3)
        )
        val bind = expressionOf<Int>("@{insert(binding, 2)}")

        // When
        val value = contextDataEvaluation.evaluateBindExpression(listOf(context), bind)

        // Then
        val expected = JSONArray().apply {
            put(1)
            put(2)
            put(3)
            put(2)
        }
        assertEquals(expected.toString(), value.toString().replace(" ", ""))
    }

    @Test
    fun evaluateAllContext_with_literal_true() {
        // Given
        val bind = expressionOf<Boolean>("@{true}")

        // When
        val value = contextDataEvaluation.evaluateBindExpression(listOf(CONTEXT_DATA), bind) as Boolean

        // Then
        assertTrue { value }
    }

    @Test
    fun evaluateAllContext_with_literal_false() {
        // Given
        val bind = expressionOf<Boolean>("@{false}")

        // When
        val value = contextDataEvaluation.evaluateBindExpression(listOf(CONTEXT_DATA), bind) as Boolean

        // Then
        assertFalse { value }
    }

    @Test
    fun evaluateAllContext_with_literal_null() {
        // Given
        val bind = expressionOf<String>("@{null}")

        // When
        val value = contextDataEvaluation.evaluateBindExpression(listOf(CONTEXT_DATA), bind)

        // Then
        assertEquals("", value)
    }

    @Test
    fun evaluateAllContext_with_operation_sum_with_hardcoded_values_and_string() {
        // Given
        val bind = expressionOf<String>("sum result: @{sum(1, 1)}")

        // When
        val value = contextDataEvaluation.evaluateBindExpression(listOf(CONTEXT_DATA), bind)

        // Then
        assertEquals("sum result: 2", value)
    }

    @Test
    fun evaluateContextBindings_with_operation_should_evaluate_contains_operation() {
        // Given
        val bind = expressionOf<Boolean>("result: @{contains(insert(${CONTEXT_ID}, 4), 4)}")
        val contextData = ContextData(
            id = CONTEXT_ID,
            value = listOf(1,2,3)
        )

        // When
        val value = contextDataEvaluation.evaluateBindExpression(listOf(contextData), bind)

        // Then
        assertTrue { value as Boolean }
    }

    @Test
    fun evaluateContextBindings_with_operation_should_throw_error_insert_operation_index_out_of_bound() {
        // Given
        val bind = expressionOf<List<ComponentModel>>("result: @{insert(${CONTEXT_ID}, 4, 4)}")
        val contextData = ContextData(
            id = CONTEXT_ID,
            value = listOf(1,2,3)
        )

        // When
        val value = contextDataEvaluation.evaluateBindExpression(listOf(contextData), bind)

        // Then
        assertNull(value)
        verify(exactly = once()) { BeagleMessageLogs.errorWhenExpressionEvaluateNullValue(any()) }
    }

    @Test
    fun evaluateContextBindings_with_operation_should_evaluate_insert_operation() {
        // Given
        val bind = expressionOf<String>("result: @{insert(context, 4, 2)}")
        val contextData = ContextData(
            id = "context",
            value = listOf(1, 2, 3)
        )

        // When
        val value = contextDataEvaluation.evaluateBindExpression(listOf(contextData), bind)

        // Then
        assertEquals("result: [1, 2, 4, 3]", value)
    }

    @Test
    fun evaluateContextBindings_with_operation_should_evaluate_remove_operation() {
        // Given
        val bind = expressionOf<String>("result: @{remove(context, 2)}")
        val contextData = ContextData(
            id = "context",
            value = listOf(1, 2, 3)
        )

        // When
        val value = contextDataEvaluation.evaluateBindExpression(listOf(contextData), bind)

        // Then
        assertEquals("result: [1, 3]", value)
    }

    @Test
    fun evaluateContextBindings_with_operation_should_evaluate_remove_index_operation() {
        // Given
        val bind = expressionOf<String>("result: @{removeIndex(context, 0)}")
        val contextData = ContextData(
            id = "context",
            value = listOf(1, 2, 3)
        )

        // When
        val value = contextDataEvaluation.evaluateBindExpression(listOf(contextData), bind)

        // Then
        assertEquals("result: [2, 3]", value)
    }

    @Test
    fun evaluateAllContext_with_operation_sum_with_binding_value() {
        // Given
        val contextValue = 1
        val contextData1 = ContextData(
            id = "context1",
            value = contextValue
        )
        val contextData2 = ContextData(
            id = "context2",
            value = contextValue
        )
        val bind = expressionOf<Int>("@{sum(context1, context2)}")

        // When
        val value = contextDataEvaluation.evaluateBindExpression(listOf(contextData1, contextData2), bind)

        // Then
        assertEquals(2, value)
    }

    @Test
    fun evaluateAllContext_with_escaping_expressions_should_return_expected_values() {
        createEscapeBindingMockCases().forEach { mockCase ->
            // Given
            val bind = expressionOf<String>(mockCase.value)

            // When
            val value = contextDataEvaluation.evaluateBindExpression(listOf(mockCase.contextData), bind)

            // Then
            assertEquals(mockCase.expected, value)
        }
    }

    private fun createEscapeBindingMockCases(): List<EscapingTestCases> = listOf(
        EscapingTestCases("@{context}", "value"),
        EscapingTestCases("@{context} test", "value test"),
        EscapingTestCases("test @{context}", "test value"),
        EscapingTestCases("test \\@{sum(1, 2)}", "test @{sum(1, 2)}"),
        EscapingTestCases("test \\@{'string'}", "test @{'string'}"),
        EscapingTestCases("test \\@{null}", "test @{null}"),
        EscapingTestCases("test \\@{true}", "test @{true}"),
        EscapingTestCases("test \\@{context}", "test @{context}"),
        EscapingTestCases("test \\\\@{context}", "test \\value"),
        EscapingTestCases("test \\\\\\@{context}", "test \\@{context}"),
        EscapingTestCases("test \\\\\\\\@{context}", "test \\\\value"),
        EscapingTestCases("test \\\\\\\\\\@{context}", "test \\\\@{context}"),
        EscapingTestCases(
            "This is a @{context} of \\ expression \\@{context}",
            "This is a value of \\ expression @{context}"
        ),
        EscapingTestCases(
            "@{context}",
            "\\\"value\\\"",
            ContextData("context", "\\\"value\\\"")
        )
    )

    data class EscapingTestCases(
        val value: String,
        val expected: String,
        val contextData: ContextData = ContextData("context", "value")
    )
}