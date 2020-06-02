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
import java.util.Timer
import kotlin.concurrent.timerTask

internal const val DEFAULT_ENDPOINT = "http://10.0.2.2:9721/"

class BeaglePreview(
    host: String? = null,
    private val reconnectInterval: Long = 1000,
    private val okHttpClient: OkHttpClient = OkHttpClient(),
    private val timer: Timer = Timer()
) {

    private val request: Request = Request
        .Builder()
        .url(host ?: DEFAULT_ENDPOINT)
        .build()
    private lateinit var webSocket: WebSocket
    private lateinit var webSocketListener: WebSocketListener
    private var shouldReconnect = true

    fun startListening(listener: WebSocketListener? = null) {
        listener?.let {
            webSocketListener = it
        }
        webSocket = okHttpClient.newWebSocket(request, webSocketListener)
    }

    fun closeWebSocket() {
        webSocket.cancel()
    }

    fun doNotReconnect() {
        shouldReconnect = false
    }

    fun reconnect() {
        if (shouldReconnect) {
            timer.schedule(timerTask {
                closeWebSocket()
                startListening()
            }, reconnectInterval)
        }
    }
}
