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

data class BindingExpr<T>(override val expression: String? = null, override var initialValue: T? = null) : Binding<T> {
    @Transient
    private lateinit var onChange: (value: T) -> Unit

    fun observes(onChange: (value: T) -> Unit) {
        this.onChange = onChange
    }

    fun notifyChange(value: T) {
        this.onChange(value)
    }
}