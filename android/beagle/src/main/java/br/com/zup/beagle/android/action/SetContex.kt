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

import br.com.zup.beagle.core.Bind
import br.com.zup.beagle.view.viewmodel.ScreenContextViewModel
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.setup.BindingAdapter
import br.com.zup.beagle.utils.generateViewModelInstance

class SetContext(
    contextId: String,
    value: Any,
    path: String? = null
) : br.com.zup.beagle.action.SetContext(contextId, value, path), Action {

    override fun execute(rootView: RootView) {
        val viewModel = rootView.generateViewModelInstance<ScreenContextViewModel>()
        viewModel.contextDataManager.updateContext(this)
    }
}

// Should be generated
class SetContextBinding(
    val contextId: Bind<String>,
    val value: Bind<Any>,
    val path: Bind<String>?
): Action, BindingAdapter {

    override fun execute(rootView: RootView) {
        SetContext(contextId.get(), value.get(), path?.get()).execute(rootView)
    }

    override fun getBindAttributes(): List<Bind<*>?> = listOf(
        contextId,
        value,
        path
    )
}