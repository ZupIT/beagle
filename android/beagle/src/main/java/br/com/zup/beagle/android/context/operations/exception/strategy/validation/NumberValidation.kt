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

import br.com.zup.beagle.android.context.operations.exception.ExceptionFactory
import br.com.zup.beagle.android.context.operations.exception.strategy.ExceptionParameterTypes
import br.com.zup.beagle.android.context.operations.parameter.Argument
import br.com.zup.beagle.android.context.operations.parameter.Parameter
import br.com.zup.beagle.android.context.operations.parameter.ParameterTypes
import br.com.zup.beagle.android.context.operations.strategy.Operations

internal class NumberValidation : Validation {

    override fun validate(operationType: Operations?, parameter: Parameter) {
        parameter.arguments.forEach {
            if (isNotNumber(it)) {
                val parameterType = parameter.arguments[parameter.arguments.lastIndex].parameterType

                ExceptionFactory.create(
                    if (parameterType == ParameterTypes.EMPTY)
                        ExceptionParameterTypes.EMPTY
                    else ExceptionParameterTypes.NUMBER,
                    parameter.operation,
                    it.toString()
                )
            }
        }
    }

    private fun isNotNumber(it: Argument) =
        it.parameterType != ParameterTypes.NUMBER
}