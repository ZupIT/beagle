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
import br.com.zup.beagle.core.WidgetStyle
import br.com.zup.beagle.fake.FlexComponentFake
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@DisplayName("Given a Widget Style Component")
internal class SetWidgetStyleTest {

    @DisplayName("When call setWidgetStyle")
    @Nested
    inner class LayoutTest {

        @Test
        @DisplayName("Then should return the current widget with instance of style")
        fun testWidgetSetStyle() {
            // Given
            val styleComponent = FlexComponentFake()
            val backgroundColor = "#f1f1f1"

            // When
            styleComponent.setWidgetStyle {
                this.backgroundColor = backgroundColor
            }

            // Then
            val expected = FlexComponentFake(
                widgetStyle = WidgetStyle(
                    backgroundColor = backgroundColor
                )
            )

            assertEquals(expected, styleComponent)
        }
    }

    @DisplayName("When call setWidgetStyle twice")
    @Nested
    inner class TwiceStyleTest {

        @Test
        @DisplayName("Then should return the current widget with instance of style")
        fun testWidgetTwiceSetWidgetStyle() {
            // Given
            val styleComponent = FlexComponentFake()
            val backgroundColor = "#f1f1f1"
            val borderColor = "#f1f1f1"

            // When
            styleComponent.setWidgetStyle {
                this.backgroundColor = backgroundColor
            }
            styleComponent.setWidgetStyle {
                this.borderColor = borderColor
            }


            // Then
            val expected = FlexComponentFake(
                widgetStyle = WidgetStyle(
                    backgroundColor = backgroundColor,
                    borderColor = borderColor,
                )
            )

            assertEquals(expected, styleComponent)
        }
    }
}