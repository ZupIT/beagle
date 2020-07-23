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
package br.com.zup.beagle.widget.ui

import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.builder.BeagleBuilder
import br.com.zup.beagle.widget.builder.BeagleListBuilder
import br.com.zup.beagle.widget.context.ContextComponent
import br.com.zup.beagle.widget.context.ContextData
import kotlin.properties.Delegates

/**
 * TabView is a component responsible for the navigation between views.
 * It works by displaying tabs corresponding to the different views that can be accessed.
 *
 * @param children define yours view has in tab
 * @param styleId reference a native style in your local styles file to be applied on this view.
 *
 */
data class TabView(
    val children: List<TabItem>,
    val styleId: String? = null,
    override val context: ContextData? = null
) : ServerDrivenComponent, ContextComponent {
    class Builder : BeagleBuilder<TabView> {
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

        fun context(block: ContextData.Builder.() -> Unit) {
            context(ContextData.Builder().apply(block).build())
        }

        override fun build() = TabView(
            children = children,
            styleId = styleId,
            context = context
        )
    }
}

fun tabView(block: TabView.Builder.() -> Unit) = TabView.Builder().apply(block).build()

/**
 * Define the view has in the tab view
 *
 * @param title displays the text on the TabView component. If it is null or not declared it won't display any text.
 * @param child
 *                  inflate a view on the TabView according to the Tab item clicked.
 *                  It could receive any view (Server Driven).
 * @param icon
 *                  display an icon image on the TabView component.
 *                  If it is left as null or not declared it won't display any icon.
 *
 */
data class TabItem(
    val title: String? = null,
    val child: ServerDrivenComponent,
    val icon: ImagePath.Local? = null
) {
    class Builder : BeagleBuilder<TabItem> {
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

        fun icon(block: ImagePath.Local.Builder.() -> Unit) {
            icon(ImagePath.Local.Builder().apply(block).build())
        }

        override fun build() = TabItem(
            title = title,
            child = child,
            icon = icon
        )
    }
}

fun tabItem(block: TabItem.Builder.() -> Unit) = TabItem.Builder().apply(block).build()