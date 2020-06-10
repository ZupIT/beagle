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

package br.com.zup.beagle.context

import androidx.collection.LruCache
import br.com.zup.beagle.action.UpdateContext
import br.com.zup.beagle.core.Bind
import br.com.zup.beagle.core.ContextData
import br.com.zup.beagle.data.serializer.BeagleMoshi
import br.com.zup.beagle.jsonpath.JsonPathFinder
import br.com.zup.beagle.jsonpath.JsonPathReplacer
import br.com.zup.beagle.logger.BeagleMessageLogs
import br.com.zup.beagle.utils.getExpressions
import com.squareup.moshi.Moshi
import org.json.JSONArray
import org.json.JSONObject
import java.lang.IllegalStateException
import java.lang.reflect.Type
import java.util.Stack

internal data class ContextBinding(
    val context: ContextData,
    val bindings: MutableList<Bind.Expression<*>>
)

internal class ContextDataManager(
    private val jsonPathFinder: JsonPathFinder = JsonPathFinder(),
    private val jsonPathReplacer: JsonPathReplacer = JsonPathReplacer(),
    private val contextPathResolver: ContextPathResolver = ContextPathResolver(),
    private val moshi: Moshi = BeagleMoshi.moshi
) {

    private val lruCache: LruCache<String, Any> = LruCache(20)
    private val contextIds: Stack<String> = Stack<String>()
    private val contexts: MutableMap<String, ContextBinding> = mutableMapOf()

    fun pushContext(contextData: ContextData) {
        contextIds.add(contextData.id)
        contexts[contextData.id] = ContextBinding(
            bindings = mutableListOf(),
            context = contextData
        )
    }

    fun popContext() {
        if (contextIds.isNotEmpty()) {
            contextIds.pop()
        }
    }

    fun addBindingToContext(binding: Bind.Expression<*>) {
        binding.value.getExpressions().forEach { bindingValue ->
            val path = bindingValue.split(".")[0]

            val contextId = if (contextIds.contains(path)) {
                path
            } else {
                contextIds.peek()
            }

            contexts[contextId]?.bindings?.add(binding)
        }
    }

    fun updateContext(updateContext: UpdateContext): Boolean {
        return contexts[updateContext.contextId]?.let { contextBinding ->
            val path = updateContext.path ?: contextBinding.context.id
            return setValue(contextBinding, path, updateContext.value)
        } ?: false
    }

    fun evaluateContextBindings() {
        contexts.forEach { entry ->
            notifyBindingChanges(entry.value)
        }
    }

    private fun getValue(contextData: ContextData, path: String): Any? {
        return if (path != contextData.id) {
            val value = lruCache[path]
            if (value != null) {
                return value
            }
            findAndCacheValue(contextData, path)
        } else {
            contextData.value
        }
    }

    private fun findAndCacheValue(contextData: ContextData, path: String): Any? {
        return try {
            val keys = contextPathResolver.getKeysFromPath(contextData.id, path)
            val foundValue = jsonPathFinder.find(keys, contextData.value)
            if (foundValue != null) {
                lruCache.put(path, foundValue)
            }
             foundValue
        } catch (ex: Exception) {
            BeagleMessageLogs.errorWhileTryingToAccessContext(ex)
            null
        }
    }

    private fun setValue(contextBinding: ContextBinding, path: String, value: Any): Boolean {
        val context = contextBinding.context
        return if (path == context.id) {
            val newContext = context.copy(value = value)
            contexts[context.id] = contextBinding.copy(context = newContext)
            true
        } else {
            return try {
                val keys = contextPathResolver.getKeysFromPath(context.id, path)
                jsonPathReplacer.replace(keys, value, context.value)
            } catch (ex: Exception) {
                BeagleMessageLogs.errorWhileTryingToChangeContext(ex)
                false
            }
        }
    }

    private fun notifyBindingChanges(contextBinding: ContextBinding) {
        val contextData = contextBinding.context
        val bindings = contextBinding.bindings

        bindings.forEach { bind ->
            val expressions = bind.value.getExpressions()

            val evaluatedValue = if (bind.type == String::class.java) {
                var text = bind.value
                expressions.forEach {
                    val value = evaluateExpression(contextData, bind.type, it)
                    text = text.replace("@\\{$it\\}".toRegex(), value.toString())
                }
                text
            } else {
                evaluateExpression(contextData, bind.type, expressions[0])
            }

            if (evaluatedValue != null) {
                bind.notifyChange(evaluatedValue)
            }
        }
    }

    private fun evaluateExpression(contextData: ContextData, type: Type, expression: String): Any? {
        val path = contextPathResolver.addContextToPath(contextData.id, expression)
        val value = getValue(contextData, path)

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
}