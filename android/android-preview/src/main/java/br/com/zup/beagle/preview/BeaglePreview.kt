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

package br.com.zup.beagle.preview

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener

internal const val PREVIEW_ENDPOINT = "http://10.0.2.2"
internal const val DEFAULT_PORT = "9721"
internal const val CLOSE_CODE = 1001
internal const val CLOSE_REASON = "onDestroy"

class BeaglePreview(host: String? = null, private val okHttpClient: OkHttpClient = OkHttpClient()) {

    private val request: Request = Request
        .Builder()
        .url("${host ?: PREVIEW_ENDPOINT}:$DEFAULT_PORT")
        .build()
    private lateinit var webSocket: WebSocket

    fun start(webSocketListener: WebSocketListener) {
        webSocket = okHttpClient.newWebSocket(request, webSocketListener)
    }

    fun close() {
        webSocket.close(code = CLOSE_CODE, reason = CLOSE_REASON)
    }
}
