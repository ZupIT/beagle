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

package br.com.zup.beagle.android.context.operations.strategy.comparison

import br.com.zup.beagle.android.context.operations.strategy.Operations
import br.com.zup.beagle.android.context.operations.strategy.BaseOperation
import br.com.zup.beagle.android.context.operations.parameter.Parameter

internal fun BaseOperation<Operations>.toBoolean(parameter: Parameter) : Any {
    val firstValue = parameter.arguments[0].value.toString().toDouble()
    val secondValue = parameter.arguments[1].value.toString().toDouble()
    val isSizeCorrect = parameter.arguments.size == 2

    return when (this.operationType) {
        ComparisionOperationTypes.GREATER_THAN -> isSizeCorrect && firstValue > secondValue
        ComparisionOperationTypes.GREATER_THAN_EQUALS -> isSizeCorrect && firstValue >= secondValue
        ComparisionOperationTypes.LESS_THEN -> isSizeCorrect && firstValue < secondValue
        ComparisionOperationTypes.LESS_THEN_EQUALS -> isSizeCorrect && firstValue <= secondValue
        ComparisionOperationTypes.EQUALS -> isSizeCorrect && firstValue == secondValue
        else -> false
    }
}
