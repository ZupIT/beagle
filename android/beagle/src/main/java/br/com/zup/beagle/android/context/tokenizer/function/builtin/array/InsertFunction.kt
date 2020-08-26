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
import br.com.zup.beagle.android.context.tokenizer.function.builtin.toJSONArray
import org.json.JSONArray

internal class InsertFunction : Function {
    override fun functionName(): String = "insert"

    override fun execute(vararg params: Any?): Any {
        val array = params[0]
        val element = params[1] as Any
        val index = params.getOrNull(2) as? Int

        if (array is Collection<*>) {
            val list = array.toMutableList()
            return insertOnList(list, element, index)
        } else if (array is JSONArray) {
            return insertOnJSONArray(array, element, index)
        }

        return emptyList<Any>()
    }

    private fun insertOnList(list: MutableList<Any?>, element: Any, index: Int?): List<Any?> {
        if (index != null) {
            list.add(index, element)
        } else {
            list.add(element)
        }

        return list
    }


    private fun insertOnJSONArray(list: JSONArray, element: Any, index: Int?): JSONArray {
        if (index != null) {
            list.put(index, element)
        } else {
            list.put(element)
        }

        return list
    }
}