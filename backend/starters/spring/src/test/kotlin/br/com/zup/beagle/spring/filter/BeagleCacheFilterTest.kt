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

import br.com.zup.beagle.cache.BeagleCacheHandler
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import org.junit.jupiter.api.Test
import org.springframework.web.util.ContentCachingResponseWrapper
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

internal class BeagleCacheFilterTest {
    companion object {
        const val STRING = "test"
        const val GET = "get"
        const val OPTIONS = "options"
    }

    @Test
    fun `doFilter when all parameters are valid and request method is GET`() {
        val cacheHandler = mockk<BeagleCacheHandler>()
        val request = mockk<HttpServletRequest>()
        val response = mockk<HttpServletResponse>()
        val chain = mockk<FilterChain>()
        val wrapper = mockk<ContentCachingResponseWrapper>(relaxUnitFun = true)

        every { request.requestURI } returns STRING
        every { request.getHeader(any()) } returns STRING
        every { request.method } returns GET
        every {
            cacheHandler.handleCache<ContentCachingResponseWrapper>(any(), any(), any(), any(), any())
        } returns wrapper

        BeagleCacheFilter(cacheHandler).doFilter(request, response, chain)

        verifyAll { cacheHandler.handleCache(STRING, STRING, STRING, any(), any()) }
        verifyAll { wrapper.copyBodyToResponse() }
    }

    @Test
    fun `doFilter when all parameters are valid and request method is not GET`() {
        val cacheHandler = mockk<BeagleCacheHandler>()
        val request = mockk<HttpServletRequest>()
        val response = mockk<HttpServletResponse>()
        val chain = mockk<FilterChain>(relaxUnitFun = true)

        every { request.requestURI } returns STRING
        every { request.getHeader(any()) } returns STRING
        every { request.method } returns OPTIONS

        BeagleCacheFilter(cacheHandler).doFilter(request, response, chain)

        verifyAll { chain.doFilter(request, response) }
    }

    @Test
    fun `doFilter when request is null`() =
        this.testCacheFilterIsNoOp(null, mockk(), mockk())

    @Test
    fun `doFilter when request is not HTTPServletRequest`() =
        this.testCacheFilterIsNoOp(mockk(), mockk<HttpServletResponse>(), mockk())

    @Test
    fun `doFilter when response is null`() =
        this.testCacheFilterIsNoOp(mockk(), null, mockk())

    @Test
    fun `doFilter when response is not HTTPServletResponse`() =
        this.testCacheFilterIsNoOp(mockk<HttpServletRequest>(), mockk(), mockk())

    @Test
    fun `doFilter when chain is null`() =
        this.testCacheFilterIsNoOp(mockk(), mockk(), null)

    private fun testCacheFilterIsNoOp(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) =
        testFilterIsNoOp(BeagleCacheFilter(mockk()), request, response, chain)
}
