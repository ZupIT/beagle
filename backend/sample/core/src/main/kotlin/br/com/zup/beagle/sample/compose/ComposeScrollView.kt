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

package br.com.zup.beagle.sample.compose

import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.sample.constants.LIGHT_GREY
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.ScrollAxis
import br.com.zup.beagle.widget.layout.ComposeComponent
import br.com.zup.beagle.widget.layout.ScrollView
import br.com.zup.beagle.widget.ui.Text

object ComposeScrollView : ComposeComponent {
    override fun build() = ScrollView(
        scrollBarEnabled = false,
        scrollDirection = ScrollAxis.HORIZONTAL,
        children = listOf(
            createText().applyStyle(Style(backgroundColor = LIGHT_GREY,
                margin = EdgeValue(
                    left = 30.unitReal()
                ))
            )
        ) + List(20) { createText() }
    )

    private fun createText() = Text("Text")
}
