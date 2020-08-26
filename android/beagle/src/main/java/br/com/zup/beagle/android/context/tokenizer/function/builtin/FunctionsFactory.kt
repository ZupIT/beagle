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

package br.com.zup.beagle.android.context.tokenizer.function.builtin

import br.com.zup.beagle.android.context.tokenizer.function.Function
import br.com.zup.beagle.android.context.tokenizer.function.builtin.array.ContainsFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.array.InsertFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.array.RemoveFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.array.RemoveIndexFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.comparison.EqFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.comparison.GtFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.comparison.GteFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.comparison.LtFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.comparison.LteFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.logic.AndFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.logic.ConditionFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.logic.NotFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.logic.OrFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.number.DivideFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.number.MultiplyFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.number.SubtractFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.number.SumFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.other.IsEmptyFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.other.IsNullFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.other.LengthFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.string.CapitalizeFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.string.ConcatFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.string.LowercaseFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.string.SubstrFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.string.UppercaseFunction

internal fun mapOfArrayFunctions(): Map<String, Function> {
    val includes = ContainsFunction()
    val insert = InsertFunction()
    val remove = RemoveFunction()
    val removeIndex = RemoveIndexFunction()

    return mapOf(
        includes.functionName() to includes,
        insert.functionName() to insert,
        remove.functionName() to remove,
        removeIndex.functionName() to removeIndex
    )
}

internal fun mapOfLogicFunctions(): Map<String, Function> {
    val and = AndFunction()
    val not = NotFunction()
    val or = OrFunction()
    val condition = ConditionFunction()

    return mapOf(
        and.functionName() to and,
        condition.functionName() to condition,
        not.functionName() to not,
        or.functionName() to or
    )
}

internal fun mapOfComparisonFunctions(): Map<String, Function> {
    val eq = EqFunction()
    val gte = GteFunction()
    val gt = GtFunction()
    val lte = LteFunction()
    val lt = LtFunction()

    return mapOf(
        eq.functionName() to eq,
        gte.functionName() to gte,
        gt.functionName() to gt,
        lte.functionName() to lte,
        lt.functionName() to lt
    )
}

internal fun mapOfStringFunctions(): Map<String, Function> {
    val concat = ConcatFunction()
    val capitalize = CapitalizeFunction()
    val uppercase = UppercaseFunction()
    val lowercase = LowercaseFunction()
    val substr = SubstrFunction()

    return mapOf(
        concat.functionName() to concat,
        capitalize.functionName() to capitalize,
        uppercase.functionName() to uppercase,
        lowercase.functionName() to lowercase,
        substr.functionName() to substr
    )
}

internal fun mapOfNumberFunctions(): Map<String, Function> {
    val divide = DivideFunction()
    val multiply = MultiplyFunction()
    val subtract = SubtractFunction()
    val sum = SumFunction()

    return mapOf(
        divide.functionName() to divide,
        multiply.functionName() to multiply,
        subtract.functionName() to subtract,
        sum.functionName() to sum
    )
}

internal fun mapOfOtherFunctions(): Map<String, Function> {
    val isNull = IsNullFunction()
    val isEmpty = IsEmptyFunction()
    val length = LengthFunction()

    return mapOf(
        isNull.functionName() to isNull,
        isEmpty.functionName() to isEmpty,
        length.functionName() to length
    )
}
