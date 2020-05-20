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

package br.com.zup.beagle.sample.widget

import br.com.zup.beagle.enums.BeaglePlatform
import br.com.zup.beagle.widget.core.ComposeComponent
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.JustifyContent
import br.com.zup.beagle.widget.Widget
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.Text

class CustomComponent(
    override val beaglePlatform: BeaglePlatform = BeaglePlatform.ALL
) : ComposeComponent() {
    override fun build(): Widget = Container(
        children = listOf(
            Button("Text 1"),
            Text("Text 1")
        )
    ).applyFlex(flex = Flex(
        justifyContent = JustifyContent.CENTER
    )
    )
}