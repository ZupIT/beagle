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

import br.com.zup.beagle.automatedtests.constants.LAZY_FAILURE_ENDPOINT
import br.com.zup.beagle.automatedtests.constants.LAZY_SUCCESS_ENDPOINT
import br.com.zup.beagle.widget.action.Navigate
import br.com.zup.beagle.widget.action.Route
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.Text

object LazyComponentScreenBuilder {
    fun build() = Screen(
        child = Container(
            listOf(
                Text("LazyComponent Screen"),
                Button(
                    text = "Call lazy successful component screen",
                    onPress = listOf(
                        Navigate.PushStack(
                            Route.Remote(LAZY_SUCCESS_ENDPOINT)
                        )
                    )
                ),
                Button(
                    text = "Call lazy failure component screen",
                    onPress = listOf(
                        Navigate.PushStack(
                            Route.Remote(LAZY_FAILURE_ENDPOINT)
                        )
                    )
                )
            )
        )
    )
}