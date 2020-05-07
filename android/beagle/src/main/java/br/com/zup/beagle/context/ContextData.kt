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

import java.util.LinkedList

// TODO: implement LruCache to deal with expressions that were previous accessed

class ContextData(
    val id: String,
    value: Any
) {

    private val jsonPathFinder = JsonPathFinder()
    private val jsonPathReplacer = JsonPathReplacer()

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
            val keys = generateKeys(path)
            return jsonPathFinder.find(keys, value)
        }
    }

    fun setValue(path: String, value: Any): Boolean {
        return if (path == id) {
            _value = value
            true
        } else {
            val keys = generateKeys(path)
            jsonPathReplacer.replace(keys, value, _value)
        }
    }

    private fun generateKeys(path: String): LinkedList<String> {
        val newPath = removeContextFromPath(path)
        val keys = JsonPathUtils.splitKeys(newPath)

        if (keys.size == 1 && keys.first == path) {
            throw JsonPathUtils.createInvalidPathException()
        }

        return keys
    }

    private fun removeContextFromPath(path: String): String {
        val newPath = path.replace(id, "")

        if (newPath.isEmpty()) {
            throw JsonPathUtils.createInvalidPathException()
        } else if (newPath.startsWith(".")) {
            return newPath.replaceFirst(".", "")
        }

        return newPath
    }
}