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

package br.com.zup.beagle.layout

import br.com.zup.beagle.builder.BeagleBuilder
import br.com.zup.beagle.builder.BeagleListBuilder
import br.com.zup.beagle.context.ContextDataBuilder
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.core.ScrollAxis
import br.com.zup.beagle.widget.layout.ScrollView
import kotlin.properties.Delegates

fun scrollView(block: ScrollViewBuilder.() -> Unit) = ScrollViewBuilder().apply(block).build()

class ScrollViewBuilder : BeagleBuilder<ScrollView> {
    var children: MutableList<ServerDrivenComponent> by Delegates.notNull()
    var scrollDirection: ScrollAxis? = null
    var scrollBarEnabled: Boolean? = null
    var context: ContextData? = null

    fun children(children: List<ServerDrivenComponent>)
        = this.apply { this.children = children.toMutableList() }
    fun scrollDirection(scrollDirection: ScrollAxis?)
        = this.apply { this.scrollDirection = scrollDirection }
    fun scrollBarEnabled(scrollBarEnabled: Boolean?)
        = this.apply { this.scrollBarEnabled = scrollBarEnabled }
    fun context(context: ContextData?) = this.apply { this.context = context }

    fun children(block: BeagleListBuilder<ServerDrivenComponent>.() -> Unit) {
        children(BeagleListBuilder<ServerDrivenComponent>().apply(block).build())
    }

    fun scrollDirection(block: () -> ScrollAxis?) {
        scrollDirection(block.invoke())
    }

    fun scrollBarEnabled(block: () -> Boolean?) {
        scrollBarEnabled(block.invoke())
    }

    fun context(block: ContextDataBuilder.() -> Unit) {
        context(ContextDataBuilder().apply(block).build())
    }

    override fun build() = ScrollView(
        children = children,
        scrollDirection = scrollDirection,
        scrollBarEnabled = scrollBarEnabled,
        context = context
    )
}