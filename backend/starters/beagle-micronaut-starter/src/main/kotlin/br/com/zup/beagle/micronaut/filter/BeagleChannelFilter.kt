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

import br.com.zup.beagle.utils.ChannelUtil
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import io.micronaut.context.annotation.Requirements
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.HttpServerFilter
import io.micronaut.http.filter.ServerFilterChain
import io.reactivex.Flowable
import org.reactivestreams.Publisher

@Filter("/**")
@Requirements(Requires(classes = [ChannelUtil::class]))
class BeagleChannelFilter(private val objectMapper: ObjectMapper) : HttpServerFilter {
    override fun doFilter(request: HttpRequest<*>, chain: ServerFilterChain): Publisher<MutableHttpResponse<*>>? {
        val response = Flowable.fromPublisher(chain.proceed(request)).blockingFirst() as MutableHttpResponse<Any>
        response.body.ifPresent {
            val jsonTree = this.objectMapper.readTree(
                this.objectMapper.writeValueAsString(it)
            ) as ObjectNode
            ChannelUtil.treatBeagleChannel(
                request.headers.get(ChannelUtil.BEAGLE_CHANNEL_HEADER),
                jsonTree
            )
            response.body(jsonTree.toPrettyString())
        }
        return Flowable.just(response)
    }
}