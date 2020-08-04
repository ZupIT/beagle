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

    fun walkingTreeAndFindKey(
        root: Any,
        keys: LinkedList<String>,
        newValue: Any?,
        createPathIfDoesNotExist: Boolean = true
    ) {
        @Suppress("UNCHECKED_CAST")
        val copyOfKeys = keys.clone() as LinkedList<String>
        var key = copyOfKeys.poll()

        var currentTree: Any = root
        while (key != null) {
            val nextKey = copyOfKeys.poll()

            if (key.isArray()) {
                val newNode = handleArray(currentTree, key, nextKey, newValue, createPathIfDoesNotExist)
                if (newNode != null) {
                    currentTree = newNode
                } else {
                    break
                }
            } else if (currentTree is JSONObject) {
                val newNode = handleJsonObject(currentTree, key, nextKey, newValue, createPathIfDoesNotExist)
                if (newNode != null) {
                    currentTree = newNode
                } else {
                    break
                }
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

    private fun handleArray(
        currentTree: Any,
        key: String,
        nextKey: String?,
        newValue: Any?,
        createPathIfDoesNotExist: Boolean
    ): Any? {
        var tree: Any? = currentTree
        if (tree !is JSONArray) {
            tree = JSONArray()
        }
        tree = createTreeToNextKey(tree, key, nextKey, createPathIfDoesNotExist)
        if (nextKey == null) {
            (tree as JSONArray).put(JsonPathUtils.getIndexOnArrayBrackets(key), newValue)
        }
        return tree
    }

    private fun handleJsonObject(
        currentTree: JSONObject,
        key: String,
        nextKey: String?,
        newValue: Any?,
        createPathIfDoesNotExist: Boolean
    ): Any? {
        val json: Any? = if (nextKey.isArray()) {
            currentTree.optJSONArray(key) ?: createNodeIfPossible(createPathIfDoesNotExist, JSONArray())
        } else {
            currentTree.optJSONObject(key) ?: createNodeIfPossible(createPathIfDoesNotExist, JSONObject())
        }
        currentTree.put(key, if (nextKey == null) newValue else json)
        return json
    }

    private fun createNodeIfPossible(createPathIfDoesNotExist: Boolean, nodeValue: Any): Any? {
        return if (createPathIfDoesNotExist) {
            nodeValue
        } else {
            null
        }
    }

    private fun createTreeToNextKey(
        jsonArray: JSONArray,
        key: String,
        nextKey: String?,
        createPathIfDoesNotExist: Boolean
    ): Any? {
        val position = JsonPathUtils.getIndexOnArrayBrackets(key)
        var opt: Any? = jsonArray.optJSONObject(position) ?: jsonArray.optJSONArray(position)
        if (createPathIfDoesNotExist && (opt == JSONObject.NULL || opt == null)) {
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