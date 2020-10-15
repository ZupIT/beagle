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
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type

internal class ContextDataEvaluation(
    private val contextDataManipulator: ContextDataManipulator = ContextDataManipulator(),
    private val expressionTokenExecutor: ExpressionTokenExecutor = ExpressionTokenExecutor(),
    private val contextExpressionReplacer: ContextExpressionReplacer = ContextExpressionReplacer(),
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
            expressions.size == 1 && bind.type != String::class.java -> {
                evaluateExpression(
                    contextsData,
                    contextCache,
                    bind,
                    expressions[0],
                    evaluatedBindings
                )
            }
            else -> {
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

                val response = contextExpressionReplacer.replace(bind, evaluatedExpressions)
                val type = getType(response)
                return try {
                    if (evaluatedExpressions.size == 1 && type == null) {
                        return when (evaluatedExpressions.entries.first().value) {
                            is Int -> response.toInt()
                            is Long -> response.toLong()
                            is Double -> response.toDouble()
                            is Boolean -> response.toBoolean()
                            else -> response
                        }
                    }
                    moshi.adapter<Any>(type ?: bind.type).fromJson(response)
                } catch (ex: Exception) {
                    response
                }
            }
        }
    }



    private fun getType(json: String): Type? {
        return try {
            JSONObject(json)::class.java
        } catch (ex: JSONException) {
            return try {
                JSONArray(json)::class.java
            } catch (ex1: JSONException) {
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

    private fun evaluateExpression(
        contextsData: List<ContextData>,
        contextCache: LruCache<String, Any>?,
        bind: Bind.Expression<*>,
        expressionToken: ExpressionToken,
        evaluatedBindings: MutableMap<String, Any>
    ): Any? {
        return getValueFromExpression(contextsData, expressionToken, contextCache, bind, evaluatedBindings)
    }

    private fun getValueFromExpression(
        contextsData: List<ContextData>,
        expressionToken: ExpressionToken,
        contextCache: LruCache<String, Any>?,
        bind: Bind.Expression<*>,
        evaluatedBindings: MutableMap<String, Any>
    ): Any? {
        return try {
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
