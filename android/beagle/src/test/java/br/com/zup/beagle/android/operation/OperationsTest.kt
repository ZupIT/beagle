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

package br.com.zup.beagle.android.operation

import br.com.zup.beagle.android.operation.builtin.array.ContainsOperation
import br.com.zup.beagle.android.operation.builtin.array.InsertOperation
import br.com.zup.beagle.android.operation.builtin.array.RemoveIndexOperation
import br.com.zup.beagle.android.operation.builtin.array.RemoveOperation
import br.com.zup.beagle.android.operation.builtin.comparison.*
import br.com.zup.beagle.android.operation.builtin.logic.AndOperation
import br.com.zup.beagle.android.operation.builtin.logic.ConditionOperation
import br.com.zup.beagle.android.operation.builtin.logic.NotOperation
import br.com.zup.beagle.android.operation.builtin.logic.OrOperation
import br.com.zup.beagle.android.operation.builtin.number.DivideOperation
import br.com.zup.beagle.android.operation.builtin.number.MultiplyOperation
import br.com.zup.beagle.android.operation.builtin.number.SubtractOperation
import br.com.zup.beagle.android.operation.builtin.number.SumOperation
import br.com.zup.beagle.android.operation.builtin.other.IsEmptyOperation
import br.com.zup.beagle.android.operation.builtin.other.LengthOperation
import br.com.zup.beagle.android.operation.builtin.string.CapitalizeOperation
import br.com.zup.beagle.android.operation.builtin.string.ConcatOperation
import br.com.zup.beagle.android.operation.builtin.string.LowercaseOperation
import br.com.zup.beagle.android.operation.builtin.string.SubstrOperation
import br.com.zup.beagle.android.operation.builtin.string.UppercaseOperation
import org.json.JSONArray
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

import org.junit.jupiter.api.assertThrows

class OperationsTest {

    // Array

    @Test
    fun insert_should_insert_element() {
        val insert = InsertOperation()
        assertEquals(listOf(9, 1, 2, 3), insert.execute(listOf(1, 2, 3), 9, 0))
        assertEquals(listOf(1, 2, 3, 9), insert.execute(listOf(1, 2, 3), 9))
        assertEquals(jsonArrayOf(9, 1, 2, 3).toString(), insert.execute(jsonArrayOf(1, 2, 3), 9, 0).toString())
        assertEquals(jsonArrayOf(1, 2, 3, 9).toString(), insert.execute(jsonArrayOf(1, 2, 3), 9).toString())
    }

    @Test
    fun contains_should_check_if_contains_element() {
        val contains = ContainsOperation()
        assertTrue(contains.execute(listOf(1, 2, 3), 2))
        assertFalse(contains.execute(listOf(1, 2, 3), 4))
        assertTrue(contains.execute(jsonArrayOf(1, 2, 3), 2))
        assertFalse(contains.execute(jsonArrayOf(1, 2, 3), 4))
    }

    @Test
    fun remove_should_remove_all_elements() {
        val remove = RemoveOperation()
        assertEquals(listOf(1, 3), remove.execute(listOf(1, 2, 3, 2), 2))
        assertEquals(jsonArrayOf(1, 3).toString(), remove.execute(jsonArrayOf(1, 2, 3, 2), 2).toString())
    }

    @Test
    fun removeIndex_should_remove_element_at_index() {
        val removeIndex = RemoveIndexOperation()
        assertEquals(listOf(2, 3), removeIndex.execute(listOf(1, 2, 3), 0))
        assertEquals(jsonArrayOf(2, 3).toString(), removeIndex.execute(jsonArrayOf(1, 2, 3), 0).toString())
    }

    // Comparison
    @Test
    fun eq_should_compare_two_values() {
        val eq = EqOperation()

        assertTrue(eq.execute(1.0, 1.0))
        assertFalse(eq.execute(2.0, 1.0))
        assertTrue(eq.execute(1, 1))
        assertFalse(eq.execute(2, 1))
        assertTrue(eq.execute("a", "a"))
        assertFalse(eq.execute("a", "b"))
        assertTrue(eq.execute(listOf(1), listOf(1)))
        assertThrows<ArrayIndexOutOfBoundsException> { eq.execute() }
    }

    @Test
    fun gte_should_check_if_two_numbers_is_greater_or_equals() {
        val gte = GteOperation()

        assertTrue(gte.execute(1.0, 1.0))
        assertFalse(gte.execute(1.0, 2.0))
        assertTrue(gte.execute(2.0, 1.0))
        assertTrue(gte.execute(1, 1))
        assertFalse(gte.execute(1, 2))
        assertTrue(gte.execute(2, 1))
        assertThrows<ArrayIndexOutOfBoundsException>  { gte.execute() }
    }

    @Test
    fun gt_should_check_if_two_numbers_is_greater_than_other() {
        val gt = GtOperation()

        assertFalse(gt.execute(1.0, 1.0))
        assertFalse(gt.execute(1.0, 2.0))
        assertTrue(gt.execute(2.0, 1.0))
        assertFalse(gt.execute(1, 1))
        assertFalse(gt.execute(1, 2))
        assertTrue(gt.execute(2, 1))
        assertThrows<ArrayIndexOutOfBoundsException>  { gt.execute() }
    }

