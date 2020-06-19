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

package br.com.zup.beagle.android.logger

import android.util.Log
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.testutil.RandomData
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test

private const val BEAGLE_TAG = "BeagleSDK"
private val LOG = RandomData.string()

class BeagleLoggerProxyTest {

    @Before
    fun setUp() {
        mockkObject(BeagleEnvironment)
        mockkStatic(Log::class)

        every { Log.w(any(), any<String>()) }returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.v(any(), any()) } returns 0

        every { BeagleEnvironment.beagleSdk.logger } returns null
    }

    @After
    fun tearDown() {
        unmockkObject(BeagleEnvironment)
        unmockkStatic(Log::class)
    }

    @Test
    fun warning_should_call_Log_w_if_is_enable() {
        // Given
        every { BeagleEnvironment.beagleSdk.config.isLoggingEnabled } returns true

        // When
        BeagleLoggerProxy.warning(LOG)

        // Then
        verify(exactly = once()) { Log.w(BEAGLE_TAG, LOG) }
    }

    @Test
    fun warning_should_not_call_Log_w_if_is_not_enable() {
        // Given
        every { BeagleEnvironment.beagleSdk.config.isLoggingEnabled } returns false

        // When
        BeagleLoggerProxy.warning(LOG)

        // Then
        verify(exactly = 0) { Log.w(BEAGLE_TAG, LOG) }
    }

    @Test
    fun error_should_call_Log_w_if_is_enable() {
        // Given
        every { BeagleEnvironment.beagleSdk.config.isLoggingEnabled } returns true

        // When
        BeagleLoggerProxy.error(LOG)

        // Then
        verify(exactly = once()) { Log.e(BEAGLE_TAG, LOG) }
    }

    @Test
    fun error_should_not_call_Log_w_if_is_not_enable() {
        // Given
        every { BeagleEnvironment.beagleSdk.config.isLoggingEnabled } returns false

        // When
        BeagleLoggerProxy.error(LOG)

        // Then
        verify(exactly = 0) { Log.e(BEAGLE_TAG, LOG) }
    }

    @Test
    fun info_should_call_Log_w_if_is_enable() {
        // Given
        every { BeagleEnvironment.beagleSdk.config.isLoggingEnabled } returns true

        // When
        BeagleLoggerProxy.info(LOG)

        // Then
        verify(exactly = once()) { Log.i(BEAGLE_TAG, LOG) }
    }

    @Test
    fun info_should_not_call_Log_w_if_is_not_enable() {
        // Given
        every { BeagleEnvironment.beagleSdk.config.isLoggingEnabled } returns false

        // When
        BeagleLoggerProxy.info(LOG)

        // Then
        verify(exactly = 0) { Log.i(BEAGLE_TAG, LOG) }
    }
}