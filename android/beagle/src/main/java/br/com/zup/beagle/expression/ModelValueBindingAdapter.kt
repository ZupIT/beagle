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

package br.com.zup.beagle.expression

import br.com.zup.beagle.core.DataBindingComponent
import br.com.zup.beagle.core.HasChildren
import br.com.zup.beagle.core.HasOneChild
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.utils.CoroutineDispatchers
import br.com.zup.beagle.widget.core.WidgetView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.Array as KotlinArray

class ModelValueBindingAdapter {
    //TODO understand when and how to finish all jobs
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    fun evaluateBinding(dataBindingComponent: DataBindingComponent) {
        scope.launch {
            evaluateBindingSuspend(dataBindingComponent)
        }
    }

    fun evaluateInitialValue(dataBindingComponent: ServerDrivenComponent) {
        evaluateViewInitialValue(dataBindingComponent)
    }

    fun evaluateBinding(value: Value, dataBindingComponent: ServerDrivenComponent) {
        scope.launch {
            evaluateBindingSuspend(value, dataBindingComponent)
        }
    }

    private suspend fun evaluateBindingSuspend(dataBindingComponent: DataBindingComponent) {
        withContext(CoroutineDispatchers.Default) {
            if (haveModelPathOrJson(dataBindingComponent)) {
                val modelValueHelper = ModelValueHelper(dataBindingComponent)
                modelValueHelper.fetchModelValue(onSuccess = { value ->
                    evaluateBinding(value, dataBindingComponent)
                },
                    onError = Throwable::printStackTrace)
            }
        }
    }

    private fun haveModelPathOrJson(dataBindingComponent: DataBindingComponent) =
        dataBindingComponent.modelPath.isNullOrEmpty().not() ||
            dataBindingComponent.modelJson.isNullOrEmpty().not()

    private suspend fun evaluateBindingSuspend(value: Value, dataBindingComponent: ServerDrivenComponent) {
        withContext(CoroutineDispatchers.Default) {
            evaluateBindingRecursive(value, dataBindingComponent)
        }
    }

    private fun evaluateViewInitialValue(dataBindingComponent: ServerDrivenComponent) {
        (dataBindingComponent as? WidgetView)?.let { widgetView ->
            notifyInitialValues(widgetView.bindingProperties)
        }
    }

    private fun notifyInitialValues(bindingExpressions: List<BindingExpr<*>>) {
        toTypedKotlinArray<BindingExpr<Any>>(bindingExpressions).forEach { binding: BindingExpr<Any> ->
            binding.initialValue?.let {
                binding.notifyChange(
                    it
                )
            }
        }
    }

    private fun evaluateBindingRecursive(value: Value, dataBindingComponent: ServerDrivenComponent) {
        evaluateWidgetViewBinding(dataBindingComponent, value)
        if (dataBindingComponent is HasChildren && dataBindingComponent.children.isEmpty().not()) {
            dataBindingComponent.children.forEach {
                (it as? WidgetView)?.let { widgetView ->
                    if (haveModelPathOrJson(widgetView))
                        evaluateBinding(widgetView)
                    else
                        evaluateBindingRecursive(value, widgetView)
                } ?: run {
                    evaluateBindingRecursive(value, it)
                }
            }
        } else if (dataBindingComponent is HasOneChild) {
            (dataBindingComponent.child as? WidgetView)?.let { widgetView ->
                if (haveModelPathOrJson(widgetView))
                    evaluateBinding(widgetView)
                else
                    evaluateBindingRecursive(value, widgetView)
            } ?: run {
                evaluateBindingRecursive(value, dataBindingComponent.child)
            }
        }
    }

    private fun evaluateWidgetViewBinding(
        dataBindingComponent: ServerDrivenComponent,
        value: Value
    ) {
        (dataBindingComponent as? WidgetView)?.apply {
            evaluateBindingExpressions(value, this.bindingProperties)
        }
    }

    private fun evaluateBindingExpressions(data: Value, bindingExpressions: List<BindingExpr<*>>) {
        toTypedKotlinArray<BindingExpr<Any>>(bindingExpressions).forEach { binding: BindingExpr<Any> ->
            val expression = binding.expression ?: ""
            if (Binding.isBindingValue(expression)) {
                val evaluationBinding = Binding.valueOf(expression)

                val value = evaluationBinding.evaluate(data)
                if (value.isPrimitive()) {
                    scope.launch(CoroutineDispatchers.Main) {
                        when {
                            value.getAsPrimitive().isBoolean() -> binding.notifyChange(value.getAsBoolean())
                            value.getAsPrimitive().isNumber() -> binding.notifyChange(value.getAsNumber())
                            else -> binding.notifyChange(value.getAsString())
                        }
                    }
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified T> toTypedKotlinArray(list: List<*>): KotlinArray<T> {
        return (list as List<T>).toTypedArray()
    }
}
