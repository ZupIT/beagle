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

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.slot
import io.mockk.verify
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

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
        val host = "http://host"
        val subject = BeaglePreview(host, okHttpClient)

        //WHEN
        subject.start(webSocketListener)

        //THEN
        assertEquals("$host:$DEFAULT_PORT/", requestSlot.captured.url.toString())
        verify(exactly = 1) { okHttpClient.newWebSocket(requestSlot.captured, webSocketListener) }
    }

    @Test
    fun beaglePreview_should_use_default_parameter_with_default_port() {
        //GIVEN
        val subject = BeaglePreview(okHttpClient = okHttpClient)

        //WHEN
        subject.start(webSocketListener)

        //THEN
        assertEquals("$PREVIEW_ENDPOINT:$DEFAULT_PORT/", requestSlot.captured.url.toString())
    }

    @Test
    fun start_should_create_webSocket() {
        //GIVEN
        val subject = BeaglePreview(okHttpClient = okHttpClient)

        //WHEN
        subject.start(webSocketListener)

        //THEN
        verify(exactly = 1) { okHttpClient.newWebSocket(requestSlot.captured, webSocketListener) }
    }

    @Test
    fun close_should_use_code_and_reason_default() {
        //GIVEN
        val subject = BeaglePreview(okHttpClient = okHttpClient)

        //WHEN
        subject.start(webSocketListener)
        subject.close()

        //THEN
        verify(exactly = 1) { webSocket.close(code = CLOSE_CODE, reason = CLOSE_REASON) }
    }

    @Test
    fun close_should_close_webSocket() {
        //GIVEN
        val subject = BeaglePreview(okHttpClient = okHttpClient)
        val closeCodeSlot = slot<Int>()
        val closeReasonSlot = slot<String>()
        every { webSocket.close(capture(closeCodeSlot), capture(closeReasonSlot)) } returns true

        //WHEN
        subject.start(webSocketListener)
        subject.close()

        //THEN
        assertEquals(CLOSE_CODE, closeCodeSlot.captured)
        assertEquals(CLOSE_REASON, closeReasonSlot.captured)
    }
}
