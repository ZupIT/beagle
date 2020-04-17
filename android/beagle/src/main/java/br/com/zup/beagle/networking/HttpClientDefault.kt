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

package br.com.zup.beagle.networking

import br.com.zup.beagle.utils.CoroutineDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.HttpURLConnection

typealias OnSuccess = (responseData: ResponseData) -> Unit
typealias OnError = (throwable: Throwable) -> Unit

internal class HttpClientDefault : HttpClient, CoroutineScope {

    private val job = Job()
    override val coroutineContext = job + CoroutineDispatchers.IO

    override fun execute(
        request: RequestData,
        onSuccess: OnSuccess,
        onError: OnError
    ): RequestCall {
        require(!getOrDeleteOrHeadHasData(request)) { "${request.method} does not support request body" }

        launch {
            try {
                val responseData = doHttpRequest(request)
                onSuccess(responseData)
            } catch (ex: IOException) {
                onError(ex)
            }
        }

        return object : RequestCall {
            override fun cancel() {
                this@HttpClientDefault.cancel()
            }
        }
    }

    private fun getOrDeleteOrHeadHasData(request: RequestData): Boolean {
        return (request.method == HttpMethod.GET ||
                request.method == HttpMethod.DELETE ||
                request.method == HttpMethod.HEAD) &&
                request.body != null
    }

    private fun doHttpRequest(
        request: RequestData
    ): ResponseData {
        val urlConnection = request.uri.toURL().openConnection() as HttpURLConnection

        request.headers.forEach {
            urlConnection.setRequestProperty(it.key, it.value)
        }

        addRequestMethod(urlConnection, request.method)

        if (request.body != null) {
            setRequestBody(urlConnection, request.body)
        }

        try {
            return createResponseData(urlConnection)
        } catch (e: Exception) {
            throw IOException(e)
        } finally {
            urlConnection.disconnect()
        }
    }

    private fun addRequestMethod(urlConnection: HttpURLConnection, method: HttpMethod) {
        val methodValue = method.toString()

        if (method == HttpMethod.PATCH || method == HttpMethod.HEAD) {
            urlConnection.setRequestProperty("X-HTTP-Method-Override", methodValue)
            urlConnection.requestMethod = "POST"
        } else {
            urlConnection.requestMethod = methodValue
        }
    }

    private fun setRequestBody(urlConnection: HttpURLConnection, data: String) {
        urlConnection.setRequestProperty("Content-Length", data.length.toString())
        urlConnection.outputStream.write(data.toByteArray())
    }

    private fun createResponseData(urlConnection: HttpURLConnection): ResponseData {
        val byteArray = urlConnection.inputStream.readBytes()

        return ResponseData(
            statusCode = urlConnection.responseCode,
            headers = urlConnection.headerFields.map {
                val headerValue = it.value.toString()
                    .replace("[", "")
                    .replace("]", "")
                it.key to headerValue
            }.toMap(),
            data = byteArray
        )
    }
}