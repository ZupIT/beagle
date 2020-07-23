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

import br.com.zup.beagle.widget.builder.BeagleBuilder
import br.com.zup.beagle.widget.builder.BeagleListBuilder
import br.com.zup.beagle.widget.context.Bind
import kotlin.properties.Delegates

data class SendRequest(
    val url: Bind<String>,
    val method: Bind<RequestActionMethod> = Bind.Value(RequestActionMethod.GET),
    val headers: Bind<Map<String, String>>? = null,
    val data: Any? = null,
    val onSuccess: List<Action>? = null,
    val onError: List<Action>? = null,
    val onFinish: List<Action>? = null
) : Action {
    constructor(
        url: String,
        method: RequestActionMethod = RequestActionMethod.GET,
        headers: Map<String, String>? = null,
        data: Any? = null,
        onSuccess: List<Action>? = null,
        onError: List<Action>? = null,
        onFinish: List<Action>? = null
    ) : this(
        url = Bind.Value(url),
        method = Bind.Value(method),
        headers = if (headers != null) Bind.Value(headers) else headers,
        data = data,
        onSuccess = onSuccess,
        onError = onError,
        onFinish = onFinish
    )

    @Suppress("TooManyFunctions")
    class Builder : BeagleBuilder<SendRequest> {
        var url: Bind<String> by Delegates.notNull()
        var method: Bind<RequestActionMethod> = Bind.Value(RequestActionMethod.GET)
        var headers: Bind<Map<String, String>>? = null
        var data: Any? = null
        var onSuccess: MutableList<Action>? = null
        var onError: MutableList<Action>? = null
        var onFinish: MutableList<Action>? = null

        fun url(url: Bind<String>) = this.apply { this.url = url }
        fun method(method: Bind<RequestActionMethod>) = this.apply { this.method = method }
        fun headers(headers: Bind<Map<String, String>>?) = this.apply { this.headers = headers }
        fun data(data: Any?) = this.apply { this.data = data }
        fun onSuccess(onSuccess: List<Action>?) = this.apply { this.onSuccess = onSuccess?.toMutableList() }
        fun onError(onError: List<Action>?) = this.apply { this.onError = onError?.toMutableList() }
        fun onFinish(onFinish: List<Action>?) = this.apply { this.onFinish = onFinish?.toMutableList() }

        fun url(block: () -> Bind<String>) {
            url(block.invoke())
        }

        fun method(block: () -> Bind<RequestActionMethod>) {
            method(block.invoke())
        }

        fun header(block: () -> Bind<Map<String, String>>?) {
            headers(block.invoke())
        }

        fun data(block: () -> Any?) {
            data(block.invoke())
        }

        fun onSuccess(block: BeagleListBuilder<Action>.() -> Unit) {
            onSuccess(BeagleListBuilder<Action>().apply(block).buildNullable()?.toMutableList())
        }

        fun onError(block: BeagleListBuilder<Action>.() -> Unit) {
            onError(BeagleListBuilder<Action>().apply(block).buildNullable()?.toMutableList())
        }

        fun onFinish(block: BeagleListBuilder<Action>.() -> Unit) {
            onFinish(BeagleListBuilder<Action>().apply(block).buildNullable()?.toMutableList())
        }

        override fun build() = SendRequest(
            url = url,
            method = method,
            headers = headers,
            data = data,
            onSuccess = onSuccess,
            onError = onError,
            onFinish = onFinish
        )

    }

}

fun sendRequest(block: SendRequest.Builder.() -> Unit) = SendRequest.Builder().apply(block).build()

@SuppressWarnings("UNUSED_PARAMETER")
enum class RequestActionMethod {
    GET,
    POST,
    PUT,
    DELETE,
    HEAD,
    PATCH
}
