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

import br.com.zup.beagle.automatedtests.constants.SCREEN_BUTTON_ALIGN_CENTER_ENDPOINT
import br.com.zup.beagle.automatedtests.constants.SCREEN_BUTTON_ALIGN_LEFT_ENDPOINT
import br.com.zup.beagle.automatedtests.constants.SCREEN_IMAGE_ENDPOINT
import br.com.zup.beagle.automatedtests.constants.TEXT_ENDPOINT
import br.com.zup.beagle.automatedtests.constants.TOUCHABLE_ENDPOINT
import br.com.zup.beagle.automatedtests.builders.ButtonScreenBuilder
import br.com.zup.beagle.automatedtests.builders.ImageScreenBuilder
import br.com.zup.beagle.automatedtests.builders.TextScreenBuilder
import br.com.zup.beagle.automatedtests.builders.TouchableScreenBuilder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SimpleComponentsController {

    @GetMapping(SCREEN_IMAGE_ENDPOINT)
    fun getImageScreen() = ImageScreenBuilder.build()

    @GetMapping(SCREEN_BUTTON_ALIGN_CENTER_ENDPOINT)
    fun getButtonScreenAlignCenter() = ButtonScreenBuilder.buildButtonAlignCenter()

    @GetMapping(SCREEN_BUTTON_ALIGN_LEFT_ENDPOINT)
    fun getButtonScreenAlignLeft() = ButtonScreenBuilder.buildButtonAlignLeft()

    @GetMapping(TEXT_ENDPOINT)
    fun getTextScreen() = TextScreenBuilder.build()

    @GetMapping(TOUCHABLE_ENDPOINT)
    fun getTouchableScreen() = TouchableScreenBuilder.build()
}