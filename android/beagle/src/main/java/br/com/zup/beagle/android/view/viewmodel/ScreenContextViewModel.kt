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

import android.view.View
import androidx.lifecycle.ViewModel
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.action.SetContextInternal
import br.com.zup.beagle.android.context.*
import br.com.zup.beagle.android.context.ContextBinding
import br.com.zup.beagle.android.context.ContextDataEvaluation
import br.com.zup.beagle.android.context.ContextDataManager
import br.com.zup.beagle.android.utils.Observer
import java.util.*

private data class ImplicitContext(
    val sender: Any,
    val context: ContextData,
    val caller: List<Action>
)

internal class ScreenContextViewModel(
    private val contextDataManager: ContextDataManager = ContextDataManager(),
    private val contextDataEvaluation: ContextDataEvaluation = ContextDataEvaluation()
) : ViewModel() {

    private val viewIds = Stack<Int>()
    private val implicitContextData = mutableListOf<ImplicitContext>()

    fun resetIds() {
        viewIds.clear()
    }

    fun generateNewViewId(): Int {
        val newId = if (viewIds.empty()) {
            0
        } else {
            viewIds.peek() + 1
        }

        return viewIds.push(newId)
    }


    fun addContext(view: View, contextData: ContextData) {
        contextDataManager.addContext(view, contextData)
    }

    fun updateContext(originView: View, setContextInternal: SetContextInternal) {
        contextDataManager.updateContext(originView, setContextInternal)
    }

    fun <T> addBindingToContext(view: View, bind: Bind.Expression<T>, observer: Observer<T>) {
        contextDataManager.addBinding(view, bind, observer)
    }

    fun discoverAllContexts() {
        contextDataManager.discoverAllContexts()
    }

    fun notifyBindingChanges(contextBinding: ContextBinding) {
        contextDataManager.notifyBindingChanges(contextBinding)
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
    fun evaluateExpressionForImplicitContext(originView: View, bindCaller: Action, bind: Bind.Expression<*>): Any? {
        val contexts = mutableListOf<ContextData>()

        findMoreContexts(bindCaller, contexts)

        return evaluateBind(originView, contexts, bind)
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

    private fun evaluateBind(originView: View, implicitContexts: List<ContextData>, bind: Bind.Expression<*>): Any? {
        val contexts = contextDataManager.getContextsFromBind(originView, bind).toMutableList()
        contexts += implicitContexts
        return contextDataEvaluation.evaluateBindExpression(contexts, bind, mutableMapOf())
    }

    fun clearContexts(){
        contextDataManager.clearContexts()
    }
}