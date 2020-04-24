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

import br.com.zup.beagle.expression.Array
import br.com.zup.beagle.expression.Binding
import br.com.zup.beagle.expression.Null
import br.com.zup.beagle.expression.ObjectValue
import br.com.zup.beagle.expression.Primitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BindingTest : BaseTest() {

    private fun data(): ObjectValue {
        val objectValue = ObjectValue()

        val a = ObjectValue()
        val b = ObjectValue()
        val c = Primitive(10)
        val d = Primitive(true)
        val e = Array()
        e.add(Primitive("alpha"))
        e.add(Primitive(2.5))
        e.add(Primitive(false))

        b.add("c", c)
        b.add("d", d)
        a.add("b", b)
        objectValue.add("a", a)
        objectValue.add("e", e)
        objectValue.add("f", Null.INSTANCE)
        objectValue.add("g", Array())

        return objectValue
    }

    @Test
    fun should_initialize_binding() {
        //given
        val expression = "@{a.b.c.d}"
        val expectedTokenSize = 4

        //when
        val binding = Binding.DataBinding.valueOf(expression)

        //then
        assertEquals(expectedTokenSize, (binding as Binding.DataBinding).tokens.size)
    }

    @Test
    fun evaluate_binding() {
        val binding = Binding.valueOf("@{a.b.c}")

        val value = binding.evaluate(data())

        assertEquals(value.getAsString(), "10")
    }

    @Test
    fun evaluate_value_binding_2() {
        val binding = Binding.valueOf("@{a.b.d}")
        val data = data()

        var value = binding.evaluate(data)

        assertEquals(value.getAsString(), "true")

    }

    @Test
    fun evaluate_binding_array0() {
        val binding = Binding.valueOf("@{e[0]}")
        val data = data()

        var value = binding.evaluate(data)

        assertEquals(value.getAsString(),"alpha")

    }

    @Test
    fun evaluate_binding_array1() {
        val binding = Binding.valueOf("@{e[1]}")
        val data = data()

        var value = binding.evaluate(data)

        assertEquals(value.getAsString(), "2.5")

    }

    @Test
    fun evaluate_binding_array2() {
        val binding = Binding.valueOf("@{e[2]}")
        val data = data()

        var value = binding.evaluate(data)

        assertEquals(value.getAsBoolean(), false)

    }

    @Test
    fun evaluate_binding_13() {
        val binding = Binding.valueOf("@{f}")
        val data = data()

        var value = binding.evaluate(data)

        assertEquals(value.toString(), "NULL")
    }

    @Test
    fun evaluate_binding_14() {
        val binding = Binding.valueOf("@{g}")
        val data = data()

        var value = binding.evaluate(data)

        assertTrue(value.isArray())
        assertTrue(value.getAsArray().size() == 0)
    }

    @Test
    fun evaluate_binding_15() {
        val binding = Binding.valueOf("@{g.a}")
        val data = data()

        var value = binding.evaluate(data)

        assertTrue(value.isNull())
    }

}