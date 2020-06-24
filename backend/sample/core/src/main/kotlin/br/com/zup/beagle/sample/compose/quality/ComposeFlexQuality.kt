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
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.FlexDirection
import br.com.zup.beagle.widget.core.JustifyContent
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.core.TextAlignment
import br.com.zup.beagle.widget.layout.ComposeComponent
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.ui.Text

object ComposeFlexQuality : ComposeComponent {
    override fun build() = Container(
        children = listOf(
            createText(backgroundColor = "#142850", text = "1"),
            createText(backgroundColor = "#dd7631", text = "2"),
            createText(backgroundColor = "#649d66", text = "3"),
            createText(backgroundColor = "#142850", text = "4"),
            createText(backgroundColor = "#dd7631", text = "5"),
            createText(backgroundColor = "#649d66", text = "6")
        )
    ).applyStyle(
        Style(
            backgroundColor = "#0000000",
            flex = Flex(flexDirection = FlexDirection.ROW),
            padding = EdgeValue(all = 20.unitReal()
            )
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
            size = Size(width = 50.unitReal(), height = 50.unitReal())
        )
    )
}