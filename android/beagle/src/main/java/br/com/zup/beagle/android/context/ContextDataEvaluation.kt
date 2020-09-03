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

//    private fun evaluateMultipleExpressions(
//        bind: Bind.Expression<*>,
//        evaluatedExpressions: MutableMap<String, Any>
//    ): Any? {
//        val regexSymbolSeparator = getRegexSeparatorUsingSymbol(symbol = '}')
//
//        return bind.value.split(regexSymbolSeparator).joinToString("") {
//
//            val quantityOfSlashes = getSlashQuantityFromString(textToVerify = it) ?: 0
//
//            val haveDoubleSlashOccurrence = haveAtLeastOneDoubleSlashBeforeAtSignInString(textToVerify = it)
//
//            val isQuantityOfSlashesEven = isQuantityEven(quantity = quantityOfSlashes)
//
//            if (!haveDoubleSlashOccurrence || isQuantityOfSlashesEven) {
//                val key = "\\{([^\\@]*)\\}".toRegex().find(it)?.groups?.get(1)?.value
//                val value = escapeReplacement(evaluatedExpressions[key].toString())
//                it.replace("@{$key}", value)
//            } else {
//                it
//            }
//        }.replace("\\\\", "\\").replace("\\@", "@")
//
//    }

    private fun evaluateMultipleExpressions(
        bind: Bind.Expression<*>,
        evaluatedExpressions: MutableMap<String, Any>
    ): Any? {
        val stringToEvaluate = bind.value
        val expressionContentRegex = "(\\\\*)@\\{(([^'\\}]|('([^'\\\\]|\\\\.)*'))*)\\}"
        val revertedSplitedList = stringToEvaluate.split("(?<=\\})".toRegex()).reversed().toMutableList()
        val revertedListWithStringEvaluated = mutableListOf<String>()

        revertedSplitedList.forEachIndexed { index, itemFromList ->

            //Execute a match of regex in actual item
            val sequenceOfItemsFound = expressionContentRegex.toRegex().findAll(itemFromList)

            //verify if a match has been encountered
            if (sequenceOfItemsFound.count() != 0) {

                //No need to be a forEach cause, only one match per time is available
                sequenceOfItemsFound.iterator().forEach {

                    //Get the actual match
                    val fullMatch = it.value

                    //Check the quantity of slashes before @
                    val slashQuantity = "(\\\\*)@".toRegex().find(fullMatch)?.groups?.get(1)?.value?.length ?: 0

                    //If the quantity is even should evaluate value
                    if(slashQuantity % 2 == 0 ) {

                        //Get the key as a match style
                        val key = "@{${it.groupValues[2]}}"

                        //Mocked value to be replaced
//                        val value = "VALOR"
                        val value = escapeReplacement(evaluatedExpressions[key].toString())

                        //New String with replaced value
                        val fullMatchWithNormalizedSlashes = itemFromList
                            .replace(key, value)


                        //Add a new string to new reverted list
                        revertedListWithStringEvaluated.add(fullMatchWithNormalizedSlashes)
                    }
                    else {
                        //Only add same string to new reverted list
                        revertedListWithStringEvaluated.add(itemFromList)
                    }
                }
            } else {
                //in last item we do nothing
                if(index!= revertedSplitedList.size) {

                    val nextStringItem = revertedSplitedList[index+1]
                    //Concatenate not matched item with the next one from the list
                    revertedSplitedList[index+1] = nextStringItem.plus(itemFromList)
                }
            }

        }

        val revertedEvaluatedString = revertedListWithStringEvaluated
            .toList()
            .reversed()

        return revertedEvaluatedString
            .joinToString("")
            .replace("\\\\", "\\")
            .replace("\\@", "@")

    }

    private fun isQuantityEven(quantity: Int): Boolean {
        return quantity %2 == 0
    }

    private fun haveAtLeastOneDoubleSlashBeforeAtSignInString(textToVerify: String): Boolean {
        return textToVerify.matches(".*\\\\@.*".toRegex())
    }

    private fun getSlashQuantityFromString(textToVerify: String): Int? {
        return "(\\\\*)@".toRegex().find(textToVerify)?.groups?.get(1)?.value?.length
    }

    private fun getRegexSeparatorUsingSymbol(symbol: Char) : Regex {
        return "(?<=\\$symbol)".toRegex()
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
