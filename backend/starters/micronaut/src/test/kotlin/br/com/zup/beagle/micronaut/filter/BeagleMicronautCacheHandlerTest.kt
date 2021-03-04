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

import br.com.zup.beagle.micronaut.configuration.BeagleMicronautCacheProperties
import io.micronaut.http.HttpResponseFactory
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.filter.ServerFilterChain
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Flowable
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class BeagleMicronautCacheHandlerTest {
    @Test
    fun `Test createResponseFromController`() {
        val chain = mockk<ServerFilterChain>()
        val response: MutableHttpResponse<*> = HttpResponseFactory.INSTANCE.status(HttpStatus.OK, Unit)
        val publisher = Flowable.just(response)

        fun testingFunction(input: Any): Any {
            return (input as MutableHttpResponse<*>).status(HttpStatus.FOUND)
        }

        every { chain.proceed(any()) } returns publisher
        val result = BeagleMicronautCacheHandler(
            request = mockk(),
            chain = chain,
            properties = BeagleMicronautCacheProperties()
        ).createResponseFromController{
            (it as MutableHttpResponse<*>).status(HttpStatus.FOUND)
        } as Flowable<MutableHttpResponse<*>>

        val single = result.blockingSingle()

        assertEquals(single.status, HttpStatus.FOUND)
    }

    @Test
    fun `Test createResponse`() {
        val result = BeagleMicronautCacheHandler(mockk(), mockk(), mockk()).createResponse(200)

        val single = result.blockingSingle()
        assertEquals(HttpStatus.OK, single.status)
        assertEquals(single.body.get(), Unit)

    }

//    @Test
//    fun `Test getBody`() {
//        val response = mockk<MutableHttpResponse<*>>()
//
//        every { response.body } returns Optional.of(STRING)
//
//        val result = BeagleMicronautCacheHandler(mockk(), mockk()).getBody(response)
//
//        assertEquals("Optional[$STRING]", result)
//    }
//
//    @Test
//    fun `Test addHeader`() {
//        val response = mockk<MutableHttpResponse<*>>()
//
//        every { response.header(any(), any()) } returns response
//
//        BeagleMicronautCacheHandler(mockk(), mockk()).addHeader(response, STRING, STRING)
//
//        verifyAll { response.header(STRING, STRING) }
//    }
}