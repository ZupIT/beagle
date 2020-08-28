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

package br.com.zup.beagle.android.context.tokenizer

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class TokenParserTest {

    private val tokenParser = TokenParser()

    @Test
    fun parse_should_return_string() {
        // Given
        val expression = "'hello'"

        // When
        val result = tokenParser.parse(expression)

        // Then
        assertEquals(expression, result.value)
        assertTrue { result.token is TokenString }
        assertEquals("hello", (result.token as TokenString).value)
    }

    @Test
    fun parse_should_return_string_throw_exception_with_invalid_string() {
        // Given
        val expression = "'hello"

        // When Then
        assertFails { tokenParser.parse(expression) }
    }

    @Test
    fun parse_should_return_null() {
        // Given
        val expression = "null"

        // When
        val result = tokenParser.parse(expression)

        // Then
        assertEquals(expression, result.value)
        assertTrue { result.token is TokenNull }
    }

    @Test
    fun parse_should_return_true() {
        // Given
        val expression = "true"

        // When
        val result = tokenParser.parse(expression)

        // Then
        assertTrue { result.token is TokenBoolean }
        assertEquals(true, result.token.value)
    }

    @Test
    fun parse_should_return_false() {
        // Given
        val expression = "false"

        // When
        val result = tokenParser.parse(expression)

        // Then
        assertTrue { result.token is TokenBoolean }
        assertEquals(false, result.token.value)
    }

    @Test
    fun parse_should_return_1() {
        // Given
        val expression = "1"

        // When
        val result = tokenParser.parse(expression)

        // Then
        assertTrue { result.token is TokenNumber }
        assertEquals(1, result.token.value)
    }

    @Test
    fun parse_should_return_binding() {
        // Given
        val expression = "binding"

        // When
        val result = tokenParser.parse(expression)

        // Then
        assertTrue { result.token is TokenBinding }
        assertEquals(expression, result.token.value)
    }

    @Test
    fun parse_should_return_binding_with_path() {
        // Given
        val expression = "bindingId.bindingValue"

        // When
        val result = tokenParser.parse(expression)

        // Then
        assertTrue { result.token is TokenBinding }
        assertEquals(expression, result.token.value)
    }

    @Test
    fun parse_should_return_binding_with_path_list() {
        // Given
        val expression = "bindingId.bindingValue[0]"

        // When
        val result = tokenParser.parse(expression)

        // Then
        assertTrue { result.token is TokenBinding }
        assertEquals(expression, result.token.value)
    }

    @Test
    fun parse_should_return_binding_with_number() {
        // Given
        val expression = "1bind2ingId3.4bind5ingValue6"

        // When
        val result = tokenParser.parse(expression)

        // Then
        assertTrue { result.token is TokenBinding }
        assertEquals(expression, result.token.value)
    }

    @Test
    fun parse_should_return_1_0() {
        // Given
        val expression = "1.0"

        // When
        val result = tokenParser.parse(expression)

        // Then
        assertTrue { result.token is TokenNumber }
        assertEquals(1.0, result.token.value)
    }

    @Test
    fun parse_should_return_function_with_two_parameters() {
        // Given
        val expression = "gt(1, 2)"

        // When
        val result = tokenParser.parse(expression)

        // Then
        assertTrue { result.token is TokenFunction }
        val function = result.token as TokenFunction
        assertTrue { function.value[0] is TokenNumber }
        assertEquals(1, function.value[0].value)
        assertTrue { function.value[1] is TokenNumber }
        assertEquals(2, function.value[1].value)
    }

    @Test
    fun parse_should_ignore_spaces() {
        // Given
        val value = "sum  ( 4 ,   2  )   "

        // When
        val result = tokenParser.parse(value)

        // Then
        assertNotNull(result)
    }

    @Test
    fun parse_should_throw_exception_when_function_is_invalid() {
        assertTrue { tokenParser.parse("gt(1, 2").token is InvalidToken }
        assertTrue { tokenParser.parse("gt(2, 4))").token is InvalidToken }
        assertTrue { tokenParser.parse("sum(4(2)").token is InvalidToken }
        assertTrue { tokenParser.parse("sum(2))").token is InvalidToken }
        assertTrue { tokenParser.parse("sum(,),)").token is InvalidToken }
    }

    @Test
    fun parse_should_return_function_inside_function() {
        // Given
        val expression = "gt(gt(1, 3), 2)"

        // When
        val result = tokenParser.parse(expression)

        // Then
        assertTrue { result.token is TokenFunction }
        val function = result.token as TokenFunction
        assertTrue { function.value[0] is TokenFunction }
        val innerFunction = function.value[0] as TokenFunction
        assertTrue { innerFunction.value[0] is TokenNumber }
        assertEquals(1, innerFunction.value[0].value)
        assertTrue { innerFunction.value[1] is TokenNumber }
        assertEquals(3, innerFunction.value[1].value)
        assertTrue { function.value[1] is TokenNumber }
        assertEquals(2, function.value[1].value)
    }
}