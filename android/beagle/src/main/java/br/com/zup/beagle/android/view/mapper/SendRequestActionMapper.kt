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

package br.com.zup.beagle.android.view.mapper

import br.com.zup.beagle.widget.action.RequestActionMethod
import br.com.zup.beagle.widget.action.SendRequestAction
import br.com.zup.beagle.android.data.formatUrl
import br.com.zup.beagle.android.networking.HttpMethod
import br.com.zup.beagle.android.networking.RequestData
import java.net.URI

internal fun SendRequestAction.toRequestData(): RequestData = SendRequestActionMapper.toRequestData(this)

internal object SendRequestActionMapper {
    fun toRequestData(sendRequestAction: SendRequestAction): RequestData {
        val method = toHttpMethod(sendRequestAction.method)

        val urlFormatted = sendRequestAction.url.formatUrl()
        return RequestData(
            uri = URI(urlFormatted),
            method = method,
            headers = sendRequestAction.headers,
            body = sendRequestAction.body
        )
    }

    private fun toHttpMethod(method: RequestActionMethod) = when (method) {
        RequestActionMethod.GET -> HttpMethod.GET
        RequestActionMethod.POST -> HttpMethod.POST
        RequestActionMethod.PUT -> HttpMethod.PUT
        RequestActionMethod.DELETE -> HttpMethod.DELETE
        RequestActionMethod.HEAD -> HttpMethod.HEAD
        RequestActionMethod.PATCH -> HttpMethod.PATCH
    }
}