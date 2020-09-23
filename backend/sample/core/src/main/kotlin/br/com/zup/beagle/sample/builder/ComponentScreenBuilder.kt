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

import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.sample.constants.ACCESSIBILITY_SCREEN_ENDPOINT
import br.com.zup.beagle.sample.constants.BUTTON_STYLE
import br.com.zup.beagle.sample.constants.CUSTOM_PLATFORM_SAMPLE_ENDPOINT
import br.com.zup.beagle.sample.constants.NAVIGATION_TYPE_ENDPOINT
import br.com.zup.beagle.sample.constants.PLATFORM_SAMPLE_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_ACTION_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_ANALYTICS_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_BFF_NETWORK_IMAGE_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_BUILDER_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_BUTTON_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_COMPOSE_COMPONENT_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_CONTEXT_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_FORM_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_IMAGE_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_LAZY_COMPONENT_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_LIST_VIEW_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_NAVIGATION_BAR_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_NETWORK_IMAGE_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_PAGE_VIEW_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_SAFE_AREA_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_SCROLL_VIEW_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_SIMPLE_FORM_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_TAB_BAR_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_TAB_VIEW_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_TEXT_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_TEXT_INPUT_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_TOUCHABLE_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_WEB_VIEW_ENDPOINT
import br.com.zup.beagle.widget.action.Navigate
import br.com.zup.beagle.widget.action.Route
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.ScrollAxis
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScreenBuilder
import br.com.zup.beagle.widget.layout.ScrollView
import br.com.zup.beagle.widget.ui.Button

object ComponentScreenBuilder : ScreenBuilder {
    override fun build() = Screen(
        navigationBar = NavigationBar(
            title = "Choose a Component",
            showBackButton = true
        ),
        child = ScrollView(
            scrollDirection = ScrollAxis.VERTICAL,
            children = listOf(
                createMenu("Button", SCREEN_BUTTON_ENDPOINT),
                createMenu("Text", SCREEN_TEXT_ENDPOINT),
                createMenu("Local Image", SCREEN_IMAGE_ENDPOINT),
                createMenu("Remote Image", SCREEN_NETWORK_IMAGE_ENDPOINT),
                createMenu("Bff Remote Image", SCREEN_BFF_NETWORK_IMAGE_ENDPOINT),
                createMenu("TabView", SCREEN_TAB_VIEW_ENDPOINT),
                createMenu("TabBar", SCREEN_TAB_BAR_ENDPOINT),
                createMenu("ListView", SCREEN_LIST_VIEW_ENDPOINT),
                createMenu("ScrollView", SCREEN_SCROLL_VIEW_ENDPOINT),
                createMenu("PageView", SCREEN_PAGE_VIEW_ENDPOINT),
                createMenu("Action", SCREEN_ACTION_ENDPOINT),
                createMenu("ScreenBuilder", SCREEN_BUILDER_ENDPOINT),
                createMenu("Form", SCREEN_FORM_ENDPOINT),
                createMenu("LazyComponent", SCREEN_LAZY_COMPONENT_ENDPOINT),
                createMenu("NavigationBar", SCREEN_NAVIGATION_BAR_ENDPOINT),
                createMenu("NavigationType", NAVIGATION_TYPE_ENDPOINT),
                createMenu("Accessibility Screen", ACCESSIBILITY_SCREEN_ENDPOINT),
                createMenu("Compose Component", SCREEN_COMPOSE_COMPONENT_ENDPOINT),
                createMenu("Touchable", SCREEN_TOUCHABLE_ENDPOINT),
                createMenu("Analytics", SCREEN_ANALYTICS_ENDPOINT),
                createMenu("Web View", SCREEN_WEB_VIEW_ENDPOINT),
                createMenu("Platform", PLATFORM_SAMPLE_ENDPOINT),
                createMenu("Custom Platform", CUSTOM_PLATFORM_SAMPLE_ENDPOINT),
                createMenu("Context", SCREEN_CONTEXT_ENDPOINT),
                createMenu("Safe Area", SCREEN_SAFE_AREA_ENDPOINT),
                createMenu("Text Input", SCREEN_TEXT_INPUT_ENDPOINT),
                createMenu("Simple Form", SCREEN_SIMPLE_FORM_ENDPOINT)
            )
        )
    )

    private fun createMenu(text: String, path: String) = Button(
        text = text,
        onPress = listOf(Navigate.PushView(Route.Remote(path))
        ),
        styleId = BUTTON_STYLE
    ).applyStyle(Style(
        margin = EdgeValue(
            top = 8.unitReal()
        )
    )
    )
}