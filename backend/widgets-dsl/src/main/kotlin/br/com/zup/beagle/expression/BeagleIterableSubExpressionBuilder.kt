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

import br.com.zup.beagle.builder.BeagleBuilder
import br.com.zup.beagle.widget.expression.BeagleIterableSubExpression
import br.com.zup.beagle.widget.expression.ExpressionHelper
import kotlin.properties.Delegates

fun <T, N> beagleIterableSubExpression(block: BeagleIterableSubExpressionBuilder<T, N>.() -> Unit)
    = BeagleIterableSubExpressionBuilder<T, N>().apply(block).build()

class BeagleIterableSubExpressionBuilder<T, N> : BeagleBuilder<BeagleIterableSubExpression<T, N>> {
    var expression: ExpressionHelper<out Iterable<T>> by Delegates.notNull()
    var createNext: (ExpressionHelper<T>) -> N by Delegates.notNull()

    fun expression(expression: ExpressionHelper<out Iterable<T>>)
        = this.apply { this.expression = expression }

    fun createNext(createNext: (ExpressionHelper<T>) -> N)
        = this.apply { this.createNext = createNext }

    fun expression(block: () -> ExpressionHelper<out Iterable<T>>) {
        expression(block.invoke())
    }

    fun createNext(block: () -> (ExpressionHelper<T>) -> N) {
        createNext(block.invoke())
    }

    override fun build() = BeagleIterableSubExpression(expression, createNext)

}