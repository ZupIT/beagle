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

import androidx.collection.LruCache
import br.com.zup.beagle.android.context.tokenizer.ExpressionToken
import br.com.zup.beagle.android.context.tokenizer.ExpressionTokenExecutor
import br.com.zup.beagle.android.data.serializer.BeagleMoshi
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.utils.BeagleRegex.FULL_MATCH_EXPRESSION_REGEX
import br.com.zup.beagle.android.utils.BeagleRegex.FULL_MATCH_EXPRESSION_SEPARATOR_REGEX
import br.com.zup.beagle.android.utils.BeagleRegex.QUANTITY_OF_SLASHES_REGEX
import com.squareup.moshi.Moshi
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Type
import kotlin.text.Regex.Companion.escapeReplacement

internal class ContextDataEvaluation(
    private val contextDataManipulator: ContextDataManipulator = ContextDataManipulator(),
    private val expressionTokenExecutor: ExpressionTokenExecutor = ExpressionTokenExecutor(),
    private val moshi: Moshi = BeagleMoshi.moshi
) {

    fun evaluateBindExpression(
        contextData: ContextData,
        contextCache: LruCache<String, Any>,
        bind: Bind.Expression<*>,
        evaluatedBindings: MutableMap<String, Any>
    ): Any? {
        return evaluateBindExpressions(listOf(contextData), bind, contextCache, evaluatedBindings)
    }

    fun evaluateBindExpression(
        contextsData: List<ContextData>,
        bind: Bind.Expression<*>
    ): Any? {
        return evaluateBindExpressions(contextsData, bind)
    }

    private fun evaluateBindExpressions(
        contextsData: List<ContextData>,
        bind: Bind.Expression<*>,
        contextCache: LruCache<String, Any>? = null,
        evaluatedBindings: MutableMap<String, Any> = mutableMapOf()
    ): Any? {
        val expressions = bind.expressions

        return when {
            bind.type == String::class.java -> {
                val evaluatedExpressions = mutableMapOf<String, Any>()
                expressions.forEach { expressionToken ->
                    evaluateExpressionsForContext(
                        contextsData,
                        contextCache,
                        expressionToken,
                        bind,
                        evaluatedExpressions,
                        evaluatedBindings
                    )
                }

                evaluateMultipleExpressions(bind, evaluatedExpressions)
            }
            expressions.size == 1 -> evaluateExpression(
                contextsData,
                contextCache,
                bind,
                expressions[0],
                evaluatedBindings
            )
            else -> {
                BeagleMessageLogs.multipleExpressionsInValueThatIsNotString()
                null
            }
        }
    }

    @Suppress("LongParameterList")
    private fun evaluateExpressionsForContext(
        contextData: List<ContextData>,
        contextCache: LruCache<String, Any>?,
        expressionToken: ExpressionToken,
        bind: Bind.Expression<*>,
        evaluatedExpressions: MutableMap<String, Any>,
        evaluatedBindings: MutableMap<String, Any>
    ) {
        evaluatedExpressions[expressionToken.value] = evaluateExpression(
            contextData,
            contextCache,
            bind,
            expressionToken,
            evaluatedBindings
        ) ?: ""
    }


    private fun evaluateMultipleExpressions(
        bind: Bind.Expression<*>,
        evaluatedExpressions: MutableMap<String, Any>
    ): Any? {
        val stringToEvaluate = bind.value
        val regexToMatchAllExpressions = FULL_MATCH_EXPRESSION_REGEX

        val listInReverseOrderOfStringsToEvaluate = stringToEvaluate
            .split(FULL_MATCH_EXPRESSION_SEPARATOR_REGEX)
            .reversed()
            .toMutableList()

        val evaluatedItemsInReverseOrder = mutableListOf<String>()
        listInReverseOrderOfStringsToEvaluate.forEachIndexed { index, actualStringToEvaluate ->

            val matchesOccurrenceInThisItem = regexToMatchAllExpressions.findAll(actualStringToEvaluate)
            if (matchesOccurrenceInThisItem.count() > 0) {
                matchesOccurrenceInThisItem.iterator().forEach {

                    val actualMatch = it.value
                    val quantityOfSlashesInThisMatch = getQuantityOfSlashesForThisMatch(actualMatch)
                    if(isQuantityEven(quantityOfSlashesInThisMatch)) {
                        val stringWithExpressionEvaluated = getActualStringWithExpressionEvaluated(
                            matchResult = it,
                            evaluatedExpressions = evaluatedExpressions,
                            actualStringToEvaluate = actualStringToEvaluate
                        )
                        evaluatedItemsInReverseOrder.add(stringWithExpressionEvaluated)
                    }
                    else {
                        evaluatedItemsInReverseOrder.add(actualStringToEvaluate)
                    }
                }
            } else {
                joinActualMatchWithNextOne(index, listInReverseOrderOfStringsToEvaluate, actualStringToEvaluate)
            }
        }

        val revertedEvaluatedString = evaluatedItemsInReverseOrder
            .toList()
            .reversed()

        return joinStringsEvaluatedAndNormalizeSlashesOccurrence(revertedEvaluatedString)

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
        QUANTITY_OF_SLASHES_REGEX.find(actualMatch)?.groups?.get(1)?.value?.length ?: 0

    private fun getActualStringWithExpressionEvaluated( matchResult: MatchResult,
                                                        evaluatedExpressions: MutableMap<String, Any>,
                                                        actualStringToEvaluate: String): String {

        val valueToChangeInEvaluation = matchResult.groupValues[2]
        val evaluatedValueToBeReplaced = escapeReplacement(evaluatedExpressions[valueToChangeInEvaluation].toString())
        return actualStringToEvaluate
            .replace("@{$valueToChangeInEvaluation}", evaluatedValueToBeReplaced)
    }

    private fun isQuantityEven(quantity: Int): Boolean {
        return quantity %2 == 0
    }

    private fun evaluateExpression(
        contextsData: List<ContextData>,
        contextCache: LruCache<String, Any>?,
        bind: Bind.Expression<*>,
        expressionToken: ExpressionToken,
        evaluatedBindings: MutableMap<String, Any>
    ): Any? {
        val value = try {
             expressionTokenExecutor.execute(contextsData, expressionToken) { binding, contextData ->
                 return@execute if (contextData != null) {
                     getValue(contextData, contextCache, binding, bind.type)?.also {
                         evaluatedBindings[binding] = it
                     }
                 } else {
                     evaluatedBindings[binding]
                 }
            }
        } catch (ex: Exception) {
            BeagleMessageLogs.errorWhileTryingExecuteExpressionFunction(ex)
            null
        }

        return try {
            if (bind.type == String::class.java) {
                value?.toString() ?: showLogErrorAndReturn(bind)
            } else if (value is JSONArray || value is JSONObject) {
                moshi.adapter<Any>(bind.type).fromJson(value.toString()) ?: showLogErrorAndReturn(bind)
            } else {
                value ?: showLogErrorAndReturn(bind)
            }
        } catch (ex: Exception) {
            BeagleMessageLogs.errorWhileTryingToNotifyContextChanges(ex)
            null
        }
    }

    private fun showLogErrorAndReturn(bind: Bind.Expression<*>) = run {
        BeagleMessageLogs.errorWhenExpressionEvaluateNullValue("${bind.value} : ${bind.type}")
        null
    }

    private fun getValue(
        contextData: ContextData,
        contextCache: LruCache<String, Any>?,
        expression: String,
        type: Type
    ): Any? {
        return if (expression != contextData.id) {
            contextCache?.get(expression) ?: findValue(contextData, expression)?.also {
                contextCache?.put(expression, it)
            }
        } else {
            ContextValueHandler.treatValue(contextData.value, type)
        }
    }

    private fun findValue(
        contextData: ContextData,
        expression: String
    ): Any? {
        return contextDataManipulator.get(contextData, expression)
    }
}
