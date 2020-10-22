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

import br.com.zup.beagle.automatedtests.constants.LOGO_BEAGLE
import br.com.zup.beagle.automatedtests.constants.LOGO_BEAGLE_URL
import br.com.zup.beagle.automatedtests.constants.TITLE_SCREEN
import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.core.ImageContentMode
import br.com.zup.beagle.widget.core.ScrollAxis
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.NavigationBarItem
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScrollView
import br.com.zup.beagle.widget.ui.Image
import br.com.zup.beagle.widget.ui.ImagePath.Local
import br.com.zup.beagle.widget.ui.Text

object ImageScreenBuilder {
     fun build() = Screen(
        navigationBar = NavigationBar(
            title = "Beagle Image",
            showBackButton = true,
            navigationBarItems = listOf(
                NavigationBarItem(
                    text = "",
                    image = Local.justMobile("informationImage"),
                    action = Alert(
                        title = "Image",
                        message = "This widget will define a image view natively using the server driven " +
                            "information received through Beagle.",
                        labelOk = "OK"
                    )
                )
            )
        ),
        child = ScrollView(
            scrollDirection = ScrollAxis.VERTICAL,
            children = listOf(createText("Image"), Image(Local.both(LOGO_BEAGLE_URL, LOGO_BEAGLE))) +
                ImageContentMode.values().flatMap(this::createImageWithModeAndText)
        )
    )

    private fun createText(text: String) = Text(text = text, styleId = TITLE_SCREEN)

    private fun createImageWithModeAndText(mode: ImageContentMode) =
        listOf(createText("Image with contentMode = $mode"), Image(Local.both(LOGO_BEAGLE_URL, LOGO_BEAGLE), mode))
}
