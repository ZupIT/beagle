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

package br.com.zup.beagle.builder.analytics

import br.com.zup.beagle.analytics.ClickEvent
import br.com.zup.beagle.builder.BeagleBuilder
import kotlin.properties.Delegates

fun clickEvent(block: ClickEventBuilder.() -> Unit) = ClickEventBuilder().apply(block).build()

class ClickEventBuilder: BeagleBuilder<ClickEvent> {
    var category: String by Delegates.notNull()
    var label: String? = null
    var value: String? = null

    fun category(category: String) = this.apply { this.category = category }
    fun label(label: String?) = this.apply { this.label = label }
    fun value(value: String?) = this.apply { this.value = value }

    fun category(block: () -> String) {
        category(block.invoke())
    }

    fun label(block: () -> String?) {
        label(block.invoke())
    }

    fun value(block: () -> String?) {
        value(block.invoke())
    }

    override fun build() = ClickEvent(
        category = category,
        label = label,
        value = value
    )
}