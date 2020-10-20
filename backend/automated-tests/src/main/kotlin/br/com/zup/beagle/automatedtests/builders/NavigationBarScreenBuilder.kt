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

import br.com.zup.beagle.automatedtests.constants.BUTTON_STYLE_TITLE
import br.com.zup.beagle.automatedtests.constants.LOGO_BEAGLE
import br.com.zup.beagle.automatedtests.constants.NAVIGATION_BAR_STYLE
import br.com.zup.beagle.automatedtests.constants.NAVIGATION_BAR_STYLE_DEFAULT
import br.com.zup.beagle.automatedtests.constants.REPRESENTATION_NAVIGATION_BAR_ENDPOINT
import br.com.zup.beagle.automatedtests.constants.REPRESENTATION_NAVIGATION_BAR_IMAGE_ENDPOINT
import br.com.zup.beagle.automatedtests.constants.REPRESENTATION_NAVIGATION_BAR_STYLE_ENDPOINT
import br.com.zup.beagle.automatedtests.constants.REPRESENTATION_NAVIGATION_BAR_TEXT_ENDPOINT
import br.com.zup.beagle.automatedtests.constants.SCREEN_ACTION_CLICK_ENDPOINT
import br.com.zup.beagle.automatedtests.constants.TEXT_FONT_MAX
import br.com.zup.beagle.core.Accessibility
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.action.Navigate
import br.com.zup.beagle.widget.action.Route
import br.com.zup.beagle.widget.action.SetContext
import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.context.expressionOf
import br.com.zup.beagle.widget.core.AlignItems
import br.com.zup.beagle.widget.core.JustifyContent
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.NavigationBarItem
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.extensions.setId
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.ImagePath
import br.com.zup.beagle.widget.ui.Text

data class ExampleGlobalContext(
    val navigationBar: String,
    val navigationBarStyle: String,
    val navigationBarWithText: String,
    val navigationBarWithImage: String,
    val textColor: String
)

object NavigationBarScreenBuilder {
    fun build() = Screen(
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
                    image = ImagePath.Local.justMobile("informationImage"),
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
                createMenu("Default NavigationBar", REPRESENTATION_NAVIGATION_BAR_ENDPOINT),
                createMenu("NavigationBar with Style", REPRESENTATION_NAVIGATION_BAR_STYLE_ENDPOINT),
                createMenu("NavigationBar with Item(Text)", REPRESENTATION_NAVIGATION_BAR_TEXT_ENDPOINT),
                createMenu("NavigationBar with Item(Image)", REPRESENTATION_NAVIGATION_BAR_IMAGE_ENDPOINT)
            )
        )
    )

    fun navigationBar() = navigationBarScreenBuilder(
        titleNavigation = "NavigationBar",
        text = expressionOf("@{global.navigationBar}")
    )

    fun navigationBarStyle() = navigationBarScreenBuilder(
        titleNavigation = "NavigationBar",
        styleNavigation = NAVIGATION_BAR_STYLE,
        text = expressionOf("@{global.navigationBarStyle}")
    )

    fun navigationBarWithTextAsItems() = navigationBarScreenBuilder(
        titleNavigation = "NavigationBar",
        text = expressionOf("@{global.navigationBarWithText}"),
        navigationBarItems = listOf(
            NavigationBarItem(
                text = "Entrar",
                action = Navigate.PushView(Route.Remote(SCREEN_ACTION_CLICK_ENDPOINT))
            )
        )
    )

    fun navigationBarWithImageAsItem() = navigationBarScreenBuilder(
        titleNavigation = "NavigationBar",
        text = expressionOf("@{global.navigationBarWithImage}"),
        navigationBarItems = listOf(
            NavigationBarItem(
                text = "",
                image = ImagePath.Local.justMobile(LOGO_BEAGLE),
                action = Navigate.PushView(Route.Remote(SCREEN_ACTION_CLICK_ENDPOINT))
            )
        )
    )

    private fun createMenu(text: String, path: String) = Button(
        text = text,
        onPress = listOf(
            Navigate.PushView(Route.Remote(path)),
            SetContext(
                contextId = "global",
                value = ExampleGlobalContext(
                    navigationBar = "Default NavigationBar",
                    navigationBarStyle = "NavigationBar with Style",
                    navigationBarWithText = "NavigationBar with Item(Text)",
                    navigationBarWithImage = "NavigationBar with Item(Image)",
                    textColor = "#000000"
                )
            )
        ),
        styleId = BUTTON_STYLE_TITLE
    ).applyStyle(Style(
            margin = EdgeValue(
                top = 8.unitReal()
            )
        )
    )

    private fun navigationBarScreenBuilder(
        titleNavigation: String,
        styleNavigation: String? = null,
        text: Bind<String>,
        navigationBarItems: List<NavigationBarItem>? = null
    ): Screen {
        return Screen(
            navigationBar = NavigationBar(
                title = titleNavigation,
                styleId = styleNavigation,
                showBackButton = true,
                navigationBarItems = navigationBarItems
            ),
            child = createBeagleText(text)
        )
    }

    private fun createBeagleText(text: Bind<String>) = Container(
        children = listOf(
            Text(text = text, styleId = TEXT_FONT_MAX, textColor =expressionOf("@{global.textColor}")),
            Button(
                text = "Update Global Context",
                onPress = listOf(
                    Alert(title = null, message = "Look, your text has changed! Global context is working!"),
                    SetContext(
                        contextId = "global",
                        value = ExampleGlobalContext(
                            navigationBar = "Beagle Default NavigationBar",
                            navigationBarStyle = "Beagle NavigationBar with Style",
                            navigationBarWithText = "Beagle NavigationBar with Item(Text)",
                            navigationBarWithImage = "Beagle NavigationBar with Item(Image)",
                            textColor = "#cc0000"
                        )
                    )
                )
            )
        )
    ).applyFlex(
        Flex(
            grow = 1.0,
            alignItems = AlignItems.CENTER,
            justifyContent = JustifyContent.CENTER
        )
    )
}