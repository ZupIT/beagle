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

import br.com.zup.beagle.newanalytics.ActionAnalyticsConfig
import br.com.zup.beagle.newanalytics.ActionAnalyticsProperties
import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.action.Confirm
import br.com.zup.beagle.widget.action.Navigate
import br.com.zup.beagle.widget.action.Route
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.Text

object NewAnalyticsScreenBuilder {

    fun build() = Screen(
        child = Container(listOf(
            Text("Analytics 2.0"),
            actionWithoutAnalytics(),
            actionEnabled(),
            actionEnabledWithAttributes(),
            actionDisabled(),
            onlyScreenEvent()
        ))
    )

    private fun actionWithoutAnalytics() = Button(
        text = "Alert with no specific analytics configuration",
        onPress = listOf(
            Alert(
                title = "Alert Title",
                message = "AlertMessage",
                onPressOk = navigateToNativeScreen()
            )
        )
    )

    private fun actionEnabled() = Button(
        text = "Confirm with analytics local configuration",
        onPress = listOf(
            Confirm(
                analytics = ActionAnalyticsConfig.Enabled(),
                title = "Confirm Title",
                message = "Confirm Message",
                labelOk = "Accept",
                labelCancel = "cancel",
                onPressOk = navigateToNativeScreen()
            )
        )
    ).apply {
        id = "_beagle_5"
    }

    private fun actionEnabledWithAttributes() = Button(
        text = "Alert with remote analytics configuration",
        onPress = listOf(
            Alert(
                analytics = ActionAnalyticsConfig.Enabled(
                    ActionAnalyticsProperties(attributes = listOf("message"))
                ),
                title = "Alert Title",
                message = "AlertMessage",
                onPressOk = navigateToNativeScreen()
            )
        )
    ).apply {
        id = "_beagle_6"
    }

    private fun actionDisabled() = Button(
        text = "Confirm with disabled analytics configuration",
        onPress = listOf(
            Confirm(
                analytics = ActionAnalyticsConfig.Disabled,
                title = "Confirm Title",
                message = "Confirm Message",
                labelOk = "Accept",
                labelCancel = "cancel",
                onPressOk = navigateToNativeScreen()
            )
        )
    )

    private fun onlyScreenEvent() = Button(
        text = "navigateToPage",
        onPress = listOf(Navigate.PushView(
            Route.Local(onlyScreen()),
            analytics = ActionAnalyticsConfig.Disabled
        ))
    )

    private fun onlyScreen() = Screen(
        identifier = "/analytics2-navigate",
        child = Button(
            text = "navigate to local screen",
            onPress = listOf(
                Navigate.OpenNativeRoute(
                    analytics = ActionAnalyticsConfig.Disabled,
                    route = "screen-analytics-link"
                )
            )
        )
    )

    private fun navigateToNativeScreen() = Navigate.OpenNativeRoute(
        analytics = ActionAnalyticsConfig.Disabled,
        route = "screen-analytics-link",
        shouldResetApplication = true
    )
}
