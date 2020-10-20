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

import br.com.zup.beagle.automatedtests.constants.LIGHT_GREY
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.context.expressionOf
import br.com.zup.beagle.widget.context.valueOf
import br.com.zup.beagle.widget.context.valueOfNullable
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.TextAlignment
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.NavigationBarItem
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.ui.ImagePath
import br.com.zup.beagle.widget.ui.Text

data class TextContext(
    val text: String,
    val textExpressionColor: String,
    val textExpressionAlignmentLeft: String,
    val textExpressionAlignmentCenter: String,
    val textExpressionAlignmentRight: String,
    val color: String,
    val alignmentLeft: TextAlignment,
    val alignmentCenter: TextAlignment,
    val alignmentRight: TextAlignment,
)

object TextScreenBuilder {
    fun build() = Screen(
        navigationBar = NavigationBar(
            title = "Beagle Text",
            showBackButton = true,
            navigationBarItems = listOf(
                NavigationBarItem(
                    text = "",
                    image = ImagePath.Local.justMobile("informationImage"),
                    action = Alert(
                        title = "Text",
                        message = "This widget will define a text view natively using the server driven " +
                            "information received through Beagle.",
                        labelOk = "OK"
                    )
                )
            )
        ),
        child = Container(
            children = listOf(
                beagleText(text = "hello world"),
                beagleText(text = expressionOf("@{context.text}")),
                beagleText(text = "hello world with textColor", textColor = LIGHT_GREY),
                beagleText(text = expressionOf("@{context.textExpressionColor}"),
                    textColor = expressionOf("@{context.color}")),
                beagleText(text = "hello world with textAlignment LEFT", alignment = TextAlignment.LEFT),
                beagleText(text = "hello world with textAlignment CENTER", alignment = TextAlignment.CENTER),
                beagleText(text = "hello world with textAlignment RIGHT", alignment = TextAlignment.RIGHT),
                beagleText(text = expressionOf("@{context.textExpressionAlignmentLeft}"),
                    alignment = expressionOf("@{context.alignmentLeft}")),
                beagleText(text = expressionOf("@{context.textExpressionAlignmentCenter}"),
                    alignment = expressionOf("@{context.alignmentCenter}")),
                beagleText(text = expressionOf("@{context.textExpressionAlignmentRight}"),
                    alignment = expressionOf("@{context.alignmentRight}"))
            ),
            context = ContextData(
                id = "context",
                value = TextContext(
                    text = "hello world via expression",
                    textExpressionColor = "hello world with textColor via expression",
                    textExpressionAlignmentLeft = "hello world with textAlignment LEFT via expression",
                    textExpressionAlignmentCenter = "hello world with textAlignment CENTER via expression",
                    textExpressionAlignmentRight = "hello world with textAlignment RIGHT via expression",
                    color = LIGHT_GREY,
                    alignmentLeft = TextAlignment.LEFT,
                    alignmentCenter = TextAlignment.CENTER,
                    alignmentRight = TextAlignment.RIGHT
                )
            )
        )
    )

    private fun beagleText(
        text: Bind<String>,
        textColor: Bind<String>? = null,
        alignment: Bind<TextAlignment>? = null,
        styleId: String? = null,
        appearanceColor: String? = null
    ) =
        Text(text = text, styleId = styleId, textColor = textColor, alignment = alignment)
            .applyStyle(Style(
                backgroundColor = appearanceColor,
                margin = EdgeValue(
                    top = 16.unitReal(),
                    left = 16.unitReal(),
                    right = 16.unitReal()
                )
            )
            )

    private fun beagleText(
        text: String,
        textColor: String? = null,
        alignment: TextAlignment? = null,
        styleId: String? = null,
        appearanceColor: String? = null
    ) =
        beagleText(valueOf(text), valueOfNullable(textColor), valueOfNullable(alignment), styleId, appearanceColor)

}