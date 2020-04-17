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

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import br.com.zup.beagle.utils.renderScreen
import br.com.zup.beagle.view.BeagleActivity
import br.com.zup.beagle.view.ServerDrivenState
import kotlinx.android.synthetic.main.activity_preview.*
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

private const val ENDPOINT_KEY = "ENDPOINT_KEY"
private const val TAG = "BeagleSDK"

class PreviewActivity : BeagleActivity() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, PreviewActivity::class.java)
        }
    }

    private lateinit var beaglePreview: BeaglePreview

    override fun getToolbar(): Toolbar = tPreview

    override fun getServerDrivenContainerId() = R.id.flPreview

    override fun onServerDrivenContainerStateChanged(state: ServerDrivenState) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)

        beaglePreview = BeaglePreview(intent.extras?.getString(ENDPOINT_KEY))

        beaglePreview.start(object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log.d(TAG, "onOpen: ${response.message}")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                Log.d(TAG, "onMessage: $text")
                runOnUiThread {
                    if (!text.startsWith("Welcome")) {
                        flPreview.renderScreen(context = this@PreviewActivity, screenJson = text)
                    } else {
                        Toast.makeText(this@PreviewActivity, text, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                Log.d(TAG, "onClosing: $reason")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                Log.d(TAG, "onClosed: $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Log.e(TAG, "onFailure: webSocket closed")
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        beaglePreview.close()
    }
}