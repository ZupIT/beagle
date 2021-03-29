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

import br.com.zup.beagle.widget.core.UnitType
import br.com.zup.beagle.widget.core.UnitValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given an Unit Value")
internal class UnitValueTest {

    @DisplayName("When call static function real with integer number")
    @Nested
    inner class RealIntegerTest {

        @Test
        @DisplayName("Then should return a UnitValue")
        fun testStaticFunctionReal() {
            // Given
            val real = 10

            // When
            val result = UnitValue.real(real)

            // Then
            val actual = UnitValue(real.toDouble(), UnitType.REAL)
            assertEquals(actual, result)
        }
    }

    @DisplayName("When call static function real with double number")
    @Nested
    inner class RealDoubleTest {

        @Test
        @DisplayName("Then should return a UnitValue")
        fun testStaticFunctionReal() {
            // Given
            val real = 10.0

            // When
            val result = UnitValue.real(real)

            // Then
            val actual = UnitValue(real, UnitType.REAL)
            assertEquals(actual, result)
        }
    }

    @DisplayName("When call static function percent with integer number")
    @Nested
    inner class PercentIntegerTest {

        @Test
        @DisplayName("Then should return a UnitValue")
        fun testStaticFunctionReal() {
            // Given
            val real = 10

            // When
            val result = UnitValue.percent(real)

            // Then
            val actual = UnitValue(real.toDouble(), UnitType.PERCENT)
            assertEquals(actual, result)
        }
    }

    @DisplayName("When call static function percent with double number")
    @Nested
    inner class PercentDoubleTest {

        @Test
        @DisplayName("Then should return a UnitValue")
        fun testStaticFunctionPercent() {
            // Given
            val real = 10.0

            // When
            val result = UnitValue.percent(real)

            // Then
            val actual = UnitValue(real, UnitType.PERCENT)
            assertEquals(actual, result)
        }
    }

}