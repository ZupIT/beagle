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
import br.com.zup.beagle.android.components.layout.ScrollView
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.FlexDirection
import br.com.zup.beagle.widget.core.ScrollAxis
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a Moshi Adapter")
class ScrollViewSerializationTest : BaseComponentSerializationTest() {

    @DisplayName("When try to deserialize json ScrollView")
    @Nested
    inner class DeserializeJsonScrollViewTest {

        @DisplayName("Then should return correct object")
        @Test
        fun testDeserializeJsonScrollView() {
            // Given
            val expectedComponent = makeObjectScrollView()
            val json = makeScrollViewJson()

            // When
            val actual = moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedComponent, actual)
        }
    }

    @DisplayName("When try serialize object ScrollView")
    @Nested
    inner class SerializeObjectScrollViewTest {

        @DisplayName("Then should return correct json")
        @Test
        fun testSerializeJsonScrollView() {
            // Given
            val expectedJson = makeScrollViewJson().replace("\\s".toRegex(), "")
            val component = makeObjectScrollView()

            // When
            val actual = moshi.adapter(ServerDrivenComponent::class.java).toJson(component)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }
    }

    private fun makeScrollViewJson() = """
    {
        "_beagleComponent_": "beagle:scrollview",
        "children": [
            {
                "_beagleComponent_": "beagle:container",
                "children": [
                    ${makeTextJson()},
                    ${makeTextJson()},
                    ${makeTextJson()},
                    ${makeTextJson()},
                    ${makeTextJson()},
                    ${makeTextJson()}
                ],
                "style": {
                    "flex": {
                        "flexDirection": "ROW"
                    }
                }
            }
        ],
        "scrollDirection": "HORIZONTAL"
    }
"""

    private fun makeTextJson() = """
    {
        "_beagleComponent_": "beagle:text",
        "text": "Test"
    }
"""

    private fun makeObjectScrollView() = ScrollView(
        scrollDirection = ScrollAxis.HORIZONTAL,
        children = listOf(
            Container(
                children = listOf(
                    makeObjectText(),
                    makeObjectText(),
                    makeObjectText(),
                    makeObjectText(),
                    makeObjectText(),
                    makeObjectText(),
                )
            ).applyFlex(
                flex = Flex(flexDirection = FlexDirection.ROW)
            )
        )
    )

    private fun makeObjectText() = Text(
        text = "Test"
    )
}