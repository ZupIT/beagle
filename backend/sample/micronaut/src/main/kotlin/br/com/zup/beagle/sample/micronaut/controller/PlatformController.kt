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

package br.com.zup.beagle.sample.micronaut.controller

import br.com.zup.beagle.sample.constants.CUSTOM_PLATFORM_SAMPLE_ENDPOINT
import br.com.zup.beagle.sample.constants.PLATFORM_SAMPLE_ENDPOINT
import br.com.zup.beagle.sample.micronaut.service.PlatformService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller
class PlatformController(private val platformService: PlatformService) {

    @Get(CUSTOM_PLATFORM_SAMPLE_ENDPOINT)
    fun renderComponentUsingPlatform() = this.platformService.renderComponentUsingPlatform()

    @Get(PLATFORM_SAMPLE_ENDPOINT)
    fun renderComponent() = this.platformService.renderComponent()
}