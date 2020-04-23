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

import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.zup.beagle.expression.Array
import br.com.zup.beagle.expression.Binding
import br.com.zup.beagle.expression.JsonParser
import br.com.zup.beagle.expression.Null
import br.com.zup.beagle.expression.ObjectValue
import br.com.zup.beagle.expression.Primitive
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BindingTest {

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

    private val JSON = """{ "a": 
	{ "b": 
		{
		"c": 10, 
		"d": "olá",
		"e": [{"f": 15, "k": "18"}],
		"g": ["olá mundo", 2.5, true],
        "h": "11",
        "i": [{"j": "16"}]
		}
	}
}"""

    @Test
    fun json_parse_test_with_binding_success() {
        val data = JsonParser().parseJsonToValue(JSON)
        val binding = Binding.valueOf("@{a.b.c}")

        val value = binding.evaluate(data)

        assertTrue(value.isPrimitive())
        assertFalse(value.getAsPrimitive().isString())
        assertTrue(value.getAsPrimitive().isNumber())
        assertThat(value.getAsInt(), `is`(10))
    }

    @Test
    fun json_parse_test_with_binding_success11() {
        val data = JsonParser().parseJsonToValue(JSON)
        val binding = Binding.valueOf("@{a.b.i[0].j}")

        val value = binding.evaluate(data)
        assertTrue(value.isPrimitive())
        assertTrue(value.getAsPrimitive().isString())
        assertFalse(value.getAsPrimitive().isNumber())
        assertFalse(value.getAsPrimitive().isBoolean())
        assertThat(value.getAsString(), `is`("16"))
    }

    @Test
    fun should_initialize_binding() {
        //given
        val expression = "@{a.b.c.d}"
        val expectedTokenSize = 4

        //when
        val binding = Binding.DataBinding.valueOf(expression)

        //then
        assertEquals(expectedTokenSize, binding.tokens.size)
    }

    @Test
    fun evaluate_binding() {
        val binding = Binding.valueOf("@{a.b.c}")

        val value = binding.evaluate(data())

        assertThat(value.getAsString(), `is`("10"))
    }

    @Test
    fun evaluate_value_binding_2() {
        val binding = Binding.valueOf("@{a.b.d}")
        val data = data()

        var value = binding.evaluate(data)

        assertThat(value.getAsString(), `is`("true"))

    }

    @Test
    fun evaluate_binding_array0() {
        val binding = Binding.valueOf("@{e[0]}")
        val data = data()

        var value = binding.evaluate(data)

        assertThat(value.getAsString(), `is`("alpha"))

    }

    @Test
    fun evaluate_binding_array1() {
        val binding = Binding.valueOf("@{e[1]}")
        val data = data()

        var value = binding.evaluate(data)

        assertThat(value.getAsString(), `is`("2.5"))

    }

    @Test
    fun evaluate_binding_array2() {
        val binding = Binding.valueOf("@{e[2]}")
        val data = data()

        var value = binding.evaluate(data)

        assertThat(value.getAsBoolean(), `is`(false))

    }

    @Test
    fun evaluate_binding_13() {
        val binding = Binding.valueOf("@{f}")
        val data = data()

        var value = binding.evaluate(data)

        assertThat(value.toString(), `is`("NULL"))
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