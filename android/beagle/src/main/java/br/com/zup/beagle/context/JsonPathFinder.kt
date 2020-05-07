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

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.IllegalStateException
import java.util.LinkedList

internal class JsonPathFinder {

    fun find(nextKeys: LinkedList<String>, value: Any?): Any? {
        if (nextKeys.isEmpty()) return value

        var currentKey = nextKeys.pop()

        val childValue = if (currentKey.endsWith("]")) {
            if (value is JSONArray) {
                val arrayIndex = getIndexOnArrayBrackets(currentKey)
                value.safeGet(arrayIndex)?.let {
                    if (nextKeys.isEmpty()) {
                        return it
                    } else {
                        currentKey = nextKeys.pop()
                        it
                    }
                }
            } else {
                throw IllegalStateException("Expected Array but received Object")
            }
        } else {
            value
        }

        return if (childValue is JSONObject) {
            if (childValue.isNull(currentKey)) return null
            val newValue = childValue.safeGet(currentKey)
            find(nextKeys, newValue)
        } else {
            throw IllegalStateException("Invalid JSON path at key \"$currentKey\"")
        }
    }

    private fun JSONObject.safeGet(key: String): Any? {
        return try {
            this[key]
        } catch (ex: JSONException) {
            null
        }
    }

    private fun JSONArray.safeGet(index: Int): Any? {
        return try {
            this[index]
        } catch (ex: JSONException) {
            null
        }
    }

    fun getIndexOnArrayBrackets(arrayIndex: String): Int {
        return ARRAY_POSITION_REGEX.find(arrayIndex)?.groups?.get(1)?.value?.toInt() ?:
            throw IllegalStateException("Invalid array position $arrayIndex.")
    }
}