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

package br.com.zup.beagle.android.data.serializer.actions

import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.action.HttpAdditionalData
import br.com.zup.beagle.android.action.Navigate
import br.com.zup.beagle.android.action.Route
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.android.networking.HttpMethod
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a Moshi Adapter")
class ResetStackSerializationTest : BaseActionSerializationTest() {
    @DisplayName("When try to deserialize json ResetStack")
    @Nested
    inner class DeserializeJsonResetStackTest {

        @DisplayName("Then should return correct object")
        @Test
        fun testDeserializeJsonResetStack() {
            // Given
            val expectedAction = makeObjectResetStack()
            val json = makeResetStackJson()

            // When
            val actual = moshi.adapter(Action::class.java).fromJson(json)

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedAction, actual)
        }

        @DisplayName("Then should return correct object with expression")
        @Test
        fun testDeserializeJsonResetStackWithExpression() {
            // Given
            val expectedAction = makeObjectResetStackWithExpression()
            val json = makeResetStackJsonWithExpression()

            // When
            val actual = moshi.adapter(Navigate.ResetStack::class.java).fromJson(json)!!

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedAction, actual)
        }

        @DisplayName("Then should return correct object with hardcoded url")
        @Test
        fun testDeserializeJsonResetStackWithHardcodedUrl() {
            // Given
            val expectedAction = makeObjectResetStackWithHardcodedUrl()
            val json = makeResetStackJsonWithHardcodedUrl()

            // When
            val actual = moshi.adapter(Navigate.ResetStack::class.java).fromJson(json)!!

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedAction, actual)
        }
    }

    @DisplayName("When try serialize object ResetStack")
    @Nested
    inner class SerializeObjectResetStackTest {

        @DisplayName("Then should return correct json")
        @Test
        fun testSerializeJsonResetStack() {
            // Given
            val expectedJson = makeResetStackJson().replace("\\s".toRegex(), "")
            val action = makeObjectResetStack()

            // When
            val actual = moshi.adapter(Action::class.java).toJson(action)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }

        @DisplayName("Then should return correct json with expression")
        @Test
        fun testSerializeJsonResetStackWithExpression() {
            // Given
            val expectedJson = makeResetStackJsonWithExpression().replace("\\s".toRegex(), "")
            val action = makeObjectResetStackWithExpression()

            // When
            val actual = moshi.adapter(Action::class.java).toJson(action)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }

        @DisplayName("Then should return correct json with hardcoded url")
        @Test
        fun testSerializeJsonResetStackWithHardcodedUrl() {
            // Given
            val expectedJson = makeResetStackJsonWithHardcodedUrl().replace("\\s".toRegex(), "")
            val action = makeObjectResetStackWithHardcodedUrl()

            // When
            val actual = moshi.adapter(Action::class.java).toJson(action)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }


    }

    private fun makeResetStackJson() = """
    {
        "_beagleAction_": "beagle:resetstack",
        "route": {
            "url": "http://test.com",
            "shouldPrefetch": true
        },
        "controllerId": "controller"
    }
"""

    private fun makeResetStackJsonWithExpression() = """
    {
      "_beagleAction_": "beagle:resetstack",
      "route": {
        "url": "@{test}",
        "shouldPrefetch": false
      },
      "controllerId": "controller"
    }
"""

    private fun makeResetStackJsonWithHardcodedUrl() = """
    {
      "_beagleAction_": "beagle:resetstack",
      "route": {
        "url": "http://localhost:8080/test/example",
        "shouldPrefetch": false,
        "httpAdditionalData": {
           "method": "POST",
           "headers": {
                "test": "test"
           },
           "body": "test"
        }
      },
      "controllerId": "controller"
    }
"""

    private fun makeObjectResetStack() = Navigate.ResetStack(
        route = Route.Remote(
            url = "http://test.com",
            shouldPrefetch = true

        ),
        controllerId = "controller"
    )

    private fun makeObjectResetStackWithExpression() = Navigate.ResetStack(
        route = Route.Remote(
            url = expressionOf("@{test}"),
            shouldPrefetch = false
        ),
        controllerId = "controller"
    )

    private fun makeObjectResetStackWithHardcodedUrl() = Navigate.ResetStack(
        route = Route.Remote(
            url = "http://localhost:8080/test/example",
            shouldPrefetch = false,
            httpAdditionalData = HttpAdditionalData(
                method = HttpMethod.POST,
                body = "test",
                headers = mapOf("test" to "test")
            )
        ),
        controllerId = "controller"
    )
}