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
import br.com.zup.beagle.android.jsonpath.JsonPathUtils
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
        evaluatedExpressions: MutableMap<String, Any> = mutableMapOf()
    ): Any? {
        val expressions = bind.expressions

        return when {
            bind.type == String::class.java -> {
                expressions.forEach { expressionToken ->
                    evaluateExpressionsForContext(
                        contextsData,
                        contextCache,
                        expressionToken,
                        bind,
                        evaluatedExpressions
                    )
                }

                evaluateMultipleExpressions(bind, evaluatedExpressions)
            }
            expressions.size == 1 -> evaluateExpression(contextsData, contextCache, bind, expressions[0])
            else -> {
                BeagleMessageLogs.multipleExpressionsInValueThatIsNotString()
                null
            }
        }
    }

    private fun evaluateExpressionsForContext(
        contextData: List<ContextData>,
        contextCache: LruCache<String, Any>?,
        expressionToken: ExpressionToken,
        bind: Bind.Expression<*>,
        evaluatedExpressions: MutableMap<String, Any>
    ) {
        evaluatedExpressions[expressionToken.value] = evaluateExpression(
            contextData,
            contextCache,
            bind,
            expressionToken
        ) ?: ""
    }

    private fun evaluateMultipleExpressions(
        bind: Bind.Expression<*>,
        evaluatedExpressions: MutableMap<String, Any>
    ): Any? {
        val regex = "(?<=\\})".toRegex()
        return bind.value.split(regex).joinToString("") {
            val slash = "(\\\\*)@".toRegex().find(it)?.groups?.get(1)?.value?.length ?: 0
            if (!it.matches(".*\\\\@.*".toRegex()) || slash % 2 == 0) {
                val key = "\\{([^\\{]*)\\}".toRegex().find(it)?.groups?.get(1)?.value
                it.replace(
                    "\\@\\{\\w.+(\\.|\\w+)\\}".toRegex(),
                    escapeReplacement(evaluatedExpressions[key].toString())
                )
            } else {
                it
            }
        }.replace("\\\\", "\\").replace("\\@", "@")
    }

    private fun evaluateExpression(
        contextsData: List<ContextData>,
        contextCache: LruCache<String, Any>?,
        bind: Bind.Expression<*>,
        expressionToken: ExpressionToken
    ): Any? {
        val value = expressionTokenExecutor.execute(contextsData, expressionToken) { binding, contextData ->
            return@execute getValue(contextData, contextCache, binding, bind.type)
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
            contextCache?.get(expression) ?: findValueAndCache(contextData, expression)?.also {
                contextCache?.put(expression, it)
            }
        } else {
            ContextValueHandler.treatValue(contextData.value, type)
        }
    }

    private fun findValueAndCache(
        contextData: ContextData,
        expression: String
    ): Any? {
        val newPath = expression.replaceFirst("${contextData.id}.", "")
        if (newPath.isEmpty()) {
            throw JsonPathUtils.createInvalidPathException(newPath)
        }
        return contextDataManipulator.get(contextData, newPath)
    }
}
