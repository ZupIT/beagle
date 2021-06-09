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

package br.com.zup.beagle.android.data.serializer.components

import br.com.zup.beagle.android.components.form.FormInput
import br.com.zup.beagle.android.components.page.PageView
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.widget.UndefinedWidget
import br.com.zup.beagle.core.ServerDrivenComponent
import org.json.JSONObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a Moshi Adapter")
class UndefinedComponentSerializationTest : BaseComponentSerializationTest() {

    @DisplayName("When try to deserialize json UndefinedComponent")
    @Nested
    inner class DeserializeJsonUndefinedComponentTest {

        @DisplayName("Then should return correct object")
        @Test
        fun testDeserializeJsonUndefinedComponent() {
            // Given
            val json = makeUndefinedComponentJson()

            // When
            val actual = moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertTrue(actual is UndefinedWidget)
        }
    }

    @DisplayName("When try serialize object UndefinedComponent")
    @Nested
    inner class SerializeObjectUndefinedComponentTest {

        @DisplayName("Then should return correct json")
        @Test
        fun testSerializeJsonUndefinedComponent() {
            // Given
            val expectedJson = makeUndefinedWidgetJson().replace("\\s".toRegex(), "")
            val component = makeObjectUndefinedComponent()

            // When
            val actual = moshi.adapter(ServerDrivenComponent::class.java).toJson(component)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }

        @DisplayName("Then should return an UndefinedWidget of type InputWidget JSONObject")
        @Test
        fun testSerializeUndefinedComponentOfTypeInputWidget() {
            // Given
            val component = FormInput(
                name = RandomData.string(),
                child = UndefinedWidget()
            )

            // When
            val jsonComponent =
                moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

            // Then
            Assertions.assertNotNull(JSONObject(jsonComponent))
        }

        @DisplayName("Then should return an UndefinedWidget of type PageIndicator JSONObject")
        @Test
        fun testSerializeUndefinedComponentOfTypePageIndicatorComponent() {
            // Given
            val component = PageView(
                children = listOf(),
                pageIndicator = UndefinedWidget()
            )

            // When
            val jsonComponent =
                moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

            // Then
            Assertions.assertNotNull(JSONObject(jsonComponent))
        }
    }

    private fun makeUndefinedComponentJson() = """
    {
        "_beagleComponent_": "custom:new"
    }
"""

    private fun makeUndefinedWidgetJson() = """
    {
        "_beagleComponent_": "beagle:undefinedwidget"
    }
"""

    private fun makeObjectUndefinedComponent() = UndefinedWidget()
}