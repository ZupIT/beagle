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

package br.com.zup.beagle.android.networking

import java.net.URI

/**
 * RequestData is used to make HTTP requests.
 *
 * @param uri Required. Server URL.
 * @param method  HTTP method.
 * @param headers Header items for the request.
 * @param body Content that will be deliver with the request.
 */
data class RequestData(
    val uri: URI,
    val method: HttpMethod = HttpMethod.GET,
    val headers: Map<String, String> = mapOf(),
    val body: String? = null
)

/**
 * Enum with HTTP methods.
 */
enum class HttpMethod {
    /**
     * Request we representation of an resource.
     */
    GET,

    /**
     * The POST method is used when we want to create a resource.
     */
    POST,

    /**
     * Require that a resource be "saved" in the given URI.
     */
    PUT,

    /**
     * Deletes the specified resource.
     */
    DELETE,

    /**
     * Returns only the headers of a response.
     */
    HEAD,

    /**
     * Used to update parts of a resource
     */
    PATCH
}