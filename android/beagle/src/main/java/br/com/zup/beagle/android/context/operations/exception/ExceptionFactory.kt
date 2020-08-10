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

package br.com.zup.beagle.android.context.operations.exception

import br.com.zup.beagle.android.context.operations.exception.strategy.ExceptionOperationTypes
import br.com.zup.beagle.android.context.operations.exception.strategy.ExceptionParameterTypes
import br.com.zup.beagle.android.context.operations.exception.strategy.ExceptionTypes
import br.com.zup.beagle.android.context.operations.operation.Operation
import br.com.zup.beagle.android.context.operations.strategy.string.StringOperationTypes
import br.com.zup.beagle.android.exception.BeagleException

object ExceptionFactory {

    fun createException (exceptionTypes: ExceptionTypes,
                         operation: Operation? = null,
                         details: String) {
        val message: String

        if (exceptionTypes is ExceptionOperationTypes) {
            message = when (exceptionTypes) {
                ExceptionOperationTypes.NOT_FOUND -> {
                    "Could not resolve your operation. Did you check if the Operation is valid?:: $details"
                }
                ExceptionOperationTypes.INVALID_OPERATION -> {
                    "You can't use reserved input in a function name as (true, false, null, number and symbols) or use in parameter(true, false, null):: check $details in your operation"
                }
                ExceptionOperationTypes.MISSING_DELIMITERS -> {
                    "You forgot to input correct delimiters to function() or array{}:: check $details in your operation"
                }
            }
        } else {
            message = when (exceptionTypes as ExceptionParameterTypes) {
                ExceptionParameterTypes.EMPTY -> {
                    "Argument in operation ${operation?.operationToken} cannot be " +  exceptionTypes.name
                }
                ExceptionParameterTypes.NUMBER -> {
                    "Number operations parameters must be a number:: ${operation?.operationToken}"
                }
                ExceptionParameterTypes.ARRAY -> {
                    "First parameter from Array Operations ${operation?.operationToken} must be an Array"
                }
                ExceptionParameterTypes.STRING -> {
                    if (operation?.operationStrategy?.operationType == StringOperationTypes.SUBSTRING) {
                        "SubString operations must have substr('string', index(number), length(number)):: ${operation.operationToken}"
                    } else {
                        "String value must be between (apostrophe mark ->'<-):: $details"
                    }
                }
                ExceptionParameterTypes.INDEX -> {
                    "Index for operation ${operation?.operationToken} needs to be a Number:: $details"
                }
                ExceptionParameterTypes.REQUIRED_ARGS -> {
                    "Non passed required arguments for operation ${operation?.operationToken}:: args passed $details"
                }
            }
        }

        throw BeagleException(message)
    }
}