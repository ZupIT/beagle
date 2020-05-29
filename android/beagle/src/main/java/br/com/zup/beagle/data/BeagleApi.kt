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

package br.com.zup.beagle.data

import br.com.zup.beagle.exception.BeagleException
import br.com.zup.beagle.logger.BeagleMessageLogs
import br.com.zup.beagle.networking.HttpClient
import br.com.zup.beagle.networking.HttpClientFactory
import br.com.zup.beagle.networking.HttpMethod
import br.com.zup.beagle.networking.RequestData
import br.com.zup.beagle.networking.ResponseData
import br.com.zup.beagle.networking.urlbuilder.UrlBuilder
import br.com.zup.beagle.networking.urlbuilder.UrlBuilderFactory
import br.com.zup.beagle.setup.BeagleEnvironment
import br.com.zup.beagle.view.ScreenMethod
import br.com.zup.beagle.view.ScreenRequest
import kotlinx.coroutines.suspendCancellableCoroutine
import java.net.URI
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal class BeagleApi(
    private val httpClient: HttpClient = HttpClientFactory().make()
) {

    @Throws(BeagleException::class)
    suspend fun fetchData(request: RequestData): ResponseData = suspendCancellableCoroutine { cont ->
        try {
            BeagleMessageLogs.logHttpRequestData(request)
            val call = httpClient.execute(
                request = request,
                onSuccess = { response ->
                    BeagleMessageLogs.logHttpResponseData(response)
                    cont.resume(response)
                }, onError = { error ->
                BeagleMessageLogs.logUnknownHttpError(error)
                cont.resumeWithException(
                    BeagleException(error.message ?: genericErrorMessage(request.uri.toString()), error)
                )
            })
            cont.invokeOnCancellation {
                call.cancel()
            }
        } catch (ex: Exception) {
            BeagleMessageLogs.logUnknownHttpError(ex)
            cont.resumeWithException(BeagleException(ex.message ?: genericErrorMessage(request.uri.toString()), ex))
        }
    }

    private fun genericErrorMessage(url: String) = "fetchData error for url $url"
}