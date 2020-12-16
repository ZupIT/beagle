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

@RegisterOperation("remove")
internal class RemoveOperation : Operation {

    override fun execute(vararg params: OperationType?): OperationType {
        val array = (params[0] as OperationType.TypeJsonArray).value
        val element = params[1]?.value

        val result = removeElementsOnJSONArray(array, element)
        return OperationType.TypeJsonArray(result)
    }

    private fun removeElementsOnJSONArray(array: JSONArray, element: Any?): JSONArray {
        val newArray = JSONArray()
        for (i in 0 until array.length()) {
            if (array[i] != element) {
                newArray.put(array[i])
            }
        }
        return newArray
    }
}