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

typealias GlobalContextObserver = (ContextData) -> Unit

private const val GLOBAL_KEY = "global"

object GlobalContext {

    private var globalContext = ContextData(id = GLOBAL_KEY, value = "")
    private val globalContextObservers = mutableListOf<GlobalContextObserver>()
    private val contextDataManipulator = ContextDataManipulator()

    fun get(path: String? = null): Any? {
        if (path.isNullOrEmpty()) {
            return globalContext.value
        }

        return contextDataManipulator.get(globalContext, path)
    }

    fun set(value: Any, path: String? = null) {
        val result = contextDataManipulator.set(globalContext, path, value)
        notifyContextChanges(result)
    }

    fun clear(path: String? = null) {
        val result = contextDataManipulator.clear(globalContext, path)
        notifyContextChanges(result)
    }

    internal fun observeGlobalContextChange(observer: GlobalContextObserver) {
        globalContextObservers.add(observer)
    }

    internal fun clearObserverGlobalContext(observer: GlobalContextObserver) {
        globalContextObservers.remove(observer)
    }

    internal fun getContext() = globalContext

    private fun notifyContextChanges(result: ContextSetResult) {
        if (result is ContextSetResult.Succeed) {
            globalContext = result.newContext
            globalContextObservers.forEach {
                it.invoke(globalContext)
            }
        }
    }
}
