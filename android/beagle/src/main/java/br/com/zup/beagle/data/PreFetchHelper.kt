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

package br.com.zup.beagle.data

import br.com.zup.beagle.action.Action
import br.com.zup.beagle.action.Navigate
import br.com.zup.beagle.action.NavigationType
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.utils.generateViewModelInstance

internal class PreFetchHelper {

    fun handlePreFetch(rootView: RootView, action: Action) {
        if (action is Navigate && action.shouldPrefetch) {
            when (action.type) {
                NavigationType.SWAP_VIEW -> preFetch(rootView, action)
                NavigationType.ADD_VIEW -> preFetch(rootView, action)
                NavigationType.PRESENT_VIEW -> preFetch(rootView, action)
                else -> {}
            }
        }
    }

    private fun preFetch(rootView: RootView, action: Navigate) {
        val viewModel = rootView.generateViewModelInstance()
        action.path?.let {
            viewModel.fetchForCache(it)
        }
    }
}