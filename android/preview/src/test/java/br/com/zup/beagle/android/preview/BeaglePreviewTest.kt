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

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class BeaglePreviewTest {

    private val webSocketListener = mockk<WebSocketListener>(relaxed = true)

    @Test
    fun beaglePreview_should_use_host_parameter_with_port() {
        //GIVEN
        val host = "http://host:8080/"

        //WHEN
        val subject = BeaglePreview(host)

        //THEN
        assertEquals(host, subject.uri.toString())
    }

    @Test
    fun beaglePreview_should_use_default_parameter_with_default_port() {
        //WHEN
        val subject = BeaglePreview()

        //THEN
        assertEquals(DEFAULT_ENDPOINT, subject.uri.toString())
    }

    @Test
    fun onClose_should_call_onClose_only_with_remote_is_true() {
        //GIVEN
        val listener = mockk<WebSocketListener>()
        val subject = BeaglePreview(listener = listener)
        val reason = "reason"

        //WHEN
        subject.startListening(webSocketListener)
        subject.onClose(1, reason, true)

        //THEN
        verify(exactly = 1) { webSocketListener.onClose(reason) }
    }

    @Test
    fun onClose_should_not_call_onClose_when_remote_is_false() {
        //GIVEN
        val listener = mockk<WebSocketListener>()
        val subject = BeaglePreview(listener = listener)
        val reason = "reason"

        //WHEN
        subject.startListening(webSocketListener)
        subject.onClose(1, reason, false)

        //THEN
        verify(exactly = 0) { webSocketListener.onClose(reason) }
    }

    @Test
    fun onMessage_should_call_onMessage() {
        //GIVEN
        val listener = mockk<WebSocketListener>()
        val subject = BeaglePreview(listener = listener)
        val message = "message"

        //WHEN
        subject.startListening(webSocketListener)
        subject.onMessage(message)

        //THEN
        verify(exactly = 1) { webSocketListener.onMessage(message) }
    }

    @Test
    fun onError_should_call_onError() {
        //GIVEN
        val listener = mockk<WebSocketListener>()
        val subject = BeaglePreview(listener = listener)
        val exception = Exception()

        //WHEN
        subject.startListening(webSocketListener)
        subject.onError(exception)

        //THEN
        verify(exactly = 1) { webSocketListener.onError(exception) }
    }

    @Test
    fun reconnect_should_call_close_and_start_after_one_second() {
        //GIVEN
        val timer = mockk<Timer>()
        val slotInterval = slot<Long>()
        val interval = 5000L
        every { timer.schedule(any(), capture(slotInterval)) } just Runs
        val subject = BeaglePreview(reconnectInterval = interval, timer = timer)

        //WHEN
        subject.startListening(webSocketListener)
        subject.reconnectSchedule()

        //THEN
        assertEquals(interval, slotInterval.captured)
    }
}
