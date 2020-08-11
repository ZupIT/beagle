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

package br.com.zup.beagle.android.context.operations.exception.strategy

import br.com.zup.beagle.android.context.operations.operation.Operation
import br.com.zup.beagle.android.context.operations.strategy.BaseOperation
import br.com.zup.beagle.android.context.operations.strategy.Operations
import br.com.zup.beagle.android.context.operations.strategy.ProvideOperation
import br.com.zup.beagle.android.context.operations.strategy.invalid.InvalidOperation

enum class ExceptionOperationTypes : ExceptionTypes {

    NOT_FOUND {
        override fun getMessage(details: String, operation: Operation?) =
            "Could not resolve your operation. Did you check if the Operation is valid?:: $details"
    },
    INVALID_OPERATION {
        override fun getMessage(details: String, operation: Operation?) =
            "You can't use reserved input in a function name as (true, false, null, number and symbols) or use in " +
                "parameter(true, false, null):: check $details in your operation"
    },
    MISSING_DELIMITERS {
        override fun getMessage(details: String, operation: Operation?) =
            "You forgot to input correct delimiters to function() or array{}:: check $details in your operation"
    };

    companion object : ProvideOperation {
        override fun getOperationStrategy(input: String): BaseOperation<Operations> {
            return when (input) {
                "null", "false", "true" -> InvalidOperation(INVALID_OPERATION)
                else -> InvalidOperation(NOT_FOUND)
            }
        }
    }
}