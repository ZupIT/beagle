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

package br.com.zup.beagle.sample.builder

import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.analytics.ClickEvent
import br.com.zup.beagle.analytics.ScreenEvent
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.Widget
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScreenBuilder
import br.com.zup.beagle.widget.navigation.Touchable
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.Text

object AnalyticsScreenBuilder : ScreenBuilder {

    override fun build() = Screen(
        navigationBar = NavigationBar(
            title = "Analytics Widgets",
            showBackButton = true
        ),
        child = Container(
            children = listOf(
                createButton(),
                createTouchable()
            )
        ),
        screenAnalyticsEvent = ScreenEvent(
            screenName = "AnalyticsScreen"
        )
    )

    private fun createTouchable(): ServerDrivenComponent {
        val text = Text(
            text = "Touchable with Click Analytics Event"
        )
        return Touchable(
            action = Alert(
                title = "title",
                message = "message",
               labelOk = "Close"
            ),
            child = text,
            clickAnalyticsEvent = ClickEvent(
                category = "touchable",
                label = "label-touchable",
                value = "value-touchable"
            )
        )
    }

    private fun createButton(): Widget {
        val button = Button(
            text = "Button with Click Analytics Event",
            onPress = listOf(Alert(
                title = "title",
                message = "message",
               labelOk = "Close"
            )),
            clickAnalyticsEvent = ClickEvent(
                category = "button",
                label = "label-button",
                value = "value-button"
            )
        )
        return button
    }

}