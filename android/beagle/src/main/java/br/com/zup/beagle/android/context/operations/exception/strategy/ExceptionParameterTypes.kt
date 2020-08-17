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
import br.com.zup.beagle.android.context.operations.strategy.string.StringOperationTypes

internal enum class ExceptionParameterTypes : ExceptionTypes {
    EMPTY {
        override fun getMessage(details: String, operation: Operation?) =
            "Argument in operation ${operation?.operationToken} cannot be " + this.name
    },
    REQUIRED_ARGS {
        override fun getMessage(details: String, operation: Operation?) =
            "Non passed required arguments for operation ${operation?.operationToken}:: args passed $details"
    },
    NUMBER {
        override fun getMessage(details: String, operation: Operation?) =
            "Number operations parameters must be a number:: ${operation?.operationToken}"
    },
    ARRAY {
        override fun getMessage(details: String, operation: Operation?) =
            "First parameter from Array Operations ${operation?.operationToken} must be an Array"
    },
    INDEX {
        override fun getMessage(details: String, operation: Operation?) =
            "Index for operation ${operation?.operationToken} needs to be a Number:: $details"
    },
    STRING {
        override fun getMessage(details: String, operation: Operation?) =
            if (operation?.operationStrategy?.operationType == StringOperationTypes.SUBSTRING) {
                "SubString operations must have substr('string', number, number):: ${operation.operationToken}"
            } else {
                "String value must be between (apostrophe mark ->'<-):: $details"
            }
    }
}
