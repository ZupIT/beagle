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

import br.com.zup.beagle.cache.BeagleCacheHandler
import br.com.zup.beagle.constants.BEAGLE_CACHE_EXCLUDES
import br.com.zup.beagle.constants.BEAGLE_CACHE_INCLUDES
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value
import javax.inject.Singleton

@Factory
class BeagleCacheConfiguration(
    @Value("\${$BEAGLE_CACHE_INCLUDES:}") private val includeEndpoints: List<String>,
    @Value("\${$BEAGLE_CACHE_EXCLUDES:}") private val excludeEndpoints: List<String>
) {
    @Singleton
    fun beagleCacheHandler() = BeagleCacheHandler(this.excludeEndpoints, this.includeEndpoints)
}