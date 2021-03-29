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

import br.com.zup.beagle.widget.layout.Container
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given an Accessibility")
internal class AccessibilityTest {

    @DisplayName("When call function")
    @Nested
    inner class AccessibilityTest {

        @Test
        @DisplayName("Then should return a current component with correct accessibility")
        fun testAccessibility() {
            // Given
            val component = Container()

            // When
            Accessibility(accessibilityLabel = "test", self = component)

            // Then
            val expected = Container()
            expected.accessibility = br.com.zup.beagle.core.Accessibility(accessibilityLabel = "test")

            assertEquals(expected.accessibility, component.accessibility)
        }
    }
}