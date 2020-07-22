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

package br.com.zup.beagle.android.preview

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import java.util.*
import kotlin.concurrent.timerTask

internal const val DEFAULT_ENDPOINT = "http://10.0.2.2:9721/"

interface WebSocketListener {
    fun onClose(reason: String?)
    fun onMessage(message: String)
    fun onError(ex: Exception?)
}

class BeaglePreview(
    host: String? = null,
    private val reconnectInterval: Long = 1000,
    private val timer: Timer = Timer(),
    private var shouldReconnect: Boolean = true,
    private var listener: WebSocketListener? = null
) : WebSocketClient(URI(host ?: DEFAULT_ENDPOINT)) {

    fun startListening(listener: WebSocketListener) {
        this.listener = listener
        connect()
    }

    override fun onOpen(handshakedata: ServerHandshake?) {}

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        if (remote) {
            listener?.onClose(reason)
        }
    }

    override fun onMessage(message: String) {
        listener?.onMessage(message)
    }

    override fun onError(ex: Exception?) {
        listener?.onError(ex)
    }

    fun reconnectSchedule() {
        if (shouldReconnect) {
            timer.schedule(timerTask {
                reconnect()
            }, reconnectInterval)
        }
    }

    fun closeWebSocket() {
        close()
    }

    fun doNotReconnect() {
        shouldReconnect = false
    }
}
