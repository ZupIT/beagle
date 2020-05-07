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
import java.util.*

private val ARRAY_POSITION_REGEX = "\\[([^)]+)\\]".toRegex()

// TODO: implement LruCache to deal with expressions that were previous accessed

class ContextData(
    val id: String,
    value: Any
) {
    private var _value: Any
    val value: Any
        get() = _value

    init {
        this._value = value
    }

    fun getValue(path: String): Any? {
        val value = _value

        return if (path == id) {
            value
        } else {
            val newPath = path.replace(id, "")
            if (newPath.isEmpty()) {
                throw IllegalStateException("Invalid path")
            }
            val keys = splitKeys(path)
            return find(keys, value)
        }
    }

    fun setValue(path: String? = null, value: Any): Boolean {
        return if (path != null) {
            // TODO: implement set
            true
        } else {
            _value = value
            true
        }
    }

    private fun splitKeys(path: String): LinkedList<String> {
        val keysQueue = LinkedList<String>()

        path.split(".").forEach { key ->
            if (key.endsWith("]")) {
                val keyOnly = key.replace(ARRAY_POSITION_REGEX, "")
                val arrayPosition = getArrayBrackets(key) ?: ""
                keysQueue.add(keyOnly)
                keysQueue.add(arrayPosition)
            } else {
                keysQueue.add(key)
            }
        }

        return keysQueue
    }

    private fun find(nextKeys: LinkedList<String>, value: Any?): Any? {
        if (nextKeys.isEmpty()) return value

        var currentKey = nextKeys.pop()

        val childValue = if (currentKey.endsWith("]")) {
            if (value is JSONArray) {
                val arrayIndex = getIndexOnArrayBrackets(currentKey)?.toInt() ?:
                    throw IllegalStateException("Invalid array position $currentKey.")
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

    private fun getIndexOnArrayBrackets(arrayIndex: String): String? {
        return ARRAY_POSITION_REGEX.find(arrayIndex)?.groups?.get(1)?.value
    }

    private fun getArrayBrackets(arrayIndex: String): String? {
        return ARRAY_POSITION_REGEX.find(arrayIndex)?.value
    }
}