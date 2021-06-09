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
class PushViewSerializationTest : BaseActionSerializationTest() {
    @DisplayName("When try to deserialize json PushView")
    @Nested
    inner class DeserializeJsonPushViewTest {

        @DisplayName("Then should return correct object")
        @Test
        fun testDeserializeJsonPushView() {
            // Given
            val expectedAction = makeObjectPushView()
            val json = makePushViewJson()

            // When
            val actual = moshi.adapter(Action::class.java).fromJson(json)

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedAction, actual)
        }

        @DisplayName("Then should return correct object with expression")
        @Test
        fun testDeserializeJsonPushViewWithExpression() {
            // Given
            val expectedAction = makeObjectPushViewWithExpression()
            val json = makePushViewJsonWithExpression()

            // When
            val actual = moshi.adapter(Navigate.PushView::class.java).fromJson(json)!!

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedAction, actual)
        }

        @DisplayName("Then should return correct object with hardcoded url")
        @Test
        fun testDeserializeJsonPushViewWithHardcodedUrl() {
            // Given
            val expectedAction = makeObjectPushViewWithHardcodedUrl()
            val json = makePushViewJsonWithHardcodedUrl()

            // When
            val actual = moshi.adapter(Navigate.PushView::class.java).fromJson(json)!!

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedAction, actual)
        }
    }

    @DisplayName("When try serialize object PushView")
    @Nested
    inner class SerializeObjectPushViewTest {

        @DisplayName("Then should return correct json")
        @Test
        fun testSerializeJsonPushView() {
            // Given
            val expectedJson = makePushViewJson().replace("\\s".toRegex(), "")
            val action = makeObjectPushView()

            // When
            val actual = moshi.adapter(Action::class.java).toJson(action)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }

        @DisplayName("Then should return correct json with expression")
        @Test
        fun testSerializeJsonPushViewWithExpression() {
            // Given
            val expectedJson = makePushViewJsonWithExpression().replace("\\s".toRegex(), "")
            val action = makeObjectPushViewWithExpression()

            // When
            val actual = moshi.adapter(Action::class.java).toJson(action)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }

        @DisplayName("Then should return correct json with hardcoded url")
        @Test
        fun testSerializeJsonPushViewWithHardcodedUrl() {
            // Given
            val expectedJson = makePushViewJsonWithHardcodedUrl().replace("\\s".toRegex(), "")
            val action = makeObjectPushViewWithHardcodedUrl()

            // When
            val actual = moshi.adapter(Action::class.java).toJson(action)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }


    }

    private fun makePushViewJson() = """
    {
        "_beagleAction_": "beagle:pushview",
        "route": {
            "url": "http://test.com",
            "shouldPrefetch": true
        }
    }
"""

    private fun makePushViewJsonWithExpression() = """
    {
      "_beagleAction_": "beagle:pushview",
      "route": {
        "url": "@{test}",
        "shouldPrefetch": false
      }
    }
"""

    private fun makePushViewJsonWithHardcodedUrl() = """
    {
      "_beagleAction_": "beagle:pushview",
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
      }
    }
"""

    private fun makeObjectPushView() = Navigate.PushView(
        route = Route.Remote(
            url = "http://test.com",
            shouldPrefetch = true

        )
    )

    private fun makeObjectPushViewWithExpression() = Navigate.PushView(
        route = Route.Remote(
            url = expressionOf("@{test}"),
            shouldPrefetch = false
        )
    )

    private fun makeObjectPushViewWithHardcodedUrl() = Navigate.PushView(
        route = Route.Remote(
            url = "http://localhost:8080/test/example",
            shouldPrefetch = false,
            httpAdditionalData = HttpAdditionalData(
                method = HttpMethod.POST,
                body = "test",
                headers = mapOf("test" to "test")
            )
        )
    )
}