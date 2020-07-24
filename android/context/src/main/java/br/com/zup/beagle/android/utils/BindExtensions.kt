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

import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.logger.BeagleContextLogs
import br.com.zup.beagle.android.viewmodel.ScreenContextViewModel
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.generateViewModelInstance

typealias Observer<T> = (value: T) -> Unit

// This method should be used if its inside a ServerDrivenComponent
//TODO REMOVED INTERNAL
fun <T> Bind<T>.observe(
    rootView: RootView,
    observes: Observer<T?>? = null
): T? {
    return evaluateBind(rootView, this, null, observes)
}

// This method should be used if its inside a Action
internal fun <T> Bind<T>.evaluateForAction(
    rootView: RootView,
    caller: Action
): T? {
    return evaluateBind(rootView, this, caller, null)
}

private fun <T> evaluateBind(
    rootView: RootView,
    bind: Bind<T>,
    caller: Action? = null,
    observes: Observer<T?>?
): T? {
    val value = try {
        when (bind) {
            is Bind.Expression -> evaluateExpression(rootView, bind, caller)
            else -> bind.value as? T?
        }
    } catch (ex: Exception) {
        BeagleContextLogs.errorWhileTryingToEvaluateBinding(ex)
        null
    }

    if (observes != null) {
        bind.observes(observes)
    }

    if (value != null) {
        bind.notifyChange(value)
    }

    return value
}

private fun <T> evaluateExpression(
    rootView: RootView,
    bind: Bind.Expression<T>,
    caller: Action? = null
): T? {
    val viewModel = rootView.generateViewModelInstance<ScreenContextViewModel>()
    return if (caller != null) {
        viewModel.evaluateExpressionForImplicitContext(caller, bind) as? T?
    } else {
        viewModel.addBindingToContext(bind)
        null
    }
}