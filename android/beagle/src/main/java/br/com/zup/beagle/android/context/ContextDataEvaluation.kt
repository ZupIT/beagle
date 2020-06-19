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

import br.com.zup.beagle.android.data.serializer.BeagleMoshi
import br.com.zup.beagle.android.jsonpath.JsonPathFinder
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.utils.getExpressions
import com.squareup.moshi.Moshi
import org.json.JSONArray
import org.json.JSONObject
import java.lang.IllegalStateException
import java.lang.reflect.Type

internal class ContextDataEvaluation(
    private val jsonPathFinder: JsonPathFinder = JsonPathFinder(),
    private val contextPathResolver: ContextPathResolver = ContextPathResolver(),
    private val moshi: Moshi = BeagleMoshi.moshi
) {

    fun evaluateBindExpression(contextData: ContextData, bind: Bind.Expression<*>): Any? {
        val expressions = bind.value.getExpressions()

        return if (bind.type == String::class.java) {
            var text = bind.value
            expressions.forEach {
                val value = evaluateExpression(contextData, bind.type, it)
                text = text.replace("@\\{$it\\}".toRegex(), value.toString())
            }
            text
        } else {
            evaluateExpression(contextData, bind.type, expressions[0])
        }
    }

    private fun evaluateExpression(contextData: ContextData, type: Type, expression: String): Any? {
        val value = getValue(contextData, expression)

        return try {
            if (value is JSONArray || value is JSONObject) {
                moshi.adapter<Any>(type).fromJson(value.toString()) ?:
                throw IllegalStateException("JSON deserialization returned null")
            } else {
                value ?: throw IllegalStateException("Expression evaluation returned null")
            }
        } catch (ex: Exception) {
            BeagleMessageLogs.errorWhileTryingToNotifyContextChanges(ex)
            null
        }
    }

    private fun getValue(contextData: ContextData, path: String): Any? {
        return if (path != contextData.id) {
            findAndCacheValue(contextData, path)
        } else {
            contextData.value
        }
    }

    private fun findAndCacheValue(contextData: ContextData, path: String): Any? {
        return try {
            val keys = contextPathResolver.getKeysFromPath(contextData.id, path)
            jsonPathFinder.find(keys, contextData.value)
        } catch (ex: Exception) {
            BeagleMessageLogs.errorWhileTryingToAccessContext(ex)
            null
        }
    }

}