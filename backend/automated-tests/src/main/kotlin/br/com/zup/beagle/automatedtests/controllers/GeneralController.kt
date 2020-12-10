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

import br.com.zup.beagle.automatedtests.constants.ACCESSIBILITY_ENDPOINT
import br.com.zup.beagle.automatedtests.constants.ANALYTICS_ENDPOINT
import br.com.zup.beagle.automatedtests.constants.EXPRESSION_ESCAPING_ENDPOINT
import br.com.zup.beagle.automatedtests.constants.KOTLIN_DSL_ENDPOINT
import br.com.zup.beagle.automatedtests.constants.SAFE_AREA_ENDPOINT
import br.com.zup.beagle.automatedtests.constants.SCREEN_ACTION_CLICK_ENDPOINT
import br.com.zup.beagle.automatedtests.constants.SCREEN_SAFE_AREA_FALSE_ENDPOINT
import br.com.zup.beagle.automatedtests.constants.SCREEN_SAFE_AREA_TRUE_ENDPOINT
import br.com.zup.beagle.automatedtests.builders.ClickActionScreenBuilder
import br.com.zup.beagle.automatedtests.builders.AnalyticsScreenBuilder
import br.com.zup.beagle.automatedtests.builders.SafeAreaScreenBuilder
import br.com.zup.beagle.automatedtests.builders.BuilderKotlinDslScreenBuilder
import br.com.zup.beagle.automatedtests.builders.ExpressionEscapingScreenBuilder
import br.com.zup.beagle.automatedtests.builders.AccessibilityScreenBuilder
import br.com.zup.beagle.automatedtests.constants.IMAGE_WEB
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.InputStreamResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class GeneralController {

    @GetMapping(SCREEN_ACTION_CLICK_ENDPOINT)
    fun getActionClick() = ClickActionScreenBuilder.build()

    @GetMapping(ANALYTICS_ENDPOINT)
    fun getAnalyticsScreen() = AnalyticsScreenBuilder.build()

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

    @GetMapping(ACCESSIBILITY_ENDPOINT)
    fun getAccessibilityScreen() = AccessibilityScreenBuilder.build()

    @GetMapping(IMAGE_WEB, produces = [MediaType.IMAGE_JPEG_VALUE])
    fun getImageWeb(@PathVariable type:String): ResponseEntity<InputStreamResource> {

        val imageFile = ClassPathResource("images/" + if (type == "1") "beagle1.jpg" else "beagle2.jpg")
        return ResponseEntity
            .ok()
            .contentType(MediaType.IMAGE_JPEG)
            .body(InputStreamResource(imageFile.inputStream))
    }
}


