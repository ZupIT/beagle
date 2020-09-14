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

package br.com.zup.beagle.android.context

import br.com.zup.beagle.android.utils.BeagleRegex

class ContextExpressionReplacer {

    fun replace(
        bind: Bind.Expression<*>,
        evaluatedExpressions: MutableMap<String, Any>
    ): String {
        val stringToEvaluate = bind.value
        val regexToMatchAllExpressions = BeagleRegex.EXPRESSION_REGEX

        val listInReverseOrderOfStringsToEvaluate = stringToEvaluate
            .split(BeagleRegex.FULL_MATCH_EXPRESSION_SEPARATOR_REGEX)
            .reversed()
            .toMutableList()

        val evaluatedItemsInReverseOrder = mutableListOf<String>()
        listInReverseOrderOfStringsToEvaluate.forEachIndexed { index, actualStringToEvaluate ->

            val matchesOccurrenceInThisItem = regexToMatchAllExpressions.findAll(actualStringToEvaluate)
            if (matchesOccurrenceInThisItem.count() > 0) {
                replaceExpressionInOccurrences(
                    matchesOccurrenceInThisItem,
                    evaluatedExpressions,
                    actualStringToEvaluate,
                    evaluatedItemsInReverseOrder
                )
            } else {
                joinActualMatchWithNextOne(index, listInReverseOrderOfStringsToEvaluate, actualStringToEvaluate)
            }
        }

        val revertedEvaluatedString = evaluatedItemsInReverseOrder
            .toList()
            .reversed()

        return joinStringsEvaluatedAndNormalizeSlashesOccurrence(revertedEvaluatedString)

    }

    private fun replaceExpressionInOccurrences(
        matchesOccurrenceInThisItem: Sequence<MatchResult>,
        evaluatedExpressions: MutableMap<String, Any>,
        actualStringToEvaluate: String,
        evaluatedItemsInReverseOrder: MutableList<String>
    ) {
        matchesOccurrenceInThisItem.iterator().forEach {
            val actualMatch = it.value
            val quantityOfSlashesInThisMatch = getQuantityOfSlashesForThisMatch(actualMatch)
            if (isQuantityEven(quantityOfSlashesInThisMatch)) {
                val stringWithExpressionEvaluated = getActualStringWithExpressionEvaluated(
                    matchResult = it,
                    evaluatedExpressions = evaluatedExpressions,
                    actualStringToEvaluate = actualStringToEvaluate
                )
                evaluatedItemsInReverseOrder.add(stringWithExpressionEvaluated)
            } else {
                evaluatedItemsInReverseOrder.add(actualStringToEvaluate)
            }
        }
    }

    private fun joinStringsEvaluatedAndNormalizeSlashesOccurrence(revertedEvaluatedString: List<String>): String {
        return revertedEvaluatedString
            .joinToString("")
            .replace("\\\\", "\\")
            .replace("\\@", "@")
    }

    private fun joinActualMatchWithNextOne(
        index: Int,
        listInReverseOrderOfStringsToEvaluate: MutableList<String>,
        actualStringToEvaluate: String
    ) {
        if (isNotTheLastMatch(index, listInReverseOrderOfStringsToEvaluate)) {
            val nextStringItem = listInReverseOrderOfStringsToEvaluate[index + 1]
            listInReverseOrderOfStringsToEvaluate[index + 1] = nextStringItem.plus(actualStringToEvaluate)
        }
    }

    private fun isNotTheLastMatch(
        index: Int,
        listInReverseOrderOfStringsToEvaluate: MutableList<String>
    ) = index != listInReverseOrderOfStringsToEvaluate.size

    private fun getQuantityOfSlashesForThisMatch(actualMatch: String) =
        BeagleRegex.QUANTITY_OF_SLASHES_REGEX.find(actualMatch)?.groups?.get(1)?.value?.length ?: 0

    private fun getActualStringWithExpressionEvaluated(
        matchResult: MatchResult,
        evaluatedExpressions: MutableMap<String, Any>,
        actualStringToEvaluate: String
    ): String {
        val valueToChangeInEvaluation = matchResult.groupValues[2]
        val evaluatedValueToBeReplaced = evaluatedExpressions[valueToChangeInEvaluation].toString()
        return actualStringToEvaluate
            .replace("@{$valueToChangeInEvaluation}", evaluatedValueToBeReplaced)
    }

    private fun isQuantityEven(quantity: Int) = quantity %2 == 0
}