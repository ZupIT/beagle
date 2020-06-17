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

package br.com.zup.beagle.android.action

import androidx.lifecycle.Observer
import br.com.zup.beagle.android.utils.generateViewModelInstance
import br.com.zup.beagle.android.utils.get
import br.com.zup.beagle.android.utils.handleEvent
import br.com.zup.beagle.android.view.viewmodel.ActionRequestViewModel
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.context.Bind

@SuppressWarnings("UNUSED_PARAMETER")
enum class RequestActionMethod {
    GET,
    POST,
    PUT,
    DELETE,
    HEAD,
    PATCH
}

data class SendRequest(
    val url: Bind<String>,
    val method: Bind<RequestActionMethod> = Bind.Value(RequestActionMethod.GET),
    val headers: Bind<Map<String, String>>? = null,
    val data: Bind<String>? = null,
    val onSuccess: Action? = null,
    val onError: Action? = null,
    val onFinish: Action? = null
) : Action {

    constructor(
        url: String,
        method: RequestActionMethod = RequestActionMethod.GET,
        headers: Map<String, String>? = null,
        data: String? = null,
        onSuccess: Action? = null,
        onError: Action? = null,
        onFinish: Action? = null
    ) : this(
        Bind.Value(url),
        Bind.Value(method),
        if (headers != null) Bind.Value(headers) else headers,
        if (data != null) Bind.Value(data) else data,
        onSuccess,
        onError,
        onFinish
    )

    override fun execute(rootView: RootView) {
        val viewModel = rootView.generateViewModelInstance<ActionRequestViewModel>()

        viewModel.fetch(toSendRequestInternal(rootView)).observe(rootView.getLifecycleOwner(), Observer { state ->
            executeActions(rootView, this, state)
        })
    }

    private fun executeActions(
        rootView: RootView,
        action: SendRequest,
        state: ActionRequestViewModel.FetchViewState
    ) {
        action.onFinish?.let {
            action.handleEvent(rootView, it, "onFinish")
        }

        when (state) {
            is ActionRequestViewModel.FetchViewState.Error -> action.onError?.let {
                action.handleEvent(rootView, it, "onError", state.response)
            }
            is ActionRequestViewModel.FetchViewState.Success -> action.onSuccess?.let {
                action.handleEvent(rootView, it, "onSuccess", state.response)
            }
        }
    }

    private fun toSendRequestInternal(rootView: RootView) = SendRequestInternal(
        url = this.url.get(rootView) ?: "",
        method = this.method.get(rootView) ?: RequestActionMethod.GET,
        headers = this.headers?.get(rootView),
        data = this.data?.get(rootView),
        onSuccess = this.onSuccess,
        onError = this.onError,
        onFinish = this.onFinish
    )
}

internal data class SendRequestInternal(
    val url: String,
    val method: RequestActionMethod = RequestActionMethod.GET,
    val headers: Map<String, String>?,
    val data: Any? = null,
    val onSuccess: Action? = null,
    val onError: Action? = null,
    val onFinish: Action? = null
)