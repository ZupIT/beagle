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

package br.com.zup.beagle.spring.interceptor

import br.com.zup.beagle.platform.BeaglePlatform
import br.com.zup.beagle.platform.BeaglePlatformUtil
import br.com.zup.beagle.serialization.jackson.BeagleSerializationUtil
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.web.util.ContentCachingResponseWrapper
import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletRequest

internal class BeaglePlatformInterceptorTest {

    private val objectMapper = BeagleSerializationUtil.beagleObjectMapper()
    private val beaglePlatformInterceptor = BeaglePlatformInterceptor(this.objectMapper)

    @Test
    fun preHandle() {
        val request = mockk<HttpServletRequest>()
        val beaglePlatform = BeaglePlatform.MOBILE.name

        every {
            request.getHeader(BeaglePlatformUtil.BEAGLE_PLATFORM_HEADER)
        } returns beaglePlatform
        every {
            request.setAttribute(BeaglePlatformUtil.BEAGLE_PLATFORM_HEADER, beaglePlatform)
        } returns Unit

        this.beaglePlatformInterceptor.preHandle(request, mockk(), mockk())
        verify {
            request.setAttribute(BeaglePlatformUtil.BEAGLE_PLATFORM_HEADER, beaglePlatform)
        }
    }

    @Test
    fun postHandle() {
        val json = "{ }"
        val request = mockk<HttpServletRequest>()
        val response = mockk<ContentCachingResponseWrapper>()
        val outputStream = mockk<ServletOutputStream>()

        every {
            request.getHeader(BeaglePlatformUtil.BEAGLE_PLATFORM_HEADER)
        } returns null
        every {
            response.contentAsByteArray
        } returns json.toByteArray()
        every {
            outputStream.write(json.toByteArray())
        } returns Unit
        every {
            response.outputStream
        } returns outputStream
        every {
            response.resetBuffer()
        } returns Unit
        every {
            response.setContentLength(json.length)
        } returns Unit

        this.beaglePlatformInterceptor.postHandle(
            request = request,
            response = response,
            handler = mockk(),
            modelAndView = mockk()
        )
        verify {
            response.resetBuffer()
            outputStream.write(json.toByteArray())
            response.setContentLength(json.length)
        }
    }
}