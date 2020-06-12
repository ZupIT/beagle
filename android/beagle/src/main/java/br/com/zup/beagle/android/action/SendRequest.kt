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
import br.com.zup.beagle.action.RequestActionMethod
import br.com.zup.beagle.core.Bind
import br.com.zup.beagle.view.viewmodel.ActionRequestViewModel
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.setup.BindingAdapter
import br.com.zup.beagle.utils.generateViewModelInstance
import br.com.zup.beagle.utils.handleEvent

class SendRequest(
    url: String,
    method: RequestActionMethod = RequestActionMethod.GET,
    headers: Map<String, String>? = null,
    body: String? = null,
    onSuccess: br.com.zup.beagle.widget.core.Action? = null,
    onError: br.com.zup.beagle.widget.core.Action? = null,
    onFinish: br.com.zup.beagle.widget.core.Action? = null
) : br.com.zup.beagle.action.SendRequest(url, method, headers, body, onSuccess, onError, onFinish), Action {

    override fun execute(rootView: RootView) {
        val viewModel = rootView.generateViewModelInstance<ActionRequestViewModel>()

        viewModel.fetch(this).observe(rootView.getLifecycleOwner(), Observer { state ->
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
}

// Should be generated
class SendRequestBinding(
    val url: Bind<String>,
    val method: Bind<RequestActionMethod>,
    val headers: Bind<Map<String, String>>?,
    val body: Bind<String>?,
    val onSuccess: Bind<br.com.zup.beagle.widget.core.Action>?,
    val onError: Bind<br.com.zup.beagle.widget.core.Action>?,
    val onFinish: Bind<br.com.zup.beagle.widget.core.Action>?
) : Action, br.com.zup.beagle.widget.core.Action, BindingAdapter {

    override fun execute(rootView: RootView) {
        SendRequest(
            url.get(),
            method.get(),
            headers?.get(),
            body?.get(),
            onSuccess?.get(),
            onError?.get(),
            onFinish?.get()
        ).execute(rootView)
    }

    override fun getBindAttributes(): List<Bind<*>?> = listOf<Bind<*>?>(
        url,
        method,
        headers,
        body,
        onSuccess,
        onError,
        onFinish
    )
}