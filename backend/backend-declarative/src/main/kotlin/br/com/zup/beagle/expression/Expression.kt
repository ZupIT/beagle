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

sealed class Expression<T>(private val intermediate: String) {
    companion object {
        private const val START = "@{"
        private const val END = "}"

        fun <T> Expression<out Iterable<T>>.access(index: Int): Expression<T> = ArrayAccess(index, this)
    }

    val representation by lazy { "${this.intermediate}$END" }

    fun <N> access(member: String): Expression<N> = ObjectAccess(member, this)

    class Start<O>(initialMember: String) :
        Expression<O>("$START$initialMember")

    private class ObjectAccess<I, O>(member: String, expression: Expression<I>) :
        Expression<O>("${expression.intermediate}.$member")

    private class ArrayAccess<T>(index: Int, expression: Expression<out Iterable<T>>) :
        Expression<T>("${expression.intermediate}[$index]")
}