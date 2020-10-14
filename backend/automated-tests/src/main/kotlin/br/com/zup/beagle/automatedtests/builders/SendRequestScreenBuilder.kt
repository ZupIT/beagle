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

import br.com.zup.beagle.widget.action.*
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.Text

data class RequestButtonTitle(
    val success: String,
    val error: String
)

object SendRequestScreenBuilder {
    fun build() = Screen(
        context = ContextData(
            id = "buttonTitle",
            value = RequestButtonTitle(
                success = "onFinish with success",
                error = "onFinish with error"
            )
        ),
        child = Container(
            children =
            listOf(
                Text(
                    text = "Send Request Screen"
                ),
                Container(
                    listOf(
                        Button(
                            text = "request with success",
                            onPress = listOf(
                                SendRequest(
                                    url = "http://10.0.2.2:8080/send-request",
                                    method = RequestActionMethod.GET,
                                    onSuccess = listOf(
                                        Alert(
                                            title = "Success",
                                            message = "@{onSuccess.data}"
                                        )),
                                    onError = listOf(
                                        Alert(
                                            title = "Error",
                                            message = "@{onError.data}"
                                        )
                                    )
                                )
                            )
                        ),
                        Button(
                            text = "request with error",
                            onPress = listOf(
                                SendRequest(
                                    url = "http://localhost:8080",
                                    method = RequestActionMethod.GET,
                                    onSuccess = listOf(
                                        Alert(
                                            title = "Success",
                                            message = "@{onSuccess.data}"
                                        )),
                                    onError = listOf(
                                        Alert(
                                            title = "Error",
                                            message = "@{onError.data}"
                                        )
                                    )
                                )
                            )
                        )
                    )
                ),
                Container(
                    listOf(
                        Button(
                            text = "@{buttonTitle.success}",
                            onPress = listOf(
                                SendRequest(
                                    url = "http://localhost:8080/send-request",
                                    method = RequestActionMethod.GET,
                                    onFinish = listOf(
                                        SetContext(
                                            contextId = "buttonTitle",
                                            value = RequestButtonTitle(
                                                success = "didFinish",
                                                error = "onFinish with error"
                                            )
                                        )
                                    )
                                )
                            )
                        ),
                        Button(
                            text = "@{buttonTitle.error}",
                            onPress = listOf(
                                SendRequest(
                                    url = "http://localhost:8080",
                                    method = RequestActionMethod.GET,
                                    onFinish = listOf(
                                        SetContext(
                                            contextId = "buttonTitle",
                                            value = RequestButtonTitle(
                                                success = "onFinish with success",
                                                error = "didFinish"
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
    )
}