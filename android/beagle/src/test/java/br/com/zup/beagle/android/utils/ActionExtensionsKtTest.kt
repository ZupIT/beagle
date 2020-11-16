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

import br.com.zup.beagle.R
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.android.mockdata.createViewForContext
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.view.viewmodel.Response
import br.com.zup.beagle.android.view.viewmodel.ScreenContextViewModel
import io.mockk.every
import io.mockk.mockk
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach

class ActionExtensionsKtTest : BaseTest() {

    private val contextView = createViewForContext()
    private val bindView = createViewForContext(contextView)
    private val action = mockk<Action>(relaxed = true)

    private lateinit var viewModel: ScreenContextViewModel

    @BeforeEach
    override fun setUp() {
        super.setUp()

        viewModel = ScreenContextViewModel()

        prepareViewModelMock(viewModel)
    }

    @Test
    fun evaluateExpression_should_evaluate_bind_of_type_String_with_multiple_expressions() {
        // Given
        val bind = expressionOf<String>("Hello @{context1} and @{context2}")
        val contextValue = "hello"
        val context1 = ContextData(
            id = "context1",
            value = contextValue
        )
        val context2 = ContextData(
            id = "context2",
            value = contextValue
        )
        val contextView1 = createViewForContext()
        val contextView2 = createViewForContext(contextView1)
        val bindView = createViewForContext(contextView2)
        viewModel.addContext(contextView1, context1)
        viewModel.addContext(contextView2, context2)

        // When
        val actualValue = action.evaluateExpression(rootView, bindView, bind)

        // Then
        val expected = "Hello $contextValue and $contextValue"
        assertEquals(expected, actualValue)
    }

    @Test
    fun evaluateExpression_should_evaluate_raw_JSONObject() {
        // Given
        val value = JSONObject("""{"type":"LOADING"}""")

        // When
        val actualValue = action.evaluateExpression(rootView, bindView, value)

        // Then
        assertEquals(value.toString(), actualValue.toString())
    }

    @Test
    fun evaluateExpression_should_evaluate_raw_JSONArray() {
        // Given
        val value = JSONArray("""["hello1", "hello2"]""")

        // When
        val actualValue = action.evaluateExpression(rootView, bindView, value)

        // Then
        assertEquals(value.toString(), actualValue.toString())
    }

    @Test
    fun evaluateExpression_should_evaluate_bind_with_string_and_operation() {
        // Given
        val bind = expressionOf<String>("Hello @{sum(context1, context2)}")
        val contextValue = 1
        val context1 = ContextData(
            id = "context1",
            value = contextValue
        )
        val context2 = ContextData(
            id = "context2",
            value = contextValue
        )
        val contextView1 = createViewForContext()
        val contextView2 = createViewForContext(contextView1)
        val bindView = createViewForContext(contextView2)
        viewModel.addContext(contextView1, context1)
        viewModel.addContext(contextView2, context2)

        // When
        val actualValue = action.evaluateExpression(rootView, bindView, bind) as String

        // Then
        assertEquals("Hello 2", actualValue)
    }

    @Test
    fun evaluateExpression_should_evaluate_bind_of_type_String_with_multiple_expressions_starting_wih_expression() {
        // Given
        val bind = expressionOf<String>("@{context1} and @{context2}")
        val context1 = ContextData(
            id = "context1",
            value = RandomData.string()
        )
        val context2 = ContextData(
            id = "context2",
            value = RandomData.string()
        )
        val contextView1 = createViewForContext()
        val contextView2 = createViewForContext(contextView1)
        val bindView = createViewForContext(contextView2)
        viewModel.addContext(contextView1, context1)
        viewModel.addContext(contextView2, context2)

        // When
        val actualValue = action.evaluateExpression(rootView, bindView, bind)

        // Then
        assertEquals("${context1.value} and ${context2.value}", actualValue)
    }

