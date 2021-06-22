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
import br.com.zup.beagle.ext.setFlex
import br.com.zup.beagle.ext.setStyle
import br.com.zup.beagle.widget.context.valueOf
import br.com.zup.beagle.widget.core.FlexDirection
import br.com.zup.beagle.widget.core.ImageContentMode
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.core.UnitValue
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.ui.Image
import br.com.zup.beagle.widget.ui.ImagePath.Local
import br.com.zup.beagle.widget.ui.Text

object ImageScreenBuilder {
    fun build() = Screen(
        child = Container(
            children = listOf(
                createImages(FlexDirection.COLUMN),
                createImages(FlexDirection.ROW),
            )
        )
    )

    private fun createImages(flexDirection: FlexDirection) =
        Container(
            children = listOf(
                createStruct(
                    direction = flexDirection,
                    text = "without size",
                    image = Image(Local.justMobile(LOGO_BEAGLE))
                ),
                createStruct(
                    direction = flexDirection,
                    text = "${ImageContentMode.FIT_XY} and height = 40",
                    image = Image(
                        Local.justMobile(LOGO_BEAGLE), ImageContentMode.FIT_XY,
                    ).setStyle {
                        size = Size(height = UnitValue.real(40))
                    },
                ),
                createStruct(
                    direction = flexDirection,
                    text = "${ImageContentMode.FIT_CENTER} and width = 40",
                    image = Image(
                        Local.justMobile(LOGO_BEAGLE), ImageContentMode.FIT_CENTER,
                    ).setStyle {
                        size = Size(width = UnitValue.real(40))
                    },
                ),
                createStruct(
                    direction = flexDirection,
                    text = "${ImageContentMode.CENTER_CROP} and width = 100 and height = 40",
                    image = Image(
                        Local.justMobile(LOGO_BEAGLE), ImageContentMode.CENTER_CROP,
                    ).setStyle {
                        size = Size.box(100, 40)
                    },
                ),
                createStruct(
                    direction = flexDirection,
                    text = "${ImageContentMode.CENTER} and width = 300 and height = 40",
                    image = Image(
                        Local.justMobile(LOGO_BEAGLE), ImageContentMode.CENTER,
                    ).setStyle {
                        size = Size.box(300, 40)
                    },
                ),
            )
        ).setStyle {
            backgroundColor = valueOf(if (flexDirection == FlexDirection.COLUMN) "#F0FFF0" else "#FFF5EE")
        }

    private fun createStruct(
        direction: FlexDirection,
        text: String,
        image: Image,
    ) = Container(
        children = listOf(
            Text(text = "$direction + contentMode = $text"),
            Container(
                children = listOf(
                    image
                )
            ).setFlex {
                flexDirection = direction
            }
        )
    )
}
