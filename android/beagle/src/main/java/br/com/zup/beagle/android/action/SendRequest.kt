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
import br.com.zup.beagle.android.annotation.ContextDataValue
import br.com.zup.beagle.android.utils.generateViewModelInstance
import br.com.zup.beagle.android.utils.handleEvent
import br.com.zup.beagle.android.view.viewmodel.ActionRequestViewModel
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.utils.evaluateExpression

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
    @property:ContextDataValue
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
        Bind.Value(url),
        Bind.Value(method),
        headers?.let { Bind.Value(it) },
        data,
        onSuccess,
        onError,
        onFinish
    )

    override fun execute(rootView: RootView) {
        val viewModel = rootView.generateViewModelInstance<ActionRequestViewModel>()

        viewModel.fetch(toSendRequestInternal(rootView)).observe(rootView.getLifecycleOwner(), Observer { state ->
            executeActions(rootView, state)
        })
    }

    private fun executeActions(
        rootView: RootView,
        state: ActionRequestViewModel.FetchViewState
    ) {
        onFinish?.let {
            handleEvent(rootView, it, "onFinish")
        }

        when (state) {
            is ActionRequestViewModel.FetchViewState.Error -> onError?.let {
                handleEvent(rootView, it, "onError", state.response)
            }
            is ActionRequestViewModel.FetchViewState.Success -> onSuccess?.let {
                handleEvent(rootView, it, "onSuccess", state.response)
            }
        }
    }

    private fun toSendRequestInternal(rootView: RootView) = SendRequestInternal(
        url = evaluateExpression(rootView, this.url) ?: "",
        method = evaluateExpression(rootView, this.method) ?: RequestActionMethod.GET,
        headers = this.headers?.let { evaluateExpression(rootView, it) },
        data = this.data?.let { evaluateExpression(rootView, it.toString()) },
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
    val onSuccess: List<Action>? = null,
    val onError: List<Action>? = null,
    val onFinish: List<Action>? = null
)
