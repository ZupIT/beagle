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
import br.com.zup.beagle.fake.AccessibilityComponentFake
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@DisplayName("Given an Accessibility Component")
internal class AccessibilityComponentTest {

    @DisplayName("When call setAccessibility")
    @Nested
    inner class AccessibilityTest {

        @Test
        @DisplayName("Then should return the current widget with instance of accessibility")
        fun testWidgetSetAccessibility() {
            // Given
            val accessibilityComponent = AccessibilityComponentFake()
            val accessibilityLabel = "teste"

            // When
            accessibilityComponent.setAccessibility {
                this.accessibilityLabel = accessibilityLabel
            }

            // Then
            val expected = AccessibilityComponentFake(
                Accessibility(accessibilityLabel = accessibilityLabel)
            )

            assertEquals(expected, accessibilityComponent)
        }
    }

    @DisplayName("When call setAccessibility twice")
    @Nested
    inner class TwiceAccessibilityTest {

        @Test
        @DisplayName("Then should return the current widget with instance of accessibility")
        fun testWidgetTwiceSetAccessibility() {
            // Given
            val accessibilityComponent = AccessibilityComponentFake()
            val accessibilityLabel = "teste"
            val isHeader = true

            // When
            accessibilityComponent.setAccessibility {
                this.accessibilityLabel = accessibilityLabel
            }

            accessibilityComponent.setAccessibility {
                this.isHeader = isHeader
            }

            // Then
            val expected = AccessibilityComponentFake(
                Accessibility(accessibilityLabel = accessibilityLabel, isHeader = isHeader)
            )

            assertEquals(expected, accessibilityComponent)
        }
    }
}