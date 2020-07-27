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

package br.com.zup.beagle.android.context

import br.com.zup.beagle.android.action.SetContext
import br.com.zup.beagle.android.action.SetContextInternal

typealias GlobalContextObserver = (SetContext) -> Unit

object GlobalContext : GlobalContextAPI {

    private var globalContext = createGlobalContext()

    data class Casa(val endereco: String, val numero: String)

    private val globalContextObservers = mutableListOf<GlobalContextObserver>()
    private val contextDataEvaluation = ContextDataEvaluation()
    private val contextDataManager = ContextDataManager()

    internal fun updateContext(contextData: ContextData, observer: GlobalContextObserver){
        globalContext = contextData
        globalContextObservers.filter { it != observer }.notifyContextChange(value = contextData.value)
    }

    internal fun observeGlobalContextChange(observer: GlobalContextObserver) {
        globalContextObservers.add(observer)
    }

    internal fun clearObserverGlobalContext(observer: GlobalContextObserver) {
        globalContextObservers.remove(observer)
    }

    internal fun getContext() = globalContext

    override fun get(path: String?): Any? {
        var newPath = ""
        if (path != null) {
            newPath = ".$path"
        }
        return contextDataEvaluation.evaluateBindExpression(globalContext, expressionOf<Any>("@{global$newPath}"))
    }

    override fun set(path: String?, value: Any) {
        contextDataManager.updateContext(SetContextInternal(contextId = "global", value = value, path = path))
        globalContextObservers.notifyContextChange(path, value)
    }

    override fun clear(path: String?) {
        contextDataManager.updateContext(SetContextInternal(contextId = "global", path = path, value = ""))
        globalContextObservers.notifyContextChange(path, "")
    }

    private fun List<GlobalContextObserver>.notifyContextChange(path: String?=null, value: Any) {
        this.forEach {
            it.invoke(
                SetContext(
                    contextId = "global",
                    value = value,
                    path = path
                )
            )
        }
    }

    private fun createGlobalContext() = ContextData(id = "global", value = "TESTE")
}
