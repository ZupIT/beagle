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

package br.com.zup.beagle.jsonpath

import br.com.zup.beagle.testutil.RandomData
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Test

import org.junit.Assert.*
import kotlin.test.assertFails

private const val LONG = Long.MAX_VALUE
private const val INT = Int.MAX_VALUE
private const val BOOLEAN = true
private val DOUBLE = Double.MAX_VALUE
private val STRING = RandomData.string()
private val JSON_OBJECT = """{"a":$DOUBLE}"""

private val JSON = """
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

class JsonPathFinderTest {

    private val jsonPathFinder = JsonPathFinder()
    private val jsonObject = JSONObject(JSON)

    @Test
    fun find_should_return_null_when_value_is_null() {
        // Given
        val keys = JsonPathUtils.splitKeys("i")

        // When
        val result = jsonPathFinder.findByPath(keys, jsonObject)

        // Then
        assertNull(result)
    }

    @Test
    fun find_should_return_null_when_value_is_does_not_exists() {
        // Given
        val keys = JsonPathUtils.splitKeys("z")

        // When
        val result = jsonPathFinder.findByPath(keys, jsonObject)

        // Then
        assertNull(result)
    }

    @Test
    fun find_should_return_String() {
        // Given
        val keys = JsonPathUtils.splitKeys("a")

        // When
        val result = jsonPathFinder.findByPath(keys, jsonObject)

        // Then
        assertTrue(result is String)
        assertEquals(STRING, result)
    }

    @Test
    fun find_should_return_Boolean() {
        // Given
        val keys = JsonPathUtils.splitKeys("b.c.d")

        // When
        val result = jsonPathFinder.findByPath(keys, jsonObject)

        // Then
        assertTrue(result is Boolean)
        assertEquals(BOOLEAN, result)
    }

    @Test
    fun find_should_return_Long() {
        // Given
        val keys = JsonPathUtils.splitKeys("b.c.e")

        // When
        val result = jsonPathFinder.findByPath(keys, jsonObject)

        // Then
        assertTrue(result is Long)
        assertEquals(LONG, result)
    }

    @Test
    fun find_should_return_Int() {
        // Given
        val keys = JsonPathUtils.splitKeys("b.c.f")

        // When
        val result = jsonPathFinder.findByPath(keys, jsonObject)

        // Then
        assertTrue(result is Int)
        assertEquals(INT, result)
    }

    @Test
    fun find_should_return_JSONObject() {
        // Given
        val keys = JsonPathUtils.splitKeys("b.c.g")

        // When
        val result = jsonPathFinder.findByPath(keys, jsonObject)

        // Then
        assertTrue(result is JSONObject)
        assertEquals(JSON_OBJECT, result.toString())
    }

    @Test
    fun find_should_throw_exception_when_trying_to_access_object_position_with_array() {
        val keys = JsonPathUtils.splitKeys("b.c[0]")

        assertFails {
            jsonPathFinder.findByPath(keys, jsonObject)
        }
    }

    @Test
    fun find_should_throw_exception_when_trying_to_pass_invalid_array_position() {
        val keys = JsonPathUtils.splitKeys("b.h[]")


        assertFails {
            jsonPathFinder.findByPath(keys, jsonObject)
        }
    }

    @Test
    fun find_should_throw_exception_when_trying_to_access_invalid_array_position() {
        val keys = JsonPathUtils.splitKeys("b.h[3]")

        assertFails {
            jsonPathFinder.findByPath(keys, jsonObject)
        }
    }

    @Test
    fun find_should_return_Double_in_array_position_0() {
        // Given
        val keys = JsonPathUtils.splitKeys("b.h[0].a")

        // When
        val result = jsonPathFinder.findByPath(keys, jsonObject)

        // Then
        assertTrue(result is Double)
        assertEquals(DOUBLE, result)
    }

    @Test
    fun find_should_return_Double_in_array_position_1() {
        // Given
        val keys = JsonPathUtils.splitKeys("b.h[1].a")

        // When
        val result = jsonPathFinder.findByPath(keys, jsonObject)

        // Then
        assertTrue(result is Double)
        assertEquals(DOUBLE, result)
    }

    @Test
    fun find_should_return_JSON_inside_array_position_0() {
        // Given
        val keys = JsonPathUtils.splitKeys("b.h[0]")

        // When
        val result = jsonPathFinder.findByPath(keys, jsonObject)

        // Then
        assertEquals(JSON_OBJECT, result.toString())
    }

    @Test
    fun find_should_return_value_inside_array_position_0() {
        // Given
        val keys = JsonPathUtils.splitKeys("[0]")
        val jsonArray = JSONArray().apply {
            put("a")
            put("b")
        }

        // When
        val result = jsonPathFinder.findByPath(keys, jsonArray)

        // Then
        assertEquals("a", result.toString())
    }

    @Test
    fun find_should_return_JSON_inside_dimensional_array() {
        // Given
        val keys = JsonPathUtils.splitKeys("a[0][1]")
        val json = """
        {
            "a": [
                ["a", "b"],
                ["c", "d"]
            ]
        }
        """.trimMargin()
        val jsonObject = JSONObject(json)

        // When
        val result = jsonPathFinder.findByPath(keys, jsonObject)

        // Then
        assertEquals("b", result)
    }
}