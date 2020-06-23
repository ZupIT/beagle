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
import java.util.LinkedList

class JsonCreateTree {

    fun walkingTreeAndFindKey(root: Any, keys: LinkedList<String>, newValue: Any?): Any {
        var newJson = root

        @Suppress("UNCHECKED_CAST")
        val copyOfKeys = keys.clone() as LinkedList<String>
        var key = copyOfKeys.poll()

        val keyIsArray = key.isArray()
        if (newJson is JSONObject && keyIsArray) {
            newJson = JSONArray()
        } else if (newJson is JSONArray && !keyIsArray) {
            newJson = JSONObject()
        }

        var currentTree: Any = newJson
        while (key != null) {
            val nextKey = copyOfKeys.poll()

            if (key.isArray()) {
                if (currentTree !is JSONArray) {
                    currentTree = JSONArray()
                }
                currentTree = createTreeToNextKey(currentTree, key, nextKey)
                if (nextKey == null) {
                    (currentTree as JSONArray).put(JsonPathUtils.getIndexOnArrayBrackets(key), newValue)
                }

            } else if (currentTree is JSONObject) {

                val json: Any = if (nextKey.isArray()) {
                    currentTree.optJSONArray(key) ?: JSONArray()
                } else {
                    currentTree.optJSONObject(key) ?: JSONObject()
                }
                currentTree.put(key, if (nextKey == null) newValue else json)
                currentTree = json
            }

            key = nextKey
        }
        return newJson
    }

    private fun createTreeToNextKey(jsonArray: JSONArray, key: String, nextKey: String?): Any {
        val position = JsonPathUtils.getIndexOnArrayBrackets(key)
        var opt: Any? = jsonArray.optJSONObject(position) ?: jsonArray.optJSONArray(position)
        if (opt == JSONObject.NULL || opt == null) {
            val json: Any = if (nextKey.isArray()) JSONArray() else JSONObject()
            jsonArray.put(position, if (nextKey != null) json else JSONObject.NULL)
            if (nextKey != null) {
                if (json is JSONObject) {
                    json.put(nextKey, null)
                }
                return json
            }
            opt = jsonArray
        } else if (nextKey == null) {
            opt = jsonArray
        }
        return opt
    }

    private fun String?.isArray() = this?.startsWith("[") == true
}