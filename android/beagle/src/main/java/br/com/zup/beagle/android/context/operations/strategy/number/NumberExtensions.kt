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

import br.com.zup.beagle.android.context.operations.strategy.Operations
import br.com.zup.beagle.android.context.operations.strategy.BaseOperation
import br.com.zup.beagle.android.context.operations.parameter.Argument
import br.com.zup.beagle.android.context.operations.parameter.isDouble

internal fun BaseOperation<Operations>.toNumberResult(list: List<Argument>) : Any {
    var hasDecimal = false

    val result = list.map {
        if (it.value.isDouble()) {
            hasDecimal = true
        }

        it.value.toString().toDouble() as Number
    }.reduce { accumulated , element ->
        (this.operationType as NumberOperationTypes).calculate(accumulated, element)
    }

    return if (hasDecimal) result else result.toInt()
}
