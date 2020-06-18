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

package br.com.zup.beagle.android.jsonpath

import br.com.zup.beagle.android.testutil.RandomData
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

class JsonPathReplacerTest {

    private val jsonPathReplacer = JsonPathReplacer()
    private val jsonObject = JSONObject(JSON)

    @Test
    fun setValue_should_add_new_key_when_does_not_exist() {
        // Given
        val keys = JsonPathUtils.splitKeys("z")
        val newValue = RandomData.string()

        // When
        val result = jsonPathReplacer.replace(keys, newValue, jsonObject)

        // Then
        assertTrue(result)
        assertEquals(newValue, jsonObject.get("z"))
    }

    @Test
    fun setValue_should_set_value_on_a() {
        // Given
        val keys = JsonPathUtils.splitKeys("a")
        val newValue = RandomData.string()

        // When
        val result = jsonPathReplacer.replace(keys, newValue, jsonObject)

        // Then
        assertTrue(result)
        assertEquals(newValue, jsonObject.get("a"))
    }

    @Test
    fun setValue_should_set_value_false_on_a_c_d() {
        // Given
        val keys = JsonPathUtils.splitKeys("b.c.d")
        val newValue = false

        // When
        val result = jsonPathReplacer.replace(keys, newValue, jsonObject)

        // Then
        assertTrue(result)
        assertEquals(newValue, jsonObject.getJSONObject("b").getJSONObject("c").getBoolean("d"))
    }

    @Test
    fun setValue_should_set_new_JSONObject() {
        // Given
        val keys = JsonPathUtils.splitKeys("b.c.g")
        val doubleValue = RandomData.double()
        val newValue = JSONObject().apply {
            put("a", doubleValue)
        }

        // When
        val result = jsonPathReplacer.replace(keys, newValue, jsonObject)

        // Then
        assertTrue(result)
        assertEquals(newValue, jsonObject.getJSONObject("b").getJSONObject("c").getJSONObject("g"))
    }

    @Test
    fun setValue_should_throw_exception_when_trying_to_access_object_position_with_array() {
        val keys = JsonPathUtils.splitKeys("b.c[0]")

        assertFails {
            jsonPathReplacer.replace(keys, JSONObject(), jsonObject)
        }
    }

    @Test
    fun setValue_should_throw_exception_when_trying_to_pass_invalid_array_position() {
        val keys = JsonPathUtils.splitKeys("b.h[]")

        assertFails {
            jsonPathReplacer.replace(keys, JSONObject(), jsonObject)
        }
    }

    @Test
    fun setValue_should_throw_exception_when_trying_to_access_invalid_array_position() {
        // Given
        val keys = JsonPathUtils.splitKeys("b.h[3]")
        val newJsonObject = JSONObject().apply {
            put("a", RandomData.double())
        }

        // When
        val result = jsonPathReplacer.replace(keys, newJsonObject, jsonObject)

        // Then
        assertTrue(result)
        assertEquals(newJsonObject, jsonObject.getJSONObject("b").getJSONArray("h").getJSONObject(3))
    }

    @Test
    fun setValue_should_set_Double_in_array_position_0() {
        // Given
        val keys = JsonPathUtils.splitKeys("b.h[0].a")
        val newValue = RandomData.double()

        // When
        val result = jsonPathReplacer.replace(keys, newValue, jsonObject)

        // Then
        assertTrue(result)
        val actualValue = jsonObject.getJSONObject("b")
            .getJSONArray("h")
            .getJSONObject(0)
            .getDouble("a")
        assertEquals(newValue, actualValue, 0.0)
    }

    @Test
    fun setValue_should_set_Double_in_array_position_1() {
        // Given
        val keys = JsonPathUtils.splitKeys("b.h[1].a")
        val newValue = RandomData.double()

        // When
        val result = jsonPathReplacer.replace(keys, newValue, jsonObject)

        // Then
        assertTrue(result)
        val actualValue = jsonObject.getJSONObject("b")
            .getJSONArray("h")
            .getJSONObject(1)
            .getDouble("a")
        assertEquals(newValue, actualValue, 0.0)
    }

    @Test
    fun setValue_should_set_JSON_inside_array_position_0() {
        // Given
        val keys = JsonPathUtils.splitKeys("b.h[0]")
        val newJsonObject = JSONObject().apply {
            put("a", RandomData.double())
        }

        // When
        val result = jsonPathReplacer.replace(keys, newJsonObject, jsonObject)

        // Then
        assertTrue(result)
        val actualValue = jsonObject.getJSONObject("b").getJSONArray("h").getJSONObject(0)
        assertEquals(newJsonObject, actualValue)
    }

    @Test
    fun setValue_should_set_new_element_at_value_array() {
        // Given
        val keys = JsonPathUtils.splitKeys("[1]")
        val newValue = "hello2"
        val jsonArray = JSONArray().apply {
            put("hello")
        }

        // When
        val result = jsonPathReplacer.replace(keys, newValue, jsonArray)

        // Then
        assertTrue(result)
        assertEquals(newValue, jsonArray.getString(1))
    }

    @Test
    fun setValue_should_add_new_array_object_with_value_when_does_not_exist() {
        // Given
        val keys = JsonPathUtils.splitKeys("[0][0].a")
        val json = """
            [
                [
                    {
                        "a": "hello"
                    }
                ]
            ]
        """.trimIndent()
        val newValue = "hello2"
        val jsonArray = JSONArray(json)

        // When
        val result = jsonPathReplacer.replace(keys, newValue, jsonArray)

        // Then
        assertTrue(result)
        assertEquals(newValue, jsonArray.getJSONArray(0).getJSONObject(0).getString("a"))
    }
}