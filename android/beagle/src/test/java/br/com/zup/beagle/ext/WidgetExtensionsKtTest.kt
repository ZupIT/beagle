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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given Widget")
internal class WidgetExtensionsKtTest {

    private val widget: Widget = object : Widget() {}

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

    @DisplayName("When call flex")
    @Nested
    inner class WidgetFlexTest {

        @Test
        @DisplayName("Then should return widget")
        fun testWidgetApplyFlex() {
            // GIVEN
            val flex = Flex()

            // WHEN
            val result = widget.applyFlex(flex)


            // THEN
            assertNotNull(result)
            assertEquals(flex, result.style!!.flex)
        }

        @Test
        @DisplayName("Then should return widget")
        fun testWidgetApplyFlexWithStyle() {
            // GIVEN
            val flex = Flex()
            val style = Style(
                backgroundColor = "test"
            )
            widget.style = style

            // WHEN
            val result = widget.applyFlex(flex)


            // THEN
            val styleExpected = style.copy(flex = flex)

            assertNotNull(result)
            assertEquals(styleExpected, widget.style)
        }
    }

    @DisplayName("When call style")
    @Nested
    inner class WidgetStyleTest {

        @Test
        @DisplayName("Then should return widget")
        fun testWidgetApplyStyle() {
            // GIVEN
            val style = Style()

            // WHEN
            val result = widget.applyStyle(style)


            // THEN
            assertNotNull(result)
            assertEquals(result.style, style)
        }

        @Test
        @DisplayName("Then should return widget")
        fun testWidgetApplyStyleWithFlex() {
            // GIVEN
            val flex = Flex(flex = 1.0)
            val styleInitialized = Style(flex = flex)

            widget.style = styleInitialized

            // WHEN
            val result = widget.applyStyle(Style(backgroundColor = "test"))


            // THEN
            val styleExpected = Style(backgroundColor = "test", flex = flex)

            assertNotNull(result)
            assertEquals(styleExpected, widget.style)
        }
    }

    @DisplayName("When call accessibility")
    @Nested
    inner class WidgetAccessibilityTest {

        @Test
        @DisplayName("Then should return widget")
        fun testWidgetApplyAccessibility() {
            // GIVEN
            val accessibility = Accessibility()

            // WHEN
            val result = widget.applyAccessibility(accessibility)


            // THEN
            assertNotNull(result)
            assertEquals(result.accessibility, accessibility)
        }
    }
}