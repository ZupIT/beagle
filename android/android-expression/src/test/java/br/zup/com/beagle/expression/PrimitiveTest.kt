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

package br.zup.com.beagle.expression

import br.com.zup.beagle.expression.Primitive
import org.junit.Test
import kotlin.test.assertEquals

class PrimitiveTest {

    @Test
    fun should_initialize_simple_value_string() {
        val anyValue = "Any String"

        val beagleSimpleValue = Primitive(anyValue)

        assertEquals(anyValue, beagleSimpleValue.getAsString())
    }

    @Test
    fun should_initialize_simple_value_boolean() {
        val anyValue = true

        val beagleSimpleValue = Primitive(anyValue)

        assertEquals(anyValue, beagleSimpleValue.getAsBoolean())
    }

    @Test
    fun should_initialize_simple_value_int() {
        val anyValue = 10

        val beagleSimpleValue = Primitive(anyValue)

        assertEquals(anyValue, beagleSimpleValue.getAsInt())
    }

    @Test
    fun should_initialize_simple_value_double() {
        val anyValue = 2.5

        val beagleSimpleValue = Primitive(anyValue)

        assertEquals(anyValue, beagleSimpleValue.getAsDouble())
    }
}