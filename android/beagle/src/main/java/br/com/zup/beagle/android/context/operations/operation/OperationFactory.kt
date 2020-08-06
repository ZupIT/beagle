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

import br.com.zup.beagle.android.context.operations.exception.strategy.ExceptionOperationTypes
import br.com.zup.beagle.android.context.operations.strategy.BaseOperation
import br.com.zup.beagle.android.context.operations.strategy.Operations
import br.com.zup.beagle.android.context.operations.strategy.array.ArrayOperationTypes
import br.com.zup.beagle.android.context.operations.strategy.comparison.ComparisionOperationTypes
import br.com.zup.beagle.android.context.operations.strategy.logic.LogicOperationTypes
import br.com.zup.beagle.android.context.operations.strategy.number.NumberOperationTypes
import br.com.zup.beagle.android.context.operations.strategy.other.OtherOperationTypes
import br.com.zup.beagle.android.context.operations.strategy.string.StringOperationTypes
import java.util.Locale

internal object OperationFactory {
    fun create(value: String) : BaseOperation<Operations>? =
        value.toLowerCase(Locale.ROOT).getOperationType()
}

private fun String.getOperationType(): BaseOperation<Operations>? =
    this.getSumOperation()

private fun String.getSumOperation(): BaseOperation<Operations>? =
    NumberOperationTypes.getOperation(this) ?: getLiteralOperation()

private fun String.getLiteralOperation(): BaseOperation<Operations>? =
    ComparisionOperationTypes.getOperation(this) ?: getLogicOperation()

private fun String.getLogicOperation(): BaseOperation<Operations>? =
    LogicOperationTypes.getOperation(this) ?: getStringOperation()

private fun String.getStringOperation(): BaseOperation<Operations>? =
    StringOperationTypes.getOperation(this) ?: getArrayOperation()

private fun String.getArrayOperation(): BaseOperation<Operations>? =
    ArrayOperationTypes.getOperation(this) ?: getOtherOperation()

private fun String.getOtherOperation(): BaseOperation<Operations>? =
    OtherOperationTypes.getOperation(this) ?: getInvalidOperation()

private fun String.getInvalidOperation(): BaseOperation<Operations>? =
    ExceptionOperationTypes.getOperation(this)