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

package br.com.zup.beagle.android.context.tokenizer.function

import br.com.zup.beagle.android.context.tokenizer.function.builtin.ConditionalFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.GreaterFunction
import br.com.zup.beagle.android.context.tokenizer.function.builtin.SumFunction

class FunctionResolver {

    private val functions = createFunctions()

    fun execute(functionName: String, params: List<Any?>): Any? {
        val func = functions[functionName]
        require(func?.isParametersValid(params) == true) {
            "Invalid parameters at function \"$functionName\""
        }
        return func?.execute(params)
    }

    private fun createFunctions(): Map<String, Function<*>> {
        val gt = GreaterFunction()
        val sum = SumFunction()
        val conditional = ConditionalFunction()

        return mapOf(
            gt.functionName() to gt,
            sum.functionName() to sum,
            conditional.functionName() to conditional
        )
    }
}