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
import br.com.zup.beagle.widget.layout.Container
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a Margin")
internal class MarginTest {

    @DisplayName("When call function")
    @Nested
    inner class MarginTest {

        @Test
        @DisplayName("Then should return a current component with correct style options")
        fun testMargin() {
            // Given
            val component = Container()

            // When
            Margin(margin = EdgeValue.all(10), self = component)

            // Then
            val expected = Container()
            expected.style = Style(
                margin = EdgeValue.all(10),
                flex = Flex(),
                size = Size(),
                cornerRadius = CornerRadius()
            )

            assertEquals(expected.style, component.style)
        }
    }
}