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

package br.com.zup.beagle.android.networking.grpc

import beagle.Messages
import beagle.ScreenControllerGrpcKt
import br.com.zup.beagle.android.exception.BeagleApiException
import br.com.zup.beagle.android.networking.HttpClient
import br.com.zup.beagle.android.networking.HttpMethod
import br.com.zup.beagle.android.networking.RequestCall
import br.com.zup.beagle.android.networking.RequestData
import br.com.zup.beagle.android.networking.ResponseData
import br.com.zup.beagle.android.networking.grpc.serializer.NetworkingMoshi.moshi
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Types
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.Metadata
import io.grpc.StatusException
import io.grpc.stub.MetadataUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.URL
import java.util.logging.Logger


class GrpcClient(
    private val grpcAddress: String,
    private val customHttpClient: HttpClient?,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) : HttpClient, CoroutineScope {

    val logger: Logger = Logger.getLogger(GrpcClient::class.java.name)

    companion object {
        const val HTTP_CLIENT_NULL = "an instance of HttpClient was not found."
        const val SCREEN_NAME_NULL = "unable to parse screen name from url."
        const val UNKNOWN_ERROR = "unknown error."
    }

    private val job = Job()
    override val coroutineContext = job + dispatcher
    private val stub by lazy { ScreenControllerGrpcKt.ScreenControllerCoroutineStub(channel()) }

    override fun execute(request: RequestData, onSuccess: (responseData: ResponseData) -> Unit, onError: (responseData: ResponseData) -> Unit): RequestCall {

        return if (isGrpcRequest(request)) {
            launch {
                try {
                    onSuccess(performRequest(request))
                } catch (e: BeagleApiException) {
                    onError(e.responseData)
                }
            }
            createRequestCall()
        } else {
            if (customHttpClient == null) {
                val responseError = createErrorResponseData(HTTP_CLIENT_NULL)
                onError(responseError)
                createRequestCall()
            } else {
                customHttpClient.execute(request, onSuccess, onError)
            }
        }
    }

    private fun isGrpcRequest(request: RequestData): Boolean {
        return request.url?.startsWith(grpcAddress) ?: false
    }

    private fun getScreenName(request: RequestData): String? {
        val screenName = request.url
            ?.removePrefix("$grpcAddress")
            ?.removePrefix("/")
            ?.substringBefore("?")

        if (screenName.isNullOrEmpty()) {
            throw BeagleApiException(createErrorResponseData(SCREEN_NAME_NULL), request)
        }

        return screenName
    }

    private suspend fun performRequest(request: RequestData): ResponseData {
        try {
            val headersMetadata = getRequestHeadersMetadata(request)
            var requestStub = MetadataUtils.attachHeaders(stub, headersMetadata)

            val responseInterceptor = HeaderClientInterceptor()
            requestStub = requestStub.withInterceptors(responseInterceptor)

            val screenRequestMessage = createScreenRequestMessage(request)
            val response = requestStub.getScreen(screenRequestMessage)

            return parseResponse(response, responseInterceptor.headersMap)

        } catch (e: StatusException) {
            e.printStackTrace()

            throw BeagleApiException(createErrorResponseData(e.status.description ?: UNKNOWN_ERROR), request)
        }
    }

    private fun getRequestHeadersMetadata(request: RequestData): Metadata {
        val headers = Metadata()
        request.httpAdditionalData.headers?.forEach { entry ->
            val customHeaderKey = Metadata.Key.of(entry.key, Metadata.ASCII_STRING_MARSHALLER)
            headers.put(customHeaderKey, entry.value)
        }

        return headers
    }

    private fun getRequestParameters(request: RequestData): String {

        return when (request.httpAdditionalData.method ?: HttpMethod.GET) {
            HttpMethod.GET,
            HttpMethod.DELETE,
            HttpMethod.HEAD -> {
                queryParamsToJson(
                    extractRequestQueryParams(request)
                )
            }
            else -> {
                request.httpAdditionalData.body ?: ""
            }
        }
    }

    private fun extractRequestQueryParams(request: RequestData): Map<String, String> {
        return try {
            request.url
                ?.substringAfter("?", "")
                ?.split("&")
                ?.map { param ->
                    val list = param.split("=")
                    list[0] to list[1]
                }
                ?.toMap() ?: mutableMapOf()

        } catch (e: Exception) {
            e.printStackTrace()
            mutableMapOf()
        }
    }

    private fun queryParamsToJson(queryParams: Map<String, String>): String {
        val type = Types.newParameterizedType(MutableMap::class.java, String::class.java, String::class.java)
        val adapter: JsonAdapter<Map<String, String>> = moshi.adapter(type)

        return adapter.toJson(queryParams)
    }

    private fun createScreenRequestMessage(request: RequestData): Messages.ScreenRequest {
        val screenName = getScreenName(request)
        val params = getRequestParameters(request)

        return Messages.ScreenRequest
            .newBuilder()
            .setName(screenName)
            .setParameters(params)
            .build()
    }

    private fun parseResponse(response: Messages.ViewNode, headers: Map<String, String>): ResponseData {
        val data = responseMessageToJson(response)

        return ResponseData(
            statusCode = 200,
            data = data.toByteArray(),
            headers = headers
        )
    }

    private fun responseMessageToJson(response: Messages.ViewNode): String {
        val jsonAdapter: JsonAdapter<Messages.ViewNode> = moshi.adapter(Messages.ViewNode::class.java)
        return jsonAdapter.toJson(response)
    }

    private fun createErrorResponseData(message: String): ResponseData {
        return ResponseData(-1, data = message.toByteArray())
    }

    private fun channel(): ManagedChannel {

        val url = URL(grpcAddress)
        val port = if (url.port == -1) url.defaultPort else url.port

        val builder = ManagedChannelBuilder.forAddress(url.host, port)
        if (url.protocol == "https") {
            builder.useTransportSecurity()
        } else {
            builder.usePlaintext()
        }

        return builder.executor(Dispatchers.IO.asExecutor()).build()
    }

    private fun createRequestCall() = object : RequestCall {
        override fun cancel() {
            this@GrpcClient.cancel()
        }
    }
}