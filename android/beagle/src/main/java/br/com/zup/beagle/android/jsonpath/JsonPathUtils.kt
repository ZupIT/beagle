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

import java.lang.IllegalStateException
import java.util.*

val ARRAY_POSITION_REGEX = "\\[([^)]+)\\]".toRegex()

internal object JsonPathUtils {

    fun splitKeys(path: String): LinkedList<String> {
        val keys = LinkedList<String>()

        path.split(".").forEach { key ->
            if (key.endsWith("]")) {
                val keyOnly = key.replace(ARRAY_POSITION_REGEX, "")
                if (keyOnly.isNotEmpty()) {
                    keys.add(keyOnly)
                }
                val arrayKeys = key.replace(keyOnly, "")
                    .split("[")
                    .filter { it.isNotEmpty() }
                    .map { "[$it" }
                keys.addAll(arrayKeys)
            } else {
                keys.add(key)
            }
        }

        return keys
    }

    fun createArrayExpectedException() = IllegalStateException("Expected Array but received Object")

    fun createInvalidPathException(path: String) = IllegalStateException("Invalid JSON path at key \"$path\"")

    fun getIndexOnArrayBrackets(arrayIndex: String): Int {
        return ARRAY_POSITION_REGEX.find(arrayIndex)?.groups?.get(1)?.value?.toInt() ?:
        throw IllegalStateException("Invalid array position $arrayIndex.")
    }
}

