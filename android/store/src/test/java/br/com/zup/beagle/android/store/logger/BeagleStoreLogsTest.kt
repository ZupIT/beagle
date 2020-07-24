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

package br.com.zup.beagle.android.store.logger

import br.com.zup.beagle.android.logger.BeagleLoggerProxy
import br.com.zup.beagle.android.testutil.RandomData
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test

class BeagleStoreLogsTest {

    private val beagleLoggerInfoSlot = slot<String>()

    @Before
    fun setUp() {
        mockkObject(BeagleLoggerProxy)

        every { BeagleLoggerProxy.info(capture(beagleLoggerInfoSlot)) } just Runs
        every { BeagleLoggerProxy.warning(any()) } just Runs
        every { BeagleLoggerProxy.error(any()) } just Runs
        every { BeagleLoggerProxy.error(any(), any()) } just Runs
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun logDataNotInsertedOnDatabase_should_call_BeagleLogger_warning() {
        // Given
        val key = RandomData.string()
        val value = RandomData.string()

        // When
        BeagleStoreLogs.logDataNotInsertedOnDatabase(key, value)

        // Then
        verify(exactly = 1) {
            BeagleLoggerProxy.warning("Error when trying to insert key=$key " +
                "with value=$value on Beagle default database.")
        }

    }

}