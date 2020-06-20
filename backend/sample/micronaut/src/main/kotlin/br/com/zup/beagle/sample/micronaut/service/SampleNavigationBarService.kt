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

package br.com.zup.beagle.sample.micronaut.service

import br.com.zup.beagle.widget.action.Navigate
import br.com.zup.beagle.widget.action.Route
import br.com.zup.beagle.sample.builder.NavigationBarScreenBuilder
import br.com.zup.beagle.sample.builder.NavigationBarViewScreenBuilder
import br.com.zup.beagle.sample.constants.LOGO_BEAGLE
import br.com.zup.beagle.sample.constants.NAVIGATION_BAR_STYLE
import br.com.zup.beagle.sample.constants.SCREEN_ACTION_CLICK_ENDPOINT
import br.com.zup.beagle.widget.layout.NavigationBarItem
import javax.inject.Singleton

@Singleton
class SampleNavigationBarService {
    fun createNavigationBarView() = NavigationBarViewScreenBuilder

    fun navigationBar() = NavigationBarScreenBuilder(
        titleNavigation = "NavigationBar",
        text = "NavigationBar"
    )

    fun navigationBarStyle() = NavigationBarScreenBuilder(
        titleNavigation = "NavigationBar",
        styleNavigation = NAVIGATION_BAR_STYLE,
        text = "NavigationBar with Style"
    )

    fun navigationBarWithTextAsItems() = NavigationBarScreenBuilder(
        titleNavigation = "NavigationBar",
        text = "NavigationBar with Item(Text)",
        navigationBarItems = listOf(
            NavigationBarItem(
                text = "Entrar",
                action = Navigate.PushView(Route.Remote(SCREEN_ACTION_CLICK_ENDPOINT))
            )
        )
    )

    fun navigationBarWithImageAsItem() = NavigationBarScreenBuilder(
        titleNavigation = "NavigationBar",
        text = "NavigationBar with Item(Image)",
        navigationBarItems = listOf(
            NavigationBarItem(
                text = "",
                image = LOGO_BEAGLE,
                action = Navigate.PushView(Route.Remote(SCREEN_ACTION_CLICK_ENDPOINT))
            )
        )
    )
}
