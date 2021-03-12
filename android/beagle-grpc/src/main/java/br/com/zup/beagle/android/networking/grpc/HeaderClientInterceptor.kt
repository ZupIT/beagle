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

import io.grpc.CallOptions
import io.grpc.Channel
import io.grpc.ClientCall
import io.grpc.ClientInterceptor
import io.grpc.ForwardingClientCall
import io.grpc.ForwardingClientCallListener
import io.grpc.Metadata
import io.grpc.MethodDescriptor
import io.grpc.stub.MetadataUtils
import java.util.logging.Logger

class HeaderClientInterceptor : ClientInterceptor {

    private val logger: Logger = Logger.getLogger(HeaderClientInterceptor::class.java.name)

    val CUSTOM_HEADER_KEY: Metadata.Key<String> = Metadata.Key.of("custom_client_header_key", Metadata.ASCII_STRING_MARSHALLER)

    override fun <ReqT : Any?, RespT : Any?> interceptCall(method: MethodDescriptor<ReqT, RespT>?, callOptions: CallOptions?, next: Channel?): ClientCall<ReqT, RespT> {
        return object : ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next!!.newCall(method, callOptions)) {
            override fun start(responseListener: Listener<RespT>?, headers: Metadata) {
                /* put custom header */
//                headers.put(CUSTOM_HEADER_KEY, "customRequestValue")
                super.start(object : ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(responseListener) {
                    override fun onHeaders(headers: Metadata) {
                        /**
                         * if you don't need receive header from server,
                         * you can use [io.grpc.stub.MetadataUtils.attachHeaders]
                         * directly to send header
                         */
                        logger.info("header received from server:$headers")
                        super.onHeaders(headers)
                    }
                }, headers)
            }
        }
    }
}