    @Test
    fun lte_should_check_if_two_numbers_is_less_than_or_equals_than() {
        val lte = LteOperation()

        assertTrue(lte.execute(1.0, 1.0))
        assertTrue(lte.execute(1.0, 2.0))
        assertFalse(lte.execute(2.0, 1.0))
        assertTrue(lte.execute(1, 1))
        assertTrue(lte.execute(1, 2))
        assertFalse(lte.execute(2, 1))
        assertThrows<ArrayIndexOutOfBoundsException>  { lte.execute() }
    }

    @Test
    fun lt_should_check_if_two_numbers_is_less_than_other_number() {
        val lt = LtOperation()

        assertFalse(lt.execute(1.0, 1.0))
        assertTrue(lt.execute(1.0, 2.0))
        assertFalse(lt.execute(2.0, 1.0))
        assertFalse(lt.execute(1, 1))
        assertTrue(lt.execute(1, 2))
        assertFalse(lt.execute(2, 1))
        assertThrows<ArrayIndexOutOfBoundsException>  { lt.execute() }
    }

    // Logic

    @Test
    fun and_should_be_true_if_all_params_is_true() {
        val and = AndOperation()
        assertTrue(and.execute(true))
        assertTrue(and.execute(true, true, true))
        assertFalse(and.execute(true, false, true))
        assertThrows<UnsupportedOperationException>  { and.execute() }
    }

    @Test
    fun or_should_be_true_if_one_param_is_true() {
        val or = OrOperation()
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
        val conditionFunction = ConditionOperation()
        assertEquals("is true", conditionFunction.execute(true, "is true", "is false"))
        assertEquals("is false", conditionFunction.execute(false, "is true", "is false"))
    }

    @Test
    fun not_should_invert_boolean_parameter() {
        val not = NotOperation()
        assertFalse(not.execute(true))
        assertTrue(not.execute(false))
    }

    // Number
    @Test
    fun divide_should_divide_two_numbers() {
        val divide = DivideOperation()

        assertEquals(1.0, divide.execute(2.0, 2.0))
        assertEquals(1, divide.execute(2, 2))
        assertThrows<ArrayIndexOutOfBoundsException>  { divide.execute() }
    }

    @Test
    fun multiply_should_divide_two_numbers() {
        val multiply = MultiplyOperation()

        assertEquals(4.0, multiply.execute(2.0, 2.0))
        assertEquals(4, multiply.execute(2, 2))
        assertThrows<ArrayIndexOutOfBoundsException>  { multiply.execute() }
    }

    @Test
    fun subtract_should_divide_two_numbers() {
        val subtract = SubtractOperation()

        assertEquals(0.0, subtract.execute(2.0, 2.0))
        assertEquals(0, subtract.execute(2, 2))
        assertThrows<ArrayIndexOutOfBoundsException>  { subtract.execute() }
    }

    @Test
    fun sum_should_divide_two_numbers() {
        val sum = SumOperation()

        assertEquals(4.0, sum.execute(2.0, 2.0))
        assertEquals(4, sum.execute(2, 2))
        assertThrows<ArrayIndexOutOfBoundsException>  { sum.execute() }
    }

    // Other

    @Test
    fun isEmpty_should_check_if_values_is_empty() {
        val isEmpty = IsEmptyOperation()

        assertTrue(isEmpty.execute(""))
        assertFalse(isEmpty.execute(" "))
        assertTrue(isEmpty.execute(emptyList<Int>()))
        assertFalse(isEmpty.execute(listOf(1)))
        assertTrue(isEmpty.execute(emptyMap<String, String>()))
        assertFalse(isEmpty.execute(mapOf("" to "")))
        assertTrue(isEmpty.execute(null))
        assertThrows<ArrayIndexOutOfBoundsException>  { isEmpty.execute() }
    }

    @Test
    fun length_should_check_return_value_length() {
        val length = LengthOperation()

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
        assertTrue(IsEmptyOperation().execute(null))
    }

    // String
    @Test
    fun capitalize_should_capitalize_first_character() {
        val capitalize = CapitalizeOperation()

        assertEquals("Aaa", capitalize.execute("aaa"))
        assertEquals("AAA", capitalize.execute("AAA"))
        assertEquals("AaA", capitalize.execute("aaA"))
    }

    @Test
    fun concat_should_concatenate_all_parameters() {
        val concat = ConcatOperation()

        assertEquals("aabbcc", concat.execute("aa", "bb", "cc"))
        assertEquals("", concat.execute("", ""))
    }

    @Test
    fun lowercase_should_return_string_lowered() {
        assertEquals("aa", LowercaseOperation().execute("AA"))
    }

    @Test
    fun uppercase_should_return_string_upped() {
        assertEquals("AA", UppercaseOperation().execute("aa"))
    }

    @Test
    fun substr_should_return_sub_string() {
        val substr = SubstrOperation()
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