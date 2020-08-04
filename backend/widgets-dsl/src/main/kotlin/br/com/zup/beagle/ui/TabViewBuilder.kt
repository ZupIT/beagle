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

package br.com.zup.beagle.ui

import br.com.zup.beagle.builder.BeagleBuilder
import br.com.zup.beagle.builder.BeagleListBuilder
import br.com.zup.beagle.context.ContextDataBuilder
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.ui.ImagePath
import br.com.zup.beagle.widget.ui.TabItem
import br.com.zup.beagle.widget.ui.TabView
import kotlin.properties.Delegates

fun tabView(block: TabViewBuilder.() -> Unit) = TabViewBuilder().apply(block).build()

class TabViewBuilder : BeagleBuilder<TabView> {
    var children: MutableList<TabItem> by Delegates.notNull()
    var styleId: String? = null
    var context: ContextData? = null

    fun children(children: List<TabItem>) = this.apply { this.children = children.toMutableList() }
    fun styleId(styleId: String?) = this.apply { this.styleId = styleId }
    fun context(context: ContextData?) = this.apply { this.context = context }

    fun children(block: BeagleListBuilder<TabItem>.() -> Unit) {
        children(BeagleListBuilder<TabItem>().apply(block).build())
    }

    fun styleId(block: () -> String?) {
        styleId(block.invoke())
    }

    fun context(block: ContextDataBuilder.() -> Unit) {
        context(ContextDataBuilder().apply(block).build())
    }

    override fun build() = TabView(
        children = children,
        styleId = styleId,
        context = context
    )
}

fun tabItem(block: TabItemBuilder.() -> Unit) = TabItemBuilder().apply(block).build()

class TabItemBuilder : BeagleBuilder<TabItem> {
    var title: String? = null
    var child: ServerDrivenComponent by Delegates.notNull()
    var icon: ImagePath.Local? = null

    fun title(title: String?) = this.apply { this.title = title }
    fun child(child: ServerDrivenComponent) = this.apply { this.child = child }
    fun icon(icon: ImagePath.Local?) = this.apply { this.icon = icon }

    fun title(block: () -> String?) {
        title(block.invoke())
    }

    fun child(block: () -> ServerDrivenComponent) {
        child(block.invoke())
    }

    fun icon(block: ImagePathLocalBuilder.() -> Unit) {
        icon(ImagePathLocalBuilder().apply(block).build())
    }

    override fun build() = TabItem(
        title = title,
        child = child,
        icon = icon
    )
}