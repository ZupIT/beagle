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

import br.com.zup.beagle.automatedtests.constants.NAVIGATION_BAR_STYLE_DEFAULT
import br.com.zup.beagle.automatedtests.constants.PATH_SCREEN_DEEP_LINK_ENDPOINT
import br.com.zup.beagle.automatedtests.constants.SCREEN_ACTION_CLICK_ENDPOINT
import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.action.Navigate
import br.com.zup.beagle.widget.action.Route
import br.com.zup.beagle.widget.action.SendRequest
import br.com.zup.beagle.widget.action.Confirm
import br.com.zup.beagle.widget.core.AlignSelf
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.NavigationBarItem
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.ScrollView
import br.com.zup.beagle.widget.navigation.Touchable
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.ImagePath
import br.com.zup.beagle.widget.ui.Text

object ActionScreenBuilder {
    fun build() = Screen(
        navigationBar = NavigationBar(
            title = "Beagle Action",
            showBackButton = true,
            styleId = NAVIGATION_BAR_STYLE_DEFAULT,
            navigationBarItems = listOf(
                NavigationBarItem(
                    text = "",
                    image = ImagePath.Local.justMobile("informationImage"),
                    action = Alert(
                        title = "Action",
                        message = "This class handles transition actions between screens in the application. ",
                        labelOk = "OK"
                    )
                )
            )
        ),
        child = ScrollView(
            children = listOf(
                getAlertAction(),
                getNavigateWithPath(),
                getNavigateWithScreen(),
                getNavigateWithPathScreen(),
                getNavigateWithPrefetch(),
                getNavigateWithDeepLink(),
                getSendRequestAction(),
                getConfirmAction()
            )
        )
    )

    private fun getAlertAction() = Container(
        children = listOf(
            Text("Action dialog"),
            Touchable(
                onPress = listOf(createAlert("Some")),
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
            Text("Navigate with path"),
            Button(
                onPress = listOf(Navigate.PushView(Route.Remote(SCREEN_ACTION_CLICK_ENDPOINT))),
                text = "Click me!"
            )
        )
    )

    private fun getNavigateWithScreen() = Container(
        children = listOf(
            Text("Navigate with screen"),
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
            Text("Navigate with path and screen"),
            Button(
                onPress = listOf(Navigate.PushView(Route.Local(Screen(
                    navigationBar = NavigationBar(
                        "Navigate with path and screen",
                        showBackButton = true
                    ),
                    child = Text("Hello Screen from Navigate")
                ))
                )),
                text = "Click me!"
            )
        )
    )

    private fun getNavigateWithPrefetch() = Container(
        children = listOf(
            Text("Navigate with prefetch"),
            Button(
                onPress = listOf(Navigate.PushView(Route.Remote(shouldPrefetch = true,
                    url = SCREEN_ACTION_CLICK_ENDPOINT
                ))),
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
                onPress = listOf(SendRequest(
                    url = SCREEN_ACTION_CLICK_ENDPOINT,
                    onSuccess = listOf(
                        createAlert("Success")
                    ),
                    onError = listOf(
                        createAlert("Error")
                    ),
                    onFinish = listOf(
                        createAlert("Finish")
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
                    onPressCancel = createAlert("Cancel"),
                    onPressOk = createAlert("Finish")
                )),
                text = "Click me!"
            )
        )
    )

    private fun createAlert(title: String): Alert {
        return Alert(
            title = title,
            message = "Action",
            labelOk = "OK"
        )
    }
}