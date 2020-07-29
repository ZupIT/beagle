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

package br.com.zup.beagle.spring.configuration

import br.com.zup.beagle.serialization.jackson.BeagleModule
import com.fasterxml.jackson.databind.module.SimpleModule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.AutoConfigurations
import org.springframework.boot.test.context.FilteredClassLoader
import org.springframework.boot.test.context.runner.ApplicationContextRunner

internal class BeagleJacksonAutoConfigurationTest {
    private val contextRunner by lazy {
        ApplicationContextRunner().withConfiguration(AutoConfigurations.of(BeagleJacksonAutoConfiguration::class.java))
    }

    @Test
    fun test_BeagleJacksonAutoConfiguration_sets_up_BeagleModule_in_context() {
        this.contextRunner.run {
            assertThat(it).hasSingleBean(SimpleModule::class.java)
            assertThat(it).getBean("beagleModule").isEqualTo(it.getBean(SimpleModule::class.java))
        }
    }

    @Test
    fun test_BeagleJacksonAutoConfiguration_does_not_set_up_BeagleModule_when_its_unavailable() {
        this.contextRunner.withClassLoader(FilteredClassLoader(BeagleModule::class.java))
            .run { assertThat(it).doesNotHaveBean(SimpleModule::class.java) }
    }
}