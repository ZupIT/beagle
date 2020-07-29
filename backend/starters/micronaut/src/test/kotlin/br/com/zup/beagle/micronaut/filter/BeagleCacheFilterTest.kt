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

package br.com.zup.beagle.micronaut.filter

import br.com.zup.beagle.cache.BeagleCacheHandler
import br.com.zup.beagle.constants.BEAGLE_CACHE_ENABLED
import br.com.zup.beagle.micronaut.STRING
import br.com.zup.beagle.micronaut.containsBeans
import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.filter.ServerFilterChain
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import io.reactivex.subscribers.TestSubscriber
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class BeagleCacheFilterTest {
    @Test
    fun `Test beagleCacheFilter is in context by default`() {
        assertTrue { ApplicationContext.run().containsBeans(BeagleCacheFilter::class) }
    }

    @Test
    fun `Test beagleCacheFilter is in context when enabled is true`() {
        assertTrue {
            ApplicationContext.run(mapOf(BEAGLE_CACHE_ENABLED to true)).containsBeans(BeagleCacheFilter::class)
        }
    }

    @Test
    fun `Test beagleCacheFilter is not in context when enabled is false`() {
        assertFalse {
            ApplicationContext.run(mapOf(BEAGLE_CACHE_ENABLED to false)).containsBeans(BeagleCacheFilter::class)
        }
    }

    @Test
    fun `Test doFilter when all parameters are valid`() {
        val handler = mockk<BeagleCacheHandler>()
        val request = mockk<HttpRequest<*>>()
        val headers = mockk<HttpHeaders>()
        val response = HttpResponse.ok(Unit)

        every { handler.handleCache<MutableHttpResponse<*>>(any(), any(), any(), any()) } returns response
        every { request.path } returns STRING
        every { request.headers } returns headers
        every { headers[any()] } returns STRING

        val chain = mockk<ServerFilterChain>()
        val result = BeagleCacheFilter(handler).doFilter(request, chain)

        assertNotNull(result)
        TestSubscriber<MutableHttpResponse<*>>().also {
            result.subscribe(it)
            it.assertComplete()
            it.assertValue(response)
        }
        verifyAll { handler.handleCache(STRING, STRING, STRING, any()) }
    }

    @Test
    fun `Test doFilter when request is null`() {
        val result = BeagleCacheFilter(mockk()).doFilter(null, mockk())

        assertNull(result)
    }

    @Test
    fun `Test doFilter when chain is null`() {
        val result = BeagleCacheFilter(mockk()).doFilter(mockk(), null)

        assertNull(result)
    }
}