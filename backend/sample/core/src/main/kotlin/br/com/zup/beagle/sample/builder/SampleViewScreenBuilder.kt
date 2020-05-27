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

import br.com.zup.beagle.action.Navigate
import br.com.zup.beagle.sample.constants.PATH_SAMPLE_VIEW_ENDPOINT
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.JustifyContent
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.NavigationBarItem
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScreenBuilder
import br.com.zup.beagle.widget.ui.Button

object SampleViewScreenBuilder : ScreenBuilder {
    override fun build() = Screen(
        navigationBar = NavigationBar(
            title = "Sample Bar",
            style = "Style.Default",
            navigationBarItems = listOf(
                NavigationBarItem(
                    text = "First",
                    image = "delete",
                    action = Navigate.PopToView(PATH_SAMPLE_VIEW_ENDPOINT)
                ),
                NavigationBarItem(
                    text = "Second",
                    image = "question",
                    action = Navigate.PopView()
                )
            )
        ),
        child = Button("").applyFlex(Flex(justifyContent = JustifyContent.CENTER))
    )
}