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
import br.com.zup.beagle.android.data.serializer.BaseSerializerTest
import br.com.zup.beagle.android.data.serializer.makeObjectPageIndicator
import br.com.zup.beagle.android.data.serializer.makePageIndicatorJson
import br.com.zup.beagle.core.ServerDrivenComponent
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a PageIndicator")
class PageIndicatorSerializerTest : BaseServerDrivenComponentSerializerTest() {

    @DisplayName("When try to deserialize json")
    @Nested
    inner class PageIndicatorDeserializationTests {

        @DisplayName("Then should return correct object")
        @Test
        fun testPageIndicatorDeserialization() {
            // Given
            val expectedComponent = makeObjectPageIndicator()
            val json = makePageIndicatorJson()

            // When
            val deserializedComponent = deserialize(json) as PageIndicator

            // Then
            Assertions.assertNotNull(deserializedComponent)
            Assertions.assertEquals(expectedComponent.selectedColor, deserializedComponent.selectedColor)
            Assertions.assertEquals(expectedComponent.unselectedColor, deserializedComponent.unselectedColor)
            Assertions.assertEquals(expectedComponent.numberOfPages, deserializedComponent.numberOfPages)
        }
    }

    @DisplayName("When try to serialize object")
    @Nested
    inner class PageIndicatorSerializationTests {

        @DisplayName("Then should return correct json")
        @Test
        fun testPageIndicatorSerialization() {
            testSerializeObject(makePageIndicatorJson(), makeObjectPageIndicator())
        }
    }
}