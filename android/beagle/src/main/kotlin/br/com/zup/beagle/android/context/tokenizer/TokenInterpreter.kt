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

internal class TokenInterpreter(value: String) {

    private val reader: TokenReader = TokenReader(value)
    private var lastChar = Char.MIN_VALUE

    fun nextToken(): Token? {
        if (lastChar == Char.MIN_VALUE || lastChar.isWhitespace()) {
            lastChar = reader.ignoreWhitespace()
        }
        return readToken()
    }

    private fun readToken(): Token? {
        return when (lastChar) {
            '\'' -> readString()
            '(' -> {
                resetLastChar()
                tokenOpenBracket()
            }
            ')' -> {
                resetLastChar()
                tokenOfCloseBracket()
            }
            ',' -> {
                resetLastChar()
                tokenOfComma()
            }
            else -> readVariableOrFunction()
        }
    }

    private fun readString(): Token? {
        val str = readUnescapedString()
        lastChar = Char.MIN_VALUE
        return tokenOfString(str)
    }

    private fun readUnescapedString(): String {
        val sb = StringBuilder()
        var specialCharacterFound = false
        loop@ while (lastChar.toInt() != -1) {
            lastChar = reader.read().toChar()
            when (lastChar) {
                '\uFFFF' -> {
                    throw IllegalArgumentException("Invalid string value: $sb")
                }
                '\'' -> {
                    if (specialCharacterFound) {
                        sb.deleteCharAt(sb.length - 1)
                        sb.append(lastChar)
                    } else {
                        break@loop
                    }
                }
                else -> {
                    sb.append(lastChar)
                }
            }

            specialCharacterFound = lastChar == '\\'
        }
        return sb.toString()
    }

    private fun readVariableOrFunction(): Token? {
        val sb = StringBuilder()
        while (lastChar.isJavaIdentifierPart() || isExpressionCharacter()) {
            sb.append(lastChar)
            lastChar = reader.read().toChar()
        }
        if (Character.isWhitespace(lastChar)) {
            lastChar = reader.ignoreWhitespace()
        }

        val value = sb.toString()

        return if (lastChar == '(') {
            lastChar = Char.MIN_VALUE
            tokenOfFunctionStart(sb.toString())
        } else if (value == "true" || value == "false") {
            tokenOfBoolean(value.toBoolean())
        } else if (value == "null") {
            tokenOfNull()
        } else {
            value.getTokenNumber() ?: tokenOfBinding(value)
        }
    }

    private fun isExpressionCharacter() = lastChar == '.' || lastChar == '[' || lastChar == ']'

    private fun resetLastChar() {
        lastChar = Char.MIN_VALUE
    }

    private fun String.getTokenNumber(): TokenNumber? {
        val value: Number? = try {
            this.toInt()
        } catch (ex: NumberFormatException) {
            try {
                this.toDouble()
            } catch (ex: NumberFormatException) {
                null
            }
        }

        return if (value != null) tokenOfNumber(value) else null
    }
}