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

import br.com.zup.beagle.core.PositionType
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitPercent
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.sample.constants.TEXT_WHITE_BUTTON
import br.com.zup.beagle.sample.constants.TEXT_WHITE_DEFAULT
import br.com.zup.beagle.sample.constants.TEXT_WHITE_LARGE
import br.com.zup.beagle.sample.constants.TEXT_WHITE_MEDIUM
import br.com.zup.beagle.widget.core.AlignSelf
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.FlexDirection
import br.com.zup.beagle.widget.core.JustifyContent
import br.com.zup.beagle.widget.core.ScrollAxis
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.core.TextAlignment
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScreenBuilder
import br.com.zup.beagle.widget.layout.ScrollView
import br.com.zup.beagle.widget.ui.Text

const val RED = "#B22222"
const val LIGHT_RED = "#FF5141"
const val BLUE = "#2E02B6"
const val LIGHT_BLUE = "#00C0EF"
const val PURPLE = "#4B0082"
const val BLACK = "#000000"
const val GREEN = "#699C3E"

object PositioningBeagleScreenBuilder : ScreenBuilder {
    override fun build() = Screen(
        child = ScrollView(
            scrollDirection = ScrollAxis.VERTICAL,
            children = listOf(
                Container(
                    children = listOf(
                        red(),
                        blue(),
                        adjustHeight(),
                        purple(),
                        black(),
                        green()
                    )
                ).applyStyle(
                    Style(
                        flex = Flex(
                            grow = 1.0
                        )
                    )
                )
            )
        )
    )

    private fun red() =
        Container(
            children = listOf(
                Text(
                    text = "1",
                    styleId = TEXT_WHITE_LARGE,
                    alignment = TextAlignment.CENTER
                ).applyStyle(
                    Style(
                        size = Size(width = 100.unitPercent(), height = 220.unitReal()),
                        backgroundColor = RED
                    )
                ),
                Text(
                    text = "2",
                    styleId = TEXT_WHITE_MEDIUM,
                    alignment = TextAlignment.CENTER
                ).applyStyle(
                    Style(
                        size = Size(width = 50.unitReal(), height = 50.unitReal()),
                        backgroundColor = LIGHT_RED,
                        margin = EdgeValue(top = 16.unitReal(), left = 16.unitReal()),
                        positionType = PositionType.ABSOLUTE
                    )
                ),
                Text(
                    text = "3",
                    styleId = TEXT_WHITE_MEDIUM,
                    alignment = TextAlignment.CENTER
                ).applyStyle(
                    Style(
                        size = Size(width = 50.unitReal(), height = 50.unitReal()),
                        backgroundColor = LIGHT_RED,
                        position = EdgeValue(right = 0.unitReal(), top = 0.unitReal()),
                        margin = EdgeValue(top = 16.unitReal(), right = 16.unitReal()),
                        positionType = PositionType.ABSOLUTE
                    )
                ),
                Text(
                    text = "4",
                    styleId = TEXT_WHITE_MEDIUM,
                    alignment = TextAlignment.CENTER
                ).applyStyle(
                    Style(
                        size = Size(width = 50.unitReal(), height = 50.unitReal()),
                        backgroundColor = LIGHT_RED,
                        flex = Flex(alignSelf = AlignSelf.FLEX_END),
                        position = EdgeValue(right = 60.unitReal(), bottom = 0.unitReal()),
                        margin = EdgeValue(top = 16.unitReal(), right = 16.unitReal()),
                        positionType = PositionType.ABSOLUTE
                    )
                ),
                Text(
                    text = "5",
                    styleId = TEXT_WHITE_MEDIUM,
                    alignment = TextAlignment.CENTER
                ).applyStyle(
                    Style(
                        size = Size(width = 50.unitReal(), height = 50.unitReal()),
                        backgroundColor = LIGHT_RED,
                        flex = Flex(alignSelf = AlignSelf.FLEX_END),
                        position = EdgeValue(right = 0.unitReal(), bottom = 0.unitReal()),
                        margin = EdgeValue(top = 16.unitReal(), right = 16.unitReal()),
                        positionType = PositionType.ABSOLUTE
                    )
                )
            )
        ).applyStyle(
            Style(
                size = Size(height = 245.unitReal(), width = 100.unitPercent()),
                positionType = PositionType.ABSOLUTE
            )
        )

    private fun blueA() =
        Container(
            children = listOf(
                Text(
                    text = "6",
                    styleId = TEXT_WHITE_MEDIUM,
                    alignment = TextAlignment.CENTER
                ).applyStyle(
                    Style(
                        size = Size(height = 200.unitReal()),
                        backgroundColor = BLUE,
                        position = EdgeValue(top = 0.unitReal()),
                        margin = EdgeValue(bottom = 5.unitReal())
                    )
                ),
                Text(
                    text = "7",
                    styleId = TEXT_WHITE_MEDIUM,
                    alignment = TextAlignment.CENTER
                ).applyStyle(
                    Style(
                        size = Size(height = 40.unitReal()),
                        backgroundColor = BLUE,
                        position = EdgeValue(bottom = 0.unitReal())
                    )
                )

            )
        ).applyStyle(
            Style(
                size = Size(width = 144.unitReal()),
                margin = EdgeValue(left = 16.unitReal())
            )
        )

