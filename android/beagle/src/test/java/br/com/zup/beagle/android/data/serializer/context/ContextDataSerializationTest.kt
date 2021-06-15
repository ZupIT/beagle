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

package br.com.zup.beagle.android.data.serializer.context

import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.data.serializer.BaseSerializerTest
import br.com.zup.beagle.android.testutil.RandomData
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a Moshi Adapter")
class ContextDataSerializationTest : BaseSerializerTest<ContextData>(ContextData::class.java) {

    @DisplayName("When try to deserialize json ContextData")
    @Nested
    inner class DeserializeJsonContextDataTest {

        @DisplayName("Then should return correct object with JSONObject")
        @Test
        fun testDeserializeJsonContextDataWithJsonObject() {
            // Given
            val expectedContextData = makeObjectContextWithJsonObject()
            val contextDataJson = makeContextWithJsonObjectJson()

            // When
            val contextData = deserialize(contextDataJson)!!

            // Then
            Assertions.assertNotNull(contextData)
            Assertions.assertEquals(expectedContextData.id, contextData.id)
            val expectedValue = expectedContextData.value as JSONObject
            val actualValue = contextData.value as JSONObject
            Assertions.assertEquals(expectedValue.getBoolean("a"), actualValue.getBoolean("a"))
            Assertions.assertEquals(expectedValue.getString("b"), actualValue.getString("b"))
        }

        @DisplayName("Then should return correct object with JSONArray")
        @Test
        fun testDeserializeJsonContextDataWithJsonArray() {
            // Given
            val expectedContextData = makeObjectContextWithJsonArray()
            val expectedArray = expectedContextData.value as JSONArray
            val expectedValue = expectedArray.getJSONObject(0)
            val contextDataJson = makeContextWithJsonArrayJson()

            // When
            val contextData = deserialize(contextDataJson)!!

            // Then
            Assertions.assertNotNull(contextData)
            Assertions.assertEquals(expectedContextData.id, contextData.id)
            val actualArray = contextData.value as JSONArray
            val actualValue = actualArray.getJSONObject(0)
            Assertions.assertEquals(expectedValue.getBoolean("a"), actualValue.getBoolean("a"))
            Assertions.assertEquals(expectedValue.getString("b"), actualValue.getString("b"))
        }

        @DisplayName("Then should return correct object with primitive value")
        @Test
        fun testDeserializeJsonContextDataWithPrimitiveValue() {
            testDeserializeJson(
                makeContextWithPrimitiveValueJson(),
                makeObjectContextWithPrimitiveValue()
            )
        }

        @DisplayName("Then should return correct object with integer value")
        @Test
        fun testDeserializeJsonContextDataWithIntegerValue() {
            // Given
            val testInt = 1

            testDeserializeJson(
                makeContextWithNumberJson(testInt),
                makeObjectContextWithNumber(testInt)
            )
        }

        @DisplayName("Then should return correct object with double value")
        @Test
        fun testDeserializeJsonContextDataWithDoubleValue() {
            // Given
            val testDouble = 1.5

            testDeserializeJson(
                makeContextWithNumberJson(testDouble),
                makeObjectContextWithNumber(testDouble)
            )
        }
    }

    @DisplayName("When try serialize object ContextData")
    @Nested
    inner class SerializeObjectContextDataTest {
        @DisplayName("Then should return correct json with JSONObject")
        @Test
        fun testSerializeJsonContextDataWithJsonObject() {
            testSerializeObject(
                makeContextWithJsonObjectJson(),
                makeObjectContextWithJsonObject()
            )
        }

        @DisplayName("Then should return correct json with JSONArray")
        @Test
        fun testSerializeJsonContextDataWithJsonArray() {
            testSerializeObject(
                makeContextWithJsonArrayJson(),
                makeObjectContextWithJsonArray()
            )
        }

        @DisplayName("Then should return correct json with primitive value")
        @Test
        fun testSerializeJsonContextDataWithPrimitiveValue() {
            testSerializeObject(
                makeContextWithPrimitiveValueJson(),
                makeObjectContextWithPrimitiveValue()
            )
        }

        @DisplayName("Then should return correct json with integer value")
        @Test
        fun testSerializeJsonContextDataWithIntegerValue() {
            // Given
            val testInt = 1
            testSerializeObject(
                makeContextWithNumberJson(testInt),
                makeObjectContextWithNumber(testInt)
            )
        }

        @DisplayName("Then should return correct json with double value")
        @Test
        fun testSerializeJsonContextDataWithDoubleValue() {
            // Given
            val testDouble = 1.5
            testSerializeObject(
                makeContextWithNumberJson(testDouble),
                makeObjectContextWithNumber(testDouble)
            )
        }
    }

    @DisplayName("When try serialize and deserialize object ContextData")
    @Nested
    inner class SerializeAndDeserializeObjectContextDataTest {

        @DisplayName("Then should return a ContextData with correct double value")
        @Test
        fun serializeAndDeserializeContextDataWithDoubleTest() {
            // Given
            val contextData = ContextData(
                id = RandomData.string(),
                value = 4.7
            )

            // When
            val toJson = serialize(contextData)
            val fromJson = deserialize(toJson)

            // Then
            Assertions.assertEquals(contextData, fromJson)
        }

        @DisplayName("Then should return a ContextData with correct integer value")
        @Test
        fun serializeAndDeserializeContextDataWithIntegerTest() {
            // Given
            val contextData = ContextData(
                id = RandomData.string(),
                value = 5
            )

            // When
            val toJson = serialize(contextData)
            val fromJson = deserialize(toJson)

            // Then
            Assertions.assertEquals(contextData, fromJson)
        }
    }

    private fun makeContextWithJsonObjectJson() = """
    {
        "id": "contextId",
        "value": {
            "a": true,
            "b": "a"
        }
    }
"""

    private fun makeContextWithJsonArrayJson() = """
    {
        "id": "contextId",
        "value": [
            {
                "a": true,
                "b": "a"
            }
        ]
    }
"""

    private fun makeContextWithPrimitiveValueJson() = """
    {
        "id": "contextId",
        "value": true
    }
"""

    private fun makeContextWithNumberJson(number: Number) = """
    {
        "id": "contextId",
        "value": $number
    }
"""

    private fun makeObjectContextWithJsonObject() = ContextData(
        id = "contextId",
        value = JSONObject()
            .put("a", true)
            .put("b", "a")
    )

    private fun makeObjectContextWithJsonArray() = ContextData(
        id = "contextId",
        value = JSONArray()
            .put(
                JSONObject()
                    .put("a", true)
                    .put("b", "a")
            )
    )

    private fun makeObjectContextWithPrimitiveValue() = ContextData(
        id = "contextId",
        value = true
    )

    private fun makeObjectContextWithNumber(number: Number) = ContextData(
        id = "contextId",
        value = number
    )
}