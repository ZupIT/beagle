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

import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.web.util.ContentCachingResponseWrapper
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest

internal class BeagleSpringCacheHandlerTest {
    companion object {
        const val STRING = "test"
    }

    @Test
    fun `Test createResponseFromController`() {
        val request = mockk<HttpServletRequest>()
        val wrapper = mockk<ContentCachingResponseWrapper>()
        val chain = mockk<FilterChain>(relaxUnitFun = true)

        val result = BeagleSpringCacheHandler(request, wrapper, chain).createResponseFromController()

        assertSame(wrapper, result)
        verifyAll { chain.doFilter(request, wrapper) }
    }

    @Test
    fun `Test createResponse`() {
        val wrapper = mockk<ContentCachingResponseWrapper>(relaxUnitFun = true)

        val result = BeagleSpringCacheHandler(mockk(), wrapper, mockk()).createResponse(0)

        assertSame(wrapper, result)
        verifyAll { wrapper.status = 0 }
    }

    @Test
    fun `Test getBody`() {
        val wrapper = mockk<ContentCachingResponseWrapper>()

        every { wrapper.contentAsByteArray } returns STRING.toByteArray()

        val result = BeagleSpringCacheHandler(mockk(), mockk(), mockk()).getBody(wrapper)

        assertEquals(STRING, result)
    }

    @Test
    fun `Test addHeader`() {
        val wrapper = mockk<ContentCachingResponseWrapper>(relaxUnitFun = true)

        val result = BeagleSpringCacheHandler(mockk(), mockk(), mockk()).addHeader(wrapper, STRING, STRING)

        assertSame(wrapper, result)
        verifyAll { wrapper.setHeader(STRING, STRING) }
    }
}