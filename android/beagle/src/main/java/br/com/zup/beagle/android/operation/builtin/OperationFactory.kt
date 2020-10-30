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

package br.com.zup.beagle.android.operation.builtin

import br.com.zup.beagle.android.operation.Operation
import br.com.zup.beagle.android.operation.builtin.array.ContainsOperation
import br.com.zup.beagle.android.operation.builtin.array.InsertOperation
import br.com.zup.beagle.android.operation.builtin.array.RemoveOperation
import br.com.zup.beagle.android.operation.builtin.array.RemoveIndexOperation
import br.com.zup.beagle.android.operation.builtin.comparison.EqOperation
import br.com.zup.beagle.android.operation.builtin.comparison.GtOperation
import br.com.zup.beagle.android.operation.builtin.comparison.GteOperation
import br.com.zup.beagle.android.operation.builtin.comparison.LtOperation
import br.com.zup.beagle.android.operation.builtin.comparison.LteOperation
import br.com.zup.beagle.android.operation.builtin.logic.AndOperation
import br.com.zup.beagle.android.operation.builtin.logic.ConditionOperation
import br.com.zup.beagle.android.operation.builtin.logic.NotOperation
import br.com.zup.beagle.android.operation.builtin.logic.OrOperation
import br.com.zup.beagle.android.operation.builtin.number.DivideOperation
import br.com.zup.beagle.android.operation.builtin.number.MultiplyOperation
import br.com.zup.beagle.android.operation.builtin.number.SubtractOperation
import br.com.zup.beagle.android.operation.builtin.number.SumOperation
import br.com.zup.beagle.android.operation.builtin.other.IsEmptyOperation
import br.com.zup.beagle.android.operation.builtin.other.IsNullOperation
import br.com.zup.beagle.android.operation.builtin.other.LengthOperation
import br.com.zup.beagle.android.operation.builtin.string.CapitalizeOperation
import br.com.zup.beagle.android.operation.builtin.string.ConcatOperation
import br.com.zup.beagle.android.operation.builtin.string.LowercaseOperation
import br.com.zup.beagle.android.operation.builtin.string.SubstrOperation
import br.com.zup.beagle.android.operation.builtin.string.UppercaseOperation

internal fun mapOfArrayOperations(): Map<String, Operation> {
    val includes = ContainsOperation()
    val insert = InsertOperation()
    val remove = RemoveOperation()
    val removeIndex = RemoveIndexOperation()

    return mapOf(
        includes.operationName() to includes,
        insert.operationName() to insert,
        remove.operationName() to remove,
        removeIndex.operationName() to removeIndex
    )
}

internal fun mapOfLogicOperations(): Map<String, Operation> {
    val and = AndOperation()
    val not = NotOperation()
    val or = OrOperation()
    val condition = ConditionOperation()

    return mapOf(
        and.operationName() to and,
        condition.operationName() to condition,
        not.operationName() to not,
        or.operationName() to or
    )
}

internal fun mapOfComparisonOperations(): Map<String, Operation> {
    val eq = EqOperation()
    val gte = GteOperation()
    val gt = GtOperation()
    val lte = LteOperation()
    val lt = LtOperation()

    return mapOf(
        eq.operationName() to eq,
        gte.operationName() to gte,
        gt.operationName() to gt,
        lte.operationName() to lte,
        lt.operationName() to lt
    )
}

internal fun mapOfStringOperations(): Map<String, Operation> {
    val concat = ConcatOperation()
    val capitalize = CapitalizeOperation()
    val uppercase = UppercaseOperation()
    val lowercase = LowercaseOperation()
    val substr = SubstrOperation()

    return mapOf(
        concat.operationName() to concat,
        capitalize.operationName() to capitalize,
        uppercase.operationName() to uppercase,
        lowercase.operationName() to lowercase,
        substr.operationName() to substr
    )
}

internal fun mapOfNumberOperations(): Map<String, Operation> {
    val divide = DivideOperation()
    val multiply = MultiplyOperation()
    val subtract = SubtractOperation()
    val sum = SumOperation()

    return mapOf(
        divide.operationName() to divide,
        multiply.operationName() to multiply,
        subtract.operationName() to subtract,
        sum.operationName() to sum
    )
}

internal fun mapOfOtherOperations(): Map<String, Operation> {
    val isNull = IsNullOperation()
    val isEmpty = IsEmptyOperation()
    val length = LengthOperation()

    return mapOf(
        isNull.operationName() to isNull,
        isEmpty.operationName() to isEmpty,
        length.operationName() to length
    )
}
