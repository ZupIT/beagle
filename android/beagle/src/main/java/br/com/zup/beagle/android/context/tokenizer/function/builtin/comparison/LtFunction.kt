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

package br.com.zup.beagle.android.context.tokenizer.function.builtin.comparison

import br.com.zup.beagle.android.context.tokenizer.function.Function

internal class LtFunction : Function {

    override fun execute(vararg params: Any?): Boolean {
        val value1 = params[0]
        val value2 = params[1]

        return if (value1 is Int && value2 is Int) {
            value1 < value2
        } else {
            (value1 as Double) < (value2 as Double)
        }
    }

    override fun functionName(): String = "lt"
}