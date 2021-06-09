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

import br.com.zup.beagle.android.components.page.PageIndicator
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.core.ServerDrivenComponent
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a Moshi Adapter")
class PageIndicatorSerializationTest : BaseComponentSerializationTest() {

    @DisplayName("When try to deserialize json PageIndicator")
    @Nested
    inner class DeserializeJsonPageIndicatorTest {

        @DisplayName("Then should return correct object")
        @Test
        fun testDeserializeJsonPageIndicator() {
            // Given
            val expectedComponent = makeObjectPageIndicator()
            val json = makePageIndicatorJson()

            // When
            val actual = moshi.adapter(ServerDrivenComponent::class.java).fromJson(json) as PageIndicator

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedComponent.selectedColor, actual.selectedColor)
            Assertions.assertEquals(expectedComponent.unselectedColor, actual.unselectedColor)
            Assertions.assertEquals(expectedComponent.numberOfPages, actual.numberOfPages)
        }
    }

    @DisplayName("When try serialize object PageIndicator")
    @Nested
    inner class SerializeObjectPageIndicatorTest {

        @DisplayName("Then should return correct json")
        @Test
        fun testSerializeJsonPageIndicator() {
            // Given
            val expectedJson = makePageIndicatorJson().replace("\\s".toRegex(), "")
            val component = makeObjectPageIndicator()

            // When
            val actual = moshi.adapter(ServerDrivenComponent::class.java).toJson(component)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }
    }

    private fun makePageIndicatorJson() = """
    {
        "_beagleComponent_": "beagle:pageindicator",
        "selectedColor": "#FFFFFF",
        "unselectedColor": "#888888",
        "numberOfPages": 3,
        "currentPage": "@{contextTab}"
    }
"""

    private fun makeObjectPageIndicator() = PageIndicator(
        selectedColor = "#FFFFFF",
        unselectedColor = "#888888",
        numberOfPages = 3,
        currentPage = expressionOf("@{contextTab}")
    )
}