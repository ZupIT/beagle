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

import br.com.zup.beagle.android.action.SetContextInternal
import br.com.zup.beagle.android.jsonpath.JsonPathReplacer
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.utils.BeagleConstants
import br.com.zup.beagle.android.utils.getExpressions

internal data class ContextBinding(
    val context: ContextData,
    val bindings: MutableList<Bind.Expression<*>>
)

internal class ContextDataManager(
    private val jsonPathReplacer: JsonPathReplacer = JsonPathReplacer(),
    private val contextPathResolver: ContextPathResolver = ContextPathResolver(),
    private val contextDataEvaluation: ContextDataEvaluation = ContextDataEvaluation()
) {

    private val contexts: MutableMap<String, ContextBinding> = mutableMapOf()

    fun addContext(contextData: ContextData) {
        contexts[contextData.id] = ContextBinding(
            bindings = mutableListOf(),
            context = contextData
        )
    }

    fun removeContext(contextId: String) {
        contexts.remove(contextId)
    }

    fun addBindingToContext(binding: Bind.Expression<*>) {
        binding.value.getExpressions().forEach { bindingValue ->
            val contextId = bindingValue.split(".")[0]
            contexts[contextId]?.bindings?.add(binding)
        }
    }

    fun updateContext(setContextInternal: SetContextInternal): Boolean {
        return contexts[setContextInternal.contextId]?.let { contextBinding ->
            val path = setContextInternal.path ?: contextBinding.context.id
            val setValue = setValue(contextBinding, path, setContextInternal.value)
            if (setValue) {
                evaluateContext(setContextInternal.contextId)
            }
            setValue
        } ?: false
    }

    fun evaluateAllContext() {
        contexts.forEach { entry ->
            notifyBindingChanges(entry.value)
        }
    }

    fun evaluateBinding(bind: Bind.Expression<*>): Any? {
        val contextId = bind.getContextId()

        return contexts[contextId]?.let {
            return contextDataEvaluation.evaluateBindExpression(it.context, bind)
        }
    }

    private fun evaluateContext(contextId: String) {
        contexts[contextId]?.let {
            notifyBindingChanges(it)
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
            val value = contextDataEvaluation.evaluateBindExpression(contextData, bind)

            if (value != null) {
                bind.notifyChange(value)
            }
        }
    }

    private fun Bind.Expression<*>.getContextId(): String =
        valueInExpression().split(".")[0]

    private fun Bind.Expression<*>.valueInExpression(): String =
        BeagleConstants.EXPRESSION_REGEX.find(this.value)?.groups?.get(1)?.value ?: ""
}