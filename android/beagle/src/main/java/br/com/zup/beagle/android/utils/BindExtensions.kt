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

package br.com.zup.beagle.android.utils

import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.view.viewmodel.ScreenContextViewModel
import br.com.zup.beagle.android.widget.RootView

fun <T> Bind<T>.get(rootView: RootView, observes: ((value: T) -> Unit)? = null): T? {
    val value = try {
        when (this) {
            is Bind.Expression -> evaluateExpression(rootView, this)
            else -> this.value as? T?
        }
    } catch (ex: Exception) {
        BeagleMessageLogs.errorWhileTryingToEvaluateBinding(ex)
        null
    }

    if (value != null && observes != null) {
        this.observes(observes)
        observes(value)
    }

    return value
}

private fun <T> evaluateExpression(
    rootView: RootView,
    bind: Bind.Expression<T>
): T? {
    return rootView.generateViewModelInstance<ScreenContextViewModel>().contextDataManager.let {
        it.addBindingToContext(bind)
        it.evaluateBinding(bind) as T
    }
}