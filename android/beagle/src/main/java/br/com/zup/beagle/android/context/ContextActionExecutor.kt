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
import br.com.zup.beagle.android.data.serializer.BeagleMoshi
import br.com.zup.beagle.android.utils.generateViewModelInstance
import br.com.zup.beagle.android.view.viewmodel.ScreenContextViewModel
import br.com.zup.beagle.android.widget.RootView
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
        if (eventValue != null) {
            val viewModel = rootView.generateViewModelInstance<ScreenContextViewModel>()
            val contextData = ContextData(
                id = eventName,
                value = parseToJSONObject(eventValue)
            )
            viewModel.contextDataManager.handleContext(rootView, contextData, action)
        } else {
            action.execute(rootView)
        }
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

    private fun ContextDataManager.handleContext(
        rootView: RootView,
        contextData: ContextData,
        action: Action
    ) {
        addContext(contextData)
        action.execute(rootView)
        removeContext(contextData.id)
    }
}