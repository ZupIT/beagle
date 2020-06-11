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

import br.com.zup.beagle.platform.BeaglePlatformUtil
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.util.ContentCachingResponseWrapper
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class BeaglePlatformInterceptor(private val objectMapper: ObjectMapper) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        request.setAttribute(
            BeaglePlatformUtil.BEAGLE_PLATFORM_HEADER,
            request.getHeader(BeaglePlatformUtil.BEAGLE_PLATFORM_HEADER)
        )
        return true
    }

    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?
    ) {
        val responseWrapper = (response as ContentCachingResponseWrapper)
        val jsonTree = this.objectMapper.readTree(responseWrapper.contentAsByteArray)
        BeaglePlatformUtil.treatBeaglePlatform(
            request.getHeader(BeaglePlatformUtil.BEAGLE_PLATFORM_HEADER),
            jsonTree
        )
        val jsonData = jsonTree.toPrettyString()
        responseWrapper.resetBuffer()
        responseWrapper.outputStream.write(jsonData.toByteArray())
        responseWrapper.setContentLength(jsonData.length)
    }
}