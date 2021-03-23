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

package br.com.zup.beagle.widget.networking

/**
 * HttpAdditionalData is used to do requests.
 * @param method HTTP method.
 * @param headers Header items for the request.
 * @param body Content that will be delivered with the request.
 */
data class HttpAdditionalData(
    val method: HttpMethod? = HttpMethod.GET,
    val headers: Map<String, String>? = hashMapOf(),
    val body: Any? = null,
)

/**
 * Http method to indicate the desired action to be performed for a given resource.
 *
 */
enum class HttpMethod {
    /**
     * The GET method requests a representation of the specified resource. Requests using GET should only retrieve
     * data.
     */
    GET,

    /**
     * The POST method is used to submit an entity to the specified resource, often causing
     * a change in state or side effects on the server.
     */
    POST,

    /**
     * The PUT method replaces all current representations of the target resource with the request payload.
     */
    PUT,

    /**
     * The DELETE method deletes the specified resource.
     */
    DELETE,

    /**
     * The HEAD method asks for a response identical to that of a GET request, but without the response body.
     */
    HEAD,

    /**
     * The PATCH method is used to apply partial modifications to a resource.
     */
    PATCH,
}