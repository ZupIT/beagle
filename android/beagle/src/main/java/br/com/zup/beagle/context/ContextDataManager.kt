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
import br.com.zup.beagle.logger.BeagleMessageLogs
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.IllegalStateException
import java.util.*

/*
* 1 - Ao renderizar um componente que tem contexto atrelado, varrer os filhos e verificar quais deles tem atributos do tipo Bind.Expression
* 2 - Guardar esses atributos em um mapa onde a chave é o ID do contexto e os valores é uma lista de Bind.Expression
* 3 - Ao finalizar o build da árvore, trigar o evaluation dos valores de todos contexts/Bind.Expression
* 4 - Assim que o houver qualquer updateContext verificar se a mudança ocorreu com sucesso e trigar novamente o evalution de todos os Bind.Expression atrelado a esse context
*/

internal data class ContextBinding(
    val context: ContextData,
    val bindings: MutableList<Bind.Expression<*>>
)

internal object ContextDataManager {

    private val lruCache: LruCache<String, Any> = LruCache(20)
    private val contexts: MutableMap<String, ContextBinding> = mutableMapOf()
    private val jsonPathFinder = JsonPathFinder()
    private val jsonPathReplacer = JsonPathReplacer()
    private val moshi = BeagleMoshi.moshi

    fun addContext(contextData: ContextData) {
        contexts[contextData.id] = ContextBinding(
            bindings = mutableListOf(),
            context = contextData
        )
    }

    fun addBindingToContext(contextId: String, binding: Bind.Expression<*>) {
        contexts[contextId]?.bindings?.add(binding)
    }

    fun updateContext(updateContext: UpdateContext) {
        contexts[updateContext.contextId]?.let { viewContext ->
            val path = updateContext.path ?: viewContext.context.id
            val result = setValue(viewContext, path, updateContext.value)
            if (result) {
                notifyBindingChanges(viewContext)
            }
        }
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
                val keys = generateKeys(contextData.id, path)
                jsonPathFinder.find(keys, contextData.value)?.let { foundValue ->
                    lruCache.put(path, foundValue)
                }
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
                val keys = generateKeys(context.id, path)
                jsonPathReplacer.replace(keys, value, context.value)
            } catch (ex: Exception) {
                BeagleMessageLogs.errorWhileTryingToChangeContext(ex)
                false
            }
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

    private fun addContextToPath(contextId: String, path: String): String {
        return if (path.contains(contextId)) {
            path
        } else {
            "$contextId.$path"
        }
    }

    private fun notifyBindingChanges(contextBinding: ContextBinding) {
        val contextData = contextBinding.context
        val bindings = contextBinding.bindings

        bindings.forEach { bind ->
            val expression = "@\\{([^)]+)}".toRegex().find(bind.value)?.groups?.get(1)?.value ?: ""
            val path = addContextToPath(contextData.id, expression)
            val value = getValue(contextData, path)

            val realValue: Any = if (value is JSONArray || value is JSONObject) {
                moshi.adapter<Any>(bind.type).fromJson(value.toString()) ?: throw IllegalStateException()
            } else {
                value ?: throw IllegalStateException()
            }

            bind.notifyChanges(realValue)
        }
    }
}