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

import android.view.View
import androidx.lifecycle.Observer
import br.com.zup.beagle.android.annotation.ContextDataValue
import br.com.zup.beagle.android.utils.generateViewModelInstance
import br.com.zup.beagle.android.utils.handleEvent
import br.com.zup.beagle.android.view.viewmodel.ActionRequestViewModel
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.context.normalizeContextValue
import br.com.zup.beagle.android.utils.evaluateExpression
import br.com.zup.beagle.android.view.viewmodel.FetchViewState

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

    override fun execute(rootView: RootView, origin: View) {
        val viewModel = rootView.generateViewModelInstance<ActionRequestViewModel>()
        val setContext = toSendRequestInternal(rootView, origin)
        viewModel.fetch(setContext).observe(rootView.getLifecycleOwner(), Observer { state ->
            executeActions(rootView, state, origin)
        })
    }

    private fun executeActions(
        rootView: RootView,
        state: FetchViewState,
        origin: View
    ) {
        onFinish?.let {
            handleEvent(rootView, origin, it)
        }

        when (state) {
            is FetchViewState.Error -> onError?.let {
                handleEvent(rootView, origin, it, ContextData("onError", state.response))
            }
            is FetchViewState.Success -> onSuccess?.let {
                handleEvent(rootView, origin, it, ContextData("onSuccess", state.response))
            }
        }
    }

    private fun toSendRequestInternal(rootView: RootView, origin: View) = SendRequestInternal(
        url = evaluateExpression(rootView, origin, this.url) ?: "",
        method = evaluateExpression(rootView, origin, this.method) ?: RequestActionMethod.GET,
        headers = this.headers?.let { evaluateExpression(rootView, origin, it) },
        data = this.data?.normalizeContextValue()?.let { evaluateExpression(rootView, origin, it) },
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
