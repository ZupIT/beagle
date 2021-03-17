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
import br.com.zup.beagle.android.networking.RequestCall
import br.com.zup.beagle.android.networking.RequestData
import br.com.zup.beagle.android.networking.ResponseData
import br.com.zup.beagle.android.networking.grpc.serializer.NetworkingMoshi.moshi
import com.squareup.moshi.JsonAdapter
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.Metadata
import io.grpc.StatusException
import io.grpc.stub.MetadataUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.URL
import java.util.logging.Logger


class GrpcClient(
    private val grpcAddress: String,
    private val customHttpClient: HttpClient?,
) : HttpClient, CoroutineScope {

    val logger: Logger = Logger.getLogger(GrpcClient::class.java.name)

    companion object {
        private const val HTTP_CLIENT_NULL = "an instance of HttpClient was not found."
        private const val SCREEN_NAME_NULL = "unable to parse screen name from url."

    }

    private val job = Job()
    override val coroutineContext = job + Dispatchers.IO
    private val stub by lazy { ScreenControllerGrpcKt.ScreenControllerCoroutineStub(channel()) }

    override fun execute(request: RequestData, onSuccess: (responseData: ResponseData) -> Unit, onError: (responseData: ResponseData) -> Unit): RequestCall {

        // Se a requisição começa com grpcAddress, então trata pelo grpc
        // Senão, encaminha para o client HTTP
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
        // TODO: verificar a questão da /
        return request.url?.removePrefix("$grpcAddress/")
    }

    private suspend fun performRequest(request: RequestData): ResponseData {
        try {
            val screenName = getScreenName(request)
            if (screenName != null) {
                val screenRequestMessage = Messages.ScreenRequest.newBuilder().setName(screenName).build()

                val headersMetadata = createHeadersMetadata(request)
                var newStub = MetadataUtils.attachHeaders(stub, headersMetadata)

                val interceptor = HeaderClientInterceptor()
                newStub = newStub.withInterceptors(interceptor)

                val response = newStub.getScreen(screenRequestMessage)

                logger.info("intercepted Headers: ${interceptor.headersMap}")
                return parseResponse(response, interceptor.headersMap)
            } else {
                throw BeagleApiException(createErrorResponseData(SCREEN_NAME_NULL), request)
            }

        } catch (e: StatusException) {
            e.printStackTrace()
            // TODO: tratar códigos de erro
            // TODO: Não esquecer de tratar o 304 (cache)
            throw BeagleApiException(createErrorResponseData("Erro genérico, será alterado"), request)
        }
    }


    private fun createHeadersMetadata(request: RequestData): Metadata {
        val headers = Metadata()
        request.httpAdditionalData.headers?.forEach { entry ->
            val customHeaderKey = Metadata.Key.of(entry.key, Metadata.ASCII_STRING_MARSHALLER)
            headers.put(customHeaderKey, entry.value)
        }

        return headers
    }

    private fun parseResponse(response: Messages.ViewNode, headers: Map<String, String>): ResponseData {

        val jsonAdapter: JsonAdapter<Messages.ViewNode> = moshi.adapter(Messages.ViewNode::class.java)
        val json = jsonAdapter.toJson(response)

        //TODO: tem status text quando é 200?
        return ResponseData(
            statusCode = 200,
            data = json.toByteArray(),
            headers = headers
        )
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