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

package br.com.zup.beagle.android.context.operations.operation

import br.com.zup.beagle.android.context.operations.exception.ExceptionWrapper
import br.com.zup.beagle.android.context.operations.strategy.BaseOperation
import br.com.zup.beagle.android.context.operations.strategy.Operations

/**
 * @param operationToken Store the operation token that will be solved to update the principal operation with result
 *
 * @param operationStrategy The OperationType is the strategy of all operations
 * @see BaseOperation
 *
 * @param operationValue The value that will be solved by strategy above
 *
 * **/

data class Operation (
    val operationToken: String,
    val operationStrategy: BaseOperation<Operations>?,
    val operationValue: String
) {

    init {
        ExceptionWrapper.validateOperation(this)
    }

    fun validate() : Any? = operationStrategy?.execute(this.toParameterType())
}
