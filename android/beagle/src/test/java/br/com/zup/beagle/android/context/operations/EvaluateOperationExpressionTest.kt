package br.com.zup.beagle.android.context.operations

import br.com.zup.beagle.android.context.operations.core.EvaluateOperationExpression
import br.com.zup.beagle.android.context.operations.core.OperationExpressionReader
import br.com.zup.beagle.android.context.operations.exception.ContextOperationException
import br.com.zup.beagle.android.context.operations.exception.strategy.ExceptionOperationTypes
import br.com.zup.beagle.android.context.operations.exception.strategy.ExceptionParameterTypes
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class EvaluateOperationExpressionTest {

    private lateinit var evaluate: EvaluateOperationExpression
    private lateinit var reader: OperationExpressionReader
    private val colorRedResult = "colorRed"
    private val colorGreenResult = "ColorGreen"
    private val integerZeroResult = 0
    private val integerThreeResult = 3
    private val integerFourResult = 4
    private val integerFiveResult = 5
    private val capitalizeResultCompleteName = "Name Secondname Lastname"
    private val capitalizeTwoChars = "Te"

    @Before
    fun setUp() {
        reader = OperationExpressionReader()
    }

    @Test
    fun should_solve_sum_operation() {
        // given
        evaluate = EvaluateOperationExpression(reader, "sum(1, 2)")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(integerThreeResult, result)
    }

    @Test
    fun should_solve_subtract_operation() {
        // given
        evaluate = EvaluateOperationExpression(reader, "subtract(5, 2)")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(integerThreeResult, result)
    }

    @Test
    fun should_solve_multiply_operation() {
        // given
        evaluate = EvaluateOperationExpression(reader, "multiply(1, 3)")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(integerThreeResult, result)
    }

    @Test
    fun should_solve_divide_operation() {
        // given
        evaluate = EvaluateOperationExpression(reader, "divide(9, 3)")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(integerThreeResult, result)
    }

    @Test
    fun should_solve_anyNumberOperation_as_double() {
        // given
        evaluate = EvaluateOperationExpression(reader, "divide(1.0, 10.0)")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(0.1, result)
    }

    @Test
    fun should_solve_condition_operation_with_false() {
        // given
        evaluate = EvaluateOperationExpression(reader, "condition(lt(sum(6, 3), 5), 'colorRed', 'ColorGreen')")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(colorGreenResult, result)
    }

    @Test
    fun should_solve_gt_operation() {
        // given
        evaluate = EvaluateOperationExpression(reader, "condition(gt(sum(6, 3), 5), 'colorRed', 'ColorGreen')")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(colorRedResult, result)
    }

    @Test
    fun should_solve_gte_operation() {
        // given
        evaluate = EvaluateOperationExpression(reader, "condition(gte(sum(2, 3), 5), 'colorRed', 'ColorGreen')")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(colorRedResult, result)
    }

    @Test
    fun should_solve_lte_operation() {
        // given
        evaluate = EvaluateOperationExpression(reader, "condition(lte(sum(2, 3), 5), 'colorRed', 'ColorGreen')")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(colorRedResult, result)
    }

    @Test
    fun should_solve_eq_operation() {
        // given
        evaluate = EvaluateOperationExpression(reader, "condition(eq(sum(2, 3), 5), 'colorRed', 'ColorGreen')")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(colorRedResult, result)
    }

    @Test
    fun should_solve_and_operation_false() {
        // given
        evaluate = EvaluateOperationExpression(reader, 
            "condition(and(eq(sum(2, 3), 5), eq(sum(2, 4), 5)), 'colorRed', 'ColorGreen')"
        )

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(colorGreenResult, result)
    }

    @Test
    fun should_solve_and_operation_true() {
        // given
        evaluate = EvaluateOperationExpression(reader, 
            "condition(and(eq(sum(2, 3), 5), eq(5, sum(2, 3))), 'colorRed', 'ColorGreen')"
        )

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(colorRedResult, result)
    }

    @Test
    fun should_solve_not_operation_true_and_return_false() {
        // given
        evaluate = EvaluateOperationExpression(reader, 
            "condition(not(and(eq(sum(2, 3), 5), eq(5, sum(2, 3)))), 'colorRed', 'ColorGreen')"
        )

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(colorGreenResult, result)
    }

    @Test
    fun should_solve_or_operation_true() {
        // given
        evaluate = EvaluateOperationExpression(reader, 
            "condition(or(eq(sum(4, 3), 5), eq(5, sum(2, 3))), 'colorRed', 'ColorGreen')"
        )

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(colorRedResult, result)
    }

    @Test
    fun should_solve_or_operation_false() {
        // given
        evaluate = EvaluateOperationExpression(reader, 
                "condition(or(eq(sum(4, 3), 5), eq(5, sum(4, 3))), 'colorRed', 'ColorGreen')"
        )

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(colorGreenResult, result)
    }

    @Test
    fun should_solve_condition_operation_with_true() {
        // given
        evaluate = EvaluateOperationExpression(reader, "condition(eq(sum(3, 2), 5), 'colorRed', 'colorGreen')")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(colorRedResult, result)
    }

    @Test
    fun should_solve_concat_operation_keeping_whitespaces() {
        // given
        evaluate = EvaluateOperationExpression(reader, 
            "concat('value1', 'value2 whitespace', ' sidespace value3 sidespace ')"
        )
        val expected = "value1value2 whitespace sidespace value3 sidespace "

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun should_solve_concat_operation_and_capitalize_whitespace_at_beginning() {
        // given
        evaluate = EvaluateOperationExpression(reader, 
            "concat(capitalize('name'), capitalize(' secondname'), capitalize(' lastname'))"
        )

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(capitalizeResultCompleteName, result)
    }

    @Test
    fun should_solve_concat_operation_and_capitalize_whitespace_at_end() {
        // given
        evaluate = EvaluateOperationExpression(reader, 
            "concat(capitalize('name '), capitalize('secondname '), capitalize('lastname'))"
        )

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(capitalizeResultCompleteName, result)
    }

    @Test
    fun should_solve_lowercase_operation() {
        // given
        evaluate = EvaluateOperationExpression(reader, "lowercase('TEST')")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals("test", result)
    }

    @Test
    fun should_solve_capitalize_operation() {
        // given
        evaluate = EvaluateOperationExpression(reader, "capitalize('test')")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals("Test", result)
    }

    @Test
    fun should_solve_substring_operation() {
        // given
        evaluate = EvaluateOperationExpression(reader, "substr('test', 0, 2)")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals("te", result)
    }

    @Test
    fun should_solve_substring_operation_empty() {
        // given
        evaluate = EvaluateOperationExpression(reader, "substr('test', 0, 0)")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals("", result)
    }

    @Test
    fun should_solve_substring_operation_length_greater_then_char_count() {
        // given
        evaluate = EvaluateOperationExpression(reader, "substr('test', 8, 50)")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals("test", result)
    }

    @Test
    fun should_solve_substring_and_capitalize_operation() {
        // given
        evaluate = EvaluateOperationExpression(reader, "substr(capitalize('test'), 0, 2)")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(capitalizeTwoChars, result)
    }

    @Test
    fun should_solve_capitalize_and_substring_operation() {
        // given
        evaluate = EvaluateOperationExpression(reader, "capitalize(substr('teste', 0, 2))")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(capitalizeTwoChars, result)
    }

    @Test
    fun should_solve_complex_concat_operation() {
        // given
        evaluate = EvaluateOperationExpression(reader, "concat(capitalize(condition(lt(sum(6, 3), 5), 'name1true', " +
            "'name1false')), capitalize(condition(lt(sum(6, 3), 5), ' secondname2true', ' secondname2false')))"
        )

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals("Name1false Secondname2false", result)
    }

    @Test
    fun should_solve_uppercase_operation() {
        // given
        evaluate = EvaluateOperationExpression(reader, "uppercase('test')")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals("TEST", result)
    }

    @Test
    fun should_solve_capitalize_operation_in_uppercase() {
        // given
        evaluate = EvaluateOperationExpression(reader, "capitalize('TEST')")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals("Test", result)
    }

    @Test
    fun should_throw_exception_sum_operation_with_no_values() {
        // given
        val expression= "sum()"

        // when
        val result = assertFailsWith<ContextOperationException> {
            EvaluateOperationExpression(reader, expression).evaluate()
        }

        // then
        assertEquals(ExceptionParameterTypes.EMPTY, result.exceptionTypes)
    }

    @Test
    fun should_throw_exception_reserved_name_start_function() {
        // given
        val expression = "sum(0, null())"

        // when
        val result = assertFailsWith<ContextOperationException> {
            EvaluateOperationExpression(reader, expression).evaluate()
        }

        // then
        assertEquals(ExceptionOperationTypes.INVALID_OPERATION, result.exceptionTypes)
    }

    @Test
    fun should_solve_number_operation_with_no_value() {
        // given
        val expression = "sum(0, )"

        // when
        val result = assertFailsWith<ContextOperationException> {
            EvaluateOperationExpression(reader, expression).evaluate()
        }

        // then
        assertEquals(ExceptionParameterTypes.EMPTY, result.exceptionTypes)
    }

    @Test
    fun should_throw_exception_incorrect_parameters_number() {
        // given
        val expression = "sum(a, b)"

        // when
        val result = assertFailsWith<ContextOperationException> {
            EvaluateOperationExpression(reader, expression).evaluate()
        }

        // then
        assertEquals(ExceptionParameterTypes.NUMBER, result.exceptionTypes)
    }

    @Test
    fun should_throw_exception_incorrect_parameters_substring_operation() {
        // given
        val expression = "substr('teste', , )"

        // when
        val result = assertFailsWith<ContextOperationException> {
            EvaluateOperationExpression(reader, expression).evaluate()
        }

        // then
        assertEquals(ExceptionParameterTypes.EMPTY, result.exceptionTypes)
    }

    @Test
    fun should_solve_condition_operation_with_false_number() {
        // given
        evaluate = EvaluateOperationExpression(reader, "condition(lt(sum(6, 3), 5), 1, 0)")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(integerZeroResult, result)
    }

    @Test
    fun should_solve_array_operation_insert_add_item() {
        // given
        evaluate = EvaluateOperationExpression(reader, "insert({1, 2, 3, 4}, 5, 4)")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(integerFiveResult, (result as List<*>).size)
    }

    @Test
    fun should_solve_array_operation_insert_replace_item() {
        // given
        evaluate = EvaluateOperationExpression(reader, "insert({1, 2, 3, 4}, 5, 4)")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(integerFiveResult, (result as List<*>).size)
    }

    @Test
    fun should_solve_condition_operation_and_add_in_array() {
        // given
        evaluate = EvaluateOperationExpression(
            reader,
            "insert({1, 2, 3, 4}, condition(lt(sum(6, 3), 5), 5, 6), 4)"
        )

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(integerFiveResult, (result as List<*>).size)
        assertEquals(6, result[4])
    }

    @Test
    fun should_throw_exception_incorrect_function_delimiter() {
        // given
        val expression = "sum(0, 0"

        // when
        val result = assertFailsWith<ContextOperationException> {
            EvaluateOperationExpression(reader, expression).evaluate()
        }

        // then
        assertEquals(ExceptionOperationTypes.MISSING_DELIMITERS, result.exceptionTypes)
    }

    @Test
    fun should_throw_exception_incorrect_array_delimiter() {
        // given
        val expression = "insert({1, 2, 3, 4, 5, 4)"

        // when
        val result = assertFailsWith<ContextOperationException> {
            EvaluateOperationExpression(reader, expression).evaluate()
        }

        // then
        assertEquals(ExceptionOperationTypes.MISSING_DELIMITERS, result.exceptionTypes)
    }

    @Test
    fun should_solve_array_operation_remove_item_found() {
        // given
        evaluate = EvaluateOperationExpression(reader, "remove({1, 2, 3, 4}, 2)")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(integerThreeResult, (result as List<*>).size)
    }

    @Test
    fun should_solve_array_operation_remove_item_not_found() {
        // given
        evaluate = EvaluateOperationExpression(reader, "remove({1, 2, 3, 4}, 5)")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(integerFourResult, (result as List<*>).size)
    }

    @Test
    fun should_solve_array_operation_remove_index_item_found() {
        // given
        evaluate = EvaluateOperationExpression(reader, "removeIndex({1, 2, 3, 4}, 0)")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(integerThreeResult, (result as List<*>).size)
        assertEquals(2, result[0])
    }

    @Test
    fun should_solve_array_operation_remove_index_item_not_found() {
        // given
        evaluate = EvaluateOperationExpression(reader, "removeIndex({1, 2, 3, 4}, 50)")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(integerThreeResult, (result as List<*>).size)
    }

    @Test
    fun should_solve_array_operation_insert_item() {
        // given
        evaluate = EvaluateOperationExpression(reader, "insert({1, 2, 3, 4}, 5, 2)")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(integerFourResult, (result as List<*>).size)
    }

    @Test
    fun should_solve_array_operation_includes_item() {
        // given
        evaluate = EvaluateOperationExpression(reader, "includes({1, 2, 3, 4}, 5)")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(integerFiveResult, (result as List<*>).size)
    }

    @Test
    fun should_throw_exception_when_not_string_parameter_non_substring_operation() {
        // given
        val expression = "concat('abc', 0)"

        // when
        val result = assertFailsWith<ContextOperationException> {
            EvaluateOperationExpression(reader, expression).evaluate()
        }

        // then
        assertEquals(ExceptionParameterTypes.STRING, result.exceptionTypes)
    }

    @Test
    fun should_throw_exception_substring_operation_without_string() {
        // given
        val expression = "substr(abc, 0, 0)"

        // when
        val result = assertFailsWith<ContextOperationException> {
            EvaluateOperationExpression(reader, expression).evaluate()
        }

        // then
        assertEquals(ExceptionParameterTypes.STRING, result.exceptionTypes)
    }

    @Test
    fun should_throw_exception_substring_operation_without_required_args() {
        // given
        val expression = "substr('abc', 0)"

        // when
        val result = assertFailsWith<ContextOperationException> {
            EvaluateOperationExpression(reader, expression).evaluate()
        }

        // then
        assertEquals(ExceptionParameterTypes.REQUIRED_ARGS, result.exceptionTypes)
    }

    @Test
    fun should_throw_exception_array_operation_without_array() {
        // given
        val expression = "insert('abc', 'a', 3)"

        // when
        val result = assertFailsWith<ContextOperationException> {
            EvaluateOperationExpression(reader, expression).evaluate()
        }

        // then
        assertEquals(ExceptionParameterTypes.ARRAY, result.exceptionTypes)
    }

    @Test
    fun should_throw_exception_array_operation_without_required_args() {
        // given
        val expression = "insert({'a', 'b'}, 'c')"

        // when
        val result = assertFailsWith<ContextOperationException> {
            EvaluateOperationExpression(reader, expression).evaluate()
        }

        // then
        assertEquals(ExceptionParameterTypes.REQUIRED_ARGS, result.exceptionTypes)
    }

    @Test
    fun should_throw_exception_array_operation_different_insert_without_required_args() {
        // given
        val expression = "includes({'a', 'b'})"

        // when
        val result = assertFailsWith<ContextOperationException> {
            EvaluateOperationExpression(reader, expression).evaluate()
        }

        // then
        assertEquals(ExceptionParameterTypes.REQUIRED_ARGS, result.exceptionTypes)
    }

    @Test
    fun should_throw_exception_array_removeIndex_and_insert_operations_incorrect_index() {
        // given
        val expression = "removeIndex({'a', 'b'}, 'c')"

        // when
        val result = assertFailsWith<ContextOperationException> {
            EvaluateOperationExpression(reader, expression).evaluate()
        }

        // then
        assertEquals(ExceptionParameterTypes.INDEX, result.exceptionTypes)
    }

    @Test
    fun should_solve_isEmpty_operation_no_value() {
        // given
        evaluate = EvaluateOperationExpression(reader, "isEmpty()")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(true, result)
    }

    @Test
    fun should_solve_isEmpty_operation_string_true() {
        // given
        evaluate = EvaluateOperationExpression(reader, "isEmpty(concat('',''))")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(true, result)
    }

    @Test
    fun should_solve_isEmpty_operation_string_array() {
        // given
        evaluate = EvaluateOperationExpression(reader, "isEmpty(remove({'a'},'a'))")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(true, result)
    }

    @Test
    fun should_solve_isEmpty_operation_false() {
        // given
        evaluate = EvaluateOperationExpression(reader, "isEmpty(concat('a','b'))")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(false, result)
    }

    @Test
    fun should_solve_length_operation_string() {
        // given
        evaluate = EvaluateOperationExpression(reader, "length(concat('a','b'))")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(2, result)
    }

    @Test
    fun should_solve_length_operation_array() {
        // given
        evaluate = EvaluateOperationExpression(reader, "length(includes({1, 2, 3, 4}, 5))")

        // when
        val result = evaluate.evaluate()

        // then
        assertEquals(integerFiveResult, result)
    }

    @Test
    fun should_throw_exception_invalid_operation() {
        // given
        val expression = "friend()"

        // when
        val result = assertFailsWith<ContextOperationException> {
            EvaluateOperationExpression(reader, expression).evaluate()
        }

        // then
        assertEquals(ExceptionOperationTypes.NOT_FOUND, result.exceptionTypes)
    }
}
