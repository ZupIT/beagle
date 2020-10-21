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

import br.com.zup.beagle.automatedtests.constants.LISTVIEW_TABVIEW_ENDPOINT
import br.com.zup.beagle.automatedtests.constants.PAGEVIEW_TABVIEW_ENDPOINT
import br.com.zup.beagle.automatedtests.constants.SCREEN_TABVIEW_ENDPOINT
import br.com.zup.beagle.automatedtests.constants.SCROLLVIEW_TABVIEW_ENDPOINT
import br.com.zup.beagle.automatedtests.builders.ListViewScreenBuilder
import br.com.zup.beagle.automatedtests.builders.PageViewScreenBuilder
import br.com.zup.beagle.automatedtests.builders.ScrollViewScreenBuilder
import br.com.zup.beagle.automatedtests.builders.TabViewScreenBuilder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ContainersController {

    @GetMapping(SCREEN_TABVIEW_ENDPOINT)
    fun getTabViewScreen() = TabViewScreenBuilder.build()

    @GetMapping(PAGEVIEW_TABVIEW_ENDPOINT)
    fun getPageViewScreen() = PageViewScreenBuilder.build()

    @GetMapping(LISTVIEW_TABVIEW_ENDPOINT)
    fun getListViewScreen() = ListViewScreenBuilder.build()

    @GetMapping(SCROLLVIEW_TABVIEW_ENDPOINT)
    fun getScrollViewScreen() = ScrollViewScreenBuilder.build()
}