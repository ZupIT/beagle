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

import br.com.zup.beagle.core.BindAttribute
import br.com.zup.beagle.widget.expression.ExpressionHelper
import java.io.Serializable

sealed class Bind<T> : BindAttribute<T>, Serializable {
    data class Expression<T>(override val value: String) : Bind<T>() {
        constructor(expression: ExpressionHelper<T>) : this(expression.representation)
    }

    data class Value<T : Any>(override val value: T) : Bind<T>()
}

fun <T> expressionOf(expression: String) = Bind.Expression<T>(expression)
fun <T : Any> valueOf(value: T) = Bind.Value(value)
fun <T : Any> valueOfNullable(value: T?) = value?.let { valueOf(it) }