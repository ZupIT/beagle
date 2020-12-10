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

import br.com.zup.beagle.automatedtests.builders.ActionNotRegisteredScreenBuilder
import br.com.zup.beagle.automatedtests.builders.AlertScreenBuilder
import br.com.zup.beagle.automatedtests.builders.SetContextScreenBuilder
import br.com.zup.beagle.automatedtests.builders.ConfirmScreenBuilder
import br.com.zup.beagle.automatedtests.builders.SendRequestScreenBuilder
import br.com.zup.beagle.automatedtests.constants.ALERT_SCREEN_ENDPOINT
import br.com.zup.beagle.automatedtests.constants.NO_ACTION_SCREEN_ENDPOINT
import br.com.zup.beagle.automatedtests.constants.SET_CONTEXT_SCREEN_ENDPOINT
import br.com.zup.beagle.automatedtests.constants.CONFIRM_SCREEN_ENDPOINT
import br.com.zup.beagle.automatedtests.constants.SEND_REQUEST_ACTION_ENDPOINT
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ActionsScreenController {

    @GetMapping(NO_ACTION_SCREEN_ENDPOINT)
    fun getActionNotRegisteredScreen() = ActionNotRegisteredScreenBuilder.build()

    @GetMapping(SET_CONTEXT_SCREEN_ENDPOINT)
    fun getSetContextScreen() = SetContextScreenBuilder.build()

    @GetMapping(CONFIRM_SCREEN_ENDPOINT)
    fun getConfirmScreen() = ConfirmScreenBuilder.build()

    @GetMapping(SEND_REQUEST_ACTION_ENDPOINT)
    fun getSendRequestScreen() = SendRequestScreenBuilder.build()

    @GetMapping(ALERT_SCREEN_ENDPOINT)
    fun getAlertScreen() = AlertScreenBuilder.build()
}


