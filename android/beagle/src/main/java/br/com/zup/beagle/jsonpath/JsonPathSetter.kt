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

package br.com.zup.beagle.jsonpath

import org.json.JSONArray
import org.json.JSONObject
import java.util.*

internal class JsonPathSetter(
    private val jsonPathFinder: JsonPathFinder = JsonPathFinder()
) {

    fun setByPath(keys: LinkedList<String>, newValue: Any, root: Any): Boolean {
        return when {
            keys.isEmpty() -> false
            keys.size == 1 -> replaceValue(keys.poll(), newValue, root)
            else -> {
                val lastKey = keys.pollLast()
                val foundValue = jsonPathFinder.findByPath(keys, root)
                return replaceValue(lastKey, newValue, foundValue)
            }
        }
    }

    private fun replaceValue(key: String, value: Any, foundValue: Any?): Boolean {
        if (key.endsWith("]") && foundValue !is JSONArray) {
            throw JsonPathUtils.createArrayExpectedException()
        }

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