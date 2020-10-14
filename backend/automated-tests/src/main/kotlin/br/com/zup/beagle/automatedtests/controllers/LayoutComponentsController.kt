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

import ACTION_ENDPOINT
import IMAGE_REMOTE_ENDPOINT
import LAZY_COMPONENT_ENDPOINT
import PAGEVIEW_TWO_ENDPOINT
import SIMPLE_FORM_ENDPOINT
import TAB_BAR_ENDPOINT
import TEXT_INPUT_ENDPOINT
import WEB_VIEW_ENDPOINT
import br.com.zup.beagle.automatedtests.builders.SimpleFormScreenBuilder
import br.com.zup.beagle.automatedtests.builders.TextInputScreenBuilder
import br.com.zup.beagle.automatedtests.builders.TabBarScreenBuilder
import br.com.zup.beagle.automatedtests.builders.WebViewScreenBuilder
import br.com.zup.beagle.automatedtests.builders.LazyComponentScreenBuilder
import br.com.zup.beagle.automatedtests.builders.ImageRemoteScreenBuilder
import br.com.zup.beagle.automatedtests.builders.ActionScreenBuilder
import br.com.zup.beagle.automatedtests.builders.PageViewTwoScreenBuilder

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LayoutComponentsController {


    @GetMapping(SIMPLE_FORM_ENDPOINT)
    fun getSimpleFormScreen() = SimpleFormScreenBuilder

    @GetMapping(TEXT_INPUT_ENDPOINT)
    fun getTextInputScreen() = TextInputScreenBuilder.build()

    @GetMapping(TAB_BAR_ENDPOINT)
    fun getTabBarScreen() = TabBarScreenBuilder.build()

    @GetMapping(WEB_VIEW_ENDPOINT)
    fun getWebViewScreen() = WebViewScreenBuilder.build()

    @GetMapping(LAZY_COMPONENT_ENDPOINT)
    fun getLazyComponentScreen() = LazyComponentScreenBuilder.build()

    @GetMapping(IMAGE_REMOTE_ENDPOINT)
    fun getImageRemoteScreen() = ImageRemoteScreenBuilder.build()

    @GetMapping(ACTION_ENDPOINT)
    fun getActionScreen() = ActionScreenBuilder.build()

    @GetMapping(PAGEVIEW_TWO_ENDPOINT)
    fun getPageViewTwoScreen() = PageViewTwoScreenBuilder.build()

}