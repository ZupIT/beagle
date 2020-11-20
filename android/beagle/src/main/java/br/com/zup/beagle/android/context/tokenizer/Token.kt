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

import com.facebook.jni.annotations.DoNotStrip
import com.squareup.moshi.Json

data class ExpressionToken(
    @Json(name = "value") val value: String,
    @Json(name = "token") val token: Token
)

open class Token(
    @Json(name = "value") open val value: Any
)

internal open class TokenValue(
    @Json(name = "value") value: Any
) : Token(value)

@DoNotStrip
internal enum class TokenType {
    FUNCTION_START,
    OPEN_BRACKET,
    CLOSE_BRACKET,
    COMMA
}

internal open class GenericToken(
    @Json(name = "value") override val value: String,
    @Json(name = "type") val type: TokenType
) : Token(value)

internal class TokenBinding(
    @Json(name = "value") override val value: String
) : TokenValue(value)

internal class TokenNumber(
    @Json(name = "value") override val value: Number
) : TokenValue(value)

internal class TokenString(
    @Json(name = "value") override val value: String
) : TokenValue(value)

internal class TokenBoolean(
    @Json(name = "value") override val value: Boolean
) : TokenValue(value)

internal class TokenNull : Token(Any())

internal class InvalidToken : Token(Any()) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        return other is InvalidToken
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}

internal class TokenFunction(
    @Json(name = "name") val name: String,
    @Json(name = "value") override val value: List<Token>
) : Token(value)