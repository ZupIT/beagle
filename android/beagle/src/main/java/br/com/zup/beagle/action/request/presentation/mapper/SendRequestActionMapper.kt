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

package br.com.zup.beagle.action.request.presentation.mapper

import br.com.zup.beagle.action.RequestActionMethod
import br.com.zup.beagle.action.SendRequestAction
import br.com.zup.beagle.action.request.domain.Request
import br.com.zup.beagle.action.request.domain.RequestMethod

internal fun SendRequestAction.toRequest(): Request = SendRequestActionMapper.toRequest(this)

internal object SendRequestActionMapper {
    fun toRequest(sendRequestAction: SendRequestAction): Request {
        val method = toHttpMethod(sendRequestAction.method)

        return Request(
            url = sendRequestAction.url,
            method = method,
            headers = sendRequestAction.headers,
            body = sendRequestAction.body
        )
    }

    private fun toHttpMethod(method: RequestActionMethod) = when (method) {
        RequestActionMethod.GET -> RequestMethod.GET
        RequestActionMethod.POST -> RequestMethod.POST
        RequestActionMethod.PUT -> RequestMethod.PUT
        RequestActionMethod.DELETE -> RequestMethod.DELETE
        RequestActionMethod.HEAD -> RequestMethod.HEAD
        RequestActionMethod.PATCH -> RequestMethod.PATCH
    }
}