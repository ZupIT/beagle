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

import br.com.zup.beagle.sample.compose.quality.ComposeTabViewQuality
import br.com.zup.beagle.sample.compose.sample.ComposeSampleTabView
import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.NavigationBarItem
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScreenBuilder
import br.com.zup.beagle.widget.ui.ImagePath

class TabViewScreenBuilder(val qaFlag: Boolean) : ScreenBuilder {

    override fun build() = Screen(
        navigationBar = NavigationBar(
            title = "Beagle Tab View",
            showBackButton = true,
            navigationBarItems = listOf(
                NavigationBarItem(
                    text = "",
                    image = ImagePath.Local.justMobile("informationImage"),
                    action = Alert(
                        title = "TabView",
                        message = " Is a component that will make the navigation between views. It may happen by " +
                            "sliding through screens or by clicking at the tabs shown. ",
                        labelOk = "OK"
                    )
                )
            )
        ),
        child = if (qaFlag) ComposeTabViewQuality else ComposeSampleTabView
    )
}
