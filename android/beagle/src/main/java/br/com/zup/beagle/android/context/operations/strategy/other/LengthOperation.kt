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

package br.com.zup.beagle.android.context.operations.strategy.other

import br.com.zup.beagle.android.context.operations.strategy.Operations
import br.com.zup.beagle.android.context.operations.strategy.BaseOperation
import br.com.zup.beagle.android.context.operations.strategy.string.withoutApostrophe
import br.com.zup.beagle.android.context.operations.parameter.Parameter
import br.com.zup.beagle.android.context.operations.parameter.ParameterTypes

internal class LengthOperation(override val operationType: Operations) : BaseOperation<Operations>() {
    override fun solve(parameter: Parameter): Any {
        var value = 0

        if (parameter.arguments.isNotEmpty()) {
            if (parameter.arguments[0].parameterType == ParameterTypes.STRING) {
                value = (parameter.arguments[0].value as String).withoutApostrophe().length
            } else if (parameter.arguments[0].parameterType == ParameterTypes.ARRAY &&
                (parameter.arguments[0].value as List<Any?>).size > 0) {
                value = (parameter.arguments[0].value as List<Any?>).size
            }
        }

        return value
    }
}