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

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.Toast
import br.com.zup.beagle.android.utils.renderScreen
import br.com.zup.beagle.android.view.BeagleActivity
import br.com.zup.beagle.android.view.ServerDrivenState
import kotlinx.android.synthetic.main.activity_preview.*

private const val ENDPOINT_KEY = "ENDPOINT_KEY"
private const val RECONNECT_INTERVAL_KEY = "RECONNECT_INTERVAL_KEY"
private const val TAG = "BeagleSDK"
private const val DEFAULT_INTERVAL = 1000L

class PreviewActivity : BeagleActivity() {

    private val reconnectInterval by lazy {
        intent.extras?.getLong(RECONNECT_INTERVAL_KEY, DEFAULT_INTERVAL) ?: DEFAULT_INTERVAL
    }
    private val endpoint by lazy { intent.extras?.getString(ENDPOINT_KEY) }

    companion object {
        fun newIntent(context: Context, reconnectInterval: Long? = null, endpoint: String? = null): Intent {
            return Intent(context, PreviewActivity::class.java).apply {
                putExtra(RECONNECT_INTERVAL_KEY, reconnectInterval)
                putExtra(ENDPOINT_KEY, endpoint)
            }
        }
    }

    private lateinit var beaglePreview: BeaglePreview

    override fun getToolbar(): Toolbar = tPreview

    override fun getServerDrivenContainerId() = R.id.flPreview

    override fun onServerDrivenContainerStateChanged(state: ServerDrivenState) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)

        beaglePreview = BeaglePreview(endpoint, reconnectInterval)

        beaglePreview.startListening(object : WebSocketListener {
            override fun onClose(reason: String?) {
                Log.d(TAG, "onClose: Connection closed by remote host")
                beaglePreview.reconnectSchedule()
            }

            override fun onMessage(message: String) {
                Log.d(TAG, "onMessage: $message")
                runOnUiThread {
                    if (!message.startsWith("Welcome")) {
                        flPreview.renderScreen(activity = this@PreviewActivity, screenJson = message)
                    } else {
                        Toast.makeText(this@PreviewActivity, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onError(ex: Exception?) {
                Log.w(TAG, "onError: Closed webSocket trying to reconnect")
                beaglePreview.reconnectSchedule()
            }
        })
    }

    override fun onDestroy() {
        beaglePreview.closeWebSocket()
        beaglePreview.doNotReconnect()
        super.onDestroy()
    }
}
