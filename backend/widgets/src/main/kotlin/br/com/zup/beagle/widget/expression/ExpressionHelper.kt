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

import br.com.zup.beagle.widget.builder.BeagleBuilder
import kotlin.properties.Delegates

sealed class ExpressionHelper<T>(private val intermediate: String) {
    companion object {
        private const val START = "@{"
        private const val END = "}"

        fun <T> ExpressionHelper<out Iterable<T>>.access(index: Int): ExpressionHelper<T> = ArrayAccess(index, this)
    }

    val representation by lazy { "${this.intermediate}$END" }

    fun <N> access(member: String): ExpressionHelper<N> = ObjectAccess(member, this)

    class Start<O>(initialMember: String) :
        ExpressionHelper<O>("$START$initialMember") {
        class Builder<O> : BeagleBuilder<Start<O>> {
            var initialMember: String by Delegates.notNull()

            fun initialMember(initialMember: String) = this.apply { this.initialMember = initialMember }

            fun initialMember(block: () -> String) {
                initialMember(block.invoke())
            }

            override fun build() = Start<O>(initialMember)

        }
    }

    private class ObjectAccess<I, O>(member: String, expression: ExpressionHelper<I>) :
        ExpressionHelper<O>("${expression.intermediate}.$member")

    private class ArrayAccess<T>(index: Int, expression: ExpressionHelper<out Iterable<T>>) :
        ExpressionHelper<T>("${expression.intermediate}[$index]")
}

fun <T> start(block: ExpressionHelper.Start.Builder<T>.() -> Unit)
    = ExpressionHelper.Start.Builder<T>().apply(block).build()