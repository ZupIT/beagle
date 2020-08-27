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

package br.com.zup.beagle.android.context.tokenizer.function.builtin.array

import br.com.zup.beagle.android.context.tokenizer.function.builtin.comparison.*
import br.com.zup.beagle.android.context.tokenizer.function.builtin.logic.AndFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.logic.ConditionFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.logic.NotFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.logic.OrFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.number.DivideFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.number.MultiplyFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.number.SubtractFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.number.SumFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.other.IsEmptyFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.other.LengthFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.string.*
import org.json.JSONArray
import org.junit.Test

import org.junit.Assert.*
import kotlin.test.assertFails

class OperationsTest {

    // Array

    @Test
    fun insert_should_insert_element() {
        val insert = InsertFunction()
        assertEquals(listOf(9, 1, 2, 3), insert.execute(listOf(1, 2, 3), 9, 0))
        assertEquals(listOf(1, 2, 3, 9), insert.execute(listOf(1, 2, 3), 9))
        assertEquals(jsonArrayOf(9, 1, 2, 3).toString(), insert.execute(jsonArrayOf(1, 2, 3), 9, 0).toString())
        assertEquals(jsonArrayOf(1, 2, 3, 9).toString(), insert.execute(jsonArrayOf(1, 2, 3), 9).toString())
    }

    @Test
    fun contains_should_check_if_contains_element() {
        val contains = ContainsFunction()
        assertTrue(contains.execute(listOf(1, 2, 3), 2))
        assertFalse(contains.execute(listOf(1, 2, 3), 4))
        assertTrue(contains.execute(jsonArrayOf(1, 2, 3), 2))
        assertFalse(contains.execute(jsonArrayOf(1, 2, 3), 4))
    }

    @Test
    fun remove_should_remove_all_elements() {
        val remove = RemoveFunction()
        assertEquals(listOf(1, 3), remove.execute(listOf(1, 2, 3, 2), 2))
        assertEquals(jsonArrayOf(1, 3).toString(), remove.execute(jsonArrayOf(1, 2, 3, 2), 2).toString())
    }

    @Test
    fun removeIndex_should_remove_element_at_index() {
        val removeIndex = RemoveIndexFunction()
        assertEquals(listOf(2, 3), removeIndex.execute(listOf(1, 2, 3), 0))
        assertEquals(jsonArrayOf(2, 3).toString(), removeIndex.execute(jsonArrayOf(1, 2, 3), 0).toString())
    }

    // Comparison
    @Test
    fun eq_should_compare_two_values() {
        val eq = EqFunction()

        assertTrue(eq.execute(1.0, 1.0))
        assertFalse(eq.execute(2.0, 1.0))
        assertTrue(eq.execute(1, 1))
        assertFalse(eq.execute(2, 1))
        assertTrue(eq.execute("a", "a"))
        assertFalse(eq.execute("a", "b"))
        assertTrue(eq.execute(listOf(1), listOf(1)))
        assertFails { eq.execute() }
    }

    @Test
    fun gte_should_check_if_two_numbers_is_greater_or_equals() {
        val gte = GteFunction()

        assertTrue(gte.execute(1.0, 1.0))
        assertFalse(gte.execute(1.0, 2.0))
        assertTrue(gte.execute(2.0, 1.0))
        assertTrue(gte.execute(1, 1))
        assertFalse(gte.execute(1, 2))
        assertTrue(gte.execute(2, 1))
        assertFails { gte.execute() }
    }

    @Test
    fun gt_should_check_if_two_numbers_is_greater_than_other() {
        val gt = GtFunction()

        assertFalse(gt.execute(1.0, 1.0))
        assertFalse(gt.execute(1.0, 2.0))
        assertTrue(gt.execute(2.0, 1.0))
        assertFalse(gt.execute(1, 1))
        assertFalse(gt.execute(1, 2))
        assertTrue(gt.execute(2, 1))
        assertFails { gt.execute() }
    }

    @Test
    fun lte_should_check_if_two_numbers_is_less_than_or_equals_than() {
        val lte = LteFunction()

        assertTrue(lte.execute(1.0, 1.0))
        assertTrue(lte.execute(1.0, 2.0))
        assertFalse(lte.execute(2.0, 1.0))
        assertTrue(lte.execute(1, 1))
        assertTrue(lte.execute(1, 2))
        assertFalse(lte.execute(2, 1))
        assertFails { lte.execute() }
    }

    @Test
    fun lt_should_check_if_two_numbers_is_less_than_other_number() {
        val lt = LtFunction()

        assertFalse(lt.execute(1.0, 1.0))
        assertTrue(lt.execute(1.0, 2.0))
        assertFalse(lt.execute(2.0, 1.0))
        assertFalse(lt.execute(1, 1))
        assertTrue(lt.execute(1, 2))
        assertFalse(lt.execute(2, 1))
        assertFails { lt.execute() }
    }

