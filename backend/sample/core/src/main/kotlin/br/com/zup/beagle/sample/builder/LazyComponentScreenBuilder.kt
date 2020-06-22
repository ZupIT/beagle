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
                    path = "https://run.mocky.io/v3/d49febe5-8577-45f4-be3d-86269288166f",
                    initialState = Text("Loading...").applyFlex(
                        flex = Flex(
                            justifyContent = JustifyContent.CENTER,
                            alignSelf = AlignSelf.CENTER
                        )
                    )
                ),
                LazyComponent(
                    path = "https://run.mocky.io/v3/b3027829-8800-4ad0-85e9-137d83804b28",
                    initialState = Text("Loading...").applyFlex(
                        flex = Flex(
                            justifyContent = JustifyContent.CENTER,
                            alignSelf = AlignSelf.CENTER
                        )
                    )
                ),
                LazyComponent(
                    path = "https://run.mocky.io/v3/f6753846-ecbb-45a6-af76-bc771bb35b5b",
                    initialState = Text("Loading...").applyFlex(
                        flex = Flex(
                            justifyContent = JustifyContent.CENTER,
                            alignSelf = AlignSelf.CENTER
                        )
                    )
                ),
                LazyComponent(
                    path = "https://run.mocky.io/v3/557c4a76-8c48-4e6d-ae72-735c66a718a9",
                    initialState = Text("Loading...").applyFlex(
                        flex = Flex(
                            justifyContent = JustifyContent.CENTER,
                            alignSelf = AlignSelf.CENTER
                        )
                    )
                ),
                LazyComponent(
                    path = "https://run.mocky.io/v3/8a232bf1-eb34-45d2-84b8-a2a0f7e68072",
                    initialState = Text("Loading...").applyFlex(
                        flex = Flex(
                            justifyContent = JustifyContent.CENTER,
                            alignSelf = AlignSelf.CENTER
                        )
                    )
                ),
                LazyComponent(
                    path = "https://run.mocky.io/v3/c3642218-3de9-4c70-b0a9-dcfbe1e94ca0",
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
