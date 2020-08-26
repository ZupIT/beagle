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

package br.com.zup.beagle.android.context.tokenizer.function.builtin.other

import br.com.zup.beagle.android.context.tokenizer.function.Function
import org.json.JSONArray
import org.json.JSONObject

internal class IsEmptyFunction : Function {
    override fun functionName(): String = "isEmpty"

    override fun execute(vararg params: Any?): Boolean {
        return when (val value = params[0]) {
            is String -> value.isEmpty()
            is Collection<Any?> -> value.isEmpty()
            is JSONArray -> value.length() == 0
            is JSONObject -> value.length() == 0
            is Map<*, *> -> value.isEmpty()
            else -> true
        }
    }

}