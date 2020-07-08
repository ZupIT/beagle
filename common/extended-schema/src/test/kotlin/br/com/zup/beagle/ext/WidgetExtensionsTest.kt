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

import br.com.zup.beagle.core.Accessibility
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.widget.Widget
import br.com.zup.beagle.widget.core.Flex
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TestWidget : Widget()

class WidgetExtensionsTest {

    @Test
    fun `setId should set Id to the caller`() {
        // Given
        val widget = TestWidget()
        val id = "id"

        // When
        widget.setId(id)

        // Then
        assertEquals(id, widget.id)
    }

    @Test
    fun `applyFlex should set Flex to the caller and use existing Style object`() {
        // Given
        val widget = TestWidget()
        val style = Style(backgroundColor = "backgroundColor")
        widget.style = style
        val flex = Flex(grow = 1.0)

        // When
        widget.applyFlex(flex)

        // Then
        assertEquals(style.backgroundColor, widget.style?.backgroundColor)
        assertEquals(flex.grow, widget.style?.flex?.grow)
    }

    @Test
    fun `applyFlex should set Flex to the caller even there is no Style object`() {
        // Given
        val widget = TestWidget()
        val flex = Flex(grow = 1.0)

        // When
        widget.applyFlex(flex)

        // Then
        assertEquals(flex.grow, widget.style?.flex?.grow)
    }


    @Test
    fun `applyStyle should set Style to the caller and use his Flex if exists`() {
        // Given
        val widget = TestWidget()
        val style = Style(flex = Flex(grow = 1.0))

        // When
        widget.applyStyle(style)

        // Then
        assertEquals(style.flex?.grow, widget.style?.flex?.grow)
    }

    @Test
    fun `applyStyle should set Style to the caller and use widgets Flex if exists`() {
        // Given
        val widget = TestWidget()
        widget.applyFlex(Flex(grow = 1.0))
        val style = Style()

        // When
        widget.applyStyle(style)

        // Then
        assertEquals(style.flex?.grow, widget.style?.flex?.grow)
    }

    @Test
    fun `applyAccessibility should set Accessibility to the caller`() {
        // Given
        val widget = TestWidget()
        val accessibility = Accessibility(accessible = false, accessibilityLabel = "accessibility")

        // When
        widget.applyAccessibility(accessibility)

        // Then
        assertEquals(accessibility, widget.accessibility)
    }
}
