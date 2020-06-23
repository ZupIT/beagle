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

class JsonCreateTreeTest {

    private val jsonObject = JSONObject(JSON)

    @Test
    fun `should create sequence of array when not existing`() {
        val root = JSONArray()
        JsonCreateTree().walkingTreeAndFindKey(root,
            JsonPathUtils.splitKeys("[0].[1].[0].[2].[3]"), JSONObject.NULL)

        assertEquals(COMPLEX_JSON_ARRAY_WITH_NO_VALUES.toString(), root.toString())
    }

    @Test
    fun `should create sequence of array when an existing a sequence`() {
        val root = COMPLEX_JSON_ARRAY_WITH_VALUES
        JsonCreateTree().walkingTreeAndFindKey(root,
            JsonPathUtils.splitKeys("[0].[1].[0].[2].[3]"), JSONObject.NULL)

        assertEquals(COMPLEX_JSON_ARRAY_WITH_VALUES_RESULT.toString(), root.toString())
    }

    @Test
    fun `should create sequence of object when not existing sequence`() {
        val root = JSONObject()
        JsonCreateTree().walkingTreeAndFindKey(root,
            JsonPathUtils.splitKeys("address.city.name.city"), JSONObject.NULL)

        assertEquals(COMPLEX_JSON_OBJECT_WITH_NO_VALUES.toString(), root.toString())
    }

    @Test
    fun `should create sequence of object when existing a sequence`() {
        val root = COMPLEX_JSON_OBJECT_WITH_VALUES
        JsonCreateTree().walkingTreeAndFindKey(root,
            JsonPathUtils.splitKeys("address.city.name"), JSONObject.NULL)

        assertEquals(COMPLEX_JSON_OBJECT_WITH_VALUES_RESULT.toString(), root.toString())
    }

    @Test
    fun `should create sequence of object when current tree is array`() {
        val root = JSONObject()
        JsonCreateTree().walkingTreeAndFindKey(root,
            JsonPathUtils.splitKeys("address.city.name.city"), JSONObject.NULL)

        assertEquals(COMPLEX_JSON_OBJECT_WITH_NO_VALUES.toString(), root.toString())
    }

    @Test
    fun `should not change sequence of object when passing the path existing`() {
        val root = COMPLEX_JSON_OBJECT_WITH_ARRAY
        JsonCreateTree().walkingTreeAndFindKey(root,
            JsonPathUtils.splitKeys("context.name[2].d[0].e[5]"), "teste")

        assertEquals(COMPLEX_JSON_OBJECT_WITH_ARRAY.toString(), root.toString())
    }

    @Test
    fun setValue_should_add_new_key_when_does_not_exist() {
        // Given
        val keys = JsonPathUtils.splitKeys("z")
        val newValue = RandomData.string()

        // When
        JsonCreateTree().walkingTreeAndFindKey(jsonObject, keys, newValue)

        // Then
        assertEquals(newValue, jsonObject.get("z"))
    }

    @Test
    fun setValue_should_set_value_on_a() {
        // Given
        val keys = JsonPathUtils.splitKeys("a")
        val newValue = RandomData.string()

        // When
        JsonCreateTree().walkingTreeAndFindKey(jsonObject, keys, newValue)

        // Then
        assertEquals(newValue, jsonObject.get("a"))
    }

    @Test
    fun setValue_should_set_value_false_on_a_c_d() {
        // Given
        val keys = JsonPathUtils.splitKeys("b.c.d")
        val newValue = false

        // When
        JsonCreateTree().walkingTreeAndFindKey(jsonObject, keys, newValue)

        // Then
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
        JsonCreateTree().walkingTreeAndFindKey(jsonObject, keys, newValue)

        // Then
        assertEquals(newValue, jsonObject.getJSONObject("b").getJSONObject("c").getJSONObject("g"))
    }


    @Test
    fun setValue_should_throw_exception_when_trying_to_access_invalid_array_position() {
        // Given
        val keys = JsonPathUtils.splitKeys("b.h[3]")
        val newJsonObject = JSONObject().apply {
            put("a", RandomData.double())
        }

        // When
        JsonCreateTree().walkingTreeAndFindKey(jsonObject, keys, newJsonObject)

        // Then
        assertEquals(newJsonObject, jsonObject.getJSONObject("b").getJSONArray("h").getJSONObject(3))
    }

    @Test
    fun setValue_should_set_Double_in_array_position_0() {
        // Given
        val keys = JsonPathUtils.splitKeys("b.h[0].a")
        val newValue = RandomData.double()

        // When
        val result = JsonCreateTree().walkingTreeAndFindKey(jsonObject, keys, newValue)

        // Then
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
        JsonCreateTree().walkingTreeAndFindKey(jsonObject, keys, newValue)

        // Then
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
        val result = JsonCreateTree().walkingTreeAndFindKey(jsonObject, keys, newJsonObject)

        // Then
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
        JsonCreateTree().walkingTreeAndFindKey(jsonArray, keys, newValue)

        // Then
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
        JsonCreateTree().walkingTreeAndFindKey(jsonArray, keys, newValue)

        // Then
        assertEquals(newValue, jsonArray.getJSONArray(0).getJSONObject(0).getString("a"))
    }
}