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

/**
 * A Global Context is a variable that can assume as value of any type of variable, like a map defines a subset
 * of key/value or complex JSONs objects that defines object trees.
 *
 * It works exactly like the Context, however in a global scope, meaning that it will exists while the application is
 * still running (even on the background), which allows it to be accessed from any application point, being a component
 * or an action linked to a component or even programmatically.
 */
object GlobalContext {

    private var globalContext = ContextData(id = GLOBAL_KEY, value = "")
    private val globalContextObservers = mutableListOf<GlobalContextObserver>()
    private val contextDataManipulator = ContextDataManipulator()

    /**
     * Global context also can be recovered through a get method and it can be called from the GlobalContext object.
     *
     * @param path Represents the path that it will contain the information, on the example above,
     * the path used was  "myValue". The path parameter is optional and if every value is passed,
     * the global context will be returned.
     */
    fun get(path: String? = null): Any? {
        if (path.isNullOrEmpty()) {
            return globalContext.value
        }

        return contextDataManipulator.get(globalContext, path)
    }

    /**
     * Global context can also be defined through a set method, that can be called from the GlobalContext object.
     *
     * @param value Required. It represents an information that can be any kind, for example, other JSONs objects,
     * an array or any other object.
     * @param path It represents the path that it will contain this information, like the example above,
     * the path used was "myValue". The path parameter is not required, and when omitted it will define the value
     * informed in the object's root, overwriting any other information.
     */
    fun set(value: Any, path: String? = null) {
        val result = contextDataManipulator.set(globalContext, path, value)
        notifyContextChanges(result)
    }

    /**
     * Global context can be deleted through clear method that can be called from the GlobalContext object.
     *
     * @param path Optional. Represents the path you want to remove.
     */
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
