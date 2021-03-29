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

package br.com.zup.beagle.core

import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.core.UnitType
import br.com.zup.beagle.widget.core.UnitValue
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@DisplayName("Given a Size")
internal class SizeTest {

    @DisplayName("When call static function box with int numbers")
    @Nested
    inner class BoxIntTest {
        @Test
        @DisplayName("Then should return a Size")
        fun testSizeStaticFunctionIntegersBox() {
            // Given
            val width = 10
            val height = 10

            // When
            val result = Size.box(width, height)

            // Then
            val actual = Size(
                width = UnitValue.real(width),
                height = UnitValue.real(height),
            )

            assertEquals(actual, result)
        }
    }


    @DisplayName("When call static function box with double numbers")
    @Nested
    inner class BoxDoubleTest {
        @Test
        @DisplayName("Then should return a Size")
        fun testSizeStaticFunctionDoubleBox() {
            // Given
            val width = 10.0
            val height = 10.0

            // When
            val result = Size.box(width, height)

            // Then
            val actual = Size(
                width = UnitValue.real(width),
                height = UnitValue.real(height),
            )

            assertEquals(actual, result)
        }
    }

    @DisplayName("When call static function box with unit value")
    @Nested
    inner class BoxUnitValueTest {
        @Test
        @DisplayName("Then should return a Size")
        fun testSizeStaticFunctionUnitValueBox() {
            // Given
            val width = UnitValue(10.0, UnitType.PERCENT)
            val height = UnitValue(10.0, UnitType.PERCENT)

            // When
            val result = Size.box(width, height)

            // Then
            val actual = Size(
                width = width,
                height = height
            )

            assertEquals(actual, result)
        }
    }
}