    private fun blueB() =
        Container(
            children = listOf(
                Text(
                    text = "8",
                    styleId = TEXT_WHITE_MEDIUM,
                    alignment = TextAlignment.CENTER
                ).applyStyle(
                    Style(
                        size = Size(height = 40.unitReal()),
                        backgroundColor = "#00c0ef",
                        position = EdgeValue(top = 0.unitReal()),
                        margin = EdgeValue(bottom = 5.unitReal())
                    )
                ),
                Text(
                    text = "9",
                    styleId = TEXT_WHITE_DEFAULT,
                    alignment = TextAlignment.CENTER
                ).applyStyle(
                    Style(
                        size = Size(height = 20.unitReal()),
                        backgroundColor = LIGHT_BLUE,
                        position = EdgeValue(top = 0.unitReal()),
                        margin = EdgeValue(bottom = 5.unitReal())
                    )
                ),
                Text(
                    text = "10",
                    styleId = TEXT_WHITE_DEFAULT,
                    alignment = TextAlignment.CENTER
                ).applyStyle(
                    Style(
                        size = Size(height = 20.unitReal()),
                        backgroundColor = LIGHT_BLUE,
                        position = EdgeValue(top = 0.unitReal()),
                        margin = EdgeValue(bottom = 5.unitReal())
                    )
                ),
                Text(
                    text = "11",
                    styleId = TEXT_WHITE_DEFAULT,
                    alignment = TextAlignment.CENTER
                ).applyStyle(
                    Style(
                        size = Size(height = 20.unitReal()),
                        backgroundColor = LIGHT_BLUE,
                        position = EdgeValue(top = 0.unitReal()),
                        margin = EdgeValue(bottom = 5.unitReal())
                    )
                )
            )
        ).applyStyle(
            Style(
                margin = EdgeValue(horizontal = 16.unitReal()),
                flex = Flex(
                    grow = 1.0
                ),
                position = EdgeValue(top = 80.unitReal())
            )
        )

    private fun blue() =
        Container(
            children = listOf(
                blueA(),
                blueB()
            )
        ).applyStyle(
            Style(
                position = EdgeValue(top = 190.unitReal()),
                size = Size(width = 100.unitPercent()),
                flex = Flex(
                    flexDirection = FlexDirection.ROW
                )
            )
        )

    private fun adjustHeight() = Container(children = listOf()).applyStyle(
        Style(
            size = Size(height = 190.unitReal())
        )
    )

    private fun purple() =
        Container(
            children = listOf(
                Text(
                    text = "12",
                    styleId = TEXT_WHITE_MEDIUM,
                    alignment = TextAlignment.CENTER
                ).applyStyle(
                    Style(
                        size = Size(height = 50.unitReal()),
                        backgroundColor = PURPLE,
                        margin = EdgeValue(left = 16.unitReal(), right = 16.unitReal())
                    )
                ),
                Text(
                    text = "13",
                    styleId = TEXT_WHITE_BUTTON,
                    alignment = TextAlignment.CENTER
                ).applyStyle(
                    Style(
                        size = Size(height = 30.unitReal()),
                        backgroundColor = PURPLE,
                        margin = EdgeValue(left = 16.unitReal(), right = 16.unitReal(), top = 5.unitReal())
                    )
                ),
                Text(
                    text = "14",
                    styleId = TEXT_WHITE_BUTTON,
                    alignment = TextAlignment.CENTER
                ).applyStyle(
                    Style(
                        size = Size(height = 30.unitReal()),
                        backgroundColor = PURPLE,
                        margin = EdgeValue(left = 16.unitReal(), right = 16.unitReal(), top = 5.unitReal())
                    )
                )
            )
        ).applyStyle(
            Style(
                size = Size(width = 100.unitPercent()),
                margin = EdgeValue(top = 16.unitReal())
            )
        )

    private fun black() = Text(
        text = "15",
        styleId = TEXT_WHITE_BUTTON,
        alignment = TextAlignment.CENTER
    ).applyStyle(
        Style(
            size = Size(height = 100.unitReal(), width = 100.unitPercent()),
            backgroundColor = BLACK,
            margin = EdgeValue(left = 16.unitReal(), top = 25.unitReal())
        )
    )

    private fun green() = Container(
        children = listOf(
            Text(
                text = "16",
                styleId = TEXT_WHITE_MEDIUM,
                alignment = TextAlignment.CENTER
            ).applyStyle(
                Style(
                    size = Size(width = 50.unitReal(), height = 50.unitReal()),
                    backgroundColor = GREEN
                )
            ),
            Text(
                text = "17",
                styleId = TEXT_WHITE_MEDIUM,
                alignment = TextAlignment.CENTER
            ).applyStyle(
                Style(
                    size = Size(width = 50.unitReal(), height = 50.unitReal()),
                    backgroundColor = GREEN
                )
            )
        )
    ).applyStyle(
        Style(
            size = Size(width = 200.unitReal()),
            margin = EdgeValue(horizontal = 30.unitReal(), top = 30.unitReal(), bottom = 16.unitReal()),
            flex = Flex(
                flexDirection = FlexDirection.ROW,
                justifyContent = JustifyContent.SPACE_BETWEEN,
                alignSelf = AlignSelf.CENTER
            )
        )
    )
}