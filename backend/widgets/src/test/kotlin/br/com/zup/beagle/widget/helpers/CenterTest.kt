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

package br.com.zup.beagle.widget.helpers

import br.com.zup.beagle.core.Style
import br.com.zup.beagle.widget.core.AlignContent
import br.com.zup.beagle.widget.core.AlignItems
import br.com.zup.beagle.widget.core.AlignSelf
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.JustifyContent
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.layout.Container
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a Center")
internal class CenterTest {

    @DisplayName("When call function")
    @Nested
    inner class CenterTest {

        @Test
        @DisplayName("Then should return a current component with correct flex options")
        fun testCenter() {
            // Given
            val component = Container()

            // When
            Center(component)

            // Then
            val expected = Container()
            expected.style = Style(
                flex = Flex(
                    justifyContent = JustifyContent.CENTER,
                    alignContent = AlignContent.CENTER,
                    alignSelf = AlignSelf.CENTER,
                    alignItems = AlignItems.CENTER
                ),
                size = Size()
            )

            assertEquals(expected.style, component.style)
        }
    }
}