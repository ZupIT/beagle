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

package br.com.zup.beagle.android.operation.builtin.array

import br.com.zup.beagle.android.operation.Operation
import br.com.zup.beagle.android.operation.OperationType
import br.com.zup.beagle.annotation.RegisterOperation
import org.json.JSONArray

@RegisterOperation("insert")
internal class InsertOperation : Operation {

    override fun execute(vararg params: OperationType?): OperationType {
        val array = (params[0] as OperationType.TypeJsonArray).value
        val element = params.getOrNull(1)?.value
        val index = (params.getOrNull(2) as? OperationType.TypeNumber)?.value?.toInt()

        val result = insertOnJSONArray(array, element, index)

        return OperationType.TypeJsonArray(result)
    }

    private fun insertOnJSONArray(array: JSONArray, element: Any?, index: Int?): JSONArray {
        if (index != null) {
            return appendValueToJSONArray(array, element, index)
        } else {
            array.put(element)
        }

        return array
    }

    private fun appendValueToJSONArray(array: JSONArray, element: Any?, index: Int): JSONArray {
        val newArray = JSONArray()
        for (i in 0 until array.length()) {
            if (i == index) {
                newArray.put(element)
                if (array[i] != null) {
                    newArray.put(array[i])
                }
            } else {
                newArray.put(array[i])
            }
        }
        return newArray
    }
}