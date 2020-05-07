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

import java.lang.IllegalStateException
import java.util.LinkedList

val ARRAY_POSITION_REGEX = "\\[([^)]+)\\]".toRegex()

internal object JsonPathUtils {

    fun splitKeys(path: String): LinkedList<String> {
        val keys = LinkedList<String>()

        path.split(".").forEach { key ->
            if (key.endsWith("]")) {
                val keyOnly = key.replace(ARRAY_POSITION_REGEX, "")
                val arrayPosition = getArrayBrackets(key) ?: ""
                if (keyOnly.isNotEmpty()) {
                    keys.add(keyOnly)
                }
                keys.add(arrayPosition)
            } else {
                keys.add(key)
            }
        }

        return keys
    }

    fun createArrayExpectedException(): Exception = IllegalStateException("Expected Array but received Object")

    fun createInvalidPathException(): Exception = IllegalStateException("Invalid JSON path")

    private fun getArrayBrackets(arrayIndex: String): String? {
        return ARRAY_POSITION_REGEX.find(arrayIndex)?.value
    }
}