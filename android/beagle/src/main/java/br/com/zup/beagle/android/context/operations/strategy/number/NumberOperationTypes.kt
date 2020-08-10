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

package br.com.zup.beagle.android.context.operations.strategy.number

import br.com.zup.beagle.android.context.operations.exception.strategy.validation.OperationsValidation
import br.com.zup.beagle.android.context.operations.strategy.BaseOperation
import br.com.zup.beagle.android.context.operations.strategy.Operations

internal enum class NumberOperationTypes(val input: String) : OperationsValidation {
    SUM("sum"),
    SUBTRACT("subtract"),
    MULTIPLY("multiply"),
    DIVIDE("divide");

    companion object {
        fun getOperation(input: String): BaseOperation<Operations>? {
            val found = values().find { it.input == input }
            return if (found != null) NumberOperation(found) else null
        }
    }
}
