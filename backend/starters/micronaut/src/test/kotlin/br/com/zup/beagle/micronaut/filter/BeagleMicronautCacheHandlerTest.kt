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

import br.com.zup.beagle.micronaut.STRING
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.filter.ServerFilterChain
import io.mockk.*
import io.reactivex.Flowable
import org.junit.jupiter.api.Test
import org.reactivestreams.Publisher
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertSame

internal class BeagleMicronautCacheHandlerTest {
    @Test
    fun `Test createResponseFromController`() {
        val chain = mockk<ServerFilterChain>()
        val publisher = mockk<Flowable<MutableHttpResponse<*>>>()
        val response = mockk<MutableHttpResponse<*>>()

        every { chain.proceed(any()) } returns publisher
        every { publisher.blockingFirst() } returns response

        val result = BeagleMicronautCacheHandler(mockk(), chain).createResponseFromController()

        assertSame(response, result)
    }

    @Test
    fun `Test createResponse`() {
        val result = BeagleMicronautCacheHandler(mockk(), mockk()).createResponse(200)

        assertEquals(Unit, result.body.get())
        assertEquals(HttpStatus.OK, result.status)
    }

    @Test
    fun `Test getBody`() {
        val response = mockk<MutableHttpResponse<*>>()

        every { response.body } returns Optional.of(STRING)

        val result = BeagleMicronautCacheHandler(mockk(), mockk()).getBody(response)

        assertEquals("Optional[$STRING]", result)
    }

    @Test
    fun `Test addHeader`() {
        val response = mockk<MutableHttpResponse<*>>()

        every { response.header(any(), any()) } returns response

        BeagleMicronautCacheHandler(mockk(), mockk()).addHeader(response, STRING, STRING)

        verifyAll { response.header(STRING, STRING) }
    }
}