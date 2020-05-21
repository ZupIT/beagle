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

import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.sample.constants.LOGO_BEAGLE
import br.com.zup.beagle.widget.Widget
import br.com.zup.beagle.widget.core.AlignItems
import br.com.zup.beagle.widget.core.ComposeComponent
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.ui.Image
import br.com.zup.beagle.widget.ui.Text

class CustomComposeComponent : ComposeComponent() {
    override fun build(): ServerDrivenComponent {
        return Container(
            children = listOf(
                buildTextBeagle(),
                buildImageBeagle()
            )
        ).applyFlex(
            flex = Flex(
                alignItems = AlignItems.CENTER
            )
        )
    }

    private fun buildTextBeagle(): Widget {
        return Text(
            "Beagle framework"
        ).applyFlex(
            flex = Flex(
                alignItems = AlignItems.CENTER,
                margin = EdgeValue(
                    top = 16.unitReal(),
                    bottom = 16.unitReal()
                )
            )
        )
    }

    private fun buildImageBeagle(): Widget {
        return Image(
            LOGO_BEAGLE
        )
    }
}