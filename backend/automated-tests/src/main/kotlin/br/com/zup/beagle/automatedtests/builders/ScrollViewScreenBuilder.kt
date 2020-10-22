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

import br.com.zup.beagle.automatedtests.constants.TEXT_FONT_MAX
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitPercent
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.core.ScrollAxis
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.NavigationBarItem
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScrollView
import br.com.zup.beagle.widget.ui.ImagePath.Local
import br.com.zup.beagle.widget.ui.Text

object ScrollViewScreenBuilder {
     fun build() = Screen(
        navigationBar = NavigationBar(
            title = "Beagle ScrollView",
            showBackButton = true,
            navigationBarItems = listOf(
                NavigationBarItem(
                    text = "",
                    image = Local.justMobile("informationImage"),
                    action = Alert(
                        title = "ScrollView",
                        message = "This component is a specialized container that will display its " +
                            "components in a Scroll like view.",
                        labelOk = "OK"
                    )
                )
            )
        ),
        child = ScrollView(
            scrollDirection = ScrollAxis.VERTICAL,
            children = listOf(
                getVerticalScrollView(),
                getHorizontalScrollView()
            )
        )
    )

    private fun getVerticalScrollView() = Container(
        children = listOf(
            Text("Vertical ScrollView"),
            ScrollView(
                children = listOf(
                    createText("Vertical 1"),
                    createText("Vertical 2"),
                    createText("Vertical 3"),
                    createText("Vertical 4"),
                    createText("Vertical 5")
                ),
                scrollDirection = ScrollAxis.VERTICAL
            )
        )
    ).applyStyle(Style(
            size = Size(
                height = 130.unitReal(),
                width = 100.unitPercent()
            )
        )
    )

    private fun getHorizontalScrollView() = Container(
        children = listOf(
            Text("Horizontal ScrollView with scrollBars"),
            ScrollView(
                children = listOf(
                    createText("Horizontal 1"),
                    createText("Horizontal 2"),
                    createText("Horizontal 3"),
                    createText("Horizontal 4"),
                    createText("Horizontal 5")

                ),
                scrollDirection = ScrollAxis.HORIZONTAL
            )
        )
    )

    private fun createText(text: String) = Text(text, TEXT_FONT_MAX)
}
