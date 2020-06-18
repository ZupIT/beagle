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

package br.com.zup.beagle.android.data

import br.com.zup.beagle.android.networking.RequestData
import br.com.zup.beagle.android.networking.urlbuilder.UrlBuilder
import br.com.zup.beagle.android.setup.BeagleEnvironment
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.URI

class StringExtensionsKtTest {

    private val urlBuilder: UrlBuilder = mockk()
    private val environment: BeagleEnvironment = mockk(relaxed = true)

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `should return request data when using extension function to mapper`() {
        // Given
        mockkStatic("br.com.zup.beagle.android.data.StringExtensionsKt")
        every { any<String>().formatUrl(any(), any()) } returns ""

        // When
        val requestData = "".toRequestData(urlBuilder, environment)

        // Then
        val expectedResult = RequestData(URI(""))
        assertEquals(expectedResult, requestData)
    }

    @Test
    fun `should return new url when using extension function to format`() {
        // Given
        every { urlBuilder.format(any(), "") } returns ""

        // When
        val requestData = "".formatUrl(urlBuilder, environment)

        // Then
        assertEquals("", requestData)
    }


}