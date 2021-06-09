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

class PopToViewSerializationTest : BaseActionSerializationTest() {
    @DisplayName("When try to deserialize json PopToView")
    @Nested
    inner class DeserializeJsonPopToViewTest {

        @DisplayName("Then should return correct object")
        @Test
        fun testDeserializeJsonPopToView() {
            // Given
            val expectedAction = makeObjectPopToView()
            val json = makePopToViewJson()

            // When
            val actual = moshi.adapter(Action::class.java).fromJson(json)

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedAction, actual)
        }

        @DisplayName("Then should return correct object with expression")
        @Test
        fun testDeserializeJsonPopToViewWithExpression() {
            // Given
            val expectedAction = makeObjectPopToViewWithExpression()
            val json = makePopToViewJsonWithExpression()

            // When
            val actual = moshi.adapter(Navigate.PopToView::class.java).fromJson(json)!!

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedAction, actual)
        }
    }

    @DisplayName("When try serialize object PopToView")
    @Nested
    inner class SerializeObjectPopToViewTest {

        @DisplayName("Then should return correct json")
        @Test
        fun testSerializeJsonPopToView() {
            // Given
            val expectedJson = makePopToViewJson().replace("\\s".toRegex(), "")
            val action = makeObjectPopToView()

            // When
            val actual = moshi.adapter(Action::class.java).toJson(action)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }

        @DisplayName("Then should return correct json with expression")
        @Test
        fun testSerializeJsonPopToViewWithExpression() {
            // Given
            val expectedJson = makePopToViewJsonWithExpression().replace("\\s".toRegex(), "")
            val action = makeObjectPopToViewWithExpression()

            // When
            val actual = moshi.adapter(Action::class.java).toJson(action)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }
    }

    private fun makePopToViewJson() = """
    {
        "_beagleAction_": "beagle:poptoview",
        "route": "test"
    }
"""

    private fun makePopToViewJsonWithExpression() = """
    {
      "_beagleAction_": "beagle:poptoview",
      "route": "@{test}"
    }
"""

    private fun makeObjectPopToView() = Navigate.PopToView(
        route = "test"
    )

    private fun makeObjectPopToViewWithExpression() = Navigate.PopToView(
        route = expressionOf("@{test}")
    )
}