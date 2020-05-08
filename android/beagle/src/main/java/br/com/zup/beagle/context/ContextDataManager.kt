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

import br.com.zup.beagle.action.UpdateContext
import java.util.*

/*
* 1 - Ao renderizar um componente que tem contexto atrelado, varrer os filhos e verificar quais deles tem atributos do tipo Bind.Expression
* 2 - Guardar esses atributos em um mapa onde a chave é o ID do contexto e os valores é uma lista de Bind.Expression
* 3 - Ao finalizar o build da árvore, trigar o evaluation dos valores de todos contexts/Bind.Expression
* 4 - Assim que o houver qualquer updateContext verificar se a mudança ocorreu com sucesso e trigar novamente o evalution de todos os Bind.Expression atrelado a esse context
*/

private data class ContextBinding(
    val bindings: MutableList<Any>, // TODO: refactor to get binding expression
    val context: ContextData
)

object ContextDataManager {

    private val contexts: MutableMap<String, ContextBinding> = mutableMapOf()
    private val jsonPathFinder = JsonPathFinder()
    private val jsonPathReplacer = JsonPathReplacer()

    fun addContext(contextData: ContextData) {
        contexts[contextData.id] = ContextBinding(
            bindings = mutableListOf(),
            context = contextData
        )
    }

    fun addBindingToContext(contextId: String, binding: Any) {
        contexts[contextId]?.let {
            it.bindings.add(binding)
        }
    }

    fun updateContext(updateContext: UpdateContext) {
        contexts[updateContext.contextId]?.let { viewContext ->
            val path = updateContext.path ?: viewContext.context.id
            val result = setValue(viewContext, path, updateContext.value)
            if (result) {
                notifyBindingChanges(viewContext.bindings, viewContext.context)
            }
        }
    }

    fun getValue(contextId: String, path: String): Any? {
        // TODO: implement LruCache to deal with expressions that were previous accessed

        // TODO: handle exceptions and throw in logs
        return contexts[contextId]?.let { viewContext ->
            return getValue(viewContext.context, path)
        }
    }

    private fun getValue(contextData: ContextData, path: String): Any? {
        // TODO: handle exceptions and throw in logs
        return if (path == contextData.id) {
            contextData.value
        } else {
            val keys = generateKeys(contextData.id, path)
            return jsonPathFinder.find(keys, contextData.value)
        }
    }

    private fun setValue(contextBinding: ContextBinding, path: String, value: Any): Boolean {
        val context = contextBinding.context
        return if (path == context.id) {
            val newContext = context.copy(value = value)
            contexts[context.id] = contextBinding.copy(context = newContext)
            true
        } else {
            val keys = generateKeys(context.id, path)
            jsonPathReplacer.replace(keys, value, context.value)
        }
    }

    private fun generateKeys(contextId: String, path: String): LinkedList<String> {
        val newPath = removeContextFromPath(contextId, path)
        val keys = JsonPathUtils.splitKeys(newPath)

        if (keys.size == 1 && keys.first == path) {
            throw JsonPathUtils.createInvalidPathException()
        }

        return keys
    }

    private fun removeContextFromPath(contextId: String, path: String): String {
        val newPath = path.replace(contextId, "")

        if (newPath.isEmpty()) {
            throw JsonPathUtils.createInvalidPathException()
        } else if (newPath.startsWith(".")) {
            return newPath.replaceFirst(".", "")
        }

        return newPath
    }

    private fun notifyBindingChanges(bindings: List<Any>, contextData: ContextData) {
        bindings.forEach {
            // TODO: 1 - access expression and add context id if its not present
            // TODO: 2 - call getValue
            // TODO: 3 - deserialize data based on Bind<Class> and notify observer with new value
        }
    }
}