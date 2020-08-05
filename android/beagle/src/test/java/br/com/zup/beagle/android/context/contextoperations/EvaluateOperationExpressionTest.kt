package br.com.zup.beagle.android.context.contextoperations

import br.com.zup.beagle.android.context.contextoperations.core.EvaluateOperationExpression
import junit.framework.Assert.assertEquals
import org.junit.Test

class EvaluateOperationExpressionTest {

    private lateinit var reader: EvaluateOperationExpression

    @Test
    fun should_solve_sum_operation() {
        // given
        reader =
            EvaluateOperationExpression(
                "sum(1, 2)"
            )
        val expected = 3

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun should_solve_subtract_operation() {
        // given
        reader =
            EvaluateOperationExpression(
                "subtract(5, 2)"
            )
        val expected = 3

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun should_solve_multiply_operation() {
        // given
        reader =
            EvaluateOperationExpression(
                "multiply(1, 3)"
            )
        val expected = 3

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun should_solve_divide_operation() {
        // given
        reader =
            EvaluateOperationExpression(
                "divide(9, 3)"
            )
        val expected = 3

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun should_solve_anyNumberOperation_as_double() {
        // given
        reader =
            EvaluateOperationExpression(
                "divide(1.0, 10.0)"
            )
        val expected = 0.1

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun should_solve_condition_operation_with_false() {
        // given
        reader =
            EvaluateOperationExpression(
                "condition(lt(sum(6, 3), 5), 'colorRed', 'ColorGreen')"
            )
        val expected = "ColorGreen"

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun should_solve_gt_operation() {
        // given
        reader =
            EvaluateOperationExpression(
                "condition(gt(sum(6, 3), 5), 'colorRed', 'ColorGreen')"
            )
        val expected = "colorRed"

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun should_solve_gte_operation() {
        // given
        reader =
            EvaluateOperationExpression(
                "condition(gte(sum(2, 3), 5), 'colorRed', 'ColorGreen')"
            )
        val expected = "colorRed"

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun should_solve_lte_operation() {
        // given
        reader =
            EvaluateOperationExpression(
                "condition(lte(sum(2, 3), 5), 'colorRed', 'ColorGreen')"
            )
        val expected = "colorRed"

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun should_solve_eq_operation() {
        // given
        reader =
            EvaluateOperationExpression(
                "condition(eq(sum(2, 3), 5), 'colorRed', 'ColorGreen')"
            )
        val expected = "colorRed"

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun should_solve_and_operation_false() {
        // given
        reader =
            EvaluateOperationExpression(
                "condition(and(eq(sum(2, 3), 5), eq(sum(2, 4), 5)), 'colorRed', 'ColorGreen')"
            )
        val expected = "ColorGreen"

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun should_solve_and_operation_true() {
        // given
        reader =
            EvaluateOperationExpression(
                "condition(and(eq(sum(2, 3), 5), eq(5, sum(2, 3))), 'colorRed', 'ColorGreen')"
            )
        val expected = "colorRed"

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun should_solve_not_operation_true_and_return_false() {
        // given
        reader =
            EvaluateOperationExpression(
                "condition(not(and(eq(sum(2, 3), 5), eq(5, sum(2, 3)))), 'colorRed', 'ColorGreen')"
            )
        val expected = "ColorGreen"

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun should_solve_or_operation_true() {
        // given
        reader =
            EvaluateOperationExpression(
                "condition(or(eq(sum(4, 3), 5), eq(5, sum(2, 3))), 'colorRed', 'ColorGreen')"
            )
        val expected = "colorRed"

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun should_solve_or_operation_false() {
        // given
        reader =
            EvaluateOperationExpression(
                "condition(or(eq(sum(4, 3), 5), eq(5, sum(4, 3))), 'colorRed', 'ColorGreen')"
            )
        val expected = "ColorGreen"

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun should_solve_condition_operation_with_true() {
        // given
        reader =
            EvaluateOperationExpression(
                "condition(eq(sum(3, 2), 5), 'colorRed', 'colorGreen')"
            )
        val expected = "colorRed"

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun should_solve_concat_operation_keeping_whitespaces() {
        // given
        reader =
            EvaluateOperationExpression(
                "concat('value1', 'value2 whitespace', ' sidespace value3 sidespace ')"
            )
        val expected = "value1value2 whitespace sidespace value3 sidespace "

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun should_solve_concat_operation_and_capitalize_at_begining() {
        // given
        reader =
            EvaluateOperationExpression(
                "concat(capitalize('nome'), capitalize(' sobrenome'), capitalize(' ultimonome'))"
            )
        val expected = "Nome Sobrenome Ultimonome"

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun should_solve_concat_operation_and_capitalize_spaces_at_end() {
        // given
        reader =
            EvaluateOperationExpression(
                "concat(capitalize('nome '), capitalize('sobrenome '), capitalize('ultimonome'))"
            )
        val expected = "Nome Sobrenome Ultimonome"

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun should_solve_lowercase_operation() {
        // given
        reader =
            EvaluateOperationExpression(
                "lowercase('TESTE')"
            )
        val expected = "teste"

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun should_solve_capitalize_operation() {
        // given
        reader =
            EvaluateOperationExpression(
                "capitalize('teste')"
            )
        val expected = "Teste"

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun should_solve_substring_operation() {
        // given
        reader =
            EvaluateOperationExpression(
                "substr('teste', 0, 2)"
            )
        val expected = "te"

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun should_solve_substring_operation_first_char() {
        // given
        reader =
            EvaluateOperationExpression(
                "substr('teste', 0, 0)"
            )
        val expected = ""

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun should_solve_substring_operation_wrong_parameters() {
        // given
        reader =
            EvaluateOperationExpression(
                "substr('teste', 8, 50)"
            )
        val expected = "teste"

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun should_solve_substring_and_capitalize_operation() {
        // given
        reader =
            EvaluateOperationExpression(
                "substr(capitalize('teste'), 0, 2)"
            )
        val expected = "Te"

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun should_solve_capitalize_and_substring_operation() {
        // given
        reader =
            EvaluateOperationExpression(
                "capitalize(substr('teste', 0, 2))"
            )
        val expected = "Te"

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun should_solve_complex_concat_operation() {
        // given
        reader =
            EvaluateOperationExpression(
                "concat(capitalize(condition(lt(sum(6, 3), 5), 'nome1true', 'nome2false')), capitalize(condition(lt(sum(6, 3), 5), ' sobrenome2true', ' sobrenome2false')))"
            )
        val expected = "Nome2false Sobrenome2false"

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun should_solve_uppercase_operation() {
        // given
        reader =
            EvaluateOperationExpression(
                "uppercase('teste')"
            )
        val expected = "TESTE"

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun should_solve_capitalize_operation_in_uppercase() {
        // given
        reader =
            EvaluateOperationExpression(
                "capitalize('TESTE')"
            )
        val expected = "Teste"

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test(expected = Exception::class)
    fun should_throw_exception_sum_operation_with_no_values() {
        // given
        reader =
            EvaluateOperationExpression(
                "sum()"
            )
        val expected = 0

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test(expected = Exception::class)
    fun should_throw_exception_reserved_name_start_function() {
        // given
        reader =
            EvaluateOperationExpression(
                "sum(0, null())"
            )
        //val expected = ReservedParameterException("")

        // when
        val result = reader.evaluate()

        assertEquals(null, result)
    }

    @Test(expected = Exception::class)
    fun should_solve_number_operation_with_no_value() {
        // given
        reader =
            EvaluateOperationExpression(
                "sum(0, )"
            )
        val expected = 0

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test(expected = Exception::class)
    fun should_throw_exception_incorrect_parameters_number() {
        // given
        reader =
            EvaluateOperationExpression(
                "sum(a, b)"
            )

        // when
        reader.evaluate()

        // then
    }

    @Test(expected = Exception::class)
    fun should_throw_exception_incorrect_parameters_substring_operation() {
        // given
        reader =
            EvaluateOperationExpression(
                "substr('teste', , )"
            )

        // when
        reader.evaluate()

        // then
    }

    @Test
    fun should_solve_condition_operation_with_false_number() {
        // given
        reader =
            EvaluateOperationExpression(
                "condition(lt(sum(6, 3), 5), 1, 0)"
            )
        val expected = 0

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun should_solve_array_operation_insert_add_item() {
        // given
        reader =
            EvaluateOperationExpression(
                "insert({1, 2, 3, 4}, 5, 4)"
            )
        val expected = 5

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, (result as List<*>).size)
    }

    @Test
    fun should_solve_array_operation_insert_replace_item() {
        // given
        reader =
            EvaluateOperationExpression(
                "insert({1, 2, 3, 4}, 5, 4)"
            )
        val expected = 5

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, (result as List<*>).size)
    }

    @Test
    fun should_solve_condition_operation_and_add_in_array() {
        // given
        reader =
            EvaluateOperationExpression(
                "insert({1, 2, 3, 4}, condition(lt(sum(6, 3), 5), 5, 6), 4)"
            )
        val expected = 5
        val elementInserted = 6

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, (result as List<*>).size)
        assertEquals(elementInserted, result[4])
    }

    @Test(expected = Exception::class)
    fun should_throw_exception_incorrect_function_delimiter() {
        // given
        reader =
            EvaluateOperationExpression(
                "sum(0, 0"
            )

        // when
        reader.evaluate()

        // then
    }

    @Test(expected = Exception::class)
    fun should_throw_exception_incorrect_array_delimiter() {
        // given
        reader =
            EvaluateOperationExpression(
                "insert({1, 2, 3, 4, 5, 4)"
            )

        // when
        val result = reader.evaluate()

        // then
    }

    @Test
    fun should_solve_array_operation_remove_item_found() {
        // given
        reader =
            EvaluateOperationExpression(
                "remove({1, 2, 3, 4}, 2)"
            )
        val expected = 3

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, (result as List<*>).size)
    }

    @Test
    fun should_solve_array_operation_remove_item_not_found() {
        // given
        reader =
            EvaluateOperationExpression(
                "remove({1, 2, 3, 4}, 5)"
            )
        val expected = 4

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, (result as List<*>).size)
    }

    @Test
    fun should_solve_array_operation_remove_index_item_found() {
        // given
        reader =
            EvaluateOperationExpression(
                "removeIndex({1, 2, 3, 4}, 0)"
            )
        val expected = 3
        val firstElement = 2

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, (result as List<*>).size)
        assertEquals(firstElement, result[0])
    }

    @Test
    fun should_solve_array_operation_remove_index_item_not_found() {
        // given
        reader =
            EvaluateOperationExpression(
                "removeIndex({1, 2, 3, 4}, 50)"
            )
        val expected = 3

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, (result as List<*>).size)
    }

    @Test
    fun should_solve_array_operation_insert_item() {
        // given
        reader =
            EvaluateOperationExpression(
                "insert({1, 2, 3, 4}, 5, 2)"
            )
        val expected = 4

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, (result as List<*>).size)
    }

    @Test
    fun should_solve_array_operation_includes_item() {
        // given
        reader =
            EvaluateOperationExpression(
                "includes({1, 2, 3, 4}, 5)"
            )
        val expected = 5

        // when
        val result = reader.evaluate()

        // then
        assertEquals(expected, (result as List<*>).size)
    }

    @Test(expected = Exception::class)
    fun should_throw_exception_when_not_string_parameter_non_substring_operation() {
        // given
        reader =
            EvaluateOperationExpression(
                "concat('abc', 0)"
            )

        // when
        val result = reader.evaluate()

        // then
    }

    @Test(expected = Exception::class)
    fun should_throw_exception_substring_operation_without_string() {
        // given
        reader =
            EvaluateOperationExpression(
                "substr(abc, 0, 0)"
            )

        // when
        val result = reader.evaluate()

        // then
    }

    @Test(expected = Exception::class)
    fun should_throw_exception_substring_operation_without_required_args() {
        // given
        reader =
            EvaluateOperationExpression(
                "substr('abc', 0)"
            )

        // when
        val result = reader.evaluate()

        // then
    }

    @Test(expected = Exception::class)
    fun should_throw_exception_array_operation_without_array() {
        // given
        reader =
            EvaluateOperationExpression(
                "insert('abc', 'a', 3)"
            )

        // when
        val result = reader.evaluate()

        // then
    }

    @Test(expected = Exception::class)
    fun should_throw_exception_array_operation_without_required_args() {
        // given
        reader =
            EvaluateOperationExpression(
                "insert({'a', 'b'}, 'c')"
            )

        // when
        val result = reader.evaluate()

        // then
    }

    @Test(expected = Exception::class)
    fun should_throw_exception_array_operation_different_insert_without_required_args() {
        // given
        reader =
            EvaluateOperationExpression(
                "includes({'a', 'b'})"
            )

        // when
        val result = reader.evaluate()

        // then
    }

    @Test(expected = Exception::class)
    fun should_throw_exception_array_removeIndex_and_insert_operations_incorrect_index() {
        // given
        reader =
            EvaluateOperationExpression(
                "removeIndex({'a', 'b'}, 'c')"
            )

        // when
        val result = reader.evaluate()

        // then
    }

    @Test
    fun should_solve_isEmpty_operation_no_value() {
        // given
        reader =
            EvaluateOperationExpression(
                "isEmpty()"
            )

        // when
        val result = reader.evaluate()

        // then
        assertEquals(true, result)
    }

    @Test
    fun should_solve_isEmpty_operation_string_true() {
        // given
        reader =
            EvaluateOperationExpression(
                "isEmpty(concat('',''))"
            )

        // when
        val result = reader.evaluate()

        // then
        assertEquals(true, result)
    }

    @Test
    fun should_solve_isEmpty_operation_string_array() {
        // given
        reader =
            EvaluateOperationExpression(
                "isEmpty(remove({'a'},'a'))"
            )

        // when
        val result = reader.evaluate()

        // then
        assertEquals(true, result)
    }

    @Test
    fun should_solve_isEmpty_operation_false() {
        // given
        reader =
            EvaluateOperationExpression(
                "isEmpty(concat('a','b'))"
            )

        // when
        val result = reader.evaluate()

        // then
        assertEquals(false, result)
    }

    @Test
    fun should_solve_length_operation_string() {
        // given
        reader =
            EvaluateOperationExpression(
                "length(concat('a','b'))"
            )

        // when
        val result = reader.evaluate()

        // then
        assertEquals(2, result)
    }

    @Test
    fun should_solve_length_operation_array() {
        // given
        reader =
            EvaluateOperationExpression(
                "length(includes({1, 2, 3, 4}, 5))"
            )

        // when
        val result = reader.evaluate()

        // then
        assertEquals(5, result)
    }

    @Test(expected = Exception::class)
    fun should_throw_exception_invalid_operation() {
        // given
        reader =
            EvaluateOperationExpression(
                "amigo()"
            )

        // when
        val result = reader.evaluate()

        // then
    }
}