    // Logic

    @Test
    fun and_should_be_true_if_all_params_is_true() {
        val and = AndFunction()
        assertTrue(and.execute(true))
        assertTrue(and.execute(true, true, true))
        assertFalse(and.execute(true, false, true))
        assertFails { and.execute() }
    }

    @Test
    fun or_should_be_true_if_one_param_is_true() {
        val or = OrFunction()
        assertTrue(or.execute(true))
        assertFalse(or.execute(false))
        assertTrue(or.execute(true, true, true))
        assertTrue(or.execute(false, true, false))
        assertTrue(or.execute(true, false, false))
        assertTrue(or.execute(false, false, true))
        assertFalse(or.execute(false, false, false))
    }

    @Test
    fun condition_should_return_true_or_false_condition() {
        val conditionFunction = ConditionFunction()
        assertEquals("is true", conditionFunction.execute(true, "is true", "is false"))
        assertEquals("is false", conditionFunction.execute(false, "is true", "is false"))
    }

    @Test
    fun not_should_invert_boolean_parameter() {
        val not = NotFunction()
        assertFalse(not.execute(true))
        assertTrue(not.execute(false))
    }

    // Number
    @Test
    fun divide_should_divide_two_numbers() {
        val divide = DivideFunction()

        assertEquals(1.0, divide.execute(2.0, 2.0))
        assertEquals(1, divide.execute(2, 2))
        assertFails { divide.execute() }
    }

    @Test
    fun multiply_should_divide_two_numbers() {
        val multiply = MultiplyFunction()

        assertEquals(4.0, multiply.execute(2.0, 2.0))
        assertEquals(4, multiply.execute(2, 2))
        assertFails { multiply.execute() }
    }

    @Test
    fun subtract_should_divide_two_numbers() {
        val subtract = SubtractFunction()

        assertEquals(0.0, subtract.execute(2.0, 2.0))
        assertEquals(0, subtract.execute(2, 2))
        assertFails { subtract.execute() }
    }

    @Test
    fun sum_should_divide_two_numbers() {
        val sum = SumFunction()

        assertEquals(4.0, sum.execute(2.0, 2.0))
        assertEquals(4, sum.execute(2, 2))
        assertFails { sum.execute() }
    }

    // Other

    @Test
    fun isEmpty_should_check_if_values_is_empty() {
        val isEmpty = IsEmptyFunction()

        assertTrue(isEmpty.execute(""))
        assertFalse(isEmpty.execute(" "))
        assertTrue(isEmpty.execute(emptyList<Int>()))
        assertFalse(isEmpty.execute(listOf(1)))
        assertTrue(isEmpty.execute(emptyMap<String, String>()))
        assertFalse(isEmpty.execute(mapOf("" to "")))
        assertTrue(isEmpty.execute(null))
        assertFails { isEmpty.execute() }
    }

    @Test
    fun length_should_check_return_value_length() {
        val length = LengthFunction()

        assertEquals(0, length.execute(""))
        assertEquals(1, length.execute(" "))
        assertEquals(0, length.execute(emptyList<Int>()))
        assertEquals(1, length.execute(listOf(1)))
        assertEquals(0, length.execute(emptyMap<String, String>()))
        assertEquals(1, length.execute(mapOf("" to "")))
        assertEquals(0, length.execute(null))
    }

    @Test
    fun isNull_should_check_if_values_is_null() {
        assertTrue(IsEmptyFunction().execute(null))
    }

    // String
    @Test
    fun capitalize_should_capitalize_first_character() {
        val capitalize = CapitalizeFunction()

        assertEquals("Aaa", capitalize.execute("aaa"))
        assertEquals("AAA", capitalize.execute("AAA"))
        assertEquals("AaA", capitalize.execute("aaA"))
    }

    @Test
    fun concat_should_concatenate_all_parameters() {
        val concat = ConcatFunction()

        assertEquals("aabbcc", concat.execute("aa", "bb", "cc"))
        assertEquals("", concat.execute("", ""))
    }

    @Test
    fun lowercase_should_return_string_lowered() {
        assertEquals("aa",  LowercaseFunction().execute("AA"))
    }

    @Test
    fun uppercase_should_return_string_upped() {
        assertEquals("AA", UppercaseFunction().execute("aa"))
    }

    @Test
    fun substr_should_return_sub_string() {
        val substr = SubstrFunction()
        val str = "123456789"

        assertEquals("", substr.execute(str, 5, 0))
        assertEquals("12345", substr.execute(str, 0, 5))
        assertEquals("456", substr.execute(str, 3, 3))
        assertEquals("6789", substr.execute(str, 5))
    }

    private fun jsonArrayOf(vararg items: Int): JSONArray {
        return JSONArray().apply {
            items.forEach {
                put(it)
            }
        }
    }
}