    @Test
    fun evaluateExpression_should_evaluate_bind_of_type_double() {
        // Given
        val bind = expressionOf<Double>("@{context}")
        val contextValue = RandomData.double()
        viewModel.addContext(contextView, ContextData(
            id = "context",
            value = contextValue
        ))
        // When
        val actualValue = action.evaluateExpression(rootView, bindView, bind)

        // Then
        assertEquals(contextValue, actualValue)
    }

    @Test
    fun evaluateExpression_should_return_the_actual_double_value() {
        // Given
        val value = RandomData.double()

        // When
        val actualValue = action.evaluateExpression(rootView, bindView, value)

        // Then
        assertEquals(value, actualValue)
    }

    @Test
    fun evaluateExpression_should_return_the_actual_boolean_value() {
        // Given
        val value = RandomData.boolean()

        // When
        val actualValue = action.evaluateExpression(rootView, bindView, value)

        // Then
        assertEquals(value, actualValue)
    }

    @Test
    fun evaluateExpression_should_return_the_actual_int_value() {
        // Given
        val value = RandomData.int()

        // When
        val actualValue = action.evaluateExpression(rootView, bindView, value)

        // Then
        assertEquals(value, actualValue)
    }

    @Test
    fun evaluateExpression_should_return_the_actual_Float_value() {
        // Given
        val value = RandomData.float()

        // When
        val actualValue = action.evaluateExpression(rootView, bindView, value)

        // Then
        assertEquals(value, actualValue)
    }

    @Test
    fun evaluateExpression_should_return_the_actual_string_value() {
        // Given
        val value = RandomData.string()

        // When
        val actualValue = action.evaluateExpression(rootView, bindView, value)

        // Then
        assertEquals(value, actualValue)
    }

    @Test
    fun evaluateExpression_should_evaluate_expression_of_type_int() {
        // Given
        val contextValue = 0
        viewModel.addContext(contextView, ContextData(
            id = "context",
            value = contextValue
        ))
        val value = "@{context}"

        // When
        val actualValue = action.evaluateExpression(rootView, bindView, value)

        // Then
        assertEquals(contextValue, actualValue as Int)
    }

    @Test
    fun evaluateExpression_should_evaluate_expression_of_type_double() {
        // Given
        val contextValue = 1.0
        viewModel.addContext(contextView, ContextData(
            id = "context",
            value = contextValue
        ))
        val value = "@{context}"

        // When
        val actualValue = action.evaluateExpression(rootView, bindView, value)

        // Then
        assertEquals(contextValue, actualValue as Double)
    }

    @Test
    fun `GIVEN expression with context string number WHEN evaluate expression THEN return string with correct text`() {
        // Given
        val contextValue = "93629893111"
        viewModel.addContext(contextView, ContextData(
            id = "context",
            value = contextValue
        ))
        val value = "@{context}"

        // When
        val actualValue = action.evaluateExpression(rootView, bindView, value)

        // Then
        assertEquals(contextValue, actualValue)
    }

    @Test
    fun `GIVEN expression with context integer value WHEN evaluate expression THEN return correct integer type`() {
        // Given
        val contextValue = Integer.MAX_VALUE
        viewModel.addContext(contextView, ContextData(
            id = "context",
            value = contextValue
        ))
        val value = "@{context}"

        // When
        val actualValue = action.evaluateExpression(rootView, bindView, value)

        // Then
        assertEquals(contextValue, actualValue as Int)
    }

    @Test
    fun evaluateExpression_should_evaluate_expression_of_type_boolean() {
        // Given
        val contextValue = true
        viewModel.addContext(contextView, ContextData(
            id = "context",
            value = contextValue
        ))
        val value = "@{context}"

        // When
        val actualValue = action.evaluateExpression(rootView, bindView, value)

        // Then
        assertEquals(contextValue, actualValue as Boolean)
    }

    @Test
    fun evaluateExpression_should_evaluate_expression_of_type_string() {
        // Given
        val contextValue = "hello"
        viewModel.addContext(contextView, ContextData(
            id = "context",
            value = contextValue
        ))
        val value = "@{context}"

        // When
        val actualValue = action.evaluateExpression(rootView, bindView, value)

        // Then
        assertEquals(contextValue, actualValue)
    }

