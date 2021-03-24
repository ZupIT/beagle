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

import br.com.zup.beagle.core.CornerRadius
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.core.StyleComponent
import br.com.zup.beagle.fake.FlexComponentFake
import br.com.zup.beagle.widget.core.Size
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@DisplayName("Given an Style Component")
internal class SetStyleTest {

    @DisplayName("When call setStyle")
    @Nested
    inner class StyleTest {

        @Test
        @DisplayName("Then should return the current widget with instance of style")
        fun testWidgetSetStyle() {
            // Given
            val styleComponent = FlexComponentFake()
            val backgroundColor = "#fafafa"

            // When
            styleComponent.setStyle {
                this.backgroundColor = backgroundColor
            }

            // Then
            val expected = FlexComponentFake(
                Style(
                    backgroundColor = backgroundColor,
                    flex = br.com.zup.beagle.widget.core.Flex(),
                    size = Size(),
                    cornerRadius = CornerRadius()
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
            val backgroundColor = "#fafafa"
            val borderColor = "#f1f1f1"

            // When
            styleComponent.setStyle {
                this.backgroundColor = backgroundColor
            }
            styleComponent.setStyle {
                this.borderColor = borderColor
            }


            // Then
            val expected = FlexComponentFake(
                Style(
                    backgroundColor = backgroundColor,
                    borderColor = borderColor,
                    flex = br.com.zup.beagle.widget.core.Flex(),
                    size = Size(),
                    cornerRadius = CornerRadius()
                )
            )

            assertEquals(expected, styleComponent)
        }
    }
}