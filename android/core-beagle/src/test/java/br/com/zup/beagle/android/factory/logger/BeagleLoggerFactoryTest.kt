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

package br.com.zup.beagle.android.factory.logger

import br.com.zup.beagle.android.logger.BeagleLogger
import br.com.zup.beagle.android.logger.BeagleLoggerDefault
import br.com.zup.beagle.android.setup.BeagleEnvironment
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BeagleLoggerFactoryTest {

    @InjectMockKs
    private lateinit var beagleLoggerFactory: BeagleLoggerFactory

    @MockK
    private lateinit var loggerDispatcher: BeagleLogger

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        mockkObject(BeagleEnvironment)
        every { BeagleEnvironment.beagleSdk.logger } returns null
    }

    @After
    fun after() {
        unmockkObject(BeagleEnvironment)
    }

    @Test
    fun test_make_should_return_default_dispatcher() {
        val response = beagleLoggerFactory.make()
        assertTrue { response is BeagleLoggerDefault }
    }

    @Test
    fun test_make_should_return_custom_dispatcher() {
        // Given
        every { BeagleEnvironment.beagleSdk.logger } returns loggerDispatcher

        // When
        val actual = beagleLoggerFactory.make()

        // Then
        assertEquals(loggerDispatcher, actual)
    }
}