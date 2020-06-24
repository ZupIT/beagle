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

package br.com.zup.beagle.sample.compose.quality


import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.widget.core.AlignContent
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.FlexDirection
import br.com.zup.beagle.widget.core.FlexWrap
import br.com.zup.beagle.widget.core.JustifyContent
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.core.TextAlignment
import br.com.zup.beagle.widget.layout.ComposeComponent
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.ui.Text

object ComposeFlexQuality: ComposeComponent {
    override fun build() = Container(
        children = listOf(
            createText(backgroundColor = "#142850", text = "texto 1"),
            createText(backgroundColor = "#dd7631", text = "texto 2"),
            createText(backgroundColor = "#649d66", text = "texto 3"),
            createText(backgroundColor = "#142850", text = "texto 4"),
            createText(backgroundColor = "#dd7631", text = "texto 5"),
            createText(backgroundColor = "#649d66", text = "texto 6"),
            createText(backgroundColor = "#142850", text = "texto 7"),
            createText(backgroundColor = "#dd7631", text = "texto 8"),
            createText(backgroundColor = "#649d66", text = "texto 9"),
            createText(backgroundColor = "#142850", text = "texto 10"),
            createText(backgroundColor = "#dd7631", text = "texto 11"),
            createText(backgroundColor = "#649d66", text = "texto 12"),
            createText(backgroundColor = "#142850", text = "texto 13"),
            createText(backgroundColor = "#dd7631", text = "texto 14"),
            createText(backgroundColor = "#649d66", text = "texto 15")
        )
    ).applyFlex(
        Flex(
            grow = 1.0,
            flexDirection = FlexDirection.ROW,
            flexWrap = FlexWrap.WRAP,
            alignContent = AlignContent.STRETCH
        )
    )

    fun createText(text: String, backgroundColor: String): Container = Container(
        listOf(
            Text(
                text = text,
                alignment = TextAlignment.CENTER
            )
        )
    ).applyStyle(
        style = Style(
            backgroundColor = backgroundColor,
            size = Size(width = 100.unitReal(), height =  100.unitReal())
        )
    )
}