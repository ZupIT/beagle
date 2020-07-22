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

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.android.engine.renderer.ActivityRootView
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.view.viewmodel.ScreenContextViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ActionExtensionsKtTest : BaseTest() {

    private val rootView = mockk<ActivityRootView>()
    private val action = mockk<Action>(relaxed = true)
    private val originView = mockk<View>()

    private lateinit var viewModel: ScreenContextViewModel

    override fun setUp() {
        super.setUp()

        mockkObject(ViewModelProviderFactory)

        viewModel = ScreenContextViewModel()

        every { rootView.activity } returns mockk()
        every { ViewModelProviderFactory
            .of(any<AppCompatActivity>())
            .get(ScreenContextViewModel::class.java)
        } returns viewModel
    }

    @Test
    fun evaluateExpression_should_evaluate_bind_of_type_String_with_multiple_expressions() {
        // Given
        val bind = expressionOf<String>("Hello @{context1} and @{context2}")
        val contextValue = "hello"
        viewModel.addContext(ContextData(
            id = "context1",
            value = contextValue
        ))
        viewModel.addContext(ContextData(
            id = "context2",
            value = contextValue
        ))

        // When
        val actualValue = action.evaluateExpression(rootView, bind)

        // Then
        val expected = "Hello hello and hello"
        assertEquals(expected, actualValue)
    }

    @Test
    fun evaluateExpression_should_evaluate_bind_of_type_String_with_multiple_expressions_starting_wih_expression() {
        // Given
        val bind = expressionOf<String>("@{context1} and @{context2}")
        val contextValue = "hello"
        viewModel.addContext(ContextData(
            id = "context1",
            value = contextValue
        ))
        viewModel.addContext(ContextData(
            id = "context2",
            value = contextValue
        ))

        // When
        val actualValue = action.evaluateExpression(rootView, bind)

        // Then
        val expected = "hello and hello"
        assertEquals(expected, actualValue)
    }

    @Test
    fun evaluateExpression_should_evaluate_bind_of_type_double() {
        // Given
        val bind = expressionOf<Double>("@{context}")
        val contextValue = RandomData.double()
        viewModel.addContext(ContextData(
            id = "context",
            value = contextValue
        ))
        // When
        val actualValue = action.evaluateExpression(rootView, bind)

        // Then
        assertEquals(contextValue, actualValue)
    }

    @Test
    fun evaluateExpression_should_return_the_actual_double_value() {
        // Given
        val value = RandomData.double()

        // When
        val actualValue = action.evaluateExpression(rootView, value)

        // Then
        assertEquals(value, actualValue)
    }

    @Test
    fun evaluateExpression_should_return_the_actual_boolean_value() {
        // Given
        val value = RandomData.boolean()

        // When
        val actualValue = action.evaluateExpression(rootView, value)

        // Then
        assertEquals(value, actualValue)
    }

    @Test
    fun evaluateExpression_should_return_the_actual_int_value() {
        // Given
        val value = RandomData.int()

        // When
        val actualValue = action.evaluateExpression(rootView, value)

        // Then
        assertEquals(value, actualValue)
    }

    @Test
    fun evaluateExpression_should_return_the_actual_Float_value() {
        // Given
        val value = RandomData.float()

        // When
        val actualValue = action.evaluateExpression(rootView, value)

        // Then
        assertEquals(value, actualValue)
    }

    @Test
    fun evaluateExpression_should_return_the_actual_string_value() {
        // Given
        val value = RandomData.string()

        // When
        val actualValue = action.evaluateExpression(rootView, value)

        // Then
        assertEquals(value, actualValue)
    }

    @Test
    fun evaluateExpression_should_evaluate_expression_of_type_int() {
        // Given
        val contextValue = 0
        viewModel.addContext(ContextData(
            id = "context",
            value = contextValue
        ))
        val value = "@{context}"

        // When
        val actualValue = action.evaluateExpression(rootView, value)

        // Then
        assertEquals(contextValue, actualValue as Int)
    }

    @Test
    fun evaluateExpression_should_evaluate_expression_of_type_double() {
        // Given
        val contextValue = 1.0
        viewModel.addContext(ContextData(
            id = "context",
            value = contextValue
        ))
        val value = "@{context}"

        // When
        val actualValue = action.evaluateExpression(rootView, value)

        // Then
        assertEquals(contextValue, actualValue as Double)
    }

    @Test
    fun evaluateExpression_should_evaluate_expression_of_type_boolean() {
        // Given
        val contextValue = true
        viewModel.addContext(ContextData(
            id = "context",
            value = contextValue
        ))
        val value = "@{context}"

        // When
        val actualValue = action.evaluateExpression(rootView, value)

        // Then
        assertEquals(contextValue, actualValue as Boolean)
    }

    @Test
    fun evaluateExpression_should_evaluate_expression_of_type_string() {
        // Given
        val contextValue = "hello"
        viewModel.addContext(ContextData(
            id = "context",
            value = contextValue
        ))
        val value = "@{context}"

        // When
        val actualValue = action.evaluateExpression(rootView, value)

        // Then
        assertEquals(contextValue, actualValue)
    }

    @Test
    fun evaluateExpression_should_return_JSON_string_evaluated() {
        // Given
        val contextValue = "hello"
        viewModel.addContext(ContextData(
            id = "context",
            value = contextValue
        ))
        val value = """{"value": "@{context}""""

        // When
        val actualValue = action.evaluateExpression(rootView, value)

        // Then
        val expected = """{"value": "$contextValue""""
        assertEquals(expected, actualValue)
    }

    @Test
    fun evaluateExpression_should_return_JSONArray_evaluated() {
        // Given
        val contextValue = "hello"
        viewModel.addContext(ContextData(
            id = "context",
            value = contextValue
        ))
        val value = JSONArray().apply {
            put("@{context}")
        }

        // When
        val actualValue = action.evaluateExpression(rootView, value)

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
        viewModel.addContext(ContextData(
            id = "context",
            value = contextValue
        ))

        // When
        val actualValue = action.evaluateExpression(rootView, "@{context}")

        // Then
        assertTrue(actualValue is JSONArray)
        assertEquals("""["hello"]""", actualValue.toString())
    }

    @Test
    fun evaluateExpression_should_return_JSONArray_evaluated_with_multiple_expressions() {
        // Given
        val contextValue = "hello"
        viewModel.addContext(ContextData(
            id = "context1",
            value = contextValue
        ))
        viewModel.addContext(ContextData(
            id = "context2",
            value = contextValue
        ))
        val value = JSONArray().apply {
            put("@{context1}")
            put("@{context2}")
        }

        // When
        val actualValue = action.evaluateExpression(rootView, value)

        // Then
        assertTrue(actualValue is JSONArray)
        assertEquals("""["$contextValue","$contextValue"]""", actualValue.toString())
    }

    @Test
    fun evaluateExpression_should_return_JSONObject_evaluated() {
        // Given
        val contextValue = "hello"
        viewModel.addContext(ContextData(
            id = "context",
            value = contextValue
        ))
        val value = JSONObject().apply {
            put("value", "@{context}")
        }

        // When
        val actualValue = action.evaluateExpression(rootView, value)

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
        viewModel.addContext(ContextData(
            id = "context",
            value = value
        ))

        // When
        val actualValue = action.evaluateExpression(rootView, "@{context}")

        // Then
        assertTrue(actualValue is JSONObject)
        assertEquals("""{"value":"hello"}""", actualValue.toString())
    }

    @Test
    fun evaluateExpression_should_return_JSONObject_evaluated_with_multiple_expressions() {
        // Given
        val contextValue = "hello"
        viewModel.addContext(ContextData(
            id = "context1",
            value = contextValue
        ))
        viewModel.addContext(ContextData(
            id = "context2",
            value = contextValue
        ))
        val value = JSONObject().apply {
            put("value1", "@{context1}")
            put("value2", "@{context2}")
        }

        // When
        val actualValue = action.evaluateExpression(rootView, value)

        // Then
        assertTrue(actualValue is JSONObject)
        assertEquals("""{"value2":"$contextValue","value1":"$contextValue"}""", actualValue.toString())
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
        viewModel.addContext(ContextData(
            id = "context",
            value = explicitContextValue
        ))
        action.handleEvent(rootView, originView, secondAction, ContextData("onSuccess", implicitValue))

        // When
        val actualValue = secondAction.evaluateExpression(rootView, bind)

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
        action.handleEvent(rootView, originView, secondAction, "onChange", implicitContextValue)

        // When
        val actualValue = secondAction.evaluateExpression(rootView, bind)

        // Then
        assertEquals(implicitContextValue, actualValue)
    }
}