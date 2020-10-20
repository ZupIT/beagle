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

import br.com.zup.beagle.automatedtests.constants.NAVIGATE_ACTIONS_ENDPOINT
import br.com.zup.beagle.automatedtests.constants.NAVIGATE_RESET_ENDPOINT
import br.com.zup.beagle.widget.action.Navigate
import br.com.zup.beagle.widget.action.Route
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.context.expressionOf
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.Container

import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.Text

object GlobalSampleTextScreenBuilder {

    val FALLBACK_SCREEN = NavigateActionsResetScreenBuilder.build()

    fun build() = Screen(
        child = Container(
            context = ContextData(
                id = "relativePath",
                value = NAVIGATE_RESET_ENDPOINT
            ),
            children =
            listOf(
                Text(text = "Sample Screen"),
                Text(text = expressionOf("@{global}")),
                popButtons(),
                resetButtons()
            )
        )
    )

    private fun resetButtons(): Container = Container(
        listOf(
            Button(
                text = "ResetStack",
                onPress = listOf(
                    Navigate.ResetStack(
                        Route.Remote(url = NAVIGATE_RESET_ENDPOINT))
                )
            ),
            Button(
                text = "ResetApplication",
                onPress = listOf(
                    Navigate.ResetApplication(
                        Route.Remote(url = NAVIGATE_RESET_ENDPOINT))
                )
            ),
            Button(
                text = "ResetStackExpression",
                onPress = listOf(
                    Navigate.ResetStack(
                        Route.Remote(url = "@{relativePath}"))
                )
            ),
            Button(
                text = "ResetApplicationExpression",
                onPress = listOf(
                    Navigate.ResetApplication(
                        Route.Remote(url = "@{relativePath}"))
                )
            ),
            Button(
                text = "ResetStackExpressionFallback",
                onPress = listOf(
                    Navigate.ResetStack(
                        Route.Remote(url = "@{global}", fallback = FALLBACK_SCREEN))
                )
            ),
            Button(
                text = "ResetApplicationExpressionFallback",
                onPress = listOf(
                    Navigate.ResetApplication(
                        Route.Remote(url = "@{global}", fallback = FALLBACK_SCREEN))
                )
            )
        )
    )

    private fun popButtons(): Container = Container(
        listOf(
            Button(
                text = "PopToViewInvalidRoute",
                onPress = listOf(
                    Navigate.PopToView(route = "<3")
                )
            ),
            Button(
                text = "PopToView",
                onPress = listOf(
                    Navigate.PopToView(
                        NAVIGATE_ACTIONS_ENDPOINT
                    )
                )
            ),
            Button(
                text = "PopStack",
                onPress = listOf(
                    Navigate.PopStack()
                )
            ),
            Button(
                text = "PopView",
                onPress = listOf(
                    Navigate.PopView()
                )
            )
        )
    )
}

