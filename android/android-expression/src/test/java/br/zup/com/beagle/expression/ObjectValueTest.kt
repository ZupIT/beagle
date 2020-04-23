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

import br.com.zup.beagle.expression.ObjectValue
import br.com.zup.beagle.expression.Primitive
import org.junit.Test
import kotlin.test.assertEquals

class ObjectValueTest {

    @Test
    fun should_initialize_complex_value() {
        //given
        val simpleValueInt = 10
        val simpleValueString = "Hello World"
        val simpleValueBoolean = true
        val simpleValueDouble = 2.5

        val propertyA = "a"
        val propertyB = "b"
        val propertyC = "c"
        val propertyD = "d"
        val propertyE = "e"
        val propertyF = "f"

        val rootComplexValue = ObjectValue()
        val childComplexValue = ObjectValue()
        val grandchildComplexValue = ObjectValue()

        grandchildComplexValue[propertyD] = Primitive(simpleValueString)
        grandchildComplexValue[propertyE] = Primitive(simpleValueBoolean)
        grandchildComplexValue[propertyF] = Primitive(simpleValueDouble)


        //when
        rootComplexValue[propertyA] = Primitive(simpleValueInt)
        rootComplexValue[propertyB] = childComplexValue
        childComplexValue[propertyC] = grandchildComplexValue


        //b.c.d = simpleValueString
        //b.c.e = simpleValueBoolean
        //b.c.f = simpleValueDouble
        //then
        assertEquals(simpleValueInt, rootComplexValue[propertyA]?.getAsInt())
        assertEquals(simpleValueString, rootComplexValue[propertyB]?.getAsComplexValue()?.get(propertyC)?.getAsComplexValue()?.get(propertyD)?.getAsString())
        assertEquals(simpleValueBoolean, rootComplexValue[propertyB]?.getAsComplexValue()?.get(propertyC)?.getAsComplexValue()?.get(propertyE)?.getAsBoolean())
        assertEquals(simpleValueDouble, rootComplexValue[propertyB]?.getAsComplexValue()?.get(propertyC)?.getAsComplexValue()?.get(propertyF)?.getAsDouble())
    }

}