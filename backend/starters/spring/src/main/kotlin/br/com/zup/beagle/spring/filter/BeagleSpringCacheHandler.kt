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
import br.com.zup.beagle.cache.BeagleCacheProperties
import org.springframework.web.util.ContentCachingResponseWrapper
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest

class BeagleSpringCacheHandler(
    private val request: HttpServletRequest,
    private val wrapper: ContentCachingResponseWrapper,
    private val chain: FilterChain,
    properties: BeagleCacheProperties
) : BeagleCacheHandler(properties) {
    override fun createResponseFromController(modifyResponse: (response: Any) -> Any)
    = modifyResponse.invoke(wrapper.also { chain.doFilter(request, it) })

    override fun createResponse(status: Int) = wrapper.also { it.status = status }
    override fun getBody(response: Any) = String((response as ContentCachingResponseWrapper).contentAsByteArray)

    override fun addHeader(response: Any, key: String, value: String) {
        (response as ContentCachingResponseWrapper).setHeader(key, value)
    }
}