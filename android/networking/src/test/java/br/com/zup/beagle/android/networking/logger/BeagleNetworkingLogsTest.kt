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

package br.com.zup.beagle.android.networking.logger

import br.com.zup.beagle.android.logger.BeagleLoggerProxy
import br.com.zup.beagle.android.networking.fake.makeRequestData
import br.com.zup.beagle.android.networking.fake.makeResponseData
import br.com.zup.beagle.android.testutil.RandomData
import io.mockk.*
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class BeagleNetworkingLogsTest {

    private val beagleLoggerInfoSlot = slot<String>()

    @Before
    fun setUp() {
        mockkObject(BeagleLoggerProxy)

        every { BeagleLoggerProxy.info(capture(beagleLoggerInfoSlot)) } just Runs
        every {  BeagleLoggerProxy.warning(any()) } just Runs
        every {  BeagleLoggerProxy.error(any()) } just Runs
        every {  BeagleLoggerProxy.error(any(), any()) } just Runs
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun logHttpRequestData_should_call_BeagleLogger_info() {
        // Given
        val requestData = makeRequestData()

        // When
        BeagleNetworkingLogs.logHttpRequestData(requestData)

        // Then
        Assert.assertEquals("""
            *** HTTP REQUEST ***
            Uri=${requestData.uri}
            Method=${requestData.method}
            Headers=${requestData.headers}
            Body=${requestData.body}
        """.trimIndent(), beagleLoggerInfoSlot.captured)
    }

    @Test
    fun logHttpResponseData_should_call_BeagleLogger_info() {
        // Given
        val responseData = makeResponseData()

        // When
        BeagleNetworkingLogs.logHttpResponseData(responseData)

        // Then
        Assert.assertEquals("""
            *** HTTP RESPONSE ***
            StatusCode=${responseData.statusCode}
            Body=${String(responseData.data)}
            Headers=${responseData.headers}
        """.trimIndent(), beagleLoggerInfoSlot.captured)
    }

    @Test
    fun logUnknownHttpError_should_call_BeagleLogger_error() {
        // Given
        val throwable = mockk<Throwable>()

        // When
        BeagleNetworkingLogs.logUnknownHttpError(throwable)

        // Then
        verify(exactly = 1) {  BeagleLoggerProxy.error("Exception thrown while trying to call http client.", throwable) }
    }
}