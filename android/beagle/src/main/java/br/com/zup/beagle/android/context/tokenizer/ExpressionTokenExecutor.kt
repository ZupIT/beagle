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

package br.com.zup.beagle.android.context.tokenizer

import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.context.tokenizer.function.FunctionResolver
import br.com.zup.beagle.android.utils.getContextId
import java.lang.IllegalStateException

typealias FindValue = (String, ContextData?) -> Any?

internal class ExpressionTokenExecutor(
    private val functionResolver: FunctionResolver = FunctionResolver()
) {

    fun execute(
        contexts: List<ContextData>,
        expressionToken: ExpressionToken,
        findValue: FindValue
    ): Any? {
        if (expressionToken.token is InvalidToken) {
            return null
        }
        return interpretToken(findValue, contexts, expressionToken.token)
    }

    private fun interpretToken(findValue: FindValue, contexts: List<ContextData>, token: Token): Any? {
        return when (token) {
            is TokenNull -> null
            is TokenBinding -> interpretBinding(findValue, contexts, token)
            is TokenValue -> token.value
            is TokenFunction -> interpretFunction(findValue, contexts, token)
            else -> throw IllegalStateException("Undefined token $token")
        }
    }

    private fun interpretFunction(
        findValue: FindValue,
        contexts: List<ContextData>,
        tokenFunction: TokenFunction
    ): Any? {
        val params = arrayOfNulls<Any?>(tokenFunction.value.size)

        tokenFunction.value.forEachIndexed { index, token ->
            params[index] = interpretToken(findValue, contexts, token)
        }

        return functionResolver.execute(tokenFunction.name, *params)
    }

    private fun interpretBinding(findValue: FindValue, contexts: List<ContextData>, tokenBinding: TokenBinding): Any? {
        val contextId = tokenBinding.value.getContextId()
        val contextData = contexts.find { it.id == contextId }
        return findValue(tokenBinding.value, contextData)
    }
}