    @Test
    fun evaluateExpression_should_return_JSON_string_evaluated() {
        // Given
        val contextValue = "hello"
        viewModel.addContext(contextView, ContextData(
            id = "context",
            value = contextValue
        ))
        val value = """{"value": "@{context}""""

        // When
        val actualValue = action.evaluateExpression(rootView, bindView, value)

        // Then
        val expected = """{"value": "$contextValue""""
        assertEquals(expected, actualValue)
    }

    @Test
    fun evaluateExpression_should_return_JSONArray_evaluated() {
        // Given
        val contextValue = "hello"
        viewModel.addContext(contextView, ContextData(
            id = "context",
            value = contextValue
        ))
        val value = JSONArray().apply {
            put("@{context}")
        }

        // When
        val actualValue = action.evaluateExpression(rootView, bindView, value)

        // Then
        assertTrue(actualValue is JSONArray)
        assertEquals("""["$contextValue"]""", actualValue.toString())
    }

    @Test
    fun evaluateExpression_should_evaluate_JSONArray() {
        // Given
        val contextValue = JSONArray().apply {
            put("hello")
        }
        viewModel.addContext(contextView, ContextData(
            id = "context",
            value = contextValue
        ))

        // When
        val actualValue = action.evaluateExpression(rootView, bindView, "@{context}")

        // Then
        assertTrue(actualValue is JSONArray)
        assertEquals("""["hello"]""", actualValue.toString())
    }

    @Test
    fun evaluateExpression_should_return_JSONArray_evaluated_with_multiple_expressions() {
        // Given
        val context1 = ContextData(
            id = "context1",
            value = RandomData.string()
        )
        val context2 = ContextData(
            id = "context2",
            value = RandomData.string()
        )
        val contextView1 = createViewForContext()
        val contextView2 = createViewForContext(contextView1)
        val bindView = createViewForContext(contextView2)
        viewModel.addContext(contextView1, context1)
        viewModel.addContext(contextView2, context2)
        val value = JSONArray().apply {
            put("@{context1}")
            put("@{context2}")
        }

        // When
        val actualValue = action.evaluateExpression(rootView, bindView, value)

        // Then
        assertTrue(actualValue is JSONArray)
        assertEquals("""["${context1.value}","${context2.value}"]""", actualValue.toString())
    }

    @Test
    fun evaluateExpression_should_return_JSONObject_evaluated() {
        // Given
        val contextValue = "hello"
        viewModel.addContext(contextView, ContextData(
            id = "context",
            value = contextValue
        ))
        val value = JSONObject().apply {
            put("value", "@{context}")
        }

        // When
        val actualValue = action.evaluateExpression(rootView, bindView, value)

        // Then
        assertTrue(actualValue is JSONObject)
        assertEquals("""{"value":"$contextValue"}""", actualValue.toString())
    }

    @Test
    fun evaluateExpression_should_evaluate_JSONObject() {
        // Given
        val value = JSONObject().apply {
            put("value", "hello")
        }
        viewModel.addContext(contextView, ContextData(
            id = "context",
            value = value
        ))

        // When
        val actualValue = action.evaluateExpression(rootView, bindView, "@{context}")

        // Then
        assertTrue(actualValue is JSONObject)
        assertEquals("""{"value":"hello"}""", actualValue.toString())
    }

    @Test
    fun evaluateExpression_should_return_JSONObject_evaluated_with_multiple_expressions() {
        // Given
        val context1 = ContextData(
            id = "context1",
            value = RandomData.string()
        )
        val context2 = ContextData(
            id = "context2",
            value = RandomData.string()
        )
        val contextView1 = createViewForContext()
        val contextView2 = createViewForContext(contextView1)
        viewModel.addContext(contextView1, context1)
        viewModel.addContext(contextView2, context2)
        val value = JSONObject().apply {
            put("value1", "@{context1}")
            put("value2", "@{context2}")
        }

        // When
        val actualValue = action.evaluateExpression(rootView, contextView2, value)

        // Then
        assertTrue(actualValue is JSONObject)
        assertEquals("""{"value2":"${context2.value}","value1":"${context1.value}"}""", actualValue.toString())
    }

