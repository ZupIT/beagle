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

package br.com.zup.beagle.android.context

import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.utils.generateViewModelInstance
import br.com.zup.beagle.android.view.viewmodel.ScreenContextViewModel
import br.com.zup.beagle.android.widget.RootView

internal class ContextActionExecutor {

    fun executeActions(
        rootView: RootView,
        sender: Any,
        actions: List<Action>,
        eventName: String,
        eventValue: Any? = null
    ) {
        if (eventValue != null) {
            createImplicitContextForActions(rootView, sender, eventName, eventValue, actions)
        }

        actions.forEach {
            it.execute(rootView)
        }
    }

    private fun createImplicitContextForActions(
        rootView: RootView,
        sender: Any,
        eventName: String,
        eventValue: Any,
        actions: List<Action>
    ) {
        val viewModel = rootView.generateViewModelInstance<ScreenContextViewModel>()
        val contextData = ContextData(
            id = eventName,
            value = eventValue
        ).normalize()
        viewModel.addImplicitContext(contextData, sender, actions)
    }
}