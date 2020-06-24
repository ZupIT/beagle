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

internal class JsonCreateTree {

    fun walkingTreeAndFindKey(root: Any, keys: LinkedList<String>, newValue: Any?) {
        @Suppress("UNCHECKED_CAST")
        val copyOfKeys = keys.clone() as LinkedList<String>
        var key = copyOfKeys.poll()


        var currentTree: Any = root
        while (key != null) {
            val nextKey = copyOfKeys.poll()

            if (key.isArray()) {
                currentTree = handleArray(currentTree, key, nextKey, newValue)
            } else if (currentTree is JSONObject) {
                currentTree = handleJsonObject(currentTree, key, nextKey, newValue)
            }

            key = nextKey
        }
    }

    fun createInitialTree(root: Any, key: String?): Any {
        val keyIsArray = key.isArray()
        return when {
            root !is JSONArray && keyIsArray -> {
                JSONArray()
            }
            root !is JSONObject && !keyIsArray -> {
                JSONObject()
            }
            else -> root
        }
    }

    private fun handleArray(currentTree: Any, key: String, nextKey: String?, newValue: Any?): Any {
        var tree = currentTree
        if (tree !is JSONArray) {
            tree = JSONArray()
        }
        tree = createTreeToNextKey(tree, key, nextKey)
        if (nextKey == null) {
            (tree as JSONArray).put(JsonPathUtils.getIndexOnArrayBrackets(key), newValue)
        }
        return tree
    }

    private fun handleJsonObject(currentTree: JSONObject, key: String, nextKey: String?, newValue: Any?): Any {
        val json: Any = if (nextKey.isArray()) {
            currentTree.optJSONArray(key) ?: JSONArray()
        } else {
            currentTree.optJSONObject(key) ?: JSONObject()
        }
        currentTree.put(key, if (nextKey == null) newValue else json)
        return json
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