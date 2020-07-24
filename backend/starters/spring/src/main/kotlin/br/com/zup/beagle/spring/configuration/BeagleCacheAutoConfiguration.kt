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

import br.com.zup.beagle.cache.BeagleCacheHandler
import br.com.zup.beagle.constants.BEAGLE_CACHE_ENABLED
import br.com.zup.beagle.spring.filter.BeagleCacheFilter
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(BeagleSpringCacheProperties::class)
@ConditionalOnClass(value = [BeagleCacheFilter::class, BeagleCacheHandler::class])
@ConditionalOnProperty(value = [BEAGLE_CACHE_ENABLED], matchIfMissing = true, havingValue = "true")
open class BeagleCacheAutoConfiguration(private val properties: BeagleSpringCacheProperties) {
    @Bean
    open fun beagleCachingFilter(cacheHandler: BeagleCacheHandler) =
        FilterRegistrationBean<BeagleCacheFilter>().also { it.filter = BeagleCacheFilter(cacheHandler) }

    @Bean
    open fun beagleCacheHandler() = BeagleCacheHandler(this.properties)
}