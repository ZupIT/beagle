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

package br.com.zup.beagle.widget.action

import br.com.zup.beagle.analytics2.ActionAnalyticsConfig
import br.com.zup.beagle.widget.context.Bind

/**
 * SendRequest is used to make HTTP requests.
 *
 * @param url Required. Server URL.
 * @param method  HTTP method.
 * @param headers Header items for the request.
 * @param data Content that will be deliver with the request.
 * @param onSuccess Success action.
 * @param onError  Error action.
 * @param onFinish Finish action.
 */
data class SendRequest(
    val url: Bind<String>,
    val method: Bind<RequestActionMethod> = Bind.Value(RequestActionMethod.GET),
    val headers: Bind<Map<String, String>>? = null,
    val data: Any? = null,
    val onSuccess: List<Action>? = null,
    val onError: List<Action>? = null,
    val onFinish: List<Action>? = null,
    override var analytics: ActionAnalyticsConfig? = null
) : ActionAnalytics() {
    constructor(
        url: String,
        method: RequestActionMethod = RequestActionMethod.GET,
        headers: Map<String, String>? = null,
        data: Any? = null,
        onSuccess: List<Action>? = null,
        onError: List<Action>? = null,
        onFinish: List<Action>? = null,
        analytics: ActionAnalyticsConfig? = null
    ) : this(
        url = Bind.Value(url),
        method = Bind.Value(method),
        headers = if (headers != null) Bind.Value(headers) else headers,
        data = data,
        onSuccess = onSuccess,
        onError = onError,
        onFinish = onFinish,
        analytics = analytics
    )
}

/**
 * Enum with HTTP methods.
 */
@SuppressWarnings("UNUSED_PARAMETER")
enum class RequestActionMethod {
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
