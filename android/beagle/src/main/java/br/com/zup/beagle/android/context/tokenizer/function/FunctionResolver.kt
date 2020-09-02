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

import br.com.zup.beagle.android.context.tokenizer.function.builtin.mapOfArrayFunctions
import br.com.zup.beagle.android.context.tokenizer.function.builtin.mapOfComparisonFunctions
import br.com.zup.beagle.android.context.tokenizer.function.builtin.mapOfLogicFunctions
import br.com.zup.beagle.android.context.tokenizer.function.builtin.mapOfNumberFunctions
import br.com.zup.beagle.android.context.tokenizer.function.builtin.mapOfOtherFunctions
import br.com.zup.beagle.android.context.tokenizer.function.builtin.mapOfStringFunctions
import br.com.zup.beagle.android.logger.BeagleMessageLogs

internal class FunctionResolver {

    private val functions = createFunctions()

    fun execute(functionName: String, vararg params: Any?): Any? {
        val function = functions[functionName]

        if (function == null) {
            BeagleMessageLogs.functionWithNameDoesNotExist(functionName)
        }

        return function?.execute(*params)
    }

    private fun createFunctions(): Map<String, Function> {
        return mutableMapOf<String, Function>() +
            mapOfNumberFunctions() +
            mapOfArrayFunctions() +
            mapOfLogicFunctions() +
            mapOfComparisonFunctions() +
            mapOfStringFunctions() +
            mapOfOtherFunctions()
    }
}