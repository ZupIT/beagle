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
import br.com.zup.beagle.automatedtests.constants.LOGO_BEAGLE
import br.com.zup.beagle.automatedtests.constants.SCREEN_ACTION_CLICK_ENDPOINT
import br.com.zup.beagle.automatedtests.constants.SCREEN_TEXT_STYLE
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.widget.Widget
import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.action.Navigate
import br.com.zup.beagle.widget.action.Route
import br.com.zup.beagle.widget.core.AlignSelf
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.ScrollAxis
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.NavigationBarItem
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScrollView
import br.com.zup.beagle.widget.navigation.Touchable
import br.com.zup.beagle.widget.ui.Image
import br.com.zup.beagle.widget.ui.ImagePath.Local
import br.com.zup.beagle.widget.ui.ImagePath.Remote
import br.com.zup.beagle.widget.ui.Text

object TouchableScreenBuilder {
     fun build() = Screen(
        navigationBar = NavigationBar(
            title = "Beagle Touchable",
            showBackButton = true,
            navigationBarItems = listOf(
                NavigationBarItem(
                    text = "",
                    image = Local.justMobile("informationImage"),
                    action = Alert(
                        title = "Touchable",
                        message = "Applies click action on widgets that have no action.",
                        labelOk = "OK"
                    )
                )
            )
        ),
        child = ScrollView(
            scrollDirection = ScrollAxis.VERTICAL,
            children = listOf(
                touchableCustom(title = "Text with Touchable", item = Text("Click here!")),
                touchableCustom(title = "Image with Touchable", item = Image(Local.justMobile(LOGO_BEAGLE))),
                networkImageTouchable()
            )
        )
    )

    private fun touchableCustom(item: Widget, title: String) = Container(
        children = listOf(
            buildTitle(title),
            Touchable(
                listOf(Navigate.PushView(Route.Remote(SCREEN_ACTION_CLICK_ENDPOINT))),
                child = item.applyStyle(Style(
                    flex = Flex(
                        alignSelf = AlignSelf.CENTER
                    ),
                    margin = EdgeValue(
                        top = 8.unitReal(),
                        bottom = 8.unitReal()
                    ))
                )
            )
        )
    )

    private fun buildTitle(text: String) = Text(
        text = text,
        styleId = SCREEN_TEXT_STYLE
    ).applyStyle(Style(
        flex = Flex(
            alignSelf = AlignSelf.CENTER),
        margin = EdgeValue(
            top = 8.unitReal()
        ))
    )

    private fun networkImageTouchable() = Container(
        children = listOf(
            buildTitle("NetworkImage with Touchable"),
            Touchable(
                child = Image(
                    Remote(BEACH_NETWORK_IMAGE)
                ).applyStyle(Style(
                    size = Size(
                        width = 150.unitReal(),
                        height = 130.unitReal()
                    ),
                    flex = Flex(
                        alignSelf = AlignSelf.CENTER
                    )
                )
                ),
                onPress = listOf(Navigate.PushView(Route.Remote(SCREEN_ACTION_CLICK_ENDPOINT)))
            )
        )
    )
}
