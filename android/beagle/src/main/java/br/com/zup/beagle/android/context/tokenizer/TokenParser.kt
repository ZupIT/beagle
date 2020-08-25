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

import java.lang.IllegalStateException

class TokenParser {

    fun parse(expression: String): ExpressionToken {
        val interpreter = TokenInterpreter(expression)

        return interpreter.nextToken()?.let {
            val token = parseToken(interpreter, it)
            return ExpressionToken(expression, token)
        } ?: kotlin.run {
            throw IllegalStateException("Invalid expression")
        }
    }

    private fun parseToken(interpreter: TokenInterpreter, token: Token): Token {
        return when (token) {
            is TokenValue, is TokenNull -> token
            is GenericToken -> parseFunction(token, interpreter)
            else -> throw IllegalStateException("Undefined token $token")
        }
    }

    private fun parseFunction(token: GenericToken, interpreter: TokenInterpreter): Token {
        val params = mutableListOf<Token>()

        do {
            val nextToken = interpreter.nextToken()
            if (nextToken != null) {
                if (nextToken is GenericToken) {
                    if (nextToken.type == TokenType.CLOSE_BRACKET) {
                        break
                    } else if (nextToken.type == TokenType.COMMA) {
                        continue
                    }
                }
                params.add(parseToken(interpreter, nextToken))
            }
        } while (nextToken != null)

        return tokenOfFunction(token.value, params)
    }
}