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

package br.com.zup.beagle.sample.spring.controller

import br.com.zup.beagle.sample.constants.LIST_MODEL_ENDPOINT
import br.com.zup.beagle.sample.constants.MODEL_ENDPOINT
import br.com.zup.beagle.sample.constants.MODEL_ENDPOINT2
import br.com.zup.beagle.sample.constants.NESTED_MODEL_ENDPOINT
import br.com.zup.beagle.sample.model.SampleModel
import br.com.zup.beagle.sample.spring.service.SampleModelService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ModelController(private val sampleModelService: SampleModelService) {
    @GetMapping(MODEL_ENDPOINT)
    fun getSampleModel(): SampleModel {
        Thread.sleep(3000L)
        return this.sampleModelService.createSampleModel()
    }

    @GetMapping(MODEL_ENDPOINT2)
    fun getSampleModel2(): SampleModel {
        Thread.sleep(3000L)
        return this.sampleModelService.createSampleModel2()
    }

    @GetMapping(LIST_MODEL_ENDPOINT)
    fun getRawJsonList(): List<SampleModel>
    {
        Thread.sleep(3000L)
        return this.sampleModelService.createSampleList()
    }

    @GetMapping(NESTED_MODEL_ENDPOINT)
    fun getSampleNested() = this.sampleModelService.createSampleNested()
}