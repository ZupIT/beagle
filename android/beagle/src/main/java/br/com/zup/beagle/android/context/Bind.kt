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

@file:Suppress("UNCHECKED_CAST")

package br.com.zup.beagle.android.context

import br.com.zup.beagle.android.context.tokenizer.ExpressionToken
import br.com.zup.beagle.android.context.tokenizer.TokenParser
import br.com.zup.beagle.android.utils.BeagleRegex
import br.com.zup.beagle.android.utils.getExpressions
import br.com.zup.beagle.core.BeagleJson
import java.lang.reflect.Type

@BeagleJson
sealed class Bind<T> {
    abstract val type: Type
    abstract val value: Any

    @BeagleJson
    data class Expression<T>(
        val expressions: List<ExpressionToken>,
        override val value: String,
        override val type: Type,
    ) : Bind<T>() {
        constructor(
            expressions: List<ExpressionToken>,
            value: String,
            type: Class<T>
        ) : this(expressions, value, type as Type)
    }

    @BeagleJson
    data class Value<T : Any>(override val value: T) : Bind<T>() {
        override val type: Class<T> = value.javaClass
    }
}

internal inline fun <reified T : Any> expressionOrValueOf(text: String): Bind<T> =
    if (text.hasExpression()) expressionOf(text) else valueOf(text) as Bind<T>

internal fun expressionOrValueOfNullable(text: String?): Bind<String>? =
    if (text?.hasExpression() == true) expressionOf(text) else valueOfNullable(text)

inline fun <reified T> expressionOf(expressionText: String): Bind.Expression<T> {
    val tokenParser = TokenParser()
    val expressionTokens = expressionText.getExpressions().map { expression ->
        tokenParser.parse(expression)
    }
    return Bind.Expression(expressionTokens, expressionText, T::class.java)
}

inline fun <reified T : Any> valueOf(value: T) = Bind.Value(value)
inline fun <reified T : Any> valueOfNullable(value: T?): Bind<T>? = value?.let { valueOf(it) }

internal fun Any.hasExpression() = this.toString().contains(BeagleRegex.EXPRESSION_REGEX)
