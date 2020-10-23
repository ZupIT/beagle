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

import br.com.zup.beagle.automatedtests.constants.SCREEN_TEXT_STYLE
import br.com.zup.beagle.automatedtests.constants.STEEL_BLUE
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.NavigationBarItem
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.ui.ImagePath
import br.com.zup.beagle.widget.ui.Text

data class TextContext(
    val text: String,
    val textStyle: String,
    val textBackground: String,
    val textAll: String
)

object TextScreenBuilder {
    fun build() = Screen(
        navigationBar = NavigationBar(
            title = "Beagle Text",
            showBackButton = true,
            navigationBarItems = listOf(
                NavigationBarItem(
                    text = "",
                    image = ImagePath.Local.justMobile("informationImage"),
                    action = Alert(
                        title = "Text",
                        message = "This widget will define a text view natively using the server driven " +
                            "information received through Beagle.",
                        labelOk = "OK"
                    )
                )
            )
        ),
        child = Container(
            children = listOf(
                beagleText(text = "@{text.text}"),
                beagleText(text = "@{text.textStyle}", styleId = SCREEN_TEXT_STYLE),
                beagleText(text = "@{text.textBackground}", appearanceColor = STEEL_BLUE),
                beagleText(
                    text = "@{text.textAll}",
                    styleId = SCREEN_TEXT_STYLE,
                    appearanceColor = STEEL_BLUE
                )
            ),
            context = ContextData(
                id = "text",
                value =  TextContext(
                    text = "hello world without style",
                    textStyle = "hello world with style",
                    textBackground = "hello world with Appearance",
                    textAll = "hello world with style and Appearance"
                )
            )
        )
    )

    private fun beagleText(
        text: String,
        styleId: String? = null,
        appearanceColor: String? = null
    ) =
        Text(text = text, styleId = styleId)
            .applyStyle(Style(
                backgroundColor = appearanceColor,
                margin = EdgeValue(
                    top = 16.unitReal(),
                    left = 16.unitReal(),
                    right = 16.unitReal()
                )
            )
            )
}