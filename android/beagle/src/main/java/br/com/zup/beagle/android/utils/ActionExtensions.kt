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

package br.com.zup.beagle.android.utils

import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.android.context.ContextActionExecutor
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.widget.RootView
import org.json.JSONArray
import org.json.JSONObject

internal var contextActionExecutor = ContextActionExecutor()

/**
 * Execute a list of actions and create the implicit context with eventName and eventValue (optional).
 * @property rootView from buildView
 * @property actions is the list of actions to be executed
 * @property eventName is the name of event to be referenced inside the @property action list
 * @property eventValue is the value that the eventName name has created,
 * this could be a primitive or a object that will be serialized to JSON
 */
fun Action.handleEvent(
    rootView: RootView,
    actions: List<Action>,
    eventName: String,
    eventValue: Any? = null
) {
    contextActionExecutor.executeActions(rootView, this, actions, eventName, eventValue)
}

/**
 * Execute an action and create the implicit context with eventName and eventValue (optional).
 * @property rootView from buildView
 * @property action is the action to be executed
 * @property eventName is the name of event to be referenced inside the @property action list
 * @property eventValue is the value that the eventName name has created,
 * this could be a primitive or a object that will be serialized to JSON
 */
fun Action.handleEvent(
    rootView: RootView,
    action: Action,
    eventName: String,
    eventValue: Any? = null
) {
    contextActionExecutor.executeActions(rootView, this, listOf(action), eventName, eventValue)
}

/**
 * Evaluate the expression to a value
 * @property rootView from buildView
 * @property bind has the expression to be evaluated
 */
fun <T> Action.evaluateExpression(
    rootView: RootView,
    bind: Bind<T>
): T? {
    return bind.evaluateForAction(rootView, this)
}

internal fun Action.evaluateExpression(
    rootView: RootView,
    expressionData: String
): Any? {
    return try {
        val value = expressionOf<String>(expressionData).evaluateForAction(rootView, this) ?: ""
        when {
            value.startsWith("{") -> JSONObject(value)
            value.startsWith("[") -> JSONArray(value)
            else -> value
        }
    } catch (ex: Exception) {
        BeagleMessageLogs.errorWhileTryingToEvaluateBinding(ex)
        null
    }
}