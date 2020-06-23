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

package br.com.zup.beagle.sample.compose.sample

import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.ext.unitPercent
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.sample.compose.quality.ComposeScrollViewQuality
import br.com.zup.beagle.sample.constants.TEXT_FONT_MAX
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.ScrollAxis
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.layout.ComposeComponent
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.ScrollView
import br.com.zup.beagle.widget.ui.Text

object ComposeSampleScrollView: ComposeComponent {
    override fun build() = ScrollView(
        scrollDirection = ScrollAxis.VERTICAL,
        children = listOf(
            getVerticalScrollView(),
            getHorizontalScrollView()
        )
    )

    private fun getVerticalScrollView() = Container(
        children = listOf(
            Text("Vertical ScrollView"),
            ScrollView(
                children = listOf(
                    createText("Hello 1"),
                    createText("Hello 2"),
                    createText("Hello 3"),
                    createText("Hello 4"),
                    createText("Hello 5")
                ),
                scrollDirection = ScrollAxis.VERTICAL
            )
        )
    ).applyFlex(
        flex = Flex(
            size = Size(
                height = 130.unitReal(),
                width = 100.unitPercent()
            )
        )
    )

    private fun getHorizontalScrollView() = Container(
        children = listOf(
            Text("Horizontal ScrollView with scrollBars"),
            ScrollView(
                children = listOf(
                    createText("Hello 1"),
                    createText("Hello 2"),
                    createText("Hello 3"),
                    createText("Hello 4"),
                    createText("Hello 5")

                ),
                scrollDirection = ScrollAxis.HORIZONTAL,
                scrollBarEnabled = true
            )
        )
    )

    private fun createText(text: String) = Text(text, TEXT_FONT_MAX)
}