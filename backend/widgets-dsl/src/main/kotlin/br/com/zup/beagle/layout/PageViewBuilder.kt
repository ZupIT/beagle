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
import br.com.zup.beagle.widget.action.Action
import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.layout.PageView
import kotlin.properties.Delegates

fun pageView(block: PageViewBuilder.() -> Unit) = PageViewBuilder().apply(block).build()

class PageViewBuilder: BeagleBuilder<PageView> {
    var children: MutableList<ServerDrivenComponent> by Delegates.notNull()
    var context: ContextData? = null
    var onPageChange: MutableList<Action>? = null
    var currentPage: Bind<Int>? = null

    fun children(children: List<ServerDrivenComponent>)
        = this.apply { this.children = children.toMutableList() }
    fun context(context: ContextData?) = this.apply { this.context = context }
    fun onPageChange(onPageChange: List<Action>?)
        = this.apply { this.onPageChange = onPageChange?.toMutableList() }
    fun currentPage(currentPage: Bind<Int>?) = this.apply { this.currentPage = currentPage }

    fun children(block: BeagleListBuilder<ServerDrivenComponent>.() -> Unit) {
        children(BeagleListBuilder<ServerDrivenComponent>().apply(block).build())
    }

    fun context(block: ContextDataBuilder.() -> Unit) {
        context(ContextDataBuilder().apply(block).build())
    }

    fun onPageChange(block: BeagleListBuilder<Action>.() -> Unit) {
        onPageChange(BeagleListBuilder<Action>().apply(block).buildNullable())
    }

    fun currentPage(block: () -> Bind<Int>?) {
        currentPage(block.invoke())
    }

    override fun build() = PageView(
        children = children,
        context = context,
        onPageChange = onPageChange,
        currentPage = currentPage
    )
}