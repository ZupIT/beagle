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

import br.com.zup.beagle.platform.BeaglePlatformUtil
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.util.ContentCachingResponseWrapper
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class BeaglePlatformFilter(private val objectMapper: ObjectMapper) : Filter {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        if (chain != null && request is HttpServletRequest && response is HttpServletResponse) {
            request.setAttribute(
                BeaglePlatformUtil.BEAGLE_PLATFORM_HEADER,
                request.getHeader(BeaglePlatformUtil.BEAGLE_PLATFORM_HEADER)
            )
            val responseWrapper = ContentCachingResponseWrapper(response)
            chain.doFilter(request, responseWrapper)
            val jsonData = this.objectMapper.readTree(responseWrapper.contentAsByteArray).also {
                BeaglePlatformUtil.treatBeaglePlatform(request.getHeader(BeaglePlatformUtil.BEAGLE_PLATFORM_HEADER), it)
            }.toPrettyString()
            responseWrapper.resetBuffer()
            responseWrapper.outputStream.write(jsonData.toByteArray())
            responseWrapper.copyBodyToResponse()
        }
    }
}