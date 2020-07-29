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

import br.com.zup.beagle.builder.BeagleListBuilder
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.action.Action
import br.com.zup.beagle.widget.builder.BeagleWidgetBuilder
import br.com.zup.beagle.widget.context.Bind
import kotlin.properties.Delegates

/**
 * TabBar is a component responsible to display a tab layout.
 * It works by displaying tabs that can change a context when clicked.
 *
 * @param items define yours tabs title and icon
 * @param styleId reference a native style in your local styles file to be applied on this view.
 * @param currentTab define the expression that is observer to change the current tab selected
 * @param onTabSelection define a list of action that will be executed when a tab is selected
 *
 */
data class TabBar(
    val items: List<TabBarItem>,
    val styleId: String? = null,
    val currentTab: Bind<Int>? = null,
    val onTabSelection: List<Action>? = null
) : ServerDrivenComponent {
    companion object{
        @JvmStatic
        fun builder() = Builder()
    }
    class Builder: BeagleWidgetBuilder<TabBar> {
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
}

fun tabBar(block: TabBar.Builder.() -> Unit) = TabBar.Builder().apply(block).build()
/**
* Define the view has in the tab view
*
* @param title displays the text on the TabView component. If it is null or not declared it won't display any text.
* @param icon
*                  display an icon image on the TabView component.
*                  If it is left as null or not declared it won't display any icon.
*
*/
data class TabBarItem(
    val title: String? = null,
    val icon: ImagePath.Local? = null
) {
    companion object{
        @JvmStatic
        fun builder() = Builder()
    }
    class Builder: BeagleWidgetBuilder<TabBarItem>{
        var title: String? = null
        var icon: ImagePath.Local? = null

        fun title(title: String?) = this.apply { this.title = title }
        fun icon(icon: ImagePath.Local?) = this.apply { this.icon = icon }

        fun title(block: () -> String?){
            title(block.invoke())
        }

        fun icon(block: ImagePath.Local.Builder.() -> Unit){
            icon(ImagePath.Local.Builder().apply(block).build())
        }

        override fun build() = TabBarItem(title, icon)

    }
}

fun tabBarItem(block: TabBarItem.Builder.() -> Unit) = TabBarItem.Builder().apply(block).build()