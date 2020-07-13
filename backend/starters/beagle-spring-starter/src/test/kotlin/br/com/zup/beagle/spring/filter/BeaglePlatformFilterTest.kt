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

package br.com.zup.beagle.spring.filter

import br.com.zup.beagle.platform.BeaglePlatform
import br.com.zup.beagle.platform.BeaglePlatformUtil
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import javax.servlet.FilterChain
import javax.servlet.ServletOutputStream
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

internal class BeaglePlatformFilterTest {

    @Test
    fun `doFilter when all parameters are valid`() {
        val json = "{ }"
        val beaglePlatform = BeaglePlatform.MOBILE.name
        val objectMapper = mockk<ObjectMapper>()
        val jsonNode = mockk<JsonNode>()
        val request = mockk<HttpServletRequest>(relaxUnitFun = true)
        val response = mockk<HttpServletResponse>(relaxUnitFun = true)
        val chain = mockk<FilterChain>(relaxUnitFun = true)
        val outputStream = mockk<ServletOutputStream>(relaxUnitFun = true)
        val bodySlot = CapturingSlot<ByteArray>()

        every { objectMapper.readTree(any<ByteArray>()) } returns jsonNode
        every { objectMapper.writeValueAsBytes(any()) } returns json.toByteArray()
        every { request.getHeader(BeaglePlatformUtil.BEAGLE_PLATFORM_HEADER) } returns beaglePlatform
        every { response.contentType } returns MediaType.APPLICATION_JSON_VALUE
        every { response.isCommitted } returns false
        every { response.outputStream } returns outputStream

        BeaglePlatformFilter(objectMapper).doFilter(request, response, chain)

        verify { request.setAttribute(BeaglePlatformUtil.BEAGLE_PLATFORM_HEADER, beaglePlatform) }
        verify { outputStream.write(capture(bodySlot), any(), any()) }
        assertArrayEquals(json.toByteArray(), bodySlot.captured.sliceArray(json.indices))
    }

    @Test
    fun `doFilter when all parameters are valid and content type is not json`() {
        val beaglePlatform = BeaglePlatform.MOBILE.name
        val objectMapper = mockk<ObjectMapper>()
        val request = mockk<HttpServletRequest>(relaxUnitFun = true)
        val response = mockk<HttpServletResponse>(relaxUnitFun = true)
        val chain = mockk<FilterChain>(relaxUnitFun = true)
        val outputStream = mockk<ServletOutputStream>(relaxUnitFun = true)

        every { request.getHeader(BeaglePlatformUtil.BEAGLE_PLATFORM_HEADER) } returns beaglePlatform
        every { response.contentType } returns MediaType.IMAGE_PNG_VALUE
        every { response.isCommitted } returns false
        every { response.outputStream } returns outputStream

        BeaglePlatformFilter(objectMapper).doFilter(request, response, chain)

        verify { request.setAttribute(BeaglePlatformUtil.BEAGLE_PLATFORM_HEADER, beaglePlatform) }
    }

    @Test
    fun `doFilter when request is null`() =
        this.testPlatformFilterIsNoOp(null, mockk(), mockk())

    @Test
    fun `doFilter when request is not HTTPServletRequest`() =
        this.testPlatformFilterIsNoOp(mockk(), mockk<HttpServletResponse>(), mockk())

    @Test
    fun `doFilter when response is null`() =
        this.testPlatformFilterIsNoOp(mockk(), null, mockk())

    @Test
    fun `doFilter when response is not HTTPServletResponse`() =
        this.testPlatformFilterIsNoOp(mockk<HttpServletRequest>(), mockk(), mockk())

    @Test
    fun `doFilter when chain is null`() =
        this.testPlatformFilterIsNoOp(mockk(), mockk(), null)

    private fun testPlatformFilterIsNoOp(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) =
        testFilterIsNoOp(BeaglePlatformFilter(mockk()), request, response, chain)
}