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

import GLOBAL_TEXT_ENDPOINT
import GLOBAL_TEXT_EXPRESSION_ENDPOINT
import br.com.zup.beagle.widget.action.Navigate
import br.com.zup.beagle.widget.action.Route
import br.com.zup.beagle.widget.action.SetContext
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.Text

object NavigateActionsScreenBuilder {

    const val pushStackRemote = "PushStackRemote"
    const val pushStackRemoteExpression = "PushStackRemoteExpression"
    const val pushStackRemoteFailure = "PushStackRemoteFailure"
    const val pushStackRemoteExpressionFailure = "PushStackRemoteExpressionFailure"
    const val pushViewRemote = "PushViewRemote"
    const val pushViewRemoteExpression = "PushViewRemoteExpression"
    const val pushViewRemoteFailure = "PushViewRemoteFailure"
    const val pushViewRemoteExpressionFailure = "PushViewRemoteExpressionFailure"

    fun build() = Screen(
        child = Container(
            context = ContextData(
                id = "relativePath",
                value = "/global-text"
            ),
            onInit = listOf(
                SetContext(
                    contextId = "global",
                    value = ""
                )
            ),
            children =
            listOf(
                Text(text = "Navigation Screen"),
                pushButtonsSuccess(),
                pushButtonsFail()
            )
        )
    )

    private fun pushButtonsSuccess(): Container = Container(
        listOf(
            Button(
                text = pushStackRemote,
                onPress = listOf(
                    SetContext(
                        contextId = "global",
                        value = pushStackRemote + "Screen"
                    ),
                    Navigate.PushStack(
                        Route.Remote(GLOBAL_TEXT_ENDPOINT)
                    )
                )
            ),
            Button(
                text = pushViewRemote,
                onPress = listOf(
                    SetContext(
                        contextId = "global",
                        value = pushViewRemote + "Screen"
                    ),
                    Navigate.PushView(
                        Route.Remote(GLOBAL_TEXT_ENDPOINT)
                    )
                )
            ),
            Button(
                text = pushStackRemoteExpression,
                onPress = listOf(
                    SetContext(
                        contextId = "global",
                        value = pushStackRemoteExpression + "Screen"
                    ),
                    Navigate.PushStack(
                        Route.Remote(GLOBAL_TEXT_EXPRESSION_ENDPOINT)
                    )
                )
            ),
            Button(
                text = pushViewRemoteExpression,
                onPress = listOf(
                    SetContext(
                        contextId = "global",
                        value = pushViewRemoteExpression + "Screen"
                    ),
                    Navigate.PushView(
                        Route.Remote(GLOBAL_TEXT_EXPRESSION_ENDPOINT)
                    )
                )
            )
        )
    )

    private fun pushButtonsFail(): Container = Container(
        listOf(
            Button(
                text = pushStackRemoteFailure,
                onPress = listOf(
                    SetContext(
                        contextId = "global",
                        value = pushStackRemoteFailure + "Screen"
                    ),
                    Navigate.PushStack(
                        Route.Remote("<3")
                    )
                )
            ),
            Button(
                text = pushViewRemoteFailure,
                onPress = listOf(
                    SetContext(
                        contextId = "global",
                        value = pushViewRemoteFailure + "Screen"
                    ),
                    Navigate.PushView(
                        Route.Remote("<3")
                    )
                )
            ),
            Button(
                text = pushViewRemoteExpressionFailure,
                onPress = listOf(
                    SetContext(
                        contextId = "global",
                        value = pushViewRemoteExpressionFailure + "Screen"
                    ),
                    Navigate.PushView(
                        Route.Remote("@{global}")
                    )
                )
            ),
            Button(
                text = pushStackRemoteExpressionFailure,
                onPress = listOf(
                    SetContext(
                        contextId = "global",
                        value = pushStackRemoteExpressionFailure + "Screen"
                    ),
                    Navigate.PushStack(
                        Route.Remote("@{global}")
                    )
                )
            )
        )
    )
}