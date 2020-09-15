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

package br.com.zup.beagle.sample.builder

import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.action.SetContext
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.context.expressionOf
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.NavigationBarItem
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScreenBuilder
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.ImagePath.Local
import br.com.zup.beagle.widget.ui.TabBar
import br.com.zup.beagle.widget.ui.TabBarItem
import br.com.zup.beagle.widget.ui.Text

data class TabBarSimpleObject(val page: Int, val image: String)

object TabBarScreenBuilder : ScreenBuilder {
    private val tab1 = TabBarItem(
        title = "Tab 1",
        icon = Local.justMobile(expressionOf("@{context.image}"))
    )

    private val tab2 = TabBarItem(
        title = "Tab 2",
        icon = Local.justMobile(expressionOf("@{context.image}"))
    )

    private val tab3 = TabBarItem(
        title = "Tab 3",
        icon = Local.justMobile(expressionOf("@{context.image}"))
    )

    private val tab4 = TabBarItem(
        title = "Tab 4",
        icon = Local.justMobile("informationImage")
    )

    override fun build() = Screen(
        context = ContextData("context", TabBarSimpleObject(page = 2, image = "informationImage")),
        navigationBar = NavigationBar(
            title = "Beagle Tab Bar",
            showBackButton = true,
            navigationBarItems = listOf(
                NavigationBarItem(
                    text = "",
                    image = Local.justMobile("informationImage"),
                    action = Alert(
                        title = "TabBar",
                        message = " Is a component that will make the navigation between views. It may happen by " +
                            "sliding through screens or by clicking at the tabs shown. ",
                        labelOk = "OK"
                    )
                )
            )
        ),
        child = Container(
            children = listOf(
                TabBar(
                    items = listOf(tab1, tab2, tab3, tab4),
                    currentTab = expressionOf("@{context.page}"),
                    onTabSelection = listOf(SetContext(contextId = "context",
                        path = "page", value = "@{onTabSelection}"))
                ),
                Text(expressionOf("page @{context.page}")),
                Button("change", onPress = listOf(SetContext(contextId = "context",
                    path = "image", value = "beagle")))
            )
        )
    )
}
