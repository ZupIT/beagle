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

import br.com.zup.beagle.automatedtests.constants.BLACK
import br.com.zup.beagle.automatedtests.constants.LIGHT_GREY
import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.core.AlignItems
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScreenBuilder
import br.com.zup.beagle.widget.layout.NavigationBarItem
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.PageView
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.pager.PageIndicator
import br.com.zup.beagle.widget.ui.ImagePath.Local
import br.com.zup.beagle.widget.ui.Text

data class PageViewText(
    var pageOne: String = "",
    var pageTwo: String = "",
    var pageThree: String = ""
)

object PageViewScreenBuilder : ScreenBuilder {
    override fun build() = Screen(
        navigationBar = NavigationBar(
            "Beagle PageView",
            showBackButton = true,
            navigationBarItems = listOf(
                NavigationBarItem(
                    text = "",
                    image = Local.justMobile("informationImage"),
                    action = Alert(
                        title = "PageView",
                        message = "This component is a specialized container " +
                            "to hold pages (views) that may be swiped.",
                        labelOk = "OK"
                    )
                )
            )
        ),
        child = PageView(
            pageIndicator = PageIndicator(
                selectedColor = BLACK,
                unselectedColor = LIGHT_GREY
            ),
            children = listOf(
                Container(
                    children = listOf(
                        Text(text = "Page 1",textColor = "#000000")
                    )
                ).applyFlex(Flex(grow = 1.0,alignItems = AlignItems.CENTER)),

                Container(
                    children = listOf(
                        Text("Page 2",textColor = "#000000")
                    )
                ).applyFlex(Flex(grow = 1.0,alignItems = AlignItems.CENTER)),

                Container(
                children = listOf(
                    Text("Page 3",textColor = "#000000")
                )
            ).applyFlex(Flex(grow = 1.0,alignItems = AlignItems.CENTER))
            ) ,
            context = ContextData(
                id = "pageView",
                value = "This is my Page 1"
            )
        )
    )
}
