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

package br.com.zup.beagle.action.request.data.mapper

import br.com.zup.beagle.action.request.domain.Request
import br.com.zup.beagle.action.request.domain.RequestMethod
import br.com.zup.beagle.data.formatUrl
import br.com.zup.beagle.networking.HttpMethod
import br.com.zup.beagle.networking.RequestData
import java.net.URI

internal fun Request.toRequestData(): RequestData = RequestMapper.toRequestData(this)

internal object RequestMapper {
    fun toRequestData(request: Request): RequestData {
        val method = toHttpMethod(request.method)
        val newUrl = request.url.formatUrl()

        return RequestData(
            uri = URI(newUrl),
            method = method,
            headers = request.headers,
            body = request.body
        )
    }

    private fun toHttpMethod(requestMethod: RequestMethod) = when (requestMethod) {
        RequestMethod.GET -> HttpMethod.GET
        RequestMethod.POST -> HttpMethod.POST
        RequestMethod.PUT -> HttpMethod.PUT
        RequestMethod.DELETE -> HttpMethod.DELETE
        RequestMethod.HEAD -> HttpMethod.HEAD
        RequestMethod.PATCH -> HttpMethod.PATCH
    }
}