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

import br.com.zup.beagle.android.context.operations.common.read.ReadLogicFactory
import br.com.zup.beagle.android.context.operations.grammar.Constants

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

    internal fun getValue() = findDelimiters()

    private fun findDelimiters() : String {
        inputValue.forEachIndexed { index, char ->
            if (foundStartDelimiter(char)) {
                setStartDelimiter(index)
            } else if (foundEndDelimiter(char)) {
                return getResult(index)
            }
        }

        return Constants.EMPTY_VALUE
    }

    private fun setStartDelimiter(index: Int) {
        delimiterStartIndex = index
    }

    private fun getResult(endIndex: Int) =
        ReadLogicFactory.create(
            inputValue,
            extractValueTypes,
            delimiterStartIndex,
            endIndex
        ).extractValue()

    private fun foundStartDelimiter(char: Char) = char == extractValueTypes.start

    private fun foundEndDelimiter(char: Char) = char == extractValueTypes.end
}
