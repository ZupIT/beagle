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

import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.sample.compose.quality.ComposeImageViewQuality
import br.com.zup.beagle.sample.constants.BEACH_NETWORK_IMAGE
import br.com.zup.beagle.sample.constants.LOGO_BEAGLE
import br.com.zup.beagle.sample.constants.TITLE_SCREEN
import br.com.zup.beagle.widget.core.AlignSelf
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.ImageContentMode
import br.com.zup.beagle.widget.core.ScrollAxis
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.layout.ComposeComponent
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.ScrollView
import br.com.zup.beagle.widget.ui.Image
import br.com.zup.beagle.widget.ui.ImagePath
import br.com.zup.beagle.widget.ui.Text

object ComposeSampleImageView: ComposeComponent {
    override fun build() = ScrollView(
        scrollDirection = ScrollAxis.VERTICAL,
        children = listOf(
            Container(
                children = listOf(createText("Image Local"), Image(ImagePath.Local.justMobile(LOGO_BEAGLE))) +
                    ImageContentMode.values().flatMap(this::createImageWithModeAndText)
            ),
            Container(
                children = listOf(createText(text = "Image Remoto")) +
                    ImageContentMode.values().map { buildImage("NetworkImage with ImageContentMode.$it", it) }
            )
        )
    )

    private fun buildImage(title: String, mode: ImageContentMode? = null) = Container(
        children = listOf(
            createText(title),
            Image(ImagePath.Remote(BEACH_NETWORK_IMAGE), mode).applyStyle(Style(
                flex = Flex(
                    alignSelf = AlignSelf.CENTER
                ),
                size = Size(
                    width = 150.unitReal(),
                    height = 130.unitReal()
                ))
            )
        )
    )

    private fun createText(text: String) = Text(text = text, styleId = TITLE_SCREEN)

    private fun createImageWithModeAndText(mode: ImageContentMode) =
        listOf(createText("Image with contentMode = $mode"), Image(ImagePath.Local.justMobile(LOGO_BEAGLE), mode))
}