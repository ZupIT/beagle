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

import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.sample.constants.SCREEN_SAFE_AREA_FALSE_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_SAFE_AREA_TRUE_ENDPOINT
import br.com.zup.beagle.widget.action.Navigate
import br.com.zup.beagle.widget.action.Route
import br.com.zup.beagle.widget.core.AlignSelf
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.layout.*
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.Text

object SafeAreaBuilder: ScreenBuilder {
    override fun build() = Screen(
        navigationBar = NavigationBar(
            title = "Beagle Tab View",
            showBackButton = true
        ),
        child = Container(
            listOf(
                Button(
                    text = "Safe Area True",
                    onPress = listOf(Navigate.PushView(Route.Remote(SCREEN_SAFE_AREA_TRUE_ENDPOINT, true)))

                ),
                Button(
                    text = "Safe Area True",
                    onPress = listOf(Navigate.PushView(Route.Remote(SCREEN_SAFE_AREA_FALSE_ENDPOINT, true)))

                )
            )
        )
    )

    fun screenSafeAreaTrue(): Screen = createScreen(true)

    fun screenSafeAreaFalse(): Screen = createScreen(false)

    fun createScreen(safeArea: Boolean): Screen = Screen(
        navigationBar = NavigationBar(
            title = "Beagle Safe Area",
            showBackButton = true
        ),
        child = Container(
            listOf(
                Text("Safe area "+ safeArea.toString()).applyFlex(flex = Flex(alignSelf = AlignSelf.CENTER))
            )
        ).applyStyle(
            Style(
                backgroundColor = "#b7efcd"
            )
        ),
        safeArea = SafeArea(
            top = safeArea,
            bottom = safeArea,
            leading = safeArea,
            trailing = safeArea
        )
    )
}