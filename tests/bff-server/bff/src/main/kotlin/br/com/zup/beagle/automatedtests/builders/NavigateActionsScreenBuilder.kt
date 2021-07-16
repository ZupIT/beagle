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

import br.com.zup.beagle.automatedtests.constants.GLOBAL_TEXT_ENDPOINT
import br.com.zup.beagle.automatedtests.constants.GLOBAL_TEXT_EXPRESSION_ENDPOINT
import br.com.zup.beagle.widget.action.Navigate
import br.com.zup.beagle.widget.action.Route
import br.com.zup.beagle.widget.action.SetContext
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.Text

object NavigateActionsScreenBuilder {

    const val PUSH_STACK_REMOTE = "PushStackRemote"
    const val PUSH_STACK_REMOTE_EXPRESSION = "PushStackRemoteExpression"
    const val PUSH_STACK_REMOTE_FAILURE = "PushStackRemoteFailure"
    const val PUSH_STACK_REMOTE_EXPRESSION_FAILURE = "PushStackRemoteExpressionFailure"
    const val PUSH_VIEW_REMOTE = "PushViewRemote"
    const val PUSH_VIEW_REMOTE_EXPRESSION = "PushViewRemoteExpression"
    const val pushViewRemoteFailure = "PushViewRemoteFailure"
    const val pushViewRemoteExpressionFailure = "PushViewRemoteExpressionFailure"
    const val REGISTER_CONTROLLER_ID = "otherController"
    const val NAVIGATE_RESET_ENDPOINT_WRONG = "<3"

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
                pushButtonsFail(),
                resetToOtherServerDrivenActivity()
            )
        )
    )

    private fun resetToOtherServerDrivenActivity(): Container = Container(
        listOf(
            Button(
                text = "ResetStackOtherSDAFailsToShowButton",
                onPress = listOf(
                    Navigate.ResetStack(
                        Route.Remote(url = NAVIGATE_RESET_ENDPOINT_WRONG),
                        controllerId = REGISTER_CONTROLLER_ID)
                )
            ),
            Button(
                text = "ResetApplicationOtherSDAFailsToShowButton",
                onPress = listOf(
                    Navigate.ResetApplication(
                        Route.Remote(url = NAVIGATE_RESET_ENDPOINT_WRONG),
                        controllerId = REGISTER_CONTROLLER_ID)
                )
            ),
            Button(
                text = "ResetStackSameSDA",
                onPress = listOf(
                    Navigate.ResetStack(
                        Route.Remote(url = NAVIGATE_RESET_ENDPOINT_WRONG))
                )
            ),
            Button(
                text = "ResetApplicationSameSDA",
                onPress = listOf(
                    Navigate.ResetApplication(
                        Route.Remote(url = NAVIGATE_RESET_ENDPOINT_WRONG))
                )
            )
        ))

    private fun pushButtonsSuccess(): Container = Container(
        listOf(
            Button(
                text = PUSH_STACK_REMOTE,
                onPress = listOf(
                    SetContext(
                        contextId = "global",
                        value = PUSH_STACK_REMOTE + "Screen"
                    ),
                    Navigate.PushStack(
                        Route.Remote(GLOBAL_TEXT_ENDPOINT)
                    )
                )
            ),
            Button(
                text = PUSH_VIEW_REMOTE,
                onPress = listOf(
                    SetContext(
                        contextId = "global",
                        value = PUSH_VIEW_REMOTE + "Screen"
                    ),
                    Navigate.PushView(
                        Route.Remote(GLOBAL_TEXT_ENDPOINT)
                    )
                )
            ),
            Button(
                text = PUSH_STACK_REMOTE_EXPRESSION,
                onPress = listOf(
                    SetContext(
                        contextId = "global",
                        value = PUSH_STACK_REMOTE_EXPRESSION + "Screen"
                    ),
                    Navigate.PushStack(
                        Route.Remote(GLOBAL_TEXT_EXPRESSION_ENDPOINT)
                    )
                )
            ),
            Button(
                text = PUSH_VIEW_REMOTE_EXPRESSION,
                onPress = listOf(
                    SetContext(
                        contextId = "global",
                        value = PUSH_VIEW_REMOTE_EXPRESSION + "Screen"
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
                text = PUSH_STACK_REMOTE_FAILURE,
                onPress = listOf(
                    SetContext(
                        contextId = "global",
                        value = PUSH_STACK_REMOTE_FAILURE + "Screen"
                    ),
                    Navigate.PushStack(
                        Route.Remote("<3"), controllerId = REGISTER_CONTROLLER_ID)
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
                text = PUSH_STACK_REMOTE_EXPRESSION_FAILURE,
                onPress = listOf(
                    SetContext(
                        contextId = "global",
                        value = PUSH_STACK_REMOTE_EXPRESSION_FAILURE + "Screen"
                    ),
                    Navigate.PushStack(
                        Route.Remote("@{global}"), controllerId = REGISTER_CONTROLLER_ID
                    )
                )
            )
        )
    )
}