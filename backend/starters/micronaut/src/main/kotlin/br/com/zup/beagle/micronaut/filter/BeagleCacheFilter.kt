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

package br.com.zup.beagle.micronaut.filter

import br.com.zup.beagle.cache.BeagleCacheHandler
import br.com.zup.beagle.constants.BEAGLE_CACHE_ENABLED
import br.com.zup.beagle.micronaut.configuration.BeagleMicronautCacheProperties
import br.com.zup.beagle.platform.BeaglePlatformUtil
import io.micronaut.context.annotation.Requirements
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpMethod
import io.micronaut.http.HttpRequest
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.OncePerRequestHttpServerFilter
import io.micronaut.http.filter.ServerFilterChain
import org.reactivestreams.Publisher

@Filter(value = [Filter.MATCH_ALL_PATTERN], methods = [HttpMethod.GET])
@Requirements(
    Requires(classes = [BeagleCacheHandler::class]),
    Requires(property = BEAGLE_CACHE_ENABLED, value = "true", defaultValue = "true")
)
class BeagleCacheFilter(private val properties: BeagleMicronautCacheProperties) : OncePerRequestHttpServerFilter() {

    override fun doFilterOnce(request: HttpRequest<*>, chain: ServerFilterChain): Publisher<MutableHttpResponse<*>> {
        val beagleCacheHandler = BeagleMicronautCacheHandler(
            request = request,
            chain = chain,
            properties = properties
        )
        return beagleCacheHandler.handleCache(
            endpoint = request.path,
            receivedHash = request.headers[BeagleCacheHandler.CACHE_HEADER],
            currentPlatform = request.headers[BeaglePlatformUtil.BEAGLE_PLATFORM_HEADER]
        ) as Publisher<MutableHttpResponse<*>>
    }
}