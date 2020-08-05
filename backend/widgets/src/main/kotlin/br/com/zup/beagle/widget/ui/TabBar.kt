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
import br.com.zup.beagle.widget.action.Action
import br.com.zup.beagle.widget.context.Bind

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
) : ServerDrivenComponent

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
)