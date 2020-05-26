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
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BeagleExtensions {

    @Test
    fun unitReal_should_return_a_UnitValue_with_100_double_REAL() {
        val actual = 100.unitReal()

        assertEquals(100.0, actual.value)
        assertEquals(UnitType.REAL, actual.type)
    }

    @Test
    fun unitPercent_should_return_a_UnitValue_with_100_double_PERCENT() {
        val actual = 100.unitPercent()

        assertEquals(100.0, actual.value)
        assertEquals(UnitType.PERCENT, actual.type)
    }

    @Test
    fun unitReal_should_return_a_UnitValue_with_100_REAL() {
        val actual = 100.unitReal()

        assertEquals(100.0, actual.value)
        assertEquals(UnitType.REAL, actual.type)
    }

    @Test
    fun unitPercent_should_return_a_UnitValue_with_100_PERCENT() {
        val actual = 100.unitPercent()

        assertEquals(100.0, actual.value)
        assertEquals(UnitType.PERCENT, actual.type)
    }
}