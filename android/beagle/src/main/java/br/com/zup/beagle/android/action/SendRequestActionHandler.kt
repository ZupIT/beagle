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
import br.com.zup.beagle.action.SendRequest
import br.com.zup.beagle.android.context.ContextActionExecutor
import br.com.zup.beagle.android.engine.renderer.RootView
import br.com.zup.beagle.android.utils.generateViewModelInstance
import br.com.zup.beagle.android.utils.handleEvent
import br.com.zup.beagle.android.view.viewmodel.ActionRequestViewModel

internal class SendRequestActionHandler {

    fun handle(rootView: RootView, action: SendRequest) {
        val viewModel = rootView.generateViewModelInstance<ActionRequestViewModel>()

        viewModel.fetch(action).observe(rootView.getLifecycleOwner(), Observer { state ->
            executeActions(rootView, action, state)
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
}