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
import br.com.zup.beagle.android.testutil.RandomData
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a Moshi Adapter")
class ContextDataSerializationTest : BaseContextSerializationTest() {

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
            val contextData = moshi.adapter(ContextData::class.java).fromJson(contextDataJson)!!

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
            val contextData = moshi.adapter(ContextData::class.java).fromJson(contextDataJson)!!

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
            // Given
            val expectedContextData = makeObjectContextWithPrimitiveValue()
            val contextDataJson = makeContextWithPrimitiveValueJson()

            // When
            val contextData = moshi.adapter(ContextData::class.java).fromJson(contextDataJson)!!

            // Then
            Assertions.assertNotNull(contextData)
            Assertions.assertEquals(expectedContextData, contextData)
        }

        @DisplayName("Then should return correct object with integer value")
        @Test
        fun testDeserializeJsonContextDataWithIntegerValue() {
            // Given
            val testInt = 1
            val expectedContextData = makeObjectContextWithNumber(testInt)
            val contextDataJson = makeContextWithNumberJson(testInt)

            // When
            val contextData = moshi.adapter(ContextData::class.java).fromJson(contextDataJson)!!

            // Then
            Assertions.assertNotNull(contextData)
            Assertions.assertEquals(expectedContextData, contextData)
        }

        @DisplayName("Then should return correct object with double value")
        @Test
        fun testDeserializeJsonContextDataWithDoubleValue() {
            // Given
            val testDouble = 1.5
            val expectedContextData = makeObjectContextWithNumber(testDouble)
            val contextDataJson = makeContextWithNumberJson(testDouble)

            // When
            val contextData = moshi.adapter(ContextData::class.java).fromJson(contextDataJson)!!

            // Then
            Assertions.assertNotNull(contextData)
            Assertions.assertEquals(expectedContextData, contextData)
        }
    }

    @DisplayName("When try serialize object ContextData")
    @Nested
    inner class SerializeObjectContextDataTest {
        @DisplayName("Then should return correct json with JSONObject")
        @Test
        fun testSerializeJsonContextDataWithJsonObject() {
            // Given
            val expectedJson = makeContextWithJsonObjectJson().replace("\\s".toRegex(), "")
            val contextData = makeObjectContextWithJsonObject()

            // When
            val actual = moshi.adapter(ContextData::class.java).toJson(contextData)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }

        @DisplayName("Then should return correct json with JSONArray")
        @Test
        fun testSerializeJsonContextDataWithJsonArray() {
            // Given
            val expectedJson = makeContextWithJsonArrayJson().replace("\\s".toRegex(), "")
            val contextData = makeObjectContextWithJsonArray()

            // When
            val actual = moshi.adapter(ContextData::class.java).toJson(contextData)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }

        @DisplayName("Then should return correct json with primitive value")
        @Test
        fun testSerializeJsonContextDataWithPrimitiveValue() {
            // Given
            val expectedJson = makeContextWithPrimitiveValueJson().replace("\\s".toRegex(), "")
            val contextData = makeObjectContextWithPrimitiveValue()

            // When
            val actual = moshi.adapter(ContextData::class.java).toJson(contextData)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }

        @DisplayName("Then should return correct json with integer value")
        @Test
        fun testSerializeJsonContextDataWithIntegerValue() {
            // Given
            val testInt = 1
            val expectedJson = makeContextWithNumberJson(testInt).replace("\\s".toRegex(), "")
            val contextData = makeObjectContextWithNumber(testInt)

            // When
            val actual = moshi.adapter(ContextData::class.java).toJson(contextData)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }

        @DisplayName("Then should return correct json with double value")
        @Test
        fun testSerializeJsonContextDataWithDoubleValue() {
            // Given
            val testDouble = 1.5
            val expectedJson = makeContextWithNumberJson(testDouble).replace("\\s".toRegex(), "")
            val contextData = makeObjectContextWithNumber(testDouble)

            // When
            val actual = moshi.adapter(ContextData::class.java).toJson(contextData)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
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
            val toJson = moshi.adapter(ContextData::class.java).toJson(contextData)
            val fromJson = moshi.adapter(ContextData::class.java).fromJson(toJson)

            // Then
            Assertions.assertEquals(contextData, fromJson)
        }

        @DisplayName("Then should return a ContextData with correct integer value")
        @Test
        fun serializeAndSeserializeContextDataWithIntegerTest() {
            // Given
            val contextData = ContextData(
                id = RandomData.string(),
                value = 5
            )

            // When
            val toJson = moshi.adapter(ContextData::class.java).toJson(contextData)
            val fromJson = moshi.adapter(ContextData::class.java).fromJson(toJson)

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