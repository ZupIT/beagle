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

package br.com.zup.beagle.widget.context

import br.com.zup.beagle.widget.expression.ExpressionHelper
import java.io.Serializable

/**
 * Bind is a representation of an expression in the form of a string that can then become any type of data
 */
sealed class Bind<T> : Serializable {

    /**
     * Represents the expression of bind
     */
    data class Expression<T>(val value: String) : Bind<T>() {
        constructor(expression: ExpressionHelper<T>) : this(expression.representation)
    }

    /**
     * Represents the value of bind
     */
    data class Value<T : Any>(val value: T) : Bind<T>()

    companion object {

        /**
         * Transform the reference value of the expression string to Bind.Expression<Type>
         */
        fun <T> expression(expression: String) = expressionOf<T>(expression)

        /**
         * Transform Type value to Bind<Type>.
         */
        fun <T : Any> value(value: T) = valueOf(value)

        /**
         * Checks if the value is null. Returns if the value is not null.
         */
        fun <T : Any> valueNullable(value: T?) = valueOfNullable(value)
    }
}

/**
 * Transform the reference value of the expression string to Bind.Expression<Type>
 */
fun <T> expressionOf(expression: String) = Bind.Expression<T>(expression)

/**
 * Transform Type value to Bind<Type>.
 */
fun <T : Any> valueOf(value: T) = Bind.Value(value)

/**
 * Checks if the value is null. Returns if the value is not null.
 */
fun <T : Any> valueOfNullable(value: T?) = value?.let { valueOf(it) }