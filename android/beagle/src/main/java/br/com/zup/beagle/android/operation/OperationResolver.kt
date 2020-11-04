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

package br.com.zup.beagle.android.operation

import br.com.zup.beagle.android.exception.BeagleException
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.setup.InternalOperationFactory
import org.json.JSONArray
import org.json.JSONObject

internal class OperationResolver {

    private val functions = createOperations()

    fun execute(functionName: String, vararg params: Any?): Any? {
        val function = functions[functionName]

        val paramsMapped = params.map { parameter ->
            if (parameter == null) {
                return@map OperationType.Null
            }

            when (parameter) {
                is String -> OperationType.TypeString(parameter)
                is Number -> OperationType.TypeNumber(parameter)
                is Boolean -> OperationType.TypeBoolean(parameter)
                is JSONArray -> OperationType.TypeJsonArray(parameter)
                is JSONObject -> OperationType.TypeJsonObject(parameter)
                else -> throw BeagleException("type not mapped")
            }
        }

        if (function == null) {
            BeagleMessageLogs.functionWithNameDoesNotExist(functionName)
        }

        val result = function?.execute(*paramsMapped.toTypedArray())
        return result?.value
    }


    private fun createOperations(): Map<String, Operation> {
        return mutableMapOf<String, Operation>() +
            InternalOperationFactory.registeredOperations() +
            BeagleEnvironment.beagleSdk.registeredOperations()
    }

}