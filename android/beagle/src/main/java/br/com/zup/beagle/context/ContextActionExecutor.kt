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

package br.com.zup.beagle.context

import br.com.zup.beagle.core.Bind
import br.com.zup.beagle.core.ContextData
import br.com.zup.beagle.data.serializer.BeagleMoshi
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.setup.BindingAdapter
import br.com.zup.beagle.utils.generateViewModelInstance
import br.com.zup.beagle.view.viewmodel.ScreenContextViewModel
import br.com.zup.beagle.widget.core.Action
import org.json.JSONObject

private const val DEFAULT_KEY_NAME = "value"

internal class ContextActionExecutor {

    fun executeActions(
        rootView: RootView,
        actions: List<Action>,
        eventName: String,
        eventValue: Any? = null
    ) {
        actions.forEach {
            executeAction(rootView, it, eventName, eventValue)
        }
    }

    fun executeAction(
        rootView: RootView,
        action: Action,
        eventName: String,
        eventValue: Any? = null
    ) {
        if (action is BindingAdapter) {
            if (eventValue != null) {
                val viewModel = rootView.generateViewModelInstance<ScreenContextViewModel>()
                val contextData = ContextData(
                    id = eventName,
                    value = parseToJSONObject(eventValue)
                )
                handleContext(viewModel.contextDataManager, contextData, action.getBindAttributes())
            }

            action.getBindAttributes().filterNotNull().forEach { bind ->
                if (bind is Bind.Value) {
                    bind.bind()
                }
            }
        }

        (action as br.com.zup.beagle.android.action.Action).execute(rootView)
    }

    private fun parseToJSONObject(value: Any): JSONObject {
        return if (value is String || value is Number || value is Boolean) {
            JSONObject().apply {
                put(DEFAULT_KEY_NAME, value)
            }
        } else {
            val json = BeagleMoshi.moshi.adapter<Any>(value::class.java).toJson(value)
            JSONObject(json)
        }
    }

    private fun handleContext(
        contextDataManager: ContextDataManager,
        contextData: ContextData,
        bindAttributes: List<Bind<*>?>
    ) {
        contextDataManager.addContext(contextData)
        bindAttributes.filterNotNull().forEach { bind ->
            if (bind is Bind.Expression) {
                contextDataManager.addBindingToContext(bind)
            }
        }
        contextDataManager.evaluateContext(contextData.id)
        contextDataManager.removeContext(contextData.id)
    }
}