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

import br.com.zup.beagle.cache.HttpCacheHandler
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponseFactory
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.filter.ServerFilterChain
import io.reactivex.Flowable

internal class BeagleMicronautCacheHandler(
    private val request: HttpRequest<*>,
    private val chain: ServerFilterChain
) : HttpCacheHandler<MutableHttpResponse<*>> {
    override fun createResponseFromController(): MutableHttpResponse<*> =
        Flowable.fromPublisher(chain.proceed(request)).blockingFirst()

    override fun createResponse(status: Int): MutableHttpResponse<*> =
        HttpResponseFactory.INSTANCE.status(HttpStatus.valueOf(status), Unit)

    override fun getBody(response: MutableHttpResponse<*>): String = response.body.toString()

    override fun addHeader(response: MutableHttpResponse<*>, key: String, value: String): MutableHttpResponse<*> =
        response.header(key, value)
}