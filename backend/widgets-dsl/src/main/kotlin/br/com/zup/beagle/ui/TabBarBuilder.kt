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
import br.com.zup.beagle.widget.action.Action
import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.ui.ImagePath
import br.com.zup.beagle.widget.ui.TabBar
import br.com.zup.beagle.widget.ui.TabBarItem
import kotlin.properties.Delegates

fun tabBar(block: TabBarBuilder.() -> Unit) = TabBarBuilder().apply(block).build()

class TabBarBuilder: BeagleBuilder<TabBar> {
    var items: MutableList<TabBarItem> by Delegates.notNull()
    var styleId: String? = null
    var currentTab: Bind<Int>? = null
    var onTabSelection: MutableList<Action>? = null

    fun items(items: List<TabBarItem>) = this.apply { this.items = items.toMutableList() }
    fun styleId(styleId: String?) = this.apply { this.styleId = styleId }
    fun currentTab(currentTab: Bind<Int>?) = this.apply { this.currentTab = currentTab }
    fun onTabSelection(onTabSelection: List<Action>?)
        = this.apply { this.onTabSelection = onTabSelection?.toMutableList() }

    fun items(block: BeagleListBuilder<TabBarItem>.() -> Unit) {
        items(BeagleListBuilder<TabBarItem>().apply(block).build())
    }

    fun styleId(block: () -> String?){
        styleId(block.invoke())
    }

    fun currentTab(block: () -> Bind<Int>?){
        currentTab(block.invoke())
    }

    fun onTabSelection(block: BeagleListBuilder<Action>.() -> Unit){
        onTabSelection(BeagleListBuilder<Action>().apply(block).buildNullable())
    }

    override fun build() = TabBar(
        items = items,
        styleId = styleId,
        currentTab = currentTab,
        onTabSelection = onTabSelection
    )
}

fun tabBarItem(block: TabBarItemBuilder.() -> Unit) = TabBarItemBuilder().apply(block).build()

class TabBarItemBuilder: BeagleBuilder<TabBarItem>{
    var title: String? = null
    var icon: ImagePath.Local? = null

    fun title(title: String?) = this.apply { this.title = title }
    fun icon(icon: ImagePath.Local?) = this.apply { this.icon = icon }

    fun title(block: () -> String?){
        title(block.invoke())
    }

    fun icon(block: ImagePathLocalBuilder.() -> Unit){
        icon(ImagePathLocalBuilder().apply(block).build())
    }

    override fun build() = TabBarItem(title, icon)

}