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

package br.com.zup.beagle.android.networking.urlbuilder

import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.setup.BeagleEnvironment
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Test
import io.mockk.impl.annotations.InjectMockKs
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UrlBuilderFactoryTest : BaseTest() {

    @InjectMockKs
    private lateinit var urlBuilderFactory: UrlBuilderFactory

    @MockK
    private lateinit var urlBuilder: UrlBuilder

    @Test
    fun make_should_return_default_builder() {
        // Given
        every { BeagleEnvironment.beagleSdk.urlBuilder } returns null

        // When
        val builder = urlBuilderFactory.make()

        // Then
        assertTrue { builder is UrlBuilderDefault }
    }

    @Test
    fun make_should_return_custom_dispatcher() {
        // Given
        every { BeagleEnvironment.beagleSdk.urlBuilder } returns urlBuilder

        // When
        val actual = urlBuilderFactory.make()

        // Then
        assertEquals(urlBuilder, actual)
    }
}