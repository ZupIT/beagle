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

import android.util.LruCache
import br.com.zup.beagle.android.action.SetContextInternal
import br.com.zup.beagle.android.jsonpath.JsonCreateTree
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.utils.getContextId
import br.com.zup.beagle.android.utils.getExpressions

internal data class ContextBinding(
    val context: ContextData,
    val bindings: MutableSet<Bind.Expression<*>> = mutableSetOf(),
    val cache: LruCache<String, Any?> = LruCache(Integer.MAX_VALUE)
) {
    fun evaluateBindExpression(binding: Bind.Expression<*>): Any? {
        val expression = binding.value
        if (cache[expression] == null) {
            cache.put(expression, ContextDataEvaluation().evaluateBindExpression(context, binding))
        }

        return cache.get(expression)
    }
}

internal class ContextDataManager(
    private val jsonCreateTree: JsonCreateTree = JsonCreateTree(),
    private val contextDataTreeHelper: ContextDataTreeHelper = ContextDataTreeHelper(),
    private val contextPathResolver: ContextPathResolver = ContextPathResolver()
) {

    private val contexts: MutableMap<String, ContextBinding> = mutableMapOf()

    fun clearContexts() {
        contexts.clear()
    }

    fun addContext(contextData: ContextData) {
        if (contexts[contextData.id] == null) {
            contexts[contextData.id] = ContextBinding(
                context = contextData.normalize()
            )
        } else {
            contexts[contextData.id]?.bindings?.clear()
        }
    }

    fun getContextsFromBind(binding: Bind.Expression<*>): List<ContextData> {
        val contextIds = binding.value.getExpressions().map { it.getContextId() }
        return contexts.filterKeys { contextIds.contains(it) }.map { it.value.context }
    }

    fun addBindingToContext(binding: Bind.Expression<*>) {
        binding.value.getExpressions().forEach { expression ->
            val contextId = expression.getContextId()
            contexts[contextId]?.bindings?.add(binding)
        }
    }

    fun updateContext(setContextInternal: SetContextInternal): Boolean {
        clearContextCache(setContextInternal.contextId)
        return contexts[setContextInternal.contextId]?.let { contextBinding ->
            val path = setContextInternal.path ?: contextBinding.context.id
            val setValue = setValue(contextBinding, path, setContextInternal.value)
            if (setValue) {
                evaluateContext(setContextInternal.contextId)
            }
            setValue
        } ?: false
    }

    private fun clearContextCache(contextId: String) {
        contexts[contextId]?.cache?.evictAll()
    }

    fun evaluateContexts() {
        contexts.forEach { entry ->
            notifyBindingChanges(entry.value)
        }
    }

    private fun evaluateContext(contextId: String) {
        contexts[contextId]?.let {
            notifyBindingChanges(it)
        }
    }

    private fun setValue(contextBinding: ContextBinding, path: String, value: Any): Boolean {
        var context = contextBinding.context
        return if (path == context.id) {
            contextDataTreeHelper.setNewTreeInContextData(contexts, contextBinding, value)
            true
        } else {
            try {
                val keys = contextPathResolver.getKeysFromPath(context.id, path)
                if (keys.isEmpty()) {
                    return false
                }
                context = contextDataTreeHelper.updateContextDataWithTree(
                    contextBinding,
                    jsonCreateTree,
                    keys,
                    contexts
                )
                jsonCreateTree.walkingTreeAndFindKey(context.value, keys, value)
                true
            } catch (ex: Exception) {
                BeagleMessageLogs.errorWhileTryingToChangeContext(ex)
                false
            }
        }
    }

    private fun notifyBindingChanges(contextBinding: ContextBinding) {
        val bindings = contextBinding.bindings

        bindings.forEach { bind ->
            val value = contextBinding.evaluateBindExpression(bind)
            bind.notifyChange(value)
        }
    }
}