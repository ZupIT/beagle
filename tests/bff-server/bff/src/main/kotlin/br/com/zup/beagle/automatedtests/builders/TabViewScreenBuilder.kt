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

import br.com.zup.beagle.automatedtests.constants.BEACH_NETWORK_IMAGE
import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.core.AlignContent
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.NavigationBarItem
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.ui.Image
import br.com.zup.beagle.widget.ui.ImagePath.Local
import br.com.zup.beagle.widget.ui.ImagePath.Remote
import br.com.zup.beagle.widget.ui.TabItem
import br.com.zup.beagle.widget.ui.TabView
import br.com.zup.beagle.widget.ui.Text

data class TextTabs(
    val tabOne: String,
    val tabTwo: String,
    val tabThree: String,
    val tabFour: String
)

object TabViewScreenBuilder {
    private val tab1 = TabItem(
        title = "Tab 1",
        child = Container(
            children = listOf(
                Text("@{tabView.tabOne}"),
                Image(Remote(BEACH_NETWORK_IMAGE)),
                Text("This is Tab1's second text")
            )
        ).applyFlex(Flex(alignContent = AlignContent.CENTER))
    )

    private val tab2 = TabItem(
        title = "Tab 2",
        child = Container(
            children = listOf(
                Text("@{tabView.tabTwo}"),
                Text("This is Tab2's second text")
            )
        ).applyFlex(Flex(alignContent = AlignContent.CENTER))
    )

    private val tab3 = TabItem(
        title = "Tab 3",
        child = Container(
            children = listOf(
                Text("@{tabView.tabThree}"),
                Text("This is Tab3's second text")
            )
        ).applyFlex(Flex(alignContent = AlignContent.CENTER))
    )

    private val tab4 = TabItem(
        title = "Tab 4",
        icon = Local.justMobile("beagle"),
        child = Container(
            children = listOf(
                Text("@{tabView.tabFour}"),
                Text("This is Tab4's second text")
            )
        ).applyFlex(Flex(alignContent = AlignContent.CENTER))
    )

    fun build() = Screen(
        navigationBar = NavigationBar(
            title = "Beagle Tab View",
            showBackButton = true,
            navigationBarItems = listOf(
                NavigationBarItem(
                    text = "",
                    image = Local.justMobile("informationImage"),
                    action = Alert(
                        title = "TabView",
                        message = " Is a component that will make the navigation between views. It may happen by " +
                            "sliding through screens or by clicking at the tabs shown. ",
                        labelOk = "OK"
                    )
                )
            )
        ),
        child = TabView(
            children = listOf(tab1, tab2, tab3, tab4),
            context = ContextData(
                id = "tabView",
                value = TextTabs(
                    tabOne = "Welcome to Tab 1",
                    tabTwo = "Welcome to Tab 2",
                    tabThree = "Welcome to Tab 3",
                    tabFour = "Welcome to Tab 4"
                )
            )
        )
    )
}
