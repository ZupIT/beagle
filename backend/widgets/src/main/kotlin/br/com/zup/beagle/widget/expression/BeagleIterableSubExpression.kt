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

package br.com.zup.beagle.widget.expression

import br.com.zup.beagle.widget.builder.BeagleWidgetBuilder
import br.com.zup.beagle.widget.expression.ExpressionHelper.Companion.access
import kotlin.properties.Delegates

class BeagleIterableSubExpression<T, out N>(
    private val expression: ExpressionHelper<out Iterable<T>>,
    private val createNext: (ExpressionHelper<T>) -> N
) {
    operator fun get(index: Int) = this.createNext(this.expression.access(index))
    class Builder<T, N> : BeagleWidgetBuilder<BeagleIterableSubExpression<T, N>> {
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
}

fun <T, N> beagleIterableSubExpression(block: BeagleIterableSubExpression.Builder<T, N>.() -> Unit)
    = BeagleIterableSubExpression.Builder<T, N>().apply(block).build()