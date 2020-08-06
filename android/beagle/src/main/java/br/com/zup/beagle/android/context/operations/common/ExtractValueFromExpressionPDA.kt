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

package br.com.zup.beagle.android.context.operations.common

import br.com.zup.beagle.android.context.operations.grammar.GrammarChars

/**
 * This class is able to read an Expression/Array input and output the value using PDA
 *
 * @param inputValue Input value is the string to be read by the Algorithm
 *
 * @param extractValueTypes The input method to algorithm know's the expression
 *                          or array that must be read
 *
 * @see ExtractValueTypes.OPERATION Read the operation input e output the value
 *
 * @see ExtractValueTypes.ARRAY_PARAMETER Read the array value in a operation
 *
 * **/

internal class ExtractValueFromExpressionPDA(
    private val inputValue: String,
    private val extractValueTypes: ExtractValueTypes
) {

    private var delimiterStartIndex: Int = 0
    var startDelimiterChar: Char? = null
    var endDelimiterChar: Char? = null

    init {
        startDelimiterChar = extractValueTypes.start
        endDelimiterChar = extractValueTypes.end
    }

    internal fun getValue() : String {
        inputValue.forEachIndexed { index, char ->
            if (char == startDelimiterChar) {
                delimiterStartIndex = index
            } else if (char == endDelimiterChar) {
                return getStartOfValue(inputValue, index)
            }
        }

        return ""
    }

    private fun getStartOfValue(value: String, endIndex: Int) : String {
        var startIndex: Int

        if (extractValueTypes == ExtractValueTypes.OPERATION) {
            startIndex = delimiterStartIndex - 1
            while (startIndex > 0) {
                if (value[startIndex] != startDelimiterChar &&
                    value[startIndex] != GrammarChars.COMMA) {
                    startIndex--
                } else {
                    break
                }
            }

            if (currentCharIsCommaOrParentheses(startIndex)) {
                startIndex += 1
            }

            return value.substring(startIndex, endIndex + 1)
        } else {
            startIndex = 1
            while (startIndex > 0) {
                if (value[startIndex] != startDelimiterChar) {
                    startIndex--
                }
            }

            return value.substring(startIndex, endIndex + 1)
        }
    }

    private fun currentCharIsCommaOrParentheses(startIndex: Int) = startIndex > 0
}
