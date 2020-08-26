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

data class ExpressionToken(
    val value: String,
    val token: Token
)

open class Token(
    open val value: Any
)

internal open class TokenValue(
    value: Any
) : Token(value)

internal enum class TokenType {
    FUNCTION_START,
    OPEN_BRACKET,
    CLOSE_BRACKET,
    COMMA
}

internal open class GenericToken(
    override val value: String,
    val type: TokenType
) : Token(value)

internal class TokenBinding(
    override val value: String
) : TokenValue(value)

internal class TokenNumber(
    override val value: Number
) : TokenValue(value)

internal class TokenString(
    override val value: String
) : TokenValue(value)

internal class TokenBoolean(
    override val value: Boolean
) : TokenValue(value)

internal class TokenNull : Token(Any())

internal class InvalidToken : Token(Any())

internal class TokenFunction(
    val name: String,
    override val value: List<Token>
) : Token(value)