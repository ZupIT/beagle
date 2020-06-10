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

package br.com.zup.beagle.action

import androidx.lifecycle.Observer
import br.com.zup.beagle.context.ContextActionExecutor
import br.com.zup.beagle.view.viewmodel.ActionRequestViewModel
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.utils.generateViewModelInstance

internal class SendRequestActionHandler(
    private val contextActionExecutor: ContextActionExecutor = ContextActionExecutor()
) {

    fun handle(rootView: RootView, action: SendRequestAction) {
        val viewModel = rootView.generateViewModelInstance<ActionRequestViewModel>()

        viewModel.fetch(action).observe(rootView.getLifecycleOwner(), Observer { state ->
            executeActions(rootView, action, state)
        })
    }

    private fun executeActions(
        rootView: RootView,
        action: SendRequestAction,
        state: ActionRequestViewModel.FetchViewState
    ) {
        action.onFinish?.let {
            contextActionExecutor.executeAction(rootView, "onFinish", it)
        }

        when (state) {
            is ActionRequestViewModel.FetchViewState.Error -> action.onError?.let {
                contextActionExecutor.executeAction(rootView, "onError", it, state.response)
            }
            is ActionRequestViewModel.FetchViewState.Success -> action.onSuccess?.let {
                contextActionExecutor.executeAction(rootView, "onSuccess", it, state.response)
            }
        }
    }
}