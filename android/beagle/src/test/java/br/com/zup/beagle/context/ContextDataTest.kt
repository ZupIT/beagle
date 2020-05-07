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

package br.com.zup.beagle.context

import br.com.zup.beagle.testutil.RandomData
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import kotlin.test.assertFails

const val LONG = Long.MAX_VALUE
const val INT = Int.MAX_VALUE
const val BOOLEAN = true
val DOUBLE = Double.MAX_VALUE
val STRING = RandomData.string()
val JSON_OBJECT = """{"a":$DOUBLE}"""
val CONTEXT_ID = RandomData.string()

val JSON = """
    {
        "a": $STRING,
        "b": {
            "c": {
                "d": $BOOLEAN,
                "e": $LONG,
                "f": $INT,
                "g": $JSON_OBJECT,
            },
            "h": [
                $JSON_OBJECT,
                $JSON_OBJECT
            ]
        },
        "i": null
    }
"""

class ContextDataTest {

    /*
     * getValue Tests
     */

    @Test
    fun getValue_should_return_value() {
        // Given
        val contextData = ContextData(CONTEXT_ID, STRING)

        // When
        val value = contextData.getValue(CONTEXT_ID)

        // Then
        assertEquals(STRING, value)
    }

    @Test
    fun getValue_should_throw_exception_when_contextId_is_not_valid() {
        // Given
        val contextData = ContextData(RandomData.string(), STRING)

        // When Then
        assertFails {
            contextData.getValue(CONTEXT_ID)
        }
    }

    @Test
    fun getValue_should_return_null_when_value_is_null() {
        // Given
        val contextData = makeContextData()

        // When
        val value = contextData.getValue("$CONTEXT_ID.i")

        // Then
        assertNull(value)
    }

    @Test
    fun getValue_should_return_null_when_value_is_does_not_exists() {
        // Given
        val contextData = makeContextData()

        // When
        val value = contextData.getValue("$CONTEXT_ID.z")

        // Then
        assertNull(value)
    }

    @Test
    fun getValue_should_return_String() {
        // Given
        val contextData = makeContextData()

        // When
        val value = contextData.getValue("$CONTEXT_ID.a")

        // Then
        assertTrue(value is String)
        assertEquals(STRING, value)
    }

    @Test
    fun getValue_should_return_Boolean() {
        // Given
        val contextData = makeContextData()

        // When
        val value = contextData.getValue("b.c.d")

        // Then
        assertTrue(value is Boolean)
        assertEquals(BOOLEAN, value)
    }

    @Test
    fun getValue_should_return_Long() {
        // Given
        val contextData = makeContextData()

        // When
        val value = contextData.getValue("b.c.e")

        // Then
        assertTrue(value is Long)
        assertEquals(LONG, value)
    }

    @Test
    fun getValue_should_return_Int() {
        // Given
        val contextData = makeContextData()

        // When
        val value = contextData.getValue("b.c.f")

        // Then
        assertTrue(value is Int)
        assertEquals(INT, value)
    }

    @Test
    fun getValue_should_return_JSONObject() {
        // Given
        val contextData = makeContextData()

        // When
        val value = contextData.getValue("b.c.g")

        // Then
        assertTrue(value is JSONObject)
        assertEquals(JSON_OBJECT, value.toString())
    }

    @Test
    fun getValue_should_throw_exception_when_trying_to_access_object_position_with_array() {
        val contextData = makeContextData()

        assertFails {
            contextData.getValue("b.c[0]")
        }
    }

    @Test
    fun getValue_should_throw_exception_when_trying_to_pass_invalid_array_position() {
        val contextData = makeContextData()

        assertFails {
            contextData.getValue("b.h[]")
        }
    }

    @Test
    fun getValue_should_throw_exception_when_trying_to_access_invalid_array_position() {
        val contextData = makeContextData()

        assertFails {
            contextData.getValue("b.h[3]")
        }
    }

    @Test
    fun getValue_should_return_Double_in_array_position_0() {
        // Given
        val contextData = makeContextData()

        // When
        val value = contextData.getValue("b.h[0].a")

        // Then
        assertTrue(value is Double)
        assertEquals(DOUBLE, value)
    }

    @Test
    fun getValue_should_return_Double_in_array_position_1() {
        // Given
        val contextData = makeContextData()

        // When
        val value = contextData.getValue("b.h[1].a")

        // Then
        assertTrue(value is Double)
        assertEquals(DOUBLE, value)
    }

    @Test
    fun getValue_should_return_JSON_inside_array_position_0() {
        // Given
        val contextData = makeContextData()

        // When
        val value = contextData.getValue("b.h[0]")

        // Then
        assertEquals(JSON_OBJECT, value.toString())
    }

    /*// TODO: implement this case
    @Test
    fun getValue_should_return_JSON_inside_dimensional_array() {
        // Given
        val contextData = makeContextData()

        // When
        val value = contextData.getValue("b.h[0][0]")

        // Then
        assertEquals(JSON_OBJECT, value.toString())
    }*/

    /*
     * getValue Tests
     */

    @Test
    fun setValue_should_set_context_value() {
        // Given
        val newValue = RandomData.string()
        val contextData = ContextData(CONTEXT_ID, STRING)

        // When
        val value = contextData.setValue(CONTEXT_ID, newValue)
        val actualValue = contextData.getValue(CONTEXT_ID)

        // Then
        assertTrue(value)
        assertEquals(newValue, actualValue)
    }

