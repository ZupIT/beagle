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

package br.com.zup.beagle.android.jsonpath

import org.json.JSONArray
import org.json.JSONObject
import java.util.*

internal class JsonPathReplacer(
    private val jsonCreateTree: JsonCreateTree = JsonCreateTree()
) {

    fun replace(keys: LinkedList<String>, newValue: Any, root: Any): Pair<Boolean, Any> {
        return when {
            keys.isEmpty() -> false to root
            else -> {
                val lastKey = keys.pollLast()
                val rootAndValue = jsonCreateTree.walkingTreeAndFindKey(root, keys)
                val isReplaced = replaceValue(lastKey, newValue, rootAndValue.second)
                return isReplaced to rootAndValue.first
            }
        }
    }

    private fun replaceValue(key: String, value: Any, foundValue: Any?): Boolean {
        return when (foundValue) {
            is JSONObject -> {
                foundValue.put(key, value)
                true
            }
            is JSONArray -> {
                val arrayIndex = JsonPathUtils.getIndexOnArrayBrackets(key)
                foundValue.put(arrayIndex, value)
                true
            }
            else -> false
        }
    }
}