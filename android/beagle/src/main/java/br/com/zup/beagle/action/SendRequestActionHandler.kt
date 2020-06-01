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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.zup.beagle.action.request.presentation.ActionRequestViewModel
import br.com.zup.beagle.action.request.presentation.mapper.toRequest
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.view.ViewFactory


internal typealias SendRequestListener = (actions: List<Action>) -> Unit

internal class SendRequestActionHandler {

    fun handle(rootView: RootView, action: SendRequestAction, viewModel: ActionRequestViewModel =
        ViewModelProvider(rootView.getViewModelStoreOwner()).get(ActionRequestViewModel::class.java),
               listener: SendRequestListener) {
        viewModel.fetch(action.toRequest())
            .observe(rootView.getLifecycleOwner(), Observer { state ->
                val actions = mutableListOf<Action>()
                action.onFinish?.let {
                    actions.add(it)
                }
                when (state) {
                    is ActionRequestViewModel.FetchViewState.Error -> action.onError?.let {
                        actions.add(it)
                    }
                    is ActionRequestViewModel.FetchViewState.Success -> action.onSuccess?.let {
                        actions.add(it)
                    }
                }

                if (actions.isNotEmpty()) {
                    listener(actions)
                }

            })
    }
}