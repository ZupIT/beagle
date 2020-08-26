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

package br.com.zup.beagle.android.context.tokenizer.function.builtin.array

import br.com.zup.beagle.android.context.tokenizer.function.Function
import org.json.JSONArray

internal class RemoveFunction : Function {
    override fun functionName(): String = "remove"

    @Suppress("ReturnCount")
    override fun execute(vararg params: Any?): Any {
        val array = params[0]
        val element = params[1] as Any

        if (array is Collection<*>) {
            val list = array.toMutableList()
            return removeElementsOnList(list, element)
        } else if (array is JSONArray) {
            return removeElementsOnJSONArray(array, element)
        }


        return emptyList<Any>()
    }

    private fun removeElementsOnList(list: MutableList<Any?>, element: Any): List<Any?> {
        var shouldRemove = true
        while (shouldRemove) {
            shouldRemove = list.remove(element)
        }
        return list
    }

    private fun removeElementsOnJSONArray(array: JSONArray, element: Any): JSONArray {
        val newArray = JSONArray()
        for (i in 0 until array.length()) {
            if (array[i] != element) {
                newArray.put(array[i])
            }
        }
        return newArray
    }
}