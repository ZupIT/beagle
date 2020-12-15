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

import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.context.expressionOf
import br.com.zup.beagle.widget.core.TextAlignment
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.ui.Text

data class TextWithColor(val text: String, val color: String)
data class TextWithAlignment(
    val text: String,
    val alignmentLeft: TextAlignment,
    val alignmentCenter: TextAlignment,
    val alignmentRight: TextAlignment
)

const val TEXT_COLOR = "#008000"

object TextScreenBuilder {
    fun build() = Screen(

        child = Container(
            children = listOf(
                Text("TextScreen"),
                textRendering(),
                textColoring(),
                textAligning()
            )
        )
    )

    private fun textRendering(): Container = Container(
        context = ContextData("textValue", "TextValueViaExpression"),
        children = listOf(
            Text("TextValue"),
            Text("@{textValue}")
        )
    )

    private fun textColoring(): Container = Container(
        context = ContextData(
            id = "textWithColor",
            value = TextWithColor(text = "TextWithColorViaExpression", color = TEXT_COLOR)),
        children = listOf(
            Text(text = "TextWithColor", textColor = TEXT_COLOR),
            Text(
                text = expressionOf("@{textWithColor.text}"),
                textColor = expressionOf("@{textWithColor.color}")
            )
        )
    )

    private fun textAligning(): Container = Container(
        context = ContextData(
            id = "textWithAlignment",
            value = TextWithAlignment(
                text = "TextAligned",
                alignmentLeft = TextAlignment.LEFT,
                alignmentCenter = TextAlignment.CENTER,
                alignmentRight = TextAlignment.RIGHT
            )
        ),
        children = listOf(
            Text(text = "TextAlignedLeft", alignment = TextAlignment.LEFT),
            Text(text = "TextAlignedCenter", alignment = TextAlignment.CENTER),
            Text(text = "TextAlignedRight", alignment = TextAlignment.RIGHT),
            Text(
                text = expressionOf("@{textWithAlignment.text}" + "LeftViaExpression"),
                alignment = expressionOf("@{textWithAlignment.alignmentLeft}")
            ),
            Text(
                text = expressionOf("@{textWithAlignment.text}" + "CenterViaExpression"),
                alignment = expressionOf("@{textWithAlignment.alignmentCenter}")
            ),
            Text(
                text = expressionOf("@{textWithAlignment.text}" + "RightViaExpression"),
                alignment = expressionOf("@{textWithAlignment.alignmentRight}")
            )
        )
    )
}