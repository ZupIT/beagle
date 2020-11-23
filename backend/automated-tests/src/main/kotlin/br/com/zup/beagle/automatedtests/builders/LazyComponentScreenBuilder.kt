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

import br.com.zup.beagle.automatedtests.constants.LAZY_COMPONENT_2_ENDPOINT
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.lazy.LazyComponent
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.Text

object LazyComponentScreenBuilder {
    fun build() = Screen(
        child = Container(
            listOf(
                Text("LazyComponent Screen"),
                LazyComponent(
                    path = LAZY_COMPONENT_2_ENDPOINT,
                    initialState = Text("Loading...")
                ),
                Button(
                    text = "Call Lazy again",
                    onPress = listOf(

                    )
                    )
            )
        )
    )
}