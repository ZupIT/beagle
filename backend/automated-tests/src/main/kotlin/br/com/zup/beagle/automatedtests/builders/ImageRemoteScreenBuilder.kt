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
import br.com.zup.beagle.automatedtests.constants.TEXT_IMAGE_REMOTE
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.core.ScrollAxis
import br.com.zup.beagle.widget.core.ImageContentMode
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.core.AlignSelf
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.NavigationBarItem
import br.com.zup.beagle.widget.layout.ScrollView
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.ui.Image
import br.com.zup.beagle.widget.ui.ImagePath
import br.com.zup.beagle.widget.ui.Text

object ImageRemoteScreenBuilder {
    fun build() = Screen(
        navigationBar = NavigationBar(
            title = "Beagle Image Remote",
            showBackButton = true,
            navigationBarItems = listOf(
                NavigationBarItem(
                    text = "",
                    image = ImagePath.Local.justMobile("informationImage"),
                    action = Alert(
                        title = "Image Remote",
                        message = "It is a widget that implements an image with a URL.",
                        labelOk = "OK"
                    )
                )
            )
        ),
        child = ScrollView(
            scrollDirection = ScrollAxis.VERTICAL,
            children = listOf(buildImage(title = "Image Remote")) +
                ImageContentMode.values().map { buildImage("Image Remote with Mode.$it", it) }
        )
    )

    private fun buildImage(title: String, mode: ImageContentMode? = null) = Container(
        children = listOf(
            buildText(title),
            Image(ImagePath.Remote(BEACH_NETWORK_IMAGE), mode).applyStyle(Style(
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
        styleId = TEXT_IMAGE_REMOTE
    ).applyStyle(Style(
        flex = Flex(
            alignSelf = AlignSelf.CENTER
        ),
        margin = EdgeValue(
            top = 8.unitReal()
        ))
    )
}