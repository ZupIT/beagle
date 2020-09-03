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

import br.com.zup.beagle.android.action.Action

private data class ImplicitContext(
    val sender: Any,
    val context: ContextData,
    val caller: List<Action>
)

class ImplicitContextManager {

    private val implicitContextData = mutableListOf<ImplicitContext>()

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
    fun getImplicitContextForBind(bindCaller: Action): List<ContextData> {
        val contexts = mutableListOf<ContextData>()

        findMoreContexts(bindCaller, contexts)

        return contexts
    }

    private fun findMoreContexts(
        toCompare: Any,
        contexts: MutableList<ContextData>
    ) {
        implicitContextData.forEach { implicitContext ->
            implicitContext.caller.forEach {
                if (toCompare === it) {
                    contexts += implicitContext.context
                    findMoreContexts(implicitContext.sender, contexts)
                }
            }
        }
    }
}