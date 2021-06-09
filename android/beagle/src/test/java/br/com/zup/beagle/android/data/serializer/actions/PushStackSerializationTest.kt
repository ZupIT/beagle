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
class PushStackSerializationTest : BaseActionSerializationTest() {
    @DisplayName("When try to deserialize json PushStack")
    @Nested
    inner class DeserializeJsonPushStackTest {

        @DisplayName("Then should return correct object")
        @Test
        fun testDeserializeJsonPushStack() {
            // Given
            val expectedAction = makeObjectPushStack()
            val json = makePushStackJson()

            // When
            val actual = moshi.adapter(Action::class.java).fromJson(json)

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedAction, actual)
        }

        @DisplayName("Then should return correct object with expression")
        @Test
        fun testDeserializeJsonPushStackWithExpression() {
            // Given
            val expectedAction = makeObjectPushStackWithExpression()
            val json = makePushStackJsonWithExpression()

            // When
            val actual = moshi.adapter(Navigate.PushStack::class.java).fromJson(json)!!

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedAction, actual)
        }

        @DisplayName("Then should return correct object with hardcoded url")
        @Test
        fun testDeserializeJsonPushStackWithHardcodedUrl() {
            // Given
            val expectedAction = makeObjectPushStackWithHardcodedUrl()
            val json = makePushStackJsonWithHardcodedUrl()

            // When
            val actual = moshi.adapter(Navigate.PushStack::class.java).fromJson(json)!!

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedAction, actual)
        }
    }

    @DisplayName("When try serialize object PushStack")
    @Nested
    inner class SerializeObjectPushStackTest {

        @DisplayName("Then should return correct json")
        @Test
        fun testSerializeJsonPushStack() {
            // Given
            val expectedJson = makePushStackJson().replace("\\s".toRegex(), "")
            val action = makeObjectPushStack()

            // When
            val actual = moshi.adapter(Action::class.java).toJson(action)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }

        @DisplayName("Then should return correct json with expression")
        @Test
        fun testSerializeJsonPushStackWithExpression() {
            // Given
            val expectedJson = makePushStackJsonWithExpression().replace("\\s".toRegex(), "")
            val action = makeObjectPushStackWithExpression()

            // When
            val actual = moshi.adapter(Action::class.java).toJson(action)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }

        @DisplayName("Then should return correct json with hardcoded url")
        @Test
        fun testSerializeJsonPushStackWithHardcodedUrl() {
            // Given
            val expectedJson = makePushStackJsonWithHardcodedUrl().replace("\\s".toRegex(), "")
            val action = makeObjectPushStackWithHardcodedUrl()

            // When
            val actual = moshi.adapter(Action::class.java).toJson(action)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }


    }

    private fun makePushStackJson() = """
    {
        "_beagleAction_": "beagle:pushstack",
        "route": {
            "url": "http://test.com",
            "shouldPrefetch": true
        },
        "controllerId": "controller"
    }
"""

    private fun makePushStackJsonWithExpression() = """
    {
      "_beagleAction_": "beagle:pushstack",
      "route": {
        "url": "@{test}",
        "shouldPrefetch": false
      },
      "controllerId": "controller"
    }
"""

    private fun makePushStackJsonWithHardcodedUrl() = """
    {
      "_beagleAction_": "beagle:pushstack",
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

    private fun makeObjectPushStack() = Navigate.PushStack(
        route = Route.Remote(
            url = "http://test.com",
            shouldPrefetch = true

        ),
        controllerId = "controller"
    )

    private fun makeObjectPushStackWithExpression() = Navigate.PushStack(
        route = Route.Remote(
            url = expressionOf("@{test}"),
            shouldPrefetch = false
        ),
        controllerId = "controller"
    )

    private fun makeObjectPushStackWithHardcodedUrl() = Navigate.PushStack(
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