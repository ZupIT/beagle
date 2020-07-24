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
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.utils.normalize
import br.com.zup.beagle.android.viewmodel.ScreenContextViewModel
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.generateViewModelInstance

class ContextActionExecutor {

    @Suppress("LongParameterList")
    fun executeActions(
        rootView: RootView,
        origin: View,
        sender: Any,
        actions: List<Action>,
        context: ContextData? = null
    ) {
        if (context != null) {
            createImplicitContextForActions(rootView, sender, context, actions)
        }

        actions.forEach {
            it.execute(rootView, origin)
        }
    }

    private fun createImplicitContextForActions(
        rootView: RootView,
        sender: Any,
        context: ContextData,
        actions: List<Action>
    ) {
        val viewModel = rootView.generateViewModelInstance<ScreenContextViewModel>()
        viewModel.addImplicitContext(context.normalize(), sender, actions)
    }
}