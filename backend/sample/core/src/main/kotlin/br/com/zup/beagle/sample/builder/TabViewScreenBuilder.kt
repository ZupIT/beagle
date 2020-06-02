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

import br.com.zup.beagle.action.ShowNativeDialog
import br.com.zup.beagle.sample.constants.BEACH_NETWORK_IMAGE
import br.com.zup.beagle.widget.core.AlignContent
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.NavigationBarItem
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScreenBuilder
import br.com.zup.beagle.widget.ui.NetworkImage
import br.com.zup.beagle.widget.ui.TabItem
import br.com.zup.beagle.widget.ui.TabView
import br.com.zup.beagle.widget.ui.Text

object TabViewScreenBuilder : ScreenBuilder {
    private val tab1 = TabItem(
        title = "Tab 1",
        content = Container(
            children = listOf(
                Text("Text1 Tab 2"),
                NetworkImage(BEACH_NETWORK_IMAGE),
                Text("Text2 Tab 2")
            )
        ).applyFlex(Flex(alignContent = AlignContent.CENTER))
    )

    private val tab2 = TabItem(
        title = "Tab 2",
        content = Container(
            children = listOf(
                Text("Text1 Tab 2"),
                Text("Text2 Tab 2")
            )
        ).applyFlex(Flex(alignContent = AlignContent.CENTER))
    )

    private val tab3 = TabItem(
        title = "Tab 3",
        content = Container(
            children = listOf(
                Text("Text1 Tab 3"),
                Text("Text2 Tab 3")
            )
        ).applyFlex(Flex(alignContent = AlignContent.CENTER))
    )

    private val tab4 = TabItem(
        title = "Tab 4",
        icon = "beagle",
        content = Container(
            children = listOf(
                Text("Text1 Tab 4"),
                Text("Text2 Tab 4")
            )
        ).applyFlex(Flex(alignContent = AlignContent.CENTER))
    )

    override fun build() = Screen(
        navigationBar = NavigationBar(
            title = "Beagle Tab View",
            showBackButton = true,
            navigationBarItems = listOf(
                NavigationBarItem(
                    text = "",
                    image = "informationImage",
                    action = ShowNativeDialog(
                        title = "TabView",
                        message = " Is a component that will make the navigation between views. It may happen by " +
                            "sliding through screens or by clicking at the tabs shown. ",
                        buttonText = "OK"
                    )
                )
            )
        ),
        child = TabView(
            tabItems = listOf(tab1, tab2, tab3, tab4)
        )
    )
}
