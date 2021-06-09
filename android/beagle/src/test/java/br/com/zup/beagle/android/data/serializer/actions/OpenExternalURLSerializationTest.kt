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
import br.com.zup.beagle.android.action.Navigate
import br.com.zup.beagle.android.context.expressionOf
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a Moshi Adapter")
class OpenExternalURLSerializationTest : BaseActionSerializationTest() {
    @DisplayName("When try to deserialize json OpenExternalURL")
    @Nested
    inner class DeserializeJsonOpenExternalURLTest {

        @DisplayName("Then should return correct object")
        @Test
        fun testDeserializeJsonOpenExternalURL() {
            // Given
            val expectedAction = makeObjectOpenExternalURL()
            val json = makeOpenExternalURLJson()

            // When
            val actual = moshi.adapter(Action::class.java).fromJson(json)

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedAction, actual)
        }

        @DisplayName("Then should return correct object with expression")
        @Test
        fun testDeserializeJsonOpenExternalURLWithExpression() {
            // Given
            val expectedAction = makeObjectOpenExternalURLWithExpression()
            val json = makeOpenExternalURLJsonWithExpression()

            // When
            val actual = moshi.adapter(Navigate.OpenExternalURL::class.java).fromJson(json)!!

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedAction, actual)
        }
    }

    @DisplayName("When try serialize object OpenExternalURL")
    @Nested
    inner class SerializeObjectOpenExternalURLTest {

        @DisplayName("Then should return correct json")
        @Test
        fun testSerializeJsonOpenExternalURL() {
            // Given
            val expectedJson = makeOpenExternalURLJson().replace("\\s".toRegex(), "")
            val action = makeObjectOpenExternalURL()

            // When
            val actual = moshi.adapter(Action::class.java).toJson(action)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }

        @DisplayName("Then should return correct json with expression")
        @Test
        fun testSerializeJsonOpenExternalURLWithExpression() {
            // Given
            val expectedJson = makeOpenExternalURLJsonWithExpression().replace("\\s".toRegex(), "")
            val action = makeObjectOpenExternalURLWithExpression()

            // When
            val actual = moshi.adapter(Action::class.java).toJson(action)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }
    }

    private fun makeOpenExternalURLJson() = """
    {
        "_beagleAction_": "beagle:openexternalurl",
        "url": "http://test.com"
    }
"""

    private fun makeOpenExternalURLJsonWithExpression() = """
    {
        "_beagleAction_": "beagle:openexternalurl",
        "url": "@{test}"
    }
"""

    private fun makeObjectOpenExternalURL() = Navigate.OpenExternalURL(
        url = "http://test.com",
    )

    private fun makeObjectOpenExternalURLWithExpression() = Navigate.OpenExternalURL(
        url = expressionOf("@{test}"),
    )
}