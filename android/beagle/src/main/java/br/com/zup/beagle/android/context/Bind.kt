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

import br.com.zup.beagle.core.BindAttribute

sealed class Bind<T> : BindAttribute<T> {
    abstract val type: Class<T>

    @Transient
    private var onChange: ((value: T) -> Unit)? = null


    fun observes(onChange: (value: T) -> Unit) {
        this.onChange = onChange
    }

    fun notifyChange(value: Any) {
        val newValue = value as T
        this.onChange?.invoke(newValue)
    }

    companion object {
        inline fun <reified T> expressionOf(expression: String) = Expression(expression, T::class.java)
        inline fun <reified T : Any> valueOf(value: T) = Value(value)
        inline fun <reified T : Any> valueOfNullable(value: T?) = value?.let { valueOf(it) }
    }

    class Expression<T>(
        override val value: String,
        override val type: Class<T>
    ): Bind<T>()

    data class Value<T: Any>(override val value: T) : Bind<T>() {
        override val type: Class<T> = value.javaClass
    }
}