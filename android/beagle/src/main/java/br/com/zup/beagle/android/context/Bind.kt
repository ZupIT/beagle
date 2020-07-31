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

package br.com.zup.beagle.android.context

import br.com.zup.beagle.android.utils.BeagleConstants
import br.com.zup.beagle.core.BindAttribute

sealed class Bind<T> : BindAttribute<T> {
    abstract val type: Class<T>

    class Expression<T>(
        override val value: String,
        override val type: Class<T>
    ): Bind<T>()

    data class Value<T: Any>(override val value: T) : Bind<T>() {
        override val type: Class<T> = value.javaClass
    }
}

inline fun <reified T> expressionOf(expression: String) = Bind.Expression(expression, T::class.java)
inline fun <reified T : Any> valueOf(value: T) = Bind.Value(value)
inline fun <reified T : Any> valueOfNullable(value: T?) = value?.let { valueOf(it) }

fun Any.isExpression(): Boolean {
    return this is String && this.contains(BeagleConstants.EXPRESSION_REGEX)
}
