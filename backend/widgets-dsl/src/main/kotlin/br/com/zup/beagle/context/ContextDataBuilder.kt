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

package br.com.zup.beagle.context

import br.com.zup.beagle.builder.BeagleBuilder
import br.com.zup.beagle.widget.context.ContextData
import kotlin.properties.Delegates

@Deprecated("It was deprecated in version 1.7.0 and will be removed in a future version." +
    " Use class ContextData.", ReplaceWith("ContextData()"))
fun contextData(block: ContextDataBuilder.() -> Unit) = ContextDataBuilder().apply(block).build()

@Deprecated("It was deprecated in version 1.7.0 and will be removed in a future version." +
    " Use class ContextData.", ReplaceWith("ContextData()"))
class ContextDataBuilder: BeagleBuilder<ContextData> {
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