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
import org.json.JSONException
import org.json.JSONObject
import java.lang.IllegalStateException
import java.util.*

internal class JsonPathFinder {

    tailrec fun findByPath(nextKeys: LinkedList<String>, value: Any?): Any? {
        if (nextKeys.isEmpty()) return value

        val currentKey = nextKeys.poll()

        val childValue = if (currentKey.endsWith("]")) {
            if (value is JSONArray) {
                val arrayIndex = JsonPathUtils.getIndexOnArrayBrackets(currentKey)
                val getValue = value.safeGet(arrayIndex)
                if (getValue != null) {
                    return findByPath(nextKeys, getValue)
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
            throw IllegalStateException("Invalid JSON path at key \"$currentKey\"")
        }

        if (childValue.isNull(currentKey)) return null

        val newValue = childValue.safeGet(currentKey)
        return findByPath(nextKeys, newValue)
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