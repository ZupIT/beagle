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

import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*

class BeaglePreviewTest {

    @RelaxedMockK
    private lateinit var okHttpClient: OkHttpClient

    @RelaxedMockK
    private lateinit var webSocket: WebSocket

    @MockK
    private lateinit var webSocketListener: WebSocketListener

    private val requestSlot = slot<Request>()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { okHttpClient.newWebSocket(capture(requestSlot), any()) } returns webSocket
    }

    @Test
    fun beaglePreview_should_use_host_parameter_with_default_port() {
        //GIVEN
        val host = "http://host:8080/"
        val subject = BeaglePreview(host, okHttpClient = okHttpClient)

        //WHEN
        subject.startListening(webSocketListener)

        //THEN
        assertEquals(host, requestSlot.captured.url.toString())
        verify(exactly = 1) { okHttpClient.newWebSocket(requestSlot.captured, webSocketListener) }
    }

    @Test
    fun beaglePreview_should_use_default_parameter_with_default_port() {
        //GIVEN
        val subject = BeaglePreview(okHttpClient = okHttpClient)

        //WHEN
        subject.startListening(webSocketListener)

        //THEN
        assertEquals(DEFAULT_ENDPOINT, requestSlot.captured.url.toString())
    }

    @Test
    fun startListening_should_create_webSocket() {
        //GIVEN
        val subject = BeaglePreview(okHttpClient = okHttpClient)

        //WHEN
        subject.startListening(webSocketListener)

        //THEN
        verify(exactly = 1) { okHttpClient.newWebSocket(requestSlot.captured, webSocketListener) }
    }

    @Test
    fun closeWebSocket_should_call_cancel() {
        //GIVEN
        val subject = BeaglePreview(okHttpClient = okHttpClient)

        //WHEN
        subject.startListening(webSocketListener)
        subject.closeWebSocket()

        //THEN
        verify(exactly = 1) { webSocket.cancel() }
    }

    @Test
    fun reconnect_should_call_close_and_start_after_one_second() {
        //GIVEN
        val timer = mockk<Timer>()
        val slotInterval = slot<Long>()
        val interval = 5000L
        every { timer.schedule(any(), capture(slotInterval)) } just Runs
        val subject = BeaglePreview(okHttpClient = okHttpClient, reconnectInterval = interval, timer = timer)

        //WHEN
        subject.startListening(webSocketListener)
        subject.reconnect()

        //THEN
        assertEquals(interval, slotInterval.captured)
    }
}
