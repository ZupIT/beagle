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

package br.com.zup.beagle.android.engine.util

import br.com.zup.beagle.core.ContextComponent
import br.com.zup.beagle.android.context.ScreenContextViewModel
import br.com.zup.beagle.android.setup.BindingAdapter
import br.com.zup.beagle.android.utils.generateViewModelInstance
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.core.Bind
import br.com.zup.beagle.core.ServerDrivenComponent

internal class ContextViewRenderer {

    fun startContextBinding(rootView: RootView, component: ServerDrivenComponent) {
        val viewModel = rootView.generateViewModelInstance<ScreenContextViewModel>()

        if (component is BindingAdapter) {
            component.getBindAttributes().forEach { bind ->
                if (bind is Bind.Expression) {
                    viewModel.contextDataManager.addBindingToContext(bind)
                }
            }
        } else if (component is ContextComponent) {
            component.context?.let { context ->
                viewModel.contextDataManager.pushContext(context)
            }
        }
    }

    fun finishContextBinding(rootView: RootView, component: ServerDrivenComponent) {
        if (component is ContextComponent) {
            rootView.generateViewModelInstance<ScreenContextViewModel>().contextDataManager.popContext()
        }
    }
}