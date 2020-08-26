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

package br.com.zup.beagle.android.context.tokenizer.function.builtin.logic

import br.com.zup.beagle.android.context.tokenizer.function.Function
import br.com.zup.beagle.android.context.tokenizer.function.builtin.toListOfBooleans

internal class AndFunction : Function {

    override fun execute(vararg params: Any?): Boolean {
        return params.toListOfBooleans().reduce{ boolean1, boolean2 -> boolean1 && boolean2 }
    }

    override fun functionName(): String = "and"
}