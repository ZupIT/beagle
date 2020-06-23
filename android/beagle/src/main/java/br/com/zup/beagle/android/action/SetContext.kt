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

import br.com.zup.beagle.android.annotation.ContextDataValue
import br.com.zup.beagle.android.utils.evaluateExpression
import br.com.zup.beagle.android.utils.generateViewModelInstance
import br.com.zup.beagle.android.view.viewmodel.ScreenContextViewModel
import br.com.zup.beagle.android.widget.RootView

internal data class SetContextInternal(
    val contextId: String,
    val value: Any,
    val path: String? = null
)

data class SetContext(
    val contextId: String,
    @property:ContextDataValue
    val value: Any,
    val path: String? = null
) : Action {

    override fun execute(rootView: RootView) {
        val viewModel = rootView.generateViewModelInstance<ScreenContextViewModel>()
        viewModel.updateContext(toInternalSetContext(rootView))
    }

    private fun toInternalSetContext(rootView: RootView) = SetContextInternal(
        contextId = this.contextId,
        value = evaluateExpression(rootView, this.value.toString()) ?: "",
        path = this.path
    )
}
