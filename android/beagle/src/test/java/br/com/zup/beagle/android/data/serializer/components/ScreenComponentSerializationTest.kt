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

import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.components.layout.NavigationBar
import br.com.zup.beagle.android.components.layout.ScreenComponent
import br.com.zup.beagle.core.ServerDrivenComponent
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a Moshi Adapter")
class ScreenComponentSerializationTest : BaseComponentSerializationTest() {

    @DisplayName("When try to deserialize json ScreenComponent")
    @Nested
    inner class DeserializeJsonScreenComponentTest {

        @DisplayName("Then should return correct object")
        @Test
        fun testDeserializeJsonScreenComponent() {
            // Given
            val expectedComponent = makeObjectScreenComponent()
            val json = makeScreenComponentJson()

            // When
            val actual = moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedComponent, actual)
        }
    }

    @DisplayName("When try serialize object ScreenComponent")
    @Nested
    inner class SerializeObjectGridViewTest {

        @DisplayName("Then should return correct json")
        @Test
        fun testSerializeJsonGridView() {
            // Given
            val expectedJson = makeScreenComponentJson().replace("\\s".toRegex(), "")
            val component = makeObjectScreenComponent()

            // When
            val actual = moshi.adapter(ServerDrivenComponent::class.java).toJson(component)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }
    }

    private fun makeScreenComponentJson() = """
    {
        "_beagleComponent_": "beagle:screencomponent",
        "navigationBar": {
            "title": "Screen Title",
            "showBackButton": true
        },
        "child": {
            "_beagleComponent_": "beagle:container",
            "children": [
              {
                "_beagleComponent_": "beagle:text",
                "text": "@{item.name}"
              }
            ]
          }
    }
"""

    private fun makeObjectScreenComponent() = ScreenComponent(
        navigationBar = NavigationBar(
            title = "Screen Title",
            showBackButton = true,
        ),
        child = Container(
            children = listOf(
                Text("@{item.name}")
            )
        )
    )

}