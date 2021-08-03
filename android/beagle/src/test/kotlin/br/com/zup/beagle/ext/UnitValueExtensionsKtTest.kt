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

import br.com.zup.beagle.widget.core.UnitType
import br.com.zup.beagle.widget.core.UnitValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given number")
internal class UnitValueExtensionsKtTest {

    @DisplayName("When call unit real")
    @Nested
    inner class UnitRealTest {

        @Test
        @DisplayName("Then should convert to object unit real")
        fun testConvertIntegerToUnitReal() {
            // GIVEN
            val intNumber = 1

            // WHEN
            val result = intNumber.unitReal()

            // THEN
            val expected = UnitValue(1.0, UnitType.REAL)
            assertEquals(expected, result)
        }

        @Test
        @DisplayName("Then should convert to object unit real")
        fun testConvertDoubleToUnitReal() {
            // GIVEN
            val doubleNumber = 1.0

            // WHEN
            val result = doubleNumber.unitReal()

            // THEN
            val expected = UnitValue(1.0, UnitType.REAL)
            assertEquals(expected, result)
        }
    }

    @DisplayName("When call unit percent")
    @Nested
    inner class UnitPercentTest {

        @Test
        @DisplayName("Then should convert to object unit percent")
        fun testConvertIntegerToUnitPercent() {
            // GIVEN
            val intNumber = 1

            // WHEN
            val result = intNumber.unitPercent()

            // THEN
            val expected = UnitValue(1.0, UnitType.PERCENT)
            assertEquals(expected, result)
        }

        @Test
        @DisplayName("Then should convert to object unit percent")
        fun testConvertDoubleToUnitPercent() {
            // GIVEN
            val doubleNumber = 1.0

            // WHEN
            val result = doubleNumber.unitPercent()

            // THEN
            val expected = UnitValue(1.0, UnitType.PERCENT)
            assertEquals(expected, result)
        }
    }
}