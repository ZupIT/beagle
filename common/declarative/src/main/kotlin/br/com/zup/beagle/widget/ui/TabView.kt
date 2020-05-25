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

import br.com.zup.beagle.core.CoreDeclarativeDsl
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.ServerDrivenComponentList

/**
 * TabView is a component responsible for the navigation between views.
 * It works by displaying tabs corresponding to the different views that can be accessed.
 *
 * @param tabItems define yours view has in tab
 * @param style reference a native style in your local styles file to be applied on this view.
 *
 */
data class TabView(
    val tabItems: List<TabItem>,
    val style: String? = null
) : ServerDrivenComponent


fun tabView(block: TabViewBuilder.() -> Unit): TabView = TabViewBuilder().apply(block).build()

@CoreDeclarativeDsl
class TabViewBuilder {

    var style: String? = null

    private val tabItems = mutableListOf<TabItem>()

    fun tabItems(block: TabItems.() -> Unit) {
        tabItems.addAll(TabItems().apply(block))
    }

    fun build(): TabView = TabView(tabItems, style)

}

fun ServerDrivenComponentList.tabView(block: TabViewBuilder.() -> Unit) {
    add(TabViewBuilder().apply(block).build())
}

/**
 * Define the view has in the tab view
 *
 * @param title displays the text on the TabView component. If it is null or not declared it won't display any text.
 * @param content
 *                  inflate a view on the TabView according to the Tab item clicked.
 *                  It could receive any view (Server Driven).
 * @param icon
 *                  display an icon image on the TabView component.
 *                  If it is left as null or not declared it won't display any icon.
 *
 */
data class TabItem(
    val title: String? = null,
    val content: ServerDrivenComponent,
    val icon: String? = null
)


class TabItems : ArrayList<TabItem>() {

    fun tabItem(block: TabItemBuilder.() -> Unit) {
        add(TabItemBuilder().apply(block).build())
    }

}

class TabItemBuilder {

    var title: String? = null
    var content: ServerDrivenComponent? = null
    var icon: String? = null

    fun build(): TabItem = TabItem(title,
        //TODO NEED TO BE IMPLEMENTS REQUIRED BY DSL
        content!!, icon)

}