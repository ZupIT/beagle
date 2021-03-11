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
import br.com.zup.beagle.android.networking.HttpClient
import br.com.zup.beagle.android.networking.RequestCall
import br.com.zup.beagle.android.networking.RequestData
import br.com.zup.beagle.android.networking.ResponseData
import com.squareup.moshi.*
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.StatusException
import kotlinx.coroutines.*
import java.net.URL
import java.util.logging.Logger
import com.squareup.moshi.Moshi


class GrpcClient(
    private val grpcAddress: String,
    private val customHttpClient: HttpClient?,
) : HttpClient, CoroutineScope {
    companion object {
        const val HTTP_CLIENT_NULL = "an instance of HttpClient was not found."
    }

    // TODO: remover o logger
    private val logger = Logger.getLogger(this.javaClass.name)

    //TODO: ver sobre inicialização do moshi
    private val moshi: Moshi = Moshi.Builder().add(MessageAdapterFactory())
        .add(DataContextAdapter()).build()
    private val job = Job()
    override val coroutineContext = job + Dispatchers.IO
    private val stub by lazy { ScreenControllerGrpcKt.ScreenControllerCoroutineStub(channel()) }

    override fun execute(request: RequestData, onSuccess: (responseData: ResponseData) -> Unit, onError: (responseData: ResponseData) -> Unit): RequestCall {
        // TODO: tratar headers
        // TODO: tratar status code
        // TODO: tratar status text

        // Se a requisição começa com grpcAddress, então trata pelo grpc
        // Senão, encaminha para o client HTTP
        return if (isGrpcRequest(request)) {
            launch {
                try {
                    getScreenName(request)?.let { screenName ->
                        val req = Messages.ScreenRequest.newBuilder().setName(screenName).build()
                        val response = stub.getScreen(req)
                        val responseData = parseResponse(response)
                        onSuccess(responseData)
                        logger.info(response.beagleComponent)
                    }

                } catch (e: StatusException) {
                    e.printStackTrace()
                    // TODO: tratar códigos de erro
                    // TODO: Não esquecer de tratar o 304 (cache)
                }
            }
            createRequestCall()
        } else {
            if (customHttpClient == null) {
                onError(ResponseData(-1, data = HTTP_CLIENT_NULL.toByteArray()))
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


    private fun parseResponse(response: Messages.ViewNode): ResponseData {

        val jsonAdapter: JsonAdapter<Messages.ViewNode> = moshi.adapter(Messages.ViewNode::class.java)
        val json = jsonAdapter.toJson(response)

        logger.info(json)


        //TODO: headers e status text
        return ResponseData(
            statusCode = 200,
            data = json.toByteArray()
        )
    }

    private fun channel(): ManagedChannel {

        val url = URL(grpcAddress)
        val port = if (url.port == -1) url.defaultPort else url.port

        logger.info("Connecting to ${url.host}:$port")

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