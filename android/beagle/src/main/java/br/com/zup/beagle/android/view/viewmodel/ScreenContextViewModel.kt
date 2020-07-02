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
import br.com.zup.beagle.android.action.SetContextInternal
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.context.ContextDataEvaluation
import br.com.zup.beagle.android.context.ContextDataManager

private data class ImplicitContext(
    val sender: Any,
    val context: ContextData,
    val caller: List<Action>
)

internal class ScreenContextViewModel(
    private val contextDataManager: ContextDataManager = ContextDataManager(),
    private val contextDataEvaluation: ContextDataEvaluation = ContextDataEvaluation()
) : ViewModel() {

    private val implicitContextData = mutableListOf<ImplicitContext>()

    fun addContext(contextData: ContextData) {
        contextDataManager.addContext(contextData)
    }

    fun updateContext(setContextInternal: SetContextInternal) {
        contextDataManager.updateContext(setContextInternal)
    }

    fun addBindingToContext(bind: Bind.Expression<*>) {
        contextDataManager.addBindingToContext(bind)
    }

    fun evaluateContexts() {
        contextDataManager.evaluateContexts()
    }

    // Sender is who created the implicit context
    fun addImplicitContext(contextData: ContextData, sender: Any, actions: List<Action>) {
        implicitContextData.removeAll { it.sender == sender }
        implicitContextData += ImplicitContext(
            sender = sender,
            context = contextData,
            caller = actions
        )
    }

    // BindCaller is who owns the Bind Attribute
    fun evaluateExpressionForImplicitContext(bindCaller: Action, bind: Bind.Expression<*>): Any? {
        val contexts = mutableListOf<ContextData>()

        findMoreContexts(bindCaller, contexts)

        return evaluateBind(contexts, bind)
    }

    private fun findMoreContexts(
        toCompare: Any,
        contexts: MutableList<ContextData>
    ) {
        implicitContextData.forEach { implicitContext ->
            implicitContext.caller.forEach {
                if (toCompare == it) {
                    contexts += implicitContext.context
                    findMoreContexts(implicitContext.sender, contexts)
                }
            }
        }
    }

    private fun evaluateBind(implicitContexts: List<ContextData>, bind: Bind.Expression<*>): Any? {
        val contexts = contextDataManager.getContextsFromBind(bind).toMutableList()
        contexts += implicitContexts
        return contextDataEvaluation.evaluateBindExpression(contexts, bind)
    }
}