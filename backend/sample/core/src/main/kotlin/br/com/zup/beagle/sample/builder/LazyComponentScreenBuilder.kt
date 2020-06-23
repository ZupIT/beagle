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
import br.com.zup.beagle.sample.compose.quality.ComposeLazyComponentQuality
import br.com.zup.beagle.sample.compose.sample.ComposeSampleLazyComponent
import br.com.zup.beagle.widget.layout.*

class LazyComponentScreenBuilder(val qaFlag: Boolean) : ScreenBuilder {
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
        child = if (qaFlag) ComposeLazyComponentQuality else ComposeSampleLazyComponent
    )
}
