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
import br.com.zup.beagle.automatedtests.constants.LOGO_BEAGLE_URL
import br.com.zup.beagle.automatedtests.constants.TITLE_SCREEN
import br.com.zup.beagle.ext.setStyle
import br.com.zup.beagle.widget.core.ImageContentMode
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.core.UnitValue
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.ui.Image
import br.com.zup.beagle.widget.ui.ImagePath
import br.com.zup.beagle.widget.ui.ImagePath.Local
import br.com.zup.beagle.widget.ui.Text

object ImageScreenBuilder {
    fun build() = Screen(
        child = Container(
            children = listOf(
//                createText("Image without size"),
//                Image(
//                    Local.justMobile(LOGO_BEAGLE),
//                ),
                createText("${ImageContentMode.FIT_XY} and height = 100"),
                Image(
                    Local.justMobile(LOGO_BEAGLE), ImageContentMode.FIT_XY,
                ).setStyle {
                    size = Size(height = UnitValue.real(100))
                },
                createText("${ImageContentMode.FIT_CENTER} and width = 200"),
                Image(
                    Local.justMobile(LOGO_BEAGLE), ImageContentMode.FIT_CENTER,
                ).setStyle {
                    size = Size(width = UnitValue.real(200))
                },
                createText("${ImageContentMode.CENTER_CROP} and width = 200 and height = 100"),
                Image(
                    Local.justMobile(LOGO_BEAGLE), ImageContentMode.CENTER_CROP,
                ).setStyle {
                    size = Size.box(200, 100)
                },
                createText("${ImageContentMode.CENTER} and width = 300 and height = 100"),
                Image(
                    Local.justMobile(LOGO_BEAGLE), ImageContentMode.CENTER,
                ).setStyle {
                    size = Size.box(300, 100)
                },
            )
        )
    )

    private fun createText(text: String) = Text(text = "Image with contentMode = $text", styleId = TITLE_SCREEN)
}
