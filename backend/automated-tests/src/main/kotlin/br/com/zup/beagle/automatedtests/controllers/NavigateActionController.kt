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

import br.com.zup.beagle.automatedtests.constants.GLOBAL_TEXT_ENDPOINT
import br.com.zup.beagle.automatedtests.constants.NAVIGATE_ACTIONS_ENDPOINT
import br.com.zup.beagle.automatedtests.constants.NAVIGATE_RESET_ENDPOINT
import br.com.zup.beagle.automatedtests.builders.GlobalSampleTextScreenBuilder
import br.com.zup.beagle.automatedtests.builders.NavigateActionsResetScreenBuilder
import br.com.zup.beagle.automatedtests.builders.NavigateActionsScreenBuilder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class NavigateActionController {
    @GetMapping(NAVIGATE_ACTIONS_ENDPOINT)
    fun getNavigateActions() = NavigateActionsScreenBuilder.build()

    @GetMapping(GLOBAL_TEXT_ENDPOINT)
    fun getGlobalText() = GlobalSampleTextScreenBuilder.build()

    @GetMapping(NAVIGATE_RESET_ENDPOINT)
    fun getNavigateActionsResetScreen() = NavigateActionsResetScreenBuilder.build()
}