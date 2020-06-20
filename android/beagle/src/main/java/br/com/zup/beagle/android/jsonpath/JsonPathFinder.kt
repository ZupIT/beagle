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
import org.json.JSONException
import org.json.JSONObject
import java.util.*

internal class JsonPathFinder {

    @Suppress("ReturnCount")
    tailrec fun find(nextKeys: LinkedList<String>, value: Any?): Any? {
        if (nextKeys.isEmpty()) return value

        val currentKey = nextKeys.poll()

        val childValue = if (currentKey.endsWith("]")) {
            if (value is JSONArray) {
                val arrayIndex = JsonPathUtils.getIndexOnArrayBrackets(currentKey)
                val getValue = value.safeGet(arrayIndex)
                if (getValue != null) {
                    return find(nextKeys, getValue)
                } else {
                    null
                }
            } else {
                throw IllegalStateException("Expected Array but received Object")
            }
        } else {
            value
        }

        if (childValue !is JSONObject) {
            throw JsonPathUtils.createInvalidPathException(currentKey)
        }

        if (childValue.isNull(currentKey)) return null

        val newValue = childValue.safeGet(currentKey)
        return find(nextKeys, newValue)
    }

    private fun walkingTree(value: JSONObject, keys: LinkedList<String>) {
        var tree: JSONObject = value
        var index = 0
        do {
            val key = keys[index]
            val valueInKey = tree.opt(key)
            val hasNextKey = index < (keys.size - 1)
            val nextKey = keys.getOrNull(index + 1)

            if (key.startsWith("[")) {
                index++
                continue
            }

            if (valueInKey != null) {
                if (valueInKey is JSONObject && hasNextKey && nextKey?.startsWith("[") == false) {
                    tree = valueInKey
                    index++
                    continue
                } else if (valueInKey is JSONArray && hasNextKey && nextKey?.startsWith("[") == true) {
                    val position = nextKey.replace("[^0-9]".toRegex(), "").toInt()
                    val opt = valueInKey.opt(position)
                    if (opt == null) {
                        tree = JSONObject()
                        valueInKey.put(position, tree)
                    }
                    index += 2
                    continue
                }
            }

            if (index == (keys.size - 1)) {
                tree.put(key, JSONObject.NULL)
                break
            }
            if (hasNextKey && nextKey!!.startsWith("[") && valueInKey !is JSONArray) {
                val size = nextKey.replace("[^0-9]".toRegex(), "").toInt()
                val jsonArray = JSONArray(arrayOfNulls<Any>(size + 1))
                val nextKeyArray = index + 2
                val hasNextKeyArray = nextKeyArray < (keys.size)
                if (hasNextKeyArray) {
                    val jsonObject = JSONObject()
                    jsonObject.put(keys[nextKeyArray], null)
                    jsonArray.put(size, jsonObject)
                    tree.put(key, jsonArray)
                    tree = jsonObject
                    index += 2
                    continue
                }
                value.put(key, jsonArray)
                break
            } else if (valueInKey !is JSONObject) {
                val jsonObject = JSONObject()
                value.put(key, jsonObject)
                tree = jsonObject
            }
            index++
        } while (index < keys.size)
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
}