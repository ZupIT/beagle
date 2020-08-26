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

internal class RemoveIndexFunction : Function {
    override fun functionName(): String = "removeIndex"

    @Suppress("ReturnCount")
    override fun execute(vararg params: Any?): Any {
        val array = params[0]
        val index = params[1] as Int

        if (array is Collection<*>) {
            val list = array.toMutableList()
            list.removeAt(index)
            return list
        } else if (array is JSONArray) {
            val newArray = JSONArray()
            for (i in 0 until array.length()) {
                if (i != index) {
                    newArray.put(array[i])
                }
            }
            return newArray
        }

        return emptyList<Any>()
    }

}