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

package br.com.zup.beagle.sample.builder

import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.sample.constants.PATH_LAZY_COMPONENT_ENDPOINT
import br.com.zup.beagle.widget.core.AlignSelf
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.JustifyContent
import br.com.zup.beagle.widget.core.ScrollAxis
import br.com.zup.beagle.widget.layout.*
import br.com.zup.beagle.widget.lazy.LazyComponent
import br.com.zup.beagle.widget.ui.Text

object LazyComponentScreenBuilder : ScreenBuilder {
    override fun build() = Screen(
        navigationBar = NavigationBar(
            title = "Beagle LazyComponent",
            showBackButton = true,
            navigationBarItems = listOf(
                NavigationBarItem(
                    text = "",
                    image = "informationImage",
                    action = Alert(
                        title = "Lazy Component",
                        message = "A widget that implements loading.",
                        labelOk = "OK"
                    )
                )
            )
        ),
        child =ScrollView(
            scrollDirection = ScrollAxis.VERTICAL,
            children = listOf(
                LazyComponent(
                    path = "https://run.mocky.io/v3/c5def28e-0c3c-4028-a7a7-4ba7166316d9",
                    initialState = Text("Loading...").applyFlex(
                        flex = Flex(
                            justifyContent = JustifyContent.CENTER,
                            alignSelf = AlignSelf.CENTER
                        )
                    )
                ),
                LazyComponent(
                    path = "https://run.mocky.io/v3/c5def28e-0c3c-4028-a7a7-4ba7166316d9",
                    initialState = Text("Loading...").applyFlex(
                        flex = Flex(
                            justifyContent = JustifyContent.CENTER,
                            alignSelf = AlignSelf.CENTER
                        )
                    )
                )
            )
        )
    )
}
