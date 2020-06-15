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

import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.sample.constants.BLACK
import br.com.zup.beagle.sample.constants.LIGHT_GREY
import br.com.zup.beagle.widget.core.AlignSelf
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.TextAlignment
import br.com.zup.beagle.widget.layout.ComposeComponent
import br.com.zup.beagle.widget.layout.PageView
import br.com.zup.beagle.widget.pager.PageIndicator
import br.com.zup.beagle.widget.ui.Text

object ComposePageView : ComposeComponent {
    override fun build() = PageView(
        pageIndicator = PageIndicator(
            selectedColor = BLACK,
            unselectedColor = LIGHT_GREY
        ),
        children = (1..3).map(this::createText)
    )

    private fun createText(i: Int) = Text("Page $i", alignment = TextAlignment.CENTER)
        .applyFlex(
            Flex(
                alignSelf = AlignSelf.CENTER,
                grow = 1.0
            )
        )
}