    @Test
    fun `GIVEN expression with JsonObject and fields it is expression WHEN evaluate expression THEN return correct JsonObject`() {
        // Given
        val context1 = ContextData(
            id = "context1",
            value = 93629893111
        )
        val context2 = ContextData(
            id = "context2",
            value = 93629893130
        )
        val contextView1 = createViewForContext()
        val contextView2 = createViewForContext(contextView1)
        viewModel.addContext(contextView1, context1)
        viewModel.addContext(contextView2, context2)
        val value = JSONObject().apply {
            put("value1", "@{context1}")
            put("value2", "@{context2}")
        }

        // When
        val actualValue = action.evaluateExpression(rootView, contextView2, value)

        // Then
        assertTrue(actualValue is JSONObject)
        assertEquals("""{"value2":${context2.value},"value1":${context1.value}}""", actualValue.toString())
    }

    @Test
    fun evaluateExpression_should_return_evaluated_value_from_implicit_context_and_normal_context() {
        // Given
        val secondAction = mockk<Action>(relaxed = true)
        val bind = expressionOf<String>("Hello @{context} and @{onSuccess.value}")
        val explicitContextValue = RandomData.string()
        val implicitContextValue = RandomData.string()
        val implicitValue = JSONObject().apply {
            put("value", implicitContextValue)
        }
        viewModel.addContext(contextView, ContextData(
            id = "context",
            value = explicitContextValue
        ))
        action.handleEvent(rootView, contextView, secondAction, ContextData("onSuccess", implicitValue))

        // When
        val actualValue = secondAction.evaluateExpression(rootView, bindView, bind)

        // Then
        val expected = "Hello $explicitContextValue and $implicitContextValue"
        assertEquals(expected, actualValue)
    }

    @Test
    fun `GIVEN json array inside an object in context implicit WHEN call evaluate expression THEN show correct text`() {
        // Given
        val secondAction = mockk<Action>(relaxed = true)
        val bind = expressionOf<String>("Hello @{context} and @{onSuccess.data[0].value}")
        val explicitContextValue = RandomData.string()
        val implicitContextValue = RandomData.string()
        val implicitValue = Response(null, JSONArray().apply {
            put(JSONObject().put("value", implicitContextValue))
        })
        viewModel.addContext(contextView, ContextData(
            id = "context",
            value = explicitContextValue
        ))
        action.handleEvent(rootView, contextView, secondAction, ContextData("onSuccess", implicitValue))

        // When
        val actualValue = secondAction.evaluateExpression(rootView, bindView, bind)

        // Then
        val expected = "Hello $explicitContextValue and $implicitContextValue"
        assertEquals(expected, actualValue)
    }

    @Test
    fun evaluateExpression_should_return_evaluated_primitive_value_from_implicit_context() {
        // Given
        val secondAction = mockk<Action>(relaxed = true)
        val bind = expressionOf<Int>("@{onChange}")
        val implicitContextValue = 0
        val context = ContextData(id = "onChange", value = implicitContextValue)
        action.handleEvent(rootView, contextView, secondAction, context)

        // When
        val actualValue = secondAction.evaluateExpression(rootView, bindView, bind)

        // Then
        assertEquals(implicitContextValue, actualValue)
    }

    @Test
    fun
        `GIVEN context implicit WHEN call evaluate expression THEN show correct value with correct type`() {
        // Given
        val secondAction = mockk<Action>(relaxed = true)
        val bind = expressionOf<Int>("@{onChange}")
        val implicitContextValue = 99999
        val context = ContextData(id = "onChange", value = implicitContextValue)
        action.handleEvent(rootView, contextView, secondAction, context)

        // When
        val actualValue = secondAction.evaluateExpression(rootView, bindView, bind)

        // Then
        assertEquals(implicitContextValue, actualValue)
    }
}
