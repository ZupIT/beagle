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

package br.com.zup.beagle.micronaut.configuration

import br.com.zup.beagle.micronaut.containsBeans
import br.com.zup.beagle.serialization.jackson.BeagleModule
import io.micronaut.context.ApplicationContext
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

internal class BeagleJacksonConfigurationTest {
    @Test
    fun test_BeagleJacksonConfiguration_sets_up_BeagleModule_in_context() {
        assertTrue { ApplicationContext.run().containsBeans(BeagleJacksonConfiguration::class, BeagleModule::class) }
    }
}