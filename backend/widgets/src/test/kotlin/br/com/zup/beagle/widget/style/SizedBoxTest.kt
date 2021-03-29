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

package br.com.zup.beagle.widget.style

import br.com.zup.beagle.core.CornerRadius
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.widget.core.AlignContent
import br.com.zup.beagle.widget.core.AlignSelf
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.JustifyContent
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.core.UnitValue
import br.com.zup.beagle.widget.layout.Container
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a SizedBox")
internal class SizedBoxTest {

    @DisplayName("When call function with integers")
    @Nested
    inner class SizedBoxWithIntegersTest {

        @Test
        @DisplayName("Then should return a current component with correct style options")
        fun testSizedBox() {
            // Given
            val component = Container()

            // When
            SizedBox(width = 10, height = 10, self = component)

            // Then
            val expected = Container()
            expected.style = Style(
                flex = Flex(),
                size = Size(width = UnitValue.real(10), UnitValue.real(10))
            )

            assertEquals(expected, component)
        }
    }

    @DisplayName("When call function with double")
    @Nested
    inner class SizedBoxWithDoubleTest {

        @Test
        @DisplayName("Then should return a current component with correct style options")
        fun testSizedBox() {
            // Given
            val component = Container()

            // When
            SizedBox(width = 10.0, height = 10.0, self = component)

            // Then
            val expected = Container()
            expected.style = Style(
                flex = Flex(),
                size = Size(width = UnitValue.real(10), UnitValue.real(10))
            )

            assertEquals(expected, component)
        }
    }

    @DisplayName("When call function with unit value")
    @Nested
    inner class SizedBoxWithUnitValueTest {

        @Test
        @DisplayName("Then should return a current component with correct style options")
        fun testSizedBox() {
            // Given
            val component = Container()

            // When
            SizedBox(width = UnitValue.percent(10), height = UnitValue.percent(10), self = component)

            // Then
            val expected = Container()
            expected.style = Style(
                flex = Flex(),
                size = Size(
                    width = UnitValue.percent(10),
                    height = UnitValue.percent(10)
                ),
                cornerRadius = CornerRadius()
            )

            assertEquals(expected.style,
                component.style)
        }
    }
}