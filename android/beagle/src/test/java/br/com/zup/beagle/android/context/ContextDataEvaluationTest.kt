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

import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.jsonpath.JsonPathFinder
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.mockdata.ComponentModel
import br.com.zup.beagle.android.testutil.RandomData
import com.squareup.moshi.Moshi
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.json.JSONArray
import org.json.JSONObject
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

private val CONTEXT_ID = RandomData.string()
private val CONTEXT_DATA = ContextData(CONTEXT_ID, RandomData.string())
private val BIND = expressionOf<ComponentModel>("@{$CONTEXT_ID.a}")

internal class ContextDataEvaluationTest {

    private val jsonPathFinder = mockk<JsonPathFinder>()
    private val contextPathResolver = mockk<ContextPathResolver>()
    private val moshi = mockk<Moshi>()

    private lateinit var contextDataEvaluation: ContextDataEvaluation

    @Before
    fun setUp() {
        contextDataEvaluation = ContextDataEvaluation(
            jsonPathFinder,
            contextPathResolver,
            moshi
        )

        mockkObject(BeagleMessageLogs)

        every { contextPathResolver.getKeysFromPath(CONTEXT_DATA.id, any()) } returns LinkedList()
        every { BeagleMessageLogs.errorWhileTryingToNotifyContextChanges(any()) } just Runs
        every { BeagleMessageLogs.errorWhileTryingToAccessContext(any()) } just Runs
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun evaluateContextBindings_should_get_value_from_root_of_context() {
        // Given
        val bind = expressionOf<Int>("@{${CONTEXT_DATA.id}}")

        // When
        val actualValue = contextDataEvaluation.evaluateBindExpression(CONTEXT_DATA, bind)

        // Then
        assertEquals(CONTEXT_DATA.value, actualValue)
    }

    @Test
    fun evaluateContextBindings_should_get_value_from_context_and_deserialize_JSONOBject() {
        // Given
        val jsonObject = mockk<JSONObject>()
        every { jsonPathFinder.find(any(), any()) } returns jsonObject
        every { moshi.adapter<Any>(ComponentModel::class.java).fromJson(any<String>()) } returns mockk<ComponentModel>()

        // When
        val value = contextDataEvaluation.evaluateBindExpression(CONTEXT_DATA, BIND)

        // Then
        assertTrue { value is ComponentModel }
    }

    @Test
    fun evaluateContextBindings_should_get_value_from_context_and_deserialize_JSONArray() {
        // Given
        val jsonArray = mockk<JSONArray>()
        every { jsonPathFinder.find(any(), any()) } returns jsonArray
        every { moshi.adapter<Any>(ComponentModel::class.java).fromJson(any<String>()) } returns mockk<ComponentModel>()

        // When
        val value = contextDataEvaluation.evaluateBindExpression(CONTEXT_DATA, BIND)

        // Then
        assertTrue { value is ComponentModel }
    }

    @org.junit.Test
    fun evaluateContextBindings_should_show_error_when_moshi_returns_null() {
        // Given
        val model = mockk<JSONArray>()
        every { jsonPathFinder.find(any(), any()) } returns model
        every { moshi.adapter<Any>(any<Class<*>>()).fromJson(any<String>()) } returns null

        // When
        val value = contextDataEvaluation.evaluateBindExpression(CONTEXT_DATA, BIND)

        // Then
        assertNull(value)
        verify(exactly = once()) { BeagleMessageLogs.errorWhenExpressionEvaluateNullValue(any()) }
    }

    @Test
    fun evaluateContextBindings_should_throw_exception_when_jsonPathFinder_returns_null() {
        // Given
        every { jsonPathFinder.find(any(), any()) } returns null

        // When
        val value = contextDataEvaluation.evaluateBindExpression(CONTEXT_DATA, BIND)

        // Then
        assertNull(value)
        verify(exactly = once()) { BeagleMessageLogs.errorWhileTryingToNotifyContextChanges(any()) }
    }

    @Test
    fun evaluateContextBindings_should_throw_exception_when_trying_to_call_jsonPathFinder() {
        // Given
        every { jsonPathFinder.find(any(), any()) } throws IllegalStateException()

        // When
        val value = contextDataEvaluation.evaluateBindExpression(CONTEXT_DATA, BIND)

        // Then
        assertNull(value)
        verify { BeagleMessageLogs.errorWhileTryingToAccessContext(any()) }
        verify { BeagleMessageLogs.errorWhileTryingToNotifyContextChanges(any()) }
    }

    @Test
    fun evaluateAllContext_should_evaluate_text_string_text_expression() {
        // Given
        val bind = expressionOf<String>("This is an expression @{$CONTEXT_ID.exp1} and this @{$CONTEXT_ID.exp2}")
        every { jsonPathFinder.find(any(), any()) } returns "hello"

        // When
        val value = contextDataEvaluation.evaluateBindExpression(CONTEXT_DATA, bind)

        // Then
        val expected = "This is an expression hello and this hello"
        assertEquals(expected, value)
    }

    @Test
    fun evaluateAllContext_should_evaluate_text_string_with_json_expression() {
        // Given
        val bind = expressionOf<String>("""{"key": "@{value}"}""")
        every { jsonPathFinder.find(any(), any()) } returns JSONObject().apply {
            put("key", "hello")
        }

        // When
        contextDataEvaluation.evaluateBindExpression(CONTEXT_DATA, bind)

        // Then
        verify(exactly = 0) { moshi.adapter<Any>(String::class.java).fromJson(any<String>()) }
    }

    @Test
    fun evaluateAllContext_should_not_evaluate_multiple_expressions_that_is_not_text() {
        // Given
        val bind = expressionOf<Int>("This is an expression @{$CONTEXT_ID.exp1} and this @{$CONTEXT_ID.exp2}")
        every { BeagleMessageLogs.multipleExpressionsInValueThatIsNotString() } just Runs

        // When
        val value = contextDataEvaluation.evaluateBindExpression(CONTEXT_DATA, bind)

        // Then
        assertNull(value)
        verify(exactly = once()) { BeagleMessageLogs.multipleExpressionsInValueThatIsNotString() }
    }

    @Test
    fun evaluateAllContext_should_evaluate_empty_string_in_multiple_expressions_with_null_bind_value() {
        // Given
        val bind = expressionOf<String>("This is an expression @{$CONTEXT_ID.exp1} and this @{$CONTEXT_ID.exp2}")
        every { jsonPathFinder.find(any(), any()) } returns null

        // When
        val value = contextDataEvaluation.evaluateBindExpression(CONTEXT_DATA, bind)

        // Then
        val expected = "This is an expression  and this "
        assertEquals(expected, value)
    }

    @Test
    fun evaluateAllContext_should_return_null_in_expressions_with_null_bind_value_in_string_type() {
        // Given
        val bind = expressionOf<String>("@{$CONTEXT_ID.exp1}")
        every { jsonPathFinder.find(any(), any()) } returns null

        // When
        val value = contextDataEvaluation.evaluateBindExpression(CONTEXT_DATA, bind)

        // Then
        assertNull(value)
        verify(exactly = once()) { BeagleMessageLogs.errorWhenExpressionEvaluateNullValue(any()) }
    }

    @Test
    fun evaluateAllContext_should_return_null_in_expressions_with_null_bind_value_in_JSONArray_type() {
        // Given
        val jsonArray = mockk<JSONArray>()
        every { jsonPathFinder.find(any(), any()) } returns jsonArray
        every { moshi.adapter<Any>(any<Class<*>>()).fromJson(any<String>()) } returns null

        // When
        val value = contextDataEvaluation.evaluateBindExpression(CONTEXT_DATA, BIND)

        // Then
        assertNull(value)
        verify(exactly = once()) { BeagleMessageLogs.errorWhenExpressionEvaluateNullValue(any()) }
    }

    @Test
    fun evaluateAllContext_should_return_null_in_expressions_with_null_bind_value_in_JSONObject_type() {
        // Given
        val jsonObject = mockk<JSONObject>()
        every { jsonPathFinder.find(any(), any()) } returns jsonObject
        every { moshi.adapter<Any>(any<Class<*>>()).fromJson(any<String>()) } returns null

        // When
        val value = contextDataEvaluation.evaluateBindExpression(CONTEXT_DATA, BIND)

        // Then
        assertNull(value)
        verify(exactly = once()) { BeagleMessageLogs.errorWhenExpressionEvaluateNullValue(any()) }
    }

}