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
import br.com.zup.beagle.action.Route
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.utils.generateViewModelInstance

internal class PreFetchHelper {

    fun handlePreFetch(rootView: RootView, action: Action) {
        when (action) {
            is Navigate.PushStack -> preFetch(rootView, action.route)
            is Navigate.PushView -> preFetch(rootView, action.route)
            is Navigate.ResetApplication -> preFetch(rootView, action.route)
            is Navigate.ResetStack -> preFetch(rootView, action.route)
        }
    }

    private fun preFetch(rootView: RootView, route: Route) {
        if (route is Route.Remote && route.shouldPrefetch) {
            val viewModel = rootView.generateViewModelInstance()
            viewModel.fetchForCache(route.route)
        }
    }
}
