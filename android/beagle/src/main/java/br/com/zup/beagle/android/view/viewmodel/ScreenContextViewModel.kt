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

package br.com.zup.beagle.android.view.viewmodel

import androidx.lifecycle.ViewModel
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.context.ContextDataEvaluation
import br.com.zup.beagle.android.context.ContextDataManager

private data class ImplicitContext(
    val context: ContextData,
    val caller: List<Action>
)

internal class ScreenContextViewModel(
    val contextDataManager: ContextDataManager = ContextDataManager(),
    private val contextDataEvaluation: ContextDataEvaluation = ContextDataEvaluation()
) : ViewModel() {

    private val implicitContextData = mutableMapOf<Any, ImplicitContext>()

    // Sender is who created the implicit context
    fun addImplicitContext(contextData: ContextData, sender: Any, actions: List<Action>) {
        implicitContextData[sender] = ImplicitContext(
            context = contextData,
            caller = actions
        )
    }

    // BindCaller is who owns the Bind Attribute
    fun evaluateImplicitContextBinding(bindCaller: Any, bind: Bind.Expression<*>): Any? {
        var value: Any? = null

        implicitContextData.forEach { entry ->
            entry.value.caller.forEach {
                if (bindCaller == it) {
                    val contexts = contextDataManager.getContextsFromBind(bind) + entry.value.context
                    value = contextDataEvaluation.evaluateBindExpression(contexts, bind)
                }
            }
        }

        return value
    }
}