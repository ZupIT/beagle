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

    private val contextData = ContextData(CONTEXT_ID, JSONObject(JSON))

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
        // Given When
        val value = contextData.getValue("i")

        // Then
        assertNull(value)
    }

    @Test
    fun getValue_should_return_null_when_value_is_does_not_exists() {
        // Given When
        val value = contextData.getValue("z")

        // Then
        assertNull(value)
    }

    @Test
    fun getValue_should_return_String() {
        // Given When
        val value = contextData.getValue("a")

        // Then
        assertTrue(value is String)
        assertEquals(STRING, value)
    }

    @Test
    fun getValue_should_return_Boolean() {
        // Given When
        val value = contextData.getValue("b.c.d")

        // Then
        assertTrue(value is Boolean)
        assertEquals(BOOLEAN, value)
    }

    @Test
    fun getValue_should_return_Long() {
        // Given When
        val value = contextData.getValue("b.c.e")

        // Then
        assertTrue(value is Long)
        assertEquals(LONG, value)
    }

    @Test
    fun getValue_should_return_Int() {
        // Given When
        val value = contextData.getValue("b.c.f")

        // Then
        assertTrue(value is Int)
        assertEquals(INT, value)
    }

    @Test
    fun getValue_should_return_JSONObject() {
        // Given When
        val value = contextData.getValue("b.c.g")

        // Then
        assertTrue(value is JSONObject)
        assertEquals(JSON_OBJECT, value.toString())
    }

    @Test
    fun getValue_should_throw_exception_when_trying_to_access_object_position_with_array() {
        assertFails {
            contextData.getValue("b.c[0]")
        }
    }

    @Test
    fun getValue_should_throw_exception_when_trying_to_pass_invalid_array_position() {
        assertFails {
            contextData.getValue("b.h[]")
        }
    }

    @Test
    fun getValue_should_throw_exception_when_trying_to_access_invalid_array_position() {
        assertFails {
            contextData.getValue("b.h[3]")
        }
    }

    @Test
    fun getValue_should_return_Double_in_array_position_0() {
        // Given When
        val value = contextData.getValue("b.h[0].a")

        // Then
        assertTrue(value is Double)
        assertEquals(DOUBLE, value)
    }

    @Test
    fun getValue_should_return_Double_in_array_position_1() {
        // Given When
        val value = contextData.getValue("b.h[1].a")

        // Then
        assertTrue(value is Double)
        assertEquals(DOUBLE, value)
    }

    @Test
    fun getValue_should_return_JSON_inside_array_position_0() {
        // Given When
        val value = contextData.getValue("b.h[0]")

        // Then
        assertEquals(JSON_OBJECT, value.toString())
    }

    /*
     * getValue Tests
     */

    /*@Test
    fun setValue_should_return_value() {
        // Given
        val contextData = ContextData(CONTEXT_ID, STRING)

        // When
        val value = contextData.setValue(CONTEXT_ID, "")

        // Then
        assertEquals(STRING, value)
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
    fun setValue_should_return_false_when_key_does_not_exist() {
        // Given When
        val value = contextData.setValue("z", "")

        // Then
        assertFalse(value)
    }

    @Test
    fun setValue_should_set_value_on_a() {
        // Given
        val newValue = RandomData.string()

        // When
        val result = contextData.setValue("a", newValue)
        val actualValue = contextData.getValue("a")

        // Then
        assertTrue(result)
        assertEquals(newValue, actualValue)
    }

    @Test
    fun setValue_should_set_value_false_on_a_c_d() {
        // Given
        val newValue = false

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

        // When
        val value = contextData.setValue("b.c.g", newValue)
        val actualValue = contextData.getValue("b.c.g")

        // Then
        assertTrue(value)
        assertEquals(newValue, actualValue)
    }

    @Test
    fun setValue_should_throw_exception_when_trying_to_access_object_position_with_array() {
        assertFails {
            contextData.setValue("b.c[0]", JSONObject())
        }
    }

    @Test
    fun setValue_should_throw_exception_when_trying_to_pass_invalid_array_position() {
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

        // When
        val result = contextData.setValue("b.h[0]", jsonObject)
        val actualValue = contextData.getValue("b.h[0]")

        // Then
        assertTrue(result)
        assertEquals(jsonObject, actualValue)
    }*/
}