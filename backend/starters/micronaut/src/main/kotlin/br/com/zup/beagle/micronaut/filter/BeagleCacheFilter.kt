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
import br.com.zup.beagle.cache.RestCacheHandler
import br.com.zup.beagle.constants.BEAGLE_CACHE_ENABLED
import br.com.zup.beagle.constants.BEAGLE_CACHE_INCLUDES
import br.com.zup.beagle.platform.BeaglePlatformUtil
import io.micronaut.context.annotation.Requirements
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpMethod
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponseFactory
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.HttpServerFilter
import io.micronaut.http.filter.ServerFilterChain
import io.reactivex.Flowable
import org.reactivestreams.Publisher

@Filter(value = ["/**"], methods = [HttpMethod.GET])
@Requirements(
    Requires(classes = [BeagleCacheHandler::class]),
    Requires(property = BEAGLE_CACHE_ENABLED, value = "true", defaultValue = "true")
)
class BeagleCacheFilter(private val cacheHandler: BeagleCacheHandler) : HttpServerFilter {
    override fun doFilter(request: HttpRequest<*>?, chain: ServerFilterChain?) =
        this.cacheHandler.handleCache(
            endpoint = request?.path ?: "",
            receivedHash = request?.headers?.get(BeagleCacheHandler.CACHE_HEADER),
            currentPlatform = request?.headers?.get(BeaglePlatformUtil.BEAGLE_PLATFORM_HEADER),
            initialResponse = Publisher {
                it.onNext(HttpResponseFactory.INSTANCE.ok<Unit>())
                it.onComplete()
            },
            restHandler = object : RestCacheHandler<Publisher<MutableHttpResponse<*>>?> {
                override fun callController(response: Publisher<MutableHttpResponse<*>>?) = chain?.proceed(request)

                override fun addHashHeader(response: Publisher<MutableHttpResponse<*>>?, header: String) =
                    response?.map { it.header(BeagleCacheHandler.CACHE_HEADER, header) }

                override fun addStatus(response: Publisher<MutableHttpResponse<*>>?, status: Int) =
                    response?.map { it.status(status) }

                override fun getBody(response: Publisher<MutableHttpResponse<*>>?) =
                    Flowable.fromPublisher(response).blockingFirst().body.toString()
            }
        )

    private fun Publisher<MutableHttpResponse<*>>.map(transform: (MutableHttpResponse<*>) -> MutableHttpResponse<*>) =
        Flowable.fromPublisher(this).map(transform)
}