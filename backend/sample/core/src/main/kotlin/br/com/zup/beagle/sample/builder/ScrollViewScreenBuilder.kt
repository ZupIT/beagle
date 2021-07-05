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

import br.com.zup.beagle.ext.setStyle
import br.com.zup.beagle.sample.constants.TEXT_FONT_MAX
import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.core.ScrollAxis
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.core.UnitValue
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.NavigationBarItem
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScreenBuilder
import br.com.zup.beagle.widget.layout.ScrollView
import br.com.zup.beagle.widget.ui.ImagePath.Local
import br.com.zup.beagle.widget.ui.Text

object ScrollViewScreenBuilder : ScreenBuilder {
    override fun build() = Screen(
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
        child = Container(
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
                    createText("Hello 1"),
                    createText("Hello 2"),
                    createText("Hello 3"),
                    createText("Hello 4"),
                    createText("Hello 5")
                ),
                scrollDirection = ScrollAxis.VERTICAL
            )
        )
    ).setStyle {
        size = Size(
            height = UnitValue.real(130)
        )
    }

    private fun getHorizontalScrollView() = Container(
        children = listOf(
            Text("Horizontal ScrollView with scrollBars"),
            ScrollView(
                children = listOf(
                    createText("Hello 1"),
                    createText("Hello 2"),
                    createText("Hello 3"),
                    createText("Hello 4"),
                    createText("Hello 5"),
                    createText("Hello 6"),
                    createText("Hello 7"),
                    createText("Hello 8"),
                    createText("Hello 9"),
                    createText("Hello 10"),
                ),
                scrollDirection = ScrollAxis.HORIZONTAL
            )
        )
    ).setStyle {
        size = Size(
            height = UnitValue.real(130)
        )
    }

    private fun createText(text: String) = Text(text, TEXT_FONT_MAX)
}
