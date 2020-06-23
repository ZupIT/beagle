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

package br.com.zup.beagle.sample.compose.quality

import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.sample.beaglecustomaction.BeagleAlertAction
import br.com.zup.beagle.sample.constants.PATH_SCREEN_DEEP_LINK_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_ACTION_CLICK_ENDPOINT
import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.action.Confirm
import br.com.zup.beagle.widget.action.Navigate
import br.com.zup.beagle.widget.action.Route
import br.com.zup.beagle.widget.action.SendRequest
import br.com.zup.beagle.widget.core.AlignSelf
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.layout.ComposeComponent
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScrollView
import br.com.zup.beagle.widget.navigation.Touchable
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.Text

object ComposeActionScreenQuality: ComposeComponent {
    override fun build() = ScrollView(
        children = listOf(
            getAlertAction(),
            getNavigateWithPath(),
            getNavigateWithScreen(),
            getNavigateWithPathScreen(),
            getNavigateWithPrefetch(),
            getNavigateWithDeepLink(),
            getSendRequestAction(),
            getConfirmAction(),
            getOpenExternalURLAction(),
            getCustomAction()
        )
    )

    private fun getAlertAction() = Container(
        children = listOf(
            Text("Action dialog"),
            Touchable(
                action = Alert(
                    title = "Some",
                    message = "Action",
                    labelOk = "OK"
                ),
                child = Text("Click me!").applyFlex(
                    Flex(
                        alignSelf = AlignSelf.CENTER
                    )
                )
            )
        )
    )

    private fun getNavigateWithPath() = Container(
        children = listOf(
            Text("Navigate with PushView Route Remote"),
            Button(
                onPress = listOf(Navigate.PushView(Route.Remote(route = SCREEN_ACTION_CLICK_ENDPOINT))),
                text = "Click me!"
            )
        )
    )

    private fun getNavigateWithScreen() = Container(
        children = listOf(
            Text("Navigate with PushView Route Local"),
            Button(
                onPress = listOf(Navigate.PushView(Route.Local(Screen(
                    navigationBar = NavigationBar(
                        "Navigate with screen",
                        showBackButton = true
                    ),
                    child = Text("Hello Screen from Navigate")
                ))
                )),
                text = "Click me!"
            )
        )
    )

    private fun getNavigateWithPathScreen() = Container(
        children = listOf(
            Text("Navigate with PushView Route Remote with fallback"),
            Button(
                onPress = listOf(Navigate.PushView(Route.Remote(route = "",
                    fallback = Screen(
                        navigationBar = NavigationBar(
                            "Navigate with screen",
                            showBackButton = true
                        ),
                        child = Text("Hello Screen from Navigate")
                    )
                ))
                ),
                text = "Click me!"
            )
        )
    )

    private fun getNavigateWithPrefetch() = Container(
        children = listOf(
            Text("Navigate with PushView Route Remote with ShouldPrefetch"),
            Button(
                onPress = listOf(Navigate.PushView(Route.Remote(shouldPrefetch = true,
                    route = SCREEN_ACTION_CLICK_ENDPOINT))),
                text = "Click me!"
            )
        )
    )

    private fun getNavigateWithDeepLink() = Container(
        children = listOf(
            Text("Navigate with DeepLink"),
            Button(
                onPress = listOf(Navigate.OpenNativeRoute(
                    route = PATH_SCREEN_DEEP_LINK_ENDPOINT,
                    data = mapOf("data" to "for", "native" to "view")
                )),
                text = "Click me!"
            )
        )
    )

    private fun getSendRequestAction() = Container(
        children = listOf(
            Text("Send request action"),
            Button(
                onPress = listOf(SendRequest(url = SCREEN_ACTION_CLICK_ENDPOINT, onSuccess = Alert(
                    title = "Success",
                    message = "Action",
                    labelOk = "OK"
                ),
                    onError = Alert(
                        title = "Error",
                        message = "Action",
                        labelOk = "OK"
                    ),
                    onFinish = Alert(
                        title = "Finish",
                        message = "Action",
                        labelOk = "OK"
                    )
                )),
                text = "Click me!"
            )
        )
    )

    private fun getConfirmAction() = Container(
        children = listOf(
            Text("Confirm action"),
            Button(
                onPress = listOf(Confirm(
                    title = "Test confirm action",
                    message = "Action",
                    labelOk = "OK",
                    labelCancel = "Cancel",
                    onPressCancel = Alert(
                        title = "Finish",
                        message = "Action",
                        labelOk = "OK"
                    ),
                    onPressOk = Alert(
                        title = "Finish",
                        message = "Action",
                        labelOk = "OK"
                    )
                )),
                text = "Click me!"
            )
        )
    )

    private fun getOpenExternalURLAction() = Container(
        children = listOf(
            Text("Open External URL Action"),
            Button(
                onPress = listOf(Navigate.OpenExternalURL("https://www.zup.com.br/")),
                text = "Click me!"
            )
        )
    )

    private fun getCustomAction() = Container(
        children = listOf(
            Text("Custom Action"),
            Button(
                text = "Ola Beagle",
                onPress = listOf(
                    BeagleAlertAction(
                        title = "Ola Beagle",
                        message = "Funcionou!"
                    )
                )
            )
        )
    )
}