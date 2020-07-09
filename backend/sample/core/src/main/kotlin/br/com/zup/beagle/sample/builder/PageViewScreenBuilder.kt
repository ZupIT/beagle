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

import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.action.SetContext
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.context.expressionOf
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.layout.*
import br.com.zup.beagle.widget.pager.PageIndicator
import br.com.zup.beagle.widget.ui.ImagePath.Local
import br.com.zup.beagle.widget.ui.Text

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
        child = Container(
            listOf(
                PageView(children = listOf(Text("1"), Text("2"), Text("3"), Text("4"), Text("5")),
                    onPageChange = listOf(SetContext("context", "@{onChange.value}")),
                    currentPage = expressionOf("@{context}")),
                PageIndicator(numberOfPages = 5,
                    currentPage = expressionOf("@{context}"),
                    selectedColor = "#00ff00",
                    unselectedColor = "#ff00ff"
                )
            ),
            context = ContextData("context", 0)
        ).applyFlex(Flex(grow = 1.0))

    )
}
