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

package br.com.zup.beagle.android.context.operations.exception.strategy.validation

import br.com.zup.beagle.android.context.operations.parameter.Parameter
import br.com.zup.beagle.android.context.operations.strategy.Operations
import br.com.zup.beagle.android.context.operations.strategy.array.ArrayOperationTypes
import br.com.zup.beagle.android.context.operations.strategy.comparison.ComparisionOperationTypes
import br.com.zup.beagle.android.context.operations.strategy.number.NumberOperationTypes
import br.com.zup.beagle.android.context.operations.strategy.string.StringOperationTypes

internal object ValidationFactory : Validation {

    override fun validate(operationType: Operations?, parameter: Parameter) {
        when (operationType) {
            is StringOperationTypes -> StringValidation().validate(operationType, parameter)
            is ArrayOperationTypes -> ArrayValidation().validate(operationType, parameter)
            is NumberOperationTypes,
            is ComparisionOperationTypes -> NumberValidation().validate(operationType, parameter)
        }
    }
}