    @Test
    fun setValue_should_throw_exception_when_contextId_is_not_valid() {
        // Given
        val contextData = ContextData(RandomData.string(), STRING)

        // When Then
        assertFails {
            contextData.setValue(CONTEXT_ID, "")
        }
    }

    @Test
    fun setValue_should_add_new_key_when_does_not_exist() {
        // Given
        val newValue = RandomData.string()
        val contextData = makeContextData()

        // When
        val result = contextData.setValue("$CONTEXT_ID.z", newValue)
        val actualValue = contextData.getValue("$CONTEXT_ID.z")

        // Then
        assertTrue(result)
        assertEquals(newValue, actualValue)
    }

    /*// TODO: implement this case
    @Test
    fun setValue_should_add_new_object_with_value_when_does_not_exist() {
        // Given
        val newValue = RandomData.string()
        val contextData = makeContextData()

        // When
        val result = contextData.setValue("$CONTEXT_ID.z1.z2.z3", newValue)
        val actualValue = contextData.getValue("$CONTEXT_ID.z.a")

        // Then
        assertTrue(result)
        assertEquals(newValue, actualValue)
    }

    // TODO: implement this case
    @Test
    fun setValue_should_add_new_array_object_with_value_when_does_not_exist() {
        // Given
        val newValue = RandomData.string()
        val contextData = makeContextData()

        // When
        val result = contextData.setValue("$CONTEXT_ID[0][0].a", newValue)
        val actualValue = contextData.getValue("$CONTEXT_ID[0][0].a")

        // Then
        assertTrue(result)
        assertEquals(newValue, actualValue)
    }*/

    @Test
    fun setValue_should_set_value_on_a() {
        // Given
        val newValue = RandomData.string()
        val contextData = makeContextData()

        // When
        val result = contextData.setValue("$CONTEXT_ID.a", newValue)
        val actualValue = contextData.getValue("$CONTEXT_ID.a")

        // Then
        assertTrue(result)
        assertEquals(newValue, actualValue)
    }

    @Test
    fun setValue_should_set_value_false_on_a_c_d() {
        // Given
        val newValue = false
        val contextData = makeContextData()

        // When
        val value = contextData.setValue("b.c.d", newValue)
        val actualValue = contextData.getValue("b.c.d")

        // Then
        assertTrue(value)
        assertEquals(newValue, actualValue)
    }

    @Test
    fun setValue_should_set_new_JSONObject() {
        // Given
        val doubleValue = RandomData.double()
        val newValue = JSONObject().apply {
            put("a", doubleValue)
        }
        val contextData = makeContextData()

        // When
        val value = contextData.setValue("b.c.g", newValue)
        val actualValue = contextData.getValue("b.c.g")

        // Then
        assertTrue(value)
        assertEquals(newValue, actualValue)
    }

    @Test
    fun setValue_should_throw_exception_when_trying_to_access_object_position_with_array() {
        val contextData = makeContextData()

        assertFails {
            contextData.setValue("b.c[0]", JSONObject())
        }
    }

    @Test
    fun setValue_should_throw_exception_when_trying_to_pass_invalid_array_position() {
        val contextData = makeContextData()

        assertFails {
            contextData.setValue("b.h[]", JSONObject())
        }
    }

    @Test
    fun setValue_should_throw_exception_when_trying_to_access_invalid_array_position() {
        // Given
        val jsonObject = JSONObject().apply {
            put("a", RandomData.double())
        }
        val contextData = makeContextData()

        // When
        val result = contextData.setValue("b.h[3]", jsonObject)
        val actualValue = contextData.getValue("b.h[3]")

        // Then
        assertTrue(result)
        assertEquals(jsonObject, actualValue)
    }

    @Test
    fun setValue_should_set_Double_in_array_position_0() {
        // Given
        val newValue = RandomData.double()
        val contextData = makeContextData()

        // When
        val value = contextData.setValue("b.h[0].a", newValue)
        val actualValue = contextData.getValue("b.h[0].a")

        // Then
        assertTrue(value)
        assertEquals(newValue, actualValue)
    }

    @Test
    fun setValue_should_set_Double_in_array_position_1() {
        // Given
        val newValue = RandomData.double()
        val contextData = makeContextData()

        // When
        val value = contextData.setValue("b.h[1].a", newValue)
        val actualValue = contextData.getValue("b.h[1].a")

        // Then
        assertTrue(value)
        assertEquals(newValue, actualValue)
    }

    @Test
    fun setValue_should_set_JSON_inside_array_position_0() {
        // Given
        val jsonObject = JSONObject().apply {
            put("a", RandomData.double())
        }
        val contextData = makeContextData()

        // When
        val result = contextData.setValue("b.h[0]", jsonObject)
        val actualValue = contextData.getValue("b.h[0]")

        // Then
        assertTrue(result)
        assertEquals(jsonObject, actualValue)
    }

    @Test
    fun setValue_should_set_new_element_at_value_array() {
        // Given
        val jsonObject = JSONArray().apply {
            put("hello")
        }
        val contextData = makeContextData(value = jsonObject)

        // When
        val result = contextData.setValue("$CONTEXT_ID[1]", "hello2")
        val actualValue = contextData.getValue("$CONTEXT_ID[1]")

        // Then
        assertTrue(result)
        assertEquals("hello2", actualValue)
    }

    private fun makeContextData(value: Any = JSONObject(JSON)): ContextData {
        return ContextData(CONTEXT_ID, value)
    }
}