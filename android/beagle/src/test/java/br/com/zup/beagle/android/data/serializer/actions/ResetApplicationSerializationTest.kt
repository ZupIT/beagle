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
class ResetApplicationSerializationTest : BaseActionSerializationTest() {
    @DisplayName("When try to deserialize json ResetApplication")
    @Nested
    inner class DeserializeJsonResetApplicationTest {

        @DisplayName("Then should return correct object")
        @Test
        fun testDeserializeJsonResetApplication() {
            // Given
            val expectedAction = makeObjectResetApplication()
            val json = makeResetApplicationJson()

            // When
            val actual = moshi.adapter(Action::class.java).fromJson(json)

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedAction, actual)
        }

        @DisplayName("Then should return correct object with expression")
        @Test
        fun testDeserializeJsonResetApplicationWithExpression() {
            // Given
            val expectedAction = makeObjectResetApplicationWithExpression()
            val json = makeResetApplicationJsonWithExpression()

            // When
            val actual = moshi.adapter(Navigate.ResetApplication::class.java).fromJson(json)!!

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedAction, actual)
        }

        @DisplayName("Then should return correct object with hardcoded url")
        @Test
        fun testDeserializeJsonResetApplicationWithHardcodedUrl() {
            // Given
            val expectedAction = makeObjectResetApplicationWithHardcodedUrl()
            val json = makeResetApplicationJsonWithHardcodedUrl()

            // When
            val actual = moshi.adapter(Navigate.ResetApplication::class.java).fromJson(json)!!

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedAction, actual)
        }
    }

    @DisplayName("When try serialize object ResetApplication")
    @Nested
    inner class SerializeObjectResetApplicationTest {

        @DisplayName("Then should return correct json")
        @Test
        fun testSerializeJsonResetApplication() {
            // Given
            val expectedJson = makeResetApplicationJson().replace("\\s".toRegex(), "")
            val action = makeObjectResetApplication()

            // When
            val actual = moshi.adapter(Action::class.java).toJson(action)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }

        @DisplayName("Then should return correct json with expression")
        @Test
        fun testSerializeJsonResetApplicationWithExpression() {
            // Given
            val expectedJson = makeResetApplicationJsonWithExpression().replace("\\s".toRegex(), "")
            val action = makeObjectResetApplicationWithExpression()

            // When
            val actual = moshi.adapter(Action::class.java).toJson(action)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }

        @DisplayName("Then should return correct json with hardcoded url")
        @Test
        fun testSerializeJsonResetApplicationWithHardcodedUrl() {
            // Given
            val expectedJson = makeResetApplicationJsonWithHardcodedUrl().replace("\\s".toRegex(), "")
            val action = makeObjectResetApplicationWithHardcodedUrl()

            // When
            val actual = moshi.adapter(Action::class.java).toJson(action)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }


    }

    private fun makeResetApplicationJson() = """
    {
        "_beagleAction_": "beagle:resetapplication",
        "route": {
            "url": "http://test.com",
            "shouldPrefetch": true
        },
        "controllerId": "controller"
    }
"""

    private fun makeResetApplicationJsonWithExpression() = """
    {
      "_beagleAction_": "beagle:resetapplication",
      "route": {
        "url": "@{test}",
        "shouldPrefetch": false
      },
      "controllerId": "controller"
    }
"""

    private fun makeResetApplicationJsonWithHardcodedUrl() = """
    {
      "_beagleAction_": "beagle:resetapplication",
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

    private fun makeObjectResetApplication() = Navigate.ResetApplication(
        route = Route.Remote(
            url = "http://test.com",
            shouldPrefetch = true

        ),
        controllerId = "controller"
    )

    private fun makeObjectResetApplicationWithExpression() = Navigate.ResetApplication(
        route = Route.Remote(
            url = expressionOf("@{test}"),
            shouldPrefetch = false
        ),
        controllerId = "controller"
    )

    private fun makeObjectResetApplicationWithHardcodedUrl() = Navigate.ResetApplication(
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