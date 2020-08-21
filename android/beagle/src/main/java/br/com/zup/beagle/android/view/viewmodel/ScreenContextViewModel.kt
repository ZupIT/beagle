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
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.context.ContextDataEvaluation
import br.com.zup.beagle.android.context.ContextDataManager
import br.com.zup.beagle.android.context.ImplicitContextManager
import br.com.zup.beagle.android.utils.Observer
import java.util.LinkedList
import java.util.Queue
import java.util.Stack


@Suppress("TooManyFunctions")
internal class ScreenContextViewModel(
    private val contextDataManager: ContextDataManager = ContextDataManager(),
    private val contextDataEvaluation: ContextDataEvaluation = ContextDataEvaluation(),
    private val implicitContextManager: ImplicitContextManager = ImplicitContextManager()
) : ViewModel() {

    private val views = mutableListOf<ViewIdStruct>()

    fun createOrUpdate(parentId: Int) {
        val view = views.firstOrNull { viewIdStruct -> viewIdStruct.parentId == parentId }
        if (view != null) {
            views.remove(view)
        }

        views.add(ViewIdStruct(parentId))
    }

    fun getViewId(parentId: Int): Int {
        val view = views[parentId]

        if (view.created) {
            val id = view.localIds.pollFirst()!!
            if (view.localIds.isEmpty()) {
                view.localIds = LinkedList(view.generatedIds)
            }
            return id
        }

        val id = View.generateViewId()
        view.generatedIds.add(id)

        return id
    }

    fun addContext(view: View, contextData: ContextData) {
        contextDataManager.addContext(view, contextData)
    }

    fun updateContext(originView: View, setContextInternal: SetContextInternal) {
        contextDataManager.updateContext(originView, setContextInternal)
    }

    fun <T> addBindingToContext(view: View, bind: Bind.Expression<T>, observer: Observer<T?>) {
        contextDataManager.addBinding(view, bind, observer)
    }

    fun linkBindingToContextAndEvaluateThem() {
        contextDataManager.linkBindingToContext()
        contextDataManager.evaluateContexts()
    }

    fun addImplicitContext(contextData: ContextData, sender: Any, actions: List<Action>) {
        implicitContextManager.addImplicitContext(contextData, sender, actions)
    }

    fun evaluateExpressionForImplicitContext(originView: View, bindCaller: Action, bind: Bind.Expression<*>): Any? {
        val implicitContexts = implicitContextManager.getImplicitContextForBind(bindCaller)
        val contexts = contextDataManager.getContextsFromBind(originView, bind).toMutableList()
        contexts += implicitContexts
        return contextDataEvaluation.evaluateBindExpression(contexts, bind)
    }

    fun clearContexts() {
//        resetIds(parentId)
        contextDataManager.clearContexts()
    }

    override fun onCleared() {
        contextDataManager.clearContexts()
    }
}

data class ViewIdStruct(val parentId: Int, val generatedIds: MutableList<Int> = mutableListOf(),
                        var localIds: LinkedList<Int> = LinkedList(),
                        val created: Boolean = false)