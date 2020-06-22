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

    fun walkingTreeAndFindKey(root: Any, keys: LinkedList<String>): Pair<Any, Any> {
        var newJson = root

        var key = keys.poll()

        val keyIsArray = key.isArray()
        if (newJson is JSONObject && keyIsArray) {
            newJson = JSONArray()
        } else if (newJson is JSONArray && !keyIsArray) {
            newJson = JSONObject()
        }

        var currentTree: Any = newJson
        while (key != null) {
            val nextKey = keys.poll()

            if (key.isArray()) {
                if (currentTree !is JSONArray) {
                    currentTree = JSONArray()
                }
                currentTree = createJsonArrayToNextKey(currentTree, key, nextKey)
            } else if (currentTree is JSONObject) {
                var json: Any = currentTree.optJSONObject(key) ?: JSONObject()
                if (nextKey.isArray()) {
                    val jsonArray = currentTree.optJSONArray(key) ?: JSONArray()
                    json = createJsonArrayToNextKey(jsonArray, nextKey!!, null)
                }
                currentTree.put(key, if (nextKey == null) JSONObject.NULL else json)
                currentTree = json
            }

            key = nextKey
        }
        return Pair(newJson, currentTree)
    }

    private fun createJsonArrayToNextKey(jsonArray: JSONArray, key: String, nextKey: String?): Any {
        val position = key.replace("[^0-9]".toRegex(), "").toInt()
        var opt = jsonArray.opt(position)
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
        }
        return opt
    }

    private fun String?.isArray() = this?.startsWith("[") == true
}