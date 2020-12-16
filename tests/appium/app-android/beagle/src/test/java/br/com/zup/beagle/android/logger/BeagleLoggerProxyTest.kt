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

import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.testutil.RandomData
import io.mockk.*
import kotlin.math.log
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

private val LOG = RandomData.string()

class BeagleLoggerProxyTest {

    private val logger = mockk<BeagleLogger>(relaxUnitFun = true, relaxed = true)

    @BeforeEach
    fun setUp() {
        mockkObject(BeagleEnvironment)

        every { BeagleEnvironment.beagleSdk.config.isLoggingEnabled } returns true
        every { BeagleEnvironment.beagleSdk.logger } returns logger
        BeagleLoggerProxy.logger = logger
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun warning_should_call_Log_w_if_is_enable() {
        // When
        BeagleLoggerProxy.warning(LOG)

        // Then
        verify(exactly = once()) { logger.warning(LOG) }
    }

    @Test
    fun warning_should_not_call_Log_w_if_is_not_enable() {
        // Given
        every { BeagleEnvironment.beagleSdk.config.isLoggingEnabled } returns false

        // When
        BeagleLoggerProxy.warning(LOG)

        // Then
        verify(exactly = 0) { logger.warning(LOG) }
    }

    @Test
    fun error_should_call_Log_w_if_is_enable() {

        // When
        BeagleLoggerProxy.error(LOG)

        // Then
        verify(exactly = once()) { logger.error(LOG) }
    }

    @Test
    fun error_should_not_call_Log_w_if_is_not_enable() {
        // Given
        every { BeagleEnvironment.beagleSdk.config.isLoggingEnabled } returns false

        // When
        BeagleLoggerProxy.error(LOG)

        // Then
        verify(exactly = 0) { logger.error(LOG) }
    }

    @Test
    fun info_should_call_Log_w_if_is_enable() {
        // When
        BeagleLoggerProxy.info(LOG)

        // Then
        verify(exactly = once()) { logger.info(LOG) }
    }

    @Test
    fun info_should_not_call_Log_w_if_is_not_enable() {
        // Given
        every { BeagleEnvironment.beagleSdk.config.isLoggingEnabled } returns false

        // When
        BeagleLoggerProxy.info(LOG)

        // Then
        verify(exactly = 0) { logger.info(LOG) }
    }
}