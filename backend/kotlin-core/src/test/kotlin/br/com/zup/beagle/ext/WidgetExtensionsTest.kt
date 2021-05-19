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
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@DisplayName("Given an Style Component")
class WidgetExtensionsTest {

    private val widget: Widget = object : Widget() {}

    @DisplayName("When call applyStyle")
    @Nested
    inner class StyleTest {

        @Test
        @DisplayName("Then should return the current widget with instance of style")
        fun testWidgetApplyStyle() {
            // Given
            val backgroundColor = "#fafafa"
            val style = Style(backgroundColor = backgroundColor)

            // When
            val result = widget.applyStyle(style)

            // Then
            assertNotNull(result)
            assertEquals(style, result.style)
        }
    }

    @DisplayName("When call applyStyle and applyFlex")
    @Nested
    inner class StyleAndFlexTest {

        @Test
        @DisplayName("Then should return the current widget with instance of style")
        fun testWidgetApplyStyleAndApplyFlex() {
            // Given
            val backgroundColor = "#fafafa"
            val grow = 1.0
            val flex = Flex(grow = grow)
            val style = Style(backgroundColor = backgroundColor)
            val styleResult = Style(backgroundColor = backgroundColor, flex = flex)

            // When
            val result = widget.applyFlex(flex).applyStyle(style)

            // Then
            assertNotNull(result)
            assertEquals(styleResult, result.style)
        }
    }

    @DisplayName("When call applyFlex")
    @Nested
    inner class StyleWithFlexTest {

        @Test
        @DisplayName("Then should return the current widget with instance of style")
        fun testWidgetApplyFlex() {
            // Given
            val grow = 1.0
            val flex = Flex(grow = grow)
            val styleResult = Style(flex = flex)

            // When
            val result = widget.applyFlex(flex)

            // Then
            assertNotNull(result)
            assertEquals(styleResult, result.style)
        }
    }

    @DisplayName("")
    @Nested
    inner class AccessibilityTest {

        @Test
        @DisplayName("Then should return the current widget with instance of accessibility")
        fun testWidgetApplyAccessibility() {
            // Given
            val accessibility = Accessibility(accessible = true, accessibilityLabel = "", isHeader = true)

            // When
            val result = widget.applyAccessibility(accessibility)

            // Then
            assertNotNull(result)
            assertEquals(accessibility, result.accessibility)
        }

        @Test
        @DisplayName("Then should return the current widget with instance of accessibility")
        fun testWidgetApplyAccessibilityDefault() {
            // Given
            val accessibility = Accessibility()

            // When
            val result = widget.applyAccessibility(accessibility)

            // Then
            assertNotNull(result)
            assertEquals(accessibility, result.accessibility)
        }
    }

    @DisplayName("When call id")
    @Nested
    inner class WidgetIdTest {

        @Test
        @DisplayName("Then should return widget")
        fun testWidgetSetId() {
            // GIVEN
            val id = "id"

            // WHEN
            val result = widget.setId(id)


            // THEN
            assertNotNull(result)
            assertEquals(id, result.id)
        }

        @Test
        @DisplayName("Then should return widget")
        fun testWidgetId() {
            // GIVEN
            val id = "id"

            // WHEN
            val result = widget.id { id }


            // THEN
            assertNotNull(result)
            assertEquals(id, result.id)
        }
    }
}