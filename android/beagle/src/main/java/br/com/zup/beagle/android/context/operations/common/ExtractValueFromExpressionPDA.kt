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
        if (extractValueTypes == ExtractValueTypes.OPERATION) {
            startDelimiterChar = GrammarChars.OPEN_PARENTHESES
            endDelimiterChar = GrammarChars.CLOSE_PARENTHESES
        } else {
            startDelimiterChar = GrammarChars.OPEN_BRACKET
            endDelimiterChar = GrammarChars.CLOSE_BRACKET
        }
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
