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

package br.com.zup.beagle.expression

import br.com.zup.beagle.core.Binding

data class BindingExpr<T> internal constructor(
    override val expression: String? = null,
    override val initialValue: T? = null
) : Binding<T>

fun <T> value(initialValue: T) = BindingExpr(initialValue = initialValue)

fun <T> expr(expression: Expression<T>) = BindingExpr<T>(expression = expression.representation)

fun <T> valueExpr(initialValue: T, expression: Expression<T>) =
    BindingExpr<T>(initialValue = initialValue, expression = expression.representation)
