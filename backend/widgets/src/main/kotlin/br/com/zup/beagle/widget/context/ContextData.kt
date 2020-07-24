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

import br.com.zup.beagle.widget.builder.BeagleWidgetBuilder
import kotlin.properties.Delegates

data class ContextData(
    val id: String,
    val value: Any
) {
    class Builder : BeagleWidgetBuilder<ContextData> {
        var id: String by Delegates.notNull()
        var value: Any by Delegates.notNull()

        fun id(id: String) = this.apply { this.id = id }
        fun value(value: Any) = this.apply { this.value = value }

        fun id(block: () -> String) {
            id(block.invoke())
        }

        fun value(block: () -> Any) {
            value(block.invoke())
        }

        override fun build() = ContextData(id, value)
    }
}

fun contextData(block: ContextData.Builder.() -> Unit) = ContextData.Builder().apply(block).build()
