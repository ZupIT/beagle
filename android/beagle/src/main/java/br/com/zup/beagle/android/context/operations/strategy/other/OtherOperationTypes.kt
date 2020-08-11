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

import br.com.zup.beagle.android.context.operations.strategy.BaseOperation
import br.com.zup.beagle.android.context.operations.strategy.Operations
import br.com.zup.beagle.android.context.operations.strategy.ProvideOperation
import br.com.zup.beagle.android.context.operations.strategy.array.InsertOperation
import br.com.zup.beagle.android.context.operations.strategy.array.RemoveIndexOperation
import br.com.zup.beagle.android.context.operations.strategy.array.RemoveOperation

enum class OtherOperationTypes(val input: String) : Operations {
    IS_EMPTY("isempty"),
    IS_NULL("isnull"),
    LENGTH("length");

    companion object : ProvideOperation {
        override fun getOperationStrategy(input: String): BaseOperation<Operations>? {
            val found = values().find { it.input == input }
            return if (found != null)
                when (found) {
                    IS_EMPTY -> IsEmptyOperation(found)
                    IS_NULL -> IsNullOperation(found)
                    LENGTH -> LengthOperation(found)
                }
            else null
        }
    }
}