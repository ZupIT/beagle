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

package br.com.zup.beagle.android.context.operations.common.read

import br.com.zup.beagle.android.context.operations.common.ExtractValueTypes
import br.com.zup.beagle.android.context.operations.common.read.strategy.ArrayLogic
import br.com.zup.beagle.android.context.operations.common.read.strategy.OperationLogic
import br.com.zup.beagle.android.context.operations.common.read.strategy.ReadLogic

internal object ReadLogicFactory {
    fun create(
        inputValue: String,
        extractValueTypes: ExtractValueTypes,
        delimiterStartIndex: Int,
        delimiterEndIndex: Int
    ) : ReadLogic {

        return if (extractValueTypes == ExtractValueTypes.OPERATION) {
            OperationLogic(
                inputValue,
                extractValueTypes,
                delimiterStartIndex,
                delimiterEndIndex
            )
        } else {
            ArrayLogic(
                inputValue,
                extractValueTypes,
                delimiterEndIndex
            )
        }
    }
}