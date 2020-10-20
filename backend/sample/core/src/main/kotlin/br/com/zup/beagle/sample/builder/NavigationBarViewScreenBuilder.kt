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

import br.com.zup.beagle.core.Accessibility
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.sample.constants.BUTTON_STYLE_TITLE
import br.com.zup.beagle.sample.constants.NAVIGATION_BAR_STYLE_DEFAULT
import br.com.zup.beagle.sample.constants.REPRESENTATION_NAVIGATION_BAR_ENDPOINT
import br.com.zup.beagle.sample.constants.REPRESENTATION_NAVIGATION_BAR_IMAGE_ENDPOINT
import br.com.zup.beagle.sample.constants.REPRESENTATION_NAVIGATION_BAR_STYLE_ENDPOINT
import br.com.zup.beagle.sample.constants.REPRESENTATION_NAVIGATION_BAR_TEXT_ENDPOINT
import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.action.Navigate
import br.com.zup.beagle.widget.action.Route
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.NavigationBarItem
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScreenBuilder
import br.com.zup.beagle.widget.layout.extensions.setId
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.ImagePath.Local

object NavigationBarViewScreenBuilder : ScreenBuilder {
    override fun build() = Screen(
        navigationBar = NavigationBar(
            backButtonAccessibility = Accessibility(
                accessibilityLabel = "Voltar"
            ),
            title = "Beagle NavigationBar",
            styleId = NAVIGATION_BAR_STYLE_DEFAULT,
            showBackButton = true,
            navigationBarItems = listOf(
                NavigationBarItem(
                    text = "Ajuda",
                    accessibility = Accessibility(accessibilityLabel = "Content Description"),
                    image = Local.justMobile("informationImage"),
                    action = Alert(
                        title = "NavigationBar",
                        message = "This component that allows to place titles and button action.",
                        labelOk = "OK"
                    )
                ).setId("nbiInformation")
            )
        ),
        child = Container(
            children = listOf(
                createMenu("NavigationBar", REPRESENTATION_NAVIGATION_BAR_ENDPOINT),
                createMenu("NavigationBar with Style", REPRESENTATION_NAVIGATION_BAR_STYLE_ENDPOINT),
                createMenu("NavigationBar with Item(Text)", REPRESENTATION_NAVIGATION_BAR_TEXT_ENDPOINT),
                createMenu("NavigationBar with Item(Image)", REPRESENTATION_NAVIGATION_BAR_IMAGE_ENDPOINT)
            )
        )
    )

    private fun createMenu(text: String, path: String) = Button(
        text = text,
        onPress = listOf(Navigate.PushView(Route.Remote(path))),
        styleId = BUTTON_STYLE_TITLE
    ).applyStyle(Style(
            margin = EdgeValue(
                top = 8.unitReal()
            )
        )
    )
}
