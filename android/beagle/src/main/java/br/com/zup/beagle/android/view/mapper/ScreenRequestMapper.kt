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

import br.com.zup.beagle.android.networking.HttpMethod
import br.com.zup.beagle.android.networking.RequestData
import br.com.zup.beagle.android.networking.urlbuilder.UrlBuilder
import br.com.zup.beagle.android.networking.urlbuilder.UrlBuilderFactory
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.view.ScreenMethod
import br.com.zup.beagle.android.view.ScreenRequest
import java.net.URI

internal fun ScreenRequest.toRequestData(urlBuilder: UrlBuilder = UrlBuilderFactory().make(),
                                         beagleEnvironment: BeagleEnvironment = BeagleEnvironment): RequestData {
    return ScreenRequestMapper.toRequestData(urlBuilder = urlBuilder,
        beagleEnvironment = beagleEnvironment, screenRequest = this)
}

internal object ScreenRequestMapper {

    fun toRequestData(urlBuilder: UrlBuilder,
                      beagleEnvironment: BeagleEnvironment,
                      screenRequest: ScreenRequest): RequestData {
        val newUrl = urlBuilder.format(beagleEnvironment.beagleSdk.config.baseUrl, screenRequest.url)
        val method = generateRequestDataMethod(screenRequest.method)
        return RequestData(
            uri = URI(newUrl),
            method = method,
            headers = screenRequest.headers,
            body = screenRequest.body
        )
    }

    private fun generateRequestDataMethod(screenMethod: ScreenMethod) = when (screenMethod) {
        ScreenMethod.GET -> HttpMethod.GET
        ScreenMethod.POST -> HttpMethod.POST
        ScreenMethod.PUT -> HttpMethod.PUT
        ScreenMethod.DELETE -> HttpMethod.DELETE
        ScreenMethod.HEAD -> HttpMethod.HEAD
        ScreenMethod.PATCH -> HttpMethod.PATCH
    }
}