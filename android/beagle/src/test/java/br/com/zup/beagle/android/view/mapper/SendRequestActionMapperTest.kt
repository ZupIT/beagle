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

package br.com.zup.beagle.android.view.mapper

import br.com.zup.beagle.action.RequestActionMethod
import br.com.zup.beagle.action.SendRequestAction
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.data.formatUrl
import br.com.zup.beagle.android.networking.HttpMethod
import br.com.zup.beagle.android.networking.RequestData
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.URI

private val SEND_REQUEST_ACTION = SendRequestAction("", body = "body",
    headers = mapOf("header" to "teste"))
private val EXPECTED_RESULT = RequestData(uri = URI(""), body = "body",
    headers = mapOf("header" to "teste"))

class SendRequestActionMapperTest : BaseTest() {

    override fun setUp() {
        super.setUp()

        mockkStatic("br.com.zup.beagle.android.data.StringExtensionsKt")
        every { any<String>().formatUrl(any(), any()) } returns ""
    }

    override fun tearDown() {
        super.tearDown()
        unmockkAll()
    }

    @Test
    fun `should return request data when using extension function to mapper`() {
        // When
        val requestData = SEND_REQUEST_ACTION.toRequestData()

        // Then
        assertEquals(EXPECTED_RESULT, requestData)
    }

    @Test
    fun `should return request data when mapper`() {
        // When
        val requestData = SendRequestActionMapper.toRequestData(SEND_REQUEST_ACTION)

        // Then
        assertEquals(EXPECTED_RESULT, requestData)
    }

    @Test
    fun `should return request data with correct method when mapper`() {
        val sendRequestActions = listOf(
            SEND_REQUEST_ACTION.copy(method = RequestActionMethod.GET),
            SEND_REQUEST_ACTION.copy(method = RequestActionMethod.POST),
            SEND_REQUEST_ACTION.copy(method = RequestActionMethod.PUT),
            SEND_REQUEST_ACTION.copy(method = RequestActionMethod.DELETE),
            SEND_REQUEST_ACTION.copy(method = RequestActionMethod.HEAD),
            SEND_REQUEST_ACTION.copy(method = RequestActionMethod.PATCH)
        )

        // When
        val result = mutableListOf<RequestData>()
        sendRequestActions.forEach {
            result.add(SendRequestActionMapper.toRequestData(it))
        }

        // Then
        val expectedResult = sendRequestActions.map {
            EXPECTED_RESULT.copy(method = HttpMethod.valueOf(it.method.name))
        }
        assertEquals(expectedResult, result)
    }

}