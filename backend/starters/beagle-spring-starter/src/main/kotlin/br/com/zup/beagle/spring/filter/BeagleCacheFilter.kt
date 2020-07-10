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
import br.com.zup.beagle.cache.RestCacheHandler
import br.com.zup.beagle.platform.BeaglePlatformUtil
import org.springframework.web.util.ContentCachingResponseWrapper
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class BeagleCacheFilter(private val cacheHandler: BeagleCacheHandler) : Filter {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        if (chain != null && request is HttpServletRequest && response is HttpServletResponse) {
            this.cacheHandler.handleCache(
                endpoint = request.requestURI,
                receivedHash = request.getHeader(BeagleCacheHandler.CACHE_HEADER),
                currentPlatform = request.getHeader(BeaglePlatformUtil.BEAGLE_PLATFORM_HEADER),
                initialResponse = ContentCachingResponseWrapper(response),
                restHandler = object : RestCacheHandler<ContentCachingResponseWrapper> {
                    override fun callController(response: ContentCachingResponseWrapper) =
                        response.also { chain.doFilter(request, it) }

                    override fun addHashHeader(response: ContentCachingResponseWrapper, header: String) =
                        response.also { it.setHeader(BeagleCacheHandler.CACHE_HEADER, header) }

                    override fun addStatus(response: ContentCachingResponseWrapper, status: Int) =
                        response.also { it.status = status }

                    override fun getBody(response: ContentCachingResponseWrapper) = String(response.contentAsByteArray)
                }
            ).copyBodyToResponse()
        }
    }
}