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

import br.com.zup.beagle.android.jsonpath.JsonCreateTree
import br.com.zup.beagle.android.jsonpath.JsonPathFinder
import br.com.zup.beagle.android.jsonpath.JsonPathUtils
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import org.json.JSONArray
import org.json.JSONObject
import java.util.LinkedList

internal sealed class ContextSetResult {
    data class Succeed(val newContext: ContextData) : ContextSetResult()
    object Failure : ContextSetResult()
}

internal class ContextDataManipulator(
    private val jsonCreateTree: JsonCreateTree = JsonCreateTree(),
    private val jsonPathFinder: JsonPathFinder = JsonPathFinder()
) {

    fun set(
        context: ContextData,
        path: String? = null,
        value: Any
    ): ContextSetResult {
        return if (path.isNullOrEmpty()) {
            ContextSetResult.Succeed(context.copy(value = value))
        } else {
            try {
                val keys = createKeysFromPath(context.id, path)
                val newContext = updateContextDataWithTree(context, keys)
                jsonCreateTree.walkingTreeAndFindKey(newContext.value, keys, value)
                ContextSetResult.Succeed(newContext)
            } catch (ex: Exception) {
                BeagleMessageLogs.errorWhileTryingToChangeContext(ex)
                ContextSetResult.Failure
            }
        }
    }

    fun clear(context: ContextData, path: String? = null): ContextSetResult {
        if (path.isNullOrEmpty()) {
            return ContextSetResult.Succeed(context.copy(value = ""))
        }

        return try {
            val keys = createKeysFromPath(context.id, path)
            val lastKey = keys.pollLast()
            val lastValue = jsonPathFinder.find(keys, context.value)
            if (removePathAtKey(lastKey, lastValue)) {
                ContextSetResult.Succeed(context)
            } else {
                ContextSetResult.Failure
            }
        } catch (ex: Exception) {
            BeagleMessageLogs.errorWhileTryingToChangeContext(ex)
            ContextSetResult.Failure
        }
    }

    private fun removePathAtKey(key: String, value: Any?): Boolean {
        return when (value) {
            is JSONArray -> {
                val index = JsonPathUtils.getIndexOnArrayBrackets(key)
                value.remove(index)
                true
            }
            is JSONObject -> {
                value.remove(key)
                true
            }
            else -> false
        }
    }

     fun get(contextData: ContextData, path: String): Any? {
         return try {
             val keys = createKeysFromPath(contextData.id, path)
             jsonPathFinder.find(keys, contextData.value)
         } catch (ex: Exception) {
             BeagleMessageLogs.errorWhileTryingToAccessContext(ex)
             null
         }
    }

    private fun createKeysFromPath(contextId: String, path: String): LinkedList<String> {
        val keys = JsonPathUtils.splitKeys(path)
        val firstKey = keys.pop()

        // If contextId is present on path, we should remove it
        if (firstKey != contextId) {
            keys.push(firstKey)
        }

        return keys
    }

    private fun updateContextDataWithTree(
        context: ContextData,
        keys: LinkedList<String>
    ): ContextData {
        val initialTree = jsonCreateTree.createInitialTree(context.value, keys.first)
        if (initialTree != context.value) {
            return context.copy(value = initialTree)
        }
        return context
    }
}