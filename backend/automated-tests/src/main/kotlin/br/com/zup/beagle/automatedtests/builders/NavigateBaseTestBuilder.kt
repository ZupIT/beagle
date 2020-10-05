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

import NAVIGATE_HERE_ENDPOINT
import br.com.zup.beagle.widget.action.Navigate
import br.com.zup.beagle.widget.action.Route
import br.com.zup.beagle.widget.action.SetContext
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.layout.*
import br.com.zup.beagle.widget.ui.Button

object NavigateBaseTestBuilder {

    const val pushStackRemoteId = "PushStackRemote"

    fun build() = Screen(
        child = Container(
            onInit = listOf(
                SetContext(
                    contextId = "global",
                    value = ""
                )
            ),
            children =
            listOf(
                Button(
                    text = pushStackRemoteId,
                    onPress = listOf(
                        SetContext(
                            contextId = "global",
                            value = pushStackRemoteId
                        ),
                        Navigate.PushStack(
                            Route.Remote(NAVIGATE_HERE_ENDPOINT)
                        )
                    )
                )
            )
        )
    )
}