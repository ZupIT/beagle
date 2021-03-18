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

package br.com.zup.beagle.ext

import br.com.zup.beagle.core.Layout
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.core.StyleComponent
import br.com.zup.beagle.fake.FlexComponentFake
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Size
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@DisplayName("Given an Layout Component")
internal class SetLayoutTest {

    @DisplayName("When call setLayout")
    @Nested
    inner class LayoutTest {

        @Test
        @DisplayName("Then should return the current widget with instance of style")
        fun testWidgetSetStyle() {
            // Given
            val styleComponent = FlexComponentFake()
            val padding = EdgeValue.all(10)

            // When
            styleComponent.setLayout {
                this.padding = padding
            }

            // Then
            val expected = FlexComponentFake(
                Layout(
                    padding = padding,
                    flex = br.com.zup.beagle.widget.core.Flex(),
                    size = Size()
                )
            )

            assertEquals(expected, styleComponent)
        }
    }

    @DisplayName("When call setStyle twice")
    @Nested
    inner class TwiceStyleTest {

        @Test
        @DisplayName("Then should return the current widget with instance of style")
        fun testWidgetTwiceSetStyle() {
            // Given
            val styleComponent = FlexComponentFake()
            val padding = EdgeValue.all(10)
            val margin = EdgeValue.all(10)

            // When
            styleComponent.setLayout {
                this.padding = padding
            }
            styleComponent.setLayout {
                this.margin = margin
            }


            // Then
            val expected = FlexComponentFake(
                Layout(
                    padding = padding,
                    margin = margin,
                    flex = br.com.zup.beagle.widget.core.Flex(),
                    size = Size()
                )
            )

            assertEquals(expected, styleComponent)
        }
    }
}