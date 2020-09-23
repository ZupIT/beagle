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

package br.com.zup.beagle.automatedtests.controllers

import ACCESSIBILITY_ENDPOINT
import ACTION_ENDPOINT
import ANALYTICS_ENDPOINT
import EXPRESSION_ESCAPING_ENDPOINT
import IMAGE_REMOTE_ENDPOINT
import KOTLIN_DSL_ENDPOINT
import LAZY_COMPONENT_ENDPOINT
import LISTVIEW_TABVIEW_ENDPOINT
import NAVIGATION_BAR_ENDPOINT
import PAGEVIEW_TABVIEW_ENDPOINT
import PAGEVIEW_TWO_ENDPOINT
import REPRESENTATION_NAVIGATION_BAR_ENDPOINT
import REPRESENTATION_NAVIGATION_BAR_IMAGE_ENDPOINT
import REPRESENTATION_NAVIGATION_BAR_STYLE_ENDPOINT
import REPRESENTATION_NAVIGATION_BAR_TEXT_ENDPOINT
import SAFE_AREA_ENDPOINT
import SCREEN_BUTTON_ALIGN_CENTER_ENDPOINT
import SCREEN_BUTTON_ALIGN_LEFT_ENDPOINT
import SCREEN_IMAGE_ENDPOINT
import SCREEN_SAFE_AREA_FALSE_ENDPOINT
import SCREEN_SAFE_AREA_TRUE_ENDPOINT
import SCREEN_TABVIEW_ENDPOINT
import SCROLLVIEW_TABVIEW_ENDPOINT
import SIMPLE_FORM_ENDPOINT
import TAB_BAR_ENDPOINT
import TEXT_ENDPOINT
import TEXT_INPUT_ENDPOINT
import TOUCHABLE_ENDPOINT
import WEB_VIEW_ENDPOINT
import br.com.zup.beagle.automatedtests.builders.ImageScreenBuilder
import br.com.zup.beagle.automatedtests.builders.ButtonScreenBuilder
import br.com.zup.beagle.automatedtests.builders.TabViewScreenBuilder
import br.com.zup.beagle.automatedtests.builders.PageViewScreenBuilder
import br.com.zup.beagle.automatedtests.builders.ListViewScreenBuilder
import br.com.zup.beagle.automatedtests.builders.ScrollViewScreenBuilder
import br.com.zup.beagle.automatedtests.builders.TouchableScreenBuilder
import br.com.zup.beagle.automatedtests.builders.SimpleFormScreenBuilder
import br.com.zup.beagle.automatedtests.builders.TextInputScreenBuilder
import br.com.zup.beagle.automatedtests.builders.TabBarScreenBuilder
import br.com.zup.beagle.automatedtests.builders.WebViewScreenBuilder
import br.com.zup.beagle.automatedtests.builders.TextScreenBuilder
import br.com.zup.beagle.automatedtests.builders.LazyComponentScreenBuilder
import br.com.zup.beagle.automatedtests.builders.ImageRemoteScreenBuilder
import br.com.zup.beagle.automatedtests.builders.AnalyticsScreenBuilder
import br.com.zup.beagle.automatedtests.builders.ActionScreenBuilder
import br.com.zup.beagle.automatedtests.builders.AccessibilityScreenBuilder
import br.com.zup.beagle.automatedtests.builders.PageViewTwoScreenBuilder
import br.com.zup.beagle.automatedtests.builders.NavigationBarScreenBuilder
import br.com.zup.beagle.automatedtests.builders.SafeAreaScreenBuilder
import br.com.zup.beagle.automatedtests.builders.BuilderKotlinDslScreenBuilder
import br.com.zup.beagle.automatedtests.builders.ExpressionEscapingScreenBuilder

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LayoutComponentsController {

    @GetMapping(SCREEN_IMAGE_ENDPOINT)
    fun getImageScreen() = ImageScreenBuilder.build()

    @GetMapping(SCREEN_BUTTON_ALIGN_CENTER_ENDPOINT)
    fun getButtonScreenAlignCenter() = ButtonScreenBuilder.buildButtonAlignCenter()

    @GetMapping(SCREEN_BUTTON_ALIGN_LEFT_ENDPOINT)
    fun getButtonScreenAlignLeft() = ButtonScreenBuilder.buildButtonAlignLeft()

    @GetMapping(SCREEN_TABVIEW_ENDPOINT)
    fun getTabViewScreen() = TabViewScreenBuilder.build()

    @GetMapping(PAGEVIEW_TABVIEW_ENDPOINT)
    fun getPageViewScreen() = PageViewScreenBuilder.build()

    @GetMapping(LISTVIEW_TABVIEW_ENDPOINT)
    fun getListViewScreen() = ListViewScreenBuilder.build()

    @GetMapping(SCROLLVIEW_TABVIEW_ENDPOINT)
    fun getScrollViewScreen() = ScrollViewScreenBuilder.build()

    @GetMapping(TOUCHABLE_ENDPOINT)
    fun getTouchableScreen() = TouchableScreenBuilder.build()

    @GetMapping(SIMPLE_FORM_ENDPOINT)
    fun getSimpleFormScreen() = SimpleFormScreenBuilder

    @GetMapping(TEXT_INPUT_ENDPOINT)
    fun getTextInputScreen() = TextInputScreenBuilder.build()

    @GetMapping(TAB_BAR_ENDPOINT)
    fun getTabBarScreen() = TabBarScreenBuilder.build()

    @GetMapping(WEB_VIEW_ENDPOINT)
    fun getWebViewScreen() = WebViewScreenBuilder.build()

    @GetMapping(TEXT_ENDPOINT)
    fun getTextScreen() = TextScreenBuilder.build()

    @GetMapping(LAZY_COMPONENT_ENDPOINT)
    fun getLazyComponentScreen() = LazyComponentScreenBuilder.build()

    @GetMapping(IMAGE_REMOTE_ENDPOINT)
    fun getImageRemoteScreen() = ImageRemoteScreenBuilder.build()

    @GetMapping(ANALYTICS_ENDPOINT)
    fun getAnalyticsScreen() = AnalyticsScreenBuilder.build()

    @GetMapping(ACTION_ENDPOINT)
    fun getActionScreen() = ActionScreenBuilder.build()

    @GetMapping(ACCESSIBILITY_ENDPOINT)
    fun getAccessibilityScreen() = AccessibilityScreenBuilder.build()

    @GetMapping(PAGEVIEW_TWO_ENDPOINT)
    fun getPageViewTwoScreen() = PageViewTwoScreenBuilder.build()

    @GetMapping(NAVIGATION_BAR_ENDPOINT)
    fun getSampleNavigationBarController() = NavigationBarScreenBuilder.build()

    @GetMapping(REPRESENTATION_NAVIGATION_BAR_ENDPOINT)
    fun getSampleNavigationBar() = NavigationBarScreenBuilder.navigationBar()

    @GetMapping(REPRESENTATION_NAVIGATION_BAR_STYLE_ENDPOINT)
    fun getNavigationBarStyle() = NavigationBarScreenBuilder.navigationBarStyle()

    @GetMapping(REPRESENTATION_NAVIGATION_BAR_TEXT_ENDPOINT)
    fun getNavigationBarText() = NavigationBarScreenBuilder.navigationBarWithTextAsItems()

    @GetMapping(REPRESENTATION_NAVIGATION_BAR_IMAGE_ENDPOINT)
    fun getNavigationBarImage() = NavigationBarScreenBuilder.navigationBarWithImageAsItem()

    @GetMapping(SAFE_AREA_ENDPOINT)
    fun getSampleSafeArea() = SafeAreaScreenBuilder.build()

    @GetMapping(SCREEN_SAFE_AREA_TRUE_ENDPOINT)
    fun getSampleSafeAreaTrue() = SafeAreaScreenBuilder.createScreen(true)

    @GetMapping(SCREEN_SAFE_AREA_FALSE_ENDPOINT)
    fun getSampleSafeAreaFalse() = SafeAreaScreenBuilder.createScreen(false)

    @GetMapping(KOTLIN_DSL_ENDPOINT)
    fun getKotlinDsl() = BuilderKotlinDslScreenBuilder.build()

    @GetMapping(EXPRESSION_ESCAPING_ENDPOINT)
    fun getExpressionEscaping() = ExpressionEscapingScreenBuilder.build()

}