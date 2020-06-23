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

import br.com.zup.beagle.core.Style
import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.sample.constants.BEACH_NETWORK_IMAGE
import br.com.zup.beagle.sample.constants.TEXT_NETWORK_IMAGE
import br.com.zup.beagle.widget.core.AlignSelf
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.ImageContentMode
import br.com.zup.beagle.widget.core.ScrollAxis
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.NavigationBarItem
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScreenBuilder
import br.com.zup.beagle.widget.layout.ScrollView
import br.com.zup.beagle.widget.ui.Image
import br.com.zup.beagle.widget.ui.ImagePath.Local
import br.com.zup.beagle.widget.ui.ImagePath.Remote
import br.com.zup.beagle.widget.ui.Text

object NetworkImageScreenBuilder : ScreenBuilder {
    override fun build() = Screen(
        navigationBar = NavigationBar(
            title = "Beagle NetworkImage",
            showBackButton = true,
            navigationBarItems = listOf(
                NavigationBarItem(
                    text = "",
                    image = Local.justMobile("informationImage"),
                    action = Alert(
                        title = "NetworkImage",
                        message = "It is a widget that implements an image with a URL.",
                        labelOk = "OK"
                    )
                )
            )
        ),
        child = ScrollView(
            scrollDirection = ScrollAxis.VERTICAL,
            children = listOf(buildImage(title = "NetworkImage")) +
                ImageContentMode.values().map { buildImage("NetworkImage with ImageContentMode.$it", it) }
        )
    )

    private fun buildImage(title: String, mode: ImageContentMode? = null) = Container(
        children = listOf(
            buildText(title),
            Image(Remote(BEACH_NETWORK_IMAGE), mode).applyStyle(Style(
                flex = Flex(
                    alignSelf = AlignSelf.CENTER
                ),
                size = Size(
                    width = 150.unitReal(),
                    height = 130.unitReal()
                ))
            )
        )
    )

    private fun buildText(text: String) = Text(
        text = text,
        styleId = TEXT_NETWORK_IMAGE
    ).applyStyle(Style(
        flex = Flex(
            alignSelf = AlignSelf.CENTER
        ),
        margin = EdgeValue(
            top = 8.unitReal()
        ))
    )
}
