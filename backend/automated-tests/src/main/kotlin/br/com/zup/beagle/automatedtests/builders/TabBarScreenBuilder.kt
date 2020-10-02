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

package br.com.zup.beagle.automatedtests.builders

import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.action.SetContext
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.context.expressionOf
import br.com.zup.beagle.widget.core.AlignSelf
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.TextAlignment
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.NavigationBarItem
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.PageView
import br.com.zup.beagle.widget.ui.ImagePath
import br.com.zup.beagle.widget.ui.TabBar
import br.com.zup.beagle.widget.ui.TabBarItem
import br.com.zup.beagle.widget.ui.Text

object TabBarScreenBuilder {
    fun build() = Screen(
        navigationBar = NavigationBar(
            title = "Beagle TabBar",
            showBackButton = true,
            navigationBarItems = listOf(
                NavigationBarItem(
                    text = "",
                    image = ImagePath.Local.justMobile("informationImage"),
                    action = Alert(
                        title = "TabBar",
                        message = "",
                        labelOk = "OK"
                    )
                )
            )
        ),
        child = Container(
            listOf(
                TabBar(
                    items = listOf(tab1, tab2, tab3, tab4),
                    onTabSelection = listOf(SetContext("tabBar", "@{onTabSelection}")),
                    currentTab = expressionOf("@{tabBar}")
                ),
                PageView(
                    children = (1..4).map {
                        Text("Page $it", alignment = TextAlignment.CENTER).applyFlex(
                            Flex(
                                alignSelf = AlignSelf.CENTER,
                                grow = 1.0
                            )
                        )
                    },
                    onPageChange = listOf(SetContext("tabBar", "@{onPageChange}")),
                    currentPage = expressionOf("@{tabBar}")
                )
            ),
            context = ContextData(id = "tabBar", value = 0)
        ).applyFlex(Flex(grow = 1.0))
    )

    private val tab1 = TabBarItem(
        title = "Tab 1"
    )

    private val tab2 = TabBarItem(
        title = "Tab 2"
    )

    private val tab3 = TabBarItem(
        title = "Tab 3"
    )

    private val tab4 = TabBarItem(
        title = "Tab 4",
        icon = ImagePath.Local.justMobile("beagle")
    )
}