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

import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.sample.constants.BEACH_NETWORK_IMAGE
import br.com.zup.beagle.widget.core.AlignContent
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.layout.ComposeComponent
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.ui.Image
import br.com.zup.beagle.widget.ui.ImagePath
import br.com.zup.beagle.widget.ui.TabItem
import br.com.zup.beagle.widget.ui.TabView
import br.com.zup.beagle.widget.ui.Text

object ComposeSampleTabView: ComposeComponent {
    override fun build() = TabView(
        children = listOf(tab1, tab2, tab3, tab4)
    )

    private val tab1 = TabItem(
        title = "Tab 1",
        child = Container(
            children = listOf(
                Text("Text1 Tab 2"),
                Image(ImagePath.Remote(BEACH_NETWORK_IMAGE)),
                Text("Text2 Tab 2")
            )
        ).applyFlex(Flex(alignContent = AlignContent.CENTER))
    )

    private val tab2 = TabItem(
        title = "Tab 2",
        child = Container(
            children = listOf(
                Text("Text1 Tab 2"),
                Text("Text2 Tab 2")
            )
        ).applyFlex(Flex(alignContent = AlignContent.CENTER))
    )

    private val tab3 = TabItem(
        title = "Tab 3",
        child = Container(
            children = listOf(
                Text("Text1 Tab 3"),
                Text("Text2 Tab 3")
            )
        ).applyFlex(Flex(alignContent = AlignContent.CENTER))
    )

    private val tab4 = TabItem(
        title = "Tab 4",
        icon = ImagePath.Local.justMobile("beagle"),
        child = Container(
            children = listOf(
                Text("Text1 Tab 4"),
                Text("Text2 Tab 4")
            )
        ).applyFlex(Flex(alignContent = AlignContent.CENTER))
    )
}