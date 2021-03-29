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

import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.UnitType
import br.com.zup.beagle.widget.core.UnitValue
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given an Edge Value")
internal class EdgeValueTest {

    @DisplayName("When call static function only with integers number")
    @Nested
    inner class OnlyIntegersTest {

        @Test
        @DisplayName("Then should return a EdgeValue")
        fun testStaticFunctionOnly() {
            // Given
            val left = 10

            // When
            val result = EdgeValue.only(left = left)

            // Then
            val actual = EdgeValue(left = UnitValue.real(left))
            Assertions.assertEquals(actual, result)
        }

        @Test
        @DisplayName("Then should return a EdgeValue")
        fun testStaticFunctionOnlyWithType() {
            // Given
            val left = 10

            // When
            val result = EdgeValue.only(left = left, unitTypeLeft = UnitType.PERCENT)

            // Then
            val actual = EdgeValue(left = UnitValue.percent(left))
            Assertions.assertEquals(actual, result)
        }
    }

    @DisplayName("When call static function only with double number")
    @Nested
    inner class OnlyDoubleTest {

        @Test
        @DisplayName("Then should return a EdgeValue")
        fun testStaticFunctionOnly() {
            // Given
            val left = 10.0

            // When
            val result = EdgeValue.only(left = left)

            // Then
            val actual = EdgeValue(left = UnitValue.real(left))
            Assertions.assertEquals(actual, result)
        }

        @Test
        @DisplayName("Then should return a EdgeValue")
        fun testStaticFunctionOnlyWithType() {
            // Given
            val left = 10.0

            // When
            val result = EdgeValue.only(left = left, unitTypeLeft = UnitType.PERCENT)

            // Then
            val actual = EdgeValue(left = UnitValue.percent(left))
            Assertions.assertEquals(actual, result)
        }
    }

    @DisplayName("When call static function all with integers number")
    @Nested
    inner class AllIntegersTest {

        @Test
        @DisplayName("Then should return a EdgeValue")
        fun testStaticFunctionAll() {
            // Given
            val all = 10

            // When
            val result = EdgeValue.all(all)

            // Then
            val actual = EdgeValue(all = UnitValue.real(all))
            Assertions.assertEquals(actual, result)
        }

        @Test
        @DisplayName("Then should return a EdgeValue")
        fun testStaticFunctionAllWithType() {
            // Given
            val all = 10

            // When
            val result = EdgeValue.all(all = all, unitType = UnitType.PERCENT)

            // Then
            val actual = EdgeValue(all = UnitValue.percent(all))
            Assertions.assertEquals(actual, result)
        }
    }

    @DisplayName("When call static function all with double number")
    @Nested
    inner class AllDoubleTest {

        @Test
        @DisplayName("Then should return a EdgeValue")
        fun testStaticFunctionAll() {
            // Given
            val all = 10.0

            // When
            val result = EdgeValue.all(all)

            // Then
            val actual = EdgeValue(all = UnitValue.real(all))
            Assertions.assertEquals(actual, result)
        }

        @Test
        @DisplayName("Then should return a EdgeValue")
        fun testStaticFunctionAllWithType() {
            // Given
            val all = 10.0

            // When
            val result = EdgeValue.all(all = all, unitType = UnitType.PERCENT)

            // Then
            val actual = EdgeValue(all = UnitValue.percent(all))
            Assertions.assertEquals(actual, result)
        }
    }

    @DisplayName("When call static function horizontal with integers number")
    @Nested
    inner class HorizontalIntegersTest {

        @Test
        @DisplayName("Then should return a EdgeValue")
        fun testStaticFunctionHorizontal() {
            // Given
            val horizontal = 10

            // When
            val result = EdgeValue.horizontal(horizontal)

            // Then
            val actual = EdgeValue(horizontal = UnitValue.real(horizontal))
            Assertions.assertEquals(actual, result)
        }

        @Test
        @DisplayName("Then should return a EdgeValue")
        fun testStaticFunctionHorizontalWithType() {
            // Given
            val horizontal = 10

            // When
            val result = EdgeValue.horizontal(horizontal, unitType = UnitType.PERCENT)

            // Then
            val actual = EdgeValue(horizontal = UnitValue.percent(horizontal))
            Assertions.assertEquals(actual, result)
        }
    }

    @DisplayName("When call static function horizontal with double number")
    @Nested
    inner class HorizontalDoubleTest {

        @Test
        @DisplayName("Then should return a EdgeValue")
        fun testStaticFunctionHorizontal() {
            // Given
            val horizontal = 10.0

            // When
            val result = EdgeValue.horizontal(horizontal)

            // Then
            val actual = EdgeValue(horizontal = UnitValue.real(horizontal))
            Assertions.assertEquals(actual, result)
        }

        @Test
        @DisplayName("Then should return a EdgeValue")
        fun testStaticFunctionHorizontalWithType() {
            // Given
            val horizontal = 10.0

            // When
            val result = EdgeValue.horizontal(horizontal = horizontal, unitType = UnitType.PERCENT)

            // Then
            val actual = EdgeValue(horizontal = UnitValue.percent(horizontal))
            Assertions.assertEquals(actual, result)
        }
    }


    @DisplayName("When call static function vertical with integers number")
    @Nested
    inner class VerticalIntegersTest {

        @Test
        @DisplayName("Then should return a EdgeValue")
        fun testStaticFunctionVertical() {
            // Given
            val vertical = 10

            // When
            val result = EdgeValue.vertical(vertical)

            // Then
            val actual = EdgeValue(vertical = UnitValue.real(vertical))
            Assertions.assertEquals(actual, result)
        }

        @Test
        @DisplayName("Then should return a EdgeValue")
        fun testStaticFunctionVerticalWithType() {
            // Given
            val vertical = 10

            // When
            val result = EdgeValue.vertical(vertical, unitType = UnitType.PERCENT)

            // Then
            val actual = EdgeValue(vertical = UnitValue.percent(vertical))
            Assertions.assertEquals(actual, result)
        }
    }

    @DisplayName("When call static function vertical with double number")
    @Nested
    inner class VerticalDoubleTest {

        @Test
        @DisplayName("Then should return a EdgeValue")
        fun testStaticFunctionVertical() {
            // Given
            val vertical = 10.0

            // When
            val result = EdgeValue.vertical(vertical)

            // Then
            val actual = EdgeValue(vertical = UnitValue.real(vertical))
            Assertions.assertEquals(actual, result)
        }

        @Test
        @DisplayName("Then should return a EdgeValue")
        fun testStaticFunctionVerticalWithType() {
            // Given
            val vertical = 10.0

            // When
            val result = EdgeValue.vertical(vertical = vertical, unitType = UnitType.PERCENT)

            // Then
            val actual = EdgeValue(vertical = UnitValue.percent(vertical))
            Assertions.assertEquals(actual, result)
        }
    }
}