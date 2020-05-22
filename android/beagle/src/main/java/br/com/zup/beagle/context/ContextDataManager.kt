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
import br.com.zup.beagle.data.serializer.BeagleMoshi
import br.com.zup.beagle.jsonpath.JsonPathFinder
import br.com.zup.beagle.jsonpath.JsonPathReplacer
import br.com.zup.beagle.logger.BeagleMessageLogs
import com.squareup.moshi.Moshi
import org.json.JSONArray
import org.json.JSONObject
import java.lang.IllegalStateException
import java.util.Stack

private val EXPRESSION_REGEX = "@\\{([^)]+)}".toRegex()

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
        contextIds.pop()
    }

    fun addBindingToContext(binding: Bind.Expression<*>) {
        val bindingValue = binding.valueInExpression()
        val path = bindingValue.split(".")[0]

        val contextId = if (contextIds.contains(path)) {
            path
        } else {
            contextIds.peek()
        }

        contexts[contextId]?.bindings?.add(binding)
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
        return if (path == contextData.id) {
            contextData.value
        } else {
            val value = lruCache[path]
            if (value != null) {
                return value
            }
            return try {
                val keys = contextPathResolver.getKeysFromPath(contextData.id, path)
                val foundValue = jsonPathFinder.find(keys, contextData.value)
                if (foundValue != null) {
                    lruCache.put(path, foundValue)
                }
                return foundValue
            } catch (ex: Exception) {
                BeagleMessageLogs.errorWhileTryingToAccessContext(ex)
                null
            }
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
            val expression = bind.valueInExpression()
            val path = contextPathResolver.addContextToPath(contextData.id, expression)
            val value = getValue(contextData, path)

            try {
                val realValue: Any = if (value is JSONArray || value is JSONObject) {
                    moshi.adapter<Any>(bind.type).fromJson(value.toString()) ?:
                        throw IllegalStateException("JSON deserialization returned null")
                } else {
                    value ?: throw IllegalStateException("Expression evaluation returned null")
                }
                bind.notifyChanges(realValue)
            } catch (ex: Exception) {
                BeagleMessageLogs.errorWhileTryingToNotifyContextChanges(ex)
            }
        }
    }

    private fun Bind.Expression<*>.valueInExpression(): String =
        EXPRESSION_REGEX.find(this.value)?.groups?.get(1)?.value ?: ""
}