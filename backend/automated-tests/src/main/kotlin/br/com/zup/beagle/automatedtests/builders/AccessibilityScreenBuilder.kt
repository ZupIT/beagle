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

import br.com.zup.beagle.automatedtests.constants.BUTTON_STYLE_ACCESSIBILITY
import br.com.zup.beagle.core.Accessibility
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyAccessibility
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.core.AlignItems
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.NavigationBarItem
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.ImagePath
import br.com.zup.beagle.widget.ui.Text

object AccessibilityScreenBuilder {
    fun build() = Screen(
        navigationBar = NavigationBar(
            title = "Beagle Accessibility Screen",
            showBackButton = true,
            navigationBarItems = listOf(
                NavigationBarItem(
                    text = "",
                    image = ImagePath.Local.justMobile("informationImage"),
                    action = Alert(
                        title = "Accessibility Screen",
                        message = "This method applies accessibility in a widget",
                        labelOk = "OK"
                    )
                )
            )
        ),
        child = Container(
            children = listOf(
                textAccessibility(
                    text = "Accessibility Testing",
                    accessibilityLabel = "first text",
                    accessible = true
                ),
                textAccessibility(
                    text = "Accessibility disabled test",
                    accessibilityLabel = "second text",
                    accessible = false
                ),
                buttonAccessibility(
                    textButton = "First Text button",
                    accessibilityLabel = "This is a button as title",
                    accessible = true
                ),
                buttonAccessibility(
                    textButton = "Second Text button",
                    accessible = true
                )
            )
        )
    )

    private fun textAccessibility(
        text: String,
        accessibilityLabel: String,
        accessible: Boolean
    ) =
        Text(
            text = text
        ).applyAccessibility(
            accessibility = Accessibility(
                accessible = accessible,
                accessibilityLabel = accessibilityLabel
            )
        ).applyStyle(Style(
            margin = EdgeValue(
                top = 8.unitReal(),
                bottom = 8.unitReal()),
            flex = Flex( alignItems = AlignItems.CENTER))
        )

    private fun buttonAccessibility(
        textButton: String,
        accessibilityLabel: String? = null,
        accessible: Boolean
    ) =
        Button(
            text = textButton,
            styleId = BUTTON_STYLE_ACCESSIBILITY
        ).applyStyle(Style(
            margin = EdgeValue(
                top = 8.unitReal(),
                bottom = 8.unitReal()
            ),
            size = Size(
                height = 40.unitReal()
            ),
            flex = Flex(
                alignItems = AlignItems.CENTER)
        )
        ).applyAccessibility(
            accessibility = Accessibility(
                accessible = accessible,
                accessibilityLabel = accessibilityLabel
            )
        )
}