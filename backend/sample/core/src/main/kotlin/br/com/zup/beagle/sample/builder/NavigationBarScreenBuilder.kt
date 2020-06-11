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

import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.sample.constants.TEXT_FONT_MAX
import br.com.zup.beagle.widget.core.AlignItems
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.NavigationBarItem
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScreenBuilder
import br.com.zup.beagle.widget.ui.Text

class NavigationBarScreenBuilder(
    private val titleNavigation: String,
    private val styleNavigation: String? = null,
    private val text: String,
    private val navigationBarItems: List<NavigationBarItem>? = null
) : ScreenBuilder {
    override fun build() = Screen(
        navigationBar = NavigationBar(
            title = this.titleNavigation,
            styleId = this.styleNavigation,
            showBackButton = true,
            navigationBarItems = this.navigationBarItems
        ),
        child = this.createBeagleText(this.text)
    )

    private fun createBeagleText(text: String) = Text(text = text, styleId = TEXT_FONT_MAX)
        .applyFlex(
            flex = Flex(
                alignItems = AlignItems.CENTER
            )
        )
}
