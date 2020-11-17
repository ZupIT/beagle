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

import android.view.View
import br.com.zup.beagle.android.action.SetContextInternal
import br.com.zup.beagle.android.context.tokenizer.Token
import br.com.zup.beagle.android.context.tokenizer.TokenBinding
import br.com.zup.beagle.android.context.tokenizer.TokenFunction
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.utils.Observer
import br.com.zup.beagle.android.utils.findParentContextWithId
import br.com.zup.beagle.android.utils.getAllParentContexts
import br.com.zup.beagle.android.utils.getContextBinding
import br.com.zup.beagle.android.utils.getContextId
import br.com.zup.beagle.android.utils.setContextBinding
import br.com.zup.beagle.android.utils.setContextData

private const val GLOBAL_CONTEXT_ID = Int.MAX_VALUE

internal class ContextDataManager(
    private val contextDataEvaluation: ContextDataEvaluation = ContextDataEvaluation(),
    private val contextDataManipulator: ContextDataManipulator = ContextDataManipulator()
) {

    private var globalContext: ContextBinding = ContextBinding(GlobalContext.getContext())
    private val contexts = mutableMapOf<Int, ContextBinding>()
    private val contextsWithoutId = mutableMapOf<View, ContextBinding>()
    private val viewBinding = mutableMapOf<View, MutableSet<Binding<*>>>()
    private val globalContextObserver: GlobalContextObserver = {
        updateGlobalContext(it)
    }

    init {
        contexts[GLOBAL_CONTEXT_ID] = globalContext
        GlobalContext.observeGlobalContextChange(globalContextObserver)
    }

    fun clearContexts() {
        contextsWithoutId.clear()
        contexts.clear()
        viewBinding.clear()
        GlobalContext.clearObserverGlobalContext(globalContextObserver)
    }

    fun setIdToViewWithContext(view: View) {
        contextsWithoutId[view]?.apply {
            contexts[view.id]?.let {
                view.setContextData(it.context)
            } ?: run {
                contexts[view.id] = this
            }
            contextsWithoutId.remove(view)
        }
    }

    fun onViewIdChanged(oldId: Int, newId: Int, view: View) {
        contexts[oldId]?.let { contextBinding ->
            if (!contexts.containsKey(newId)) {
                contexts.put(newId, contextBinding)
            } else {
                contexts[newId]?.let {
                    updateContextAndReference(view, it.context)
                }
            }
        }
        contexts.remove(oldId)
    }

    fun addContext(view: View, context: ContextData, shouldOverrideExistingContext: Boolean = false) {
        if (context.id == globalContext.context.id) {
            BeagleMessageLogs.globalKeywordIsReservedForGlobalContext()
            return
        }

        val existingContext = contexts[view.id]

        if (existingContext != null) {
            if (shouldOverrideExistingContext) {
                updateContextAndReference(view, context)
            } else {
                view.setContextBinding(existingContext)
                existingContext.bindings.clear()
            }
        } else {
            updateContextAndReference(view, context)
        }
    }

    private fun updateContextAndReference(view: View, context: ContextData) {
        view.setContextData(context)
        view.getContextBinding()?.let {
            if (view.id != View.NO_ID) {
                contexts[view.id] = it
            } else {
                contextsWithoutId[view] = it
            }
        }
    }

    fun getContextData(view: View) = contexts[view.id]?.context

    fun restoreContext(view: View) {
        contexts[view.id]?.let {
            view.setContextData(it.context)
        }
    }

    fun <T> addBinding(view: View, bind: Bind.Expression<T>, observer: Observer<T?>) {
        val bindings: MutableSet<Binding<*>> = viewBinding[view] ?: mutableSetOf()
        bindings.add(Binding(
            observer = observer,
            bind = bind
        ))
        viewBinding[view] = bindings
    }

    fun linkBindingToContextAndEvaluateThem(view: View) {
        if (viewBinding.contains(view)) {
            val contextStack = view.getAllParentContextWithGlobal()
            viewBinding[view]?.forEach { binding ->
                val bindingTokens = binding.bind.filterBindingTokens()
                notifyBindingTokens(bindingTokens, contextStack, binding)
            }
            viewBinding.remove(view)
        } else {
            view.getContextBinding()?.let {
                notifyBindingChanges(it)
            }
        }
    }

    private fun notifyBindingTokens(
        bindingTokens: List<String>,
        contextStack: MutableList<ContextBinding>,
        binding: Binding<*>
    ) {
        if (bindingTokens.isNotEmpty()) {
            bindingTokens.forEach { expression ->
                linkBindingsToNotifyListeners(expression, contextStack, binding)
            }
        } else {
            val value = contextDataEvaluation.evaluateBindExpression(listOf(), binding.bind)
            binding.notifyChanges(value)
        }
    }

    private fun linkBindingsToNotifyListeners(
        expression: String,
        contextStack: MutableList<ContextBinding>,
        binding: Binding<*>
    ) {
        val contextId = expression.getContextId()
        for (contextBinding in contextStack) {
            if (contextBinding.context.id == contextId) {
                contextBinding.bindings.add(binding)
                notifyBindingChanges(contextBinding)
                break
            }
        }
    }

    fun getContextsFromBind(originView: View, binding: Bind.Expression<*>): List<ContextData> {
        val parentContexts = originView.getAllParentContextWithGlobal()
        val contextIds = binding.filterBindingTokens().map { it.getContextId() }
        return parentContexts
            .filter { contextBinding -> contextIds.contains(contextBinding.context.id) }
            .map { it.context }
    }

    fun updateContext(view: View, setContextInternal: SetContextInternal) {
        if (setContextInternal.contextId == globalContext.context.id) {
            GlobalContext.set(setContextInternal.value, setContextInternal.path)
        } else {
            view.findParentContextWithId(setContextInternal.contextId)?.let { parentView ->
                val currentContextBinding = parentView.getContextBinding()
                currentContextBinding?.let {
                    setContextValue(currentContextBinding, setContextInternal)
                }
            }
        }
    }

    private fun setContextValue(
        contextBinding: ContextBinding,
        setContextInternal: SetContextInternal
    ) {
        val result = contextDataManipulator.set(
            contextBinding.context,
            setContextInternal.path,
            setContextInternal.value
        )

        if (result is ContextSetResult.Succeed) {
            contextBinding.context = result.newContext
            contextBinding.cache.evictAll()
            notifyBindingChanges(contextBinding)
        }
    }

    internal fun notifyBindingChanges(contextBinding: ContextBinding) {
        val contextData = contextBinding.context
        val bindings = contextBinding.bindings

        bindings.forEach { binding ->
            val value = contextDataEvaluation.evaluateBindExpression(
                contextData,
                contextBinding.cache,
                binding.bind,
                binding.evaluatedExpressions
            )
            binding.notifyChanges(value)
        }
    }

    private fun View.getAllParentContextWithGlobal(): MutableList<ContextBinding> {
        val contexts = mutableListOf<ContextBinding>()
        contexts.addAll(getAllParentContexts())
        contexts.add(globalContext)
        return contexts
    }

    private fun updateGlobalContext(contextData: ContextData) {
        globalContext = globalContext.copy(context = contextData)
        globalContext.cache.evictAll()
        contexts[GLOBAL_CONTEXT_ID] = globalContext
        notifyBindingChanges(globalContext)
    }

    private fun <T> Bind.Expression<T>.filterBindingTokens(): List<String> {
        val bindings = mutableListOf<String>()

        fun addBindings(token: Token) {
            if (token is TokenFunction) {
                token.value.forEach { paramToken ->
                    addBindings(paramToken)
                }
            } else if (token is TokenBinding) {
                bindings.add(token.value)
            }
        }

        expressions.forEach { expressionToken ->
            addBindings(expressionToken.token)
        }

        return bindings
    }
}
