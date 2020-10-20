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

package br.com.zup.beagle.automatedtests.builders

import br.com.zup.beagle.automatedtests.constants.SCREEN_ACTION_CLICK_ENDPOINT
import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.widget.core.AlignItems
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.JustifyContent
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.ui.Text

object ClickActionScreenBuilder {
     fun build() = Screen(
        navigationBar = NavigationBar(
            title = "Action Click",
            showBackButton = true
        ),
        child = Text(
            text = "You clicked right",
            styleId = SCREEN_ACTION_CLICK_ENDPOINT
        ).applyFlex(
            flex = Flex(
                justifyContent = JustifyContent.CENTER,
                alignItems = AlignItems.CENTER
            )
        )
    )
}