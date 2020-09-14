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

import android.view.View
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.ContextActionExecutor
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.android.context.isExpression
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.utils.HandleEventDeprecatedConstants.HANDLE_EVENT_ACTIONS_POINTER
import br.com.zup.beagle.android.utils.HandleEventDeprecatedConstants.HANDLE_EVENT_DEPRECATED_MESSAGE
import br.com.zup.beagle.android.utils.HandleEventDeprecatedConstants.HANDLE_EVENT_POINTER
import br.com.zup.beagle.android.widget.RootView
import org.json.JSONArray
import org.json.JSONObject

internal var contextActionExecutor = ContextActionExecutor()

/**
 * Execute a list of actions and create the implicit context with eventName and eventValue (optional).
 * @property rootView from buildView
 * @property origin view that triggered the action
 * @property actions is the list of actions to be executed
 * @property context is the property that will contain the implicit context data, id and value in ContextData class
 * this could be a primitive or a object that will be serialized to JSON
 */
fun Action.handleEvent(
    rootView: RootView,
    origin: View,
    actions: List<Action>,
    context: ContextData? = null
) {
    contextActionExecutor.executeActions(rootView, origin, this, actions, context)
}

/**
 * Execute a list of actions and create the implicit context with eventName and eventValue (optional).
 * @property rootView from buildView
 * @property origin view that triggered the action
 * @property actions is the list of actions to be executed
 * @property eventName is the name of event to be referenced inside the @property action list
 * @property eventValue is the value that the eventName name has created,
 * this could be a primitive or a object that will be serialized to JSON
 */
@Deprecated(HANDLE_EVENT_DEPRECATED_MESSAGE, ReplaceWith(HANDLE_EVENT_ACTIONS_POINTER))
fun Action.handleEvent(
    rootView: RootView,
    origin: View,
    actions: List<Action>,
    eventName: String,
    eventValue: Any? = null
) {
    eventValue?.let { handleEvent(rootView, origin, actions, ContextData(eventName, eventValue)) }
        ?: handleEvent(rootView, origin, actions)
}

/**
 * Execute an action and create the implicit context with eventName and eventValue (optional).
 * @property rootView from buildView
 * @property origin view that triggered the action
 * @property action is the action to be executed
 * @property context is the property that will contain the implicit context data, id and value in ContextData class
 * this could be a primitive or a object that will be serialized to JSON
 */
fun Action.handleEvent(
    rootView: RootView,
    origin: View,
    action: Action,
    context: ContextData? = null
) {
    contextActionExecutor.executeActions(rootView, origin, this, listOf(action), context)
}

/**
 * Execute an action and create the implicit context with eventName and eventValue (optional).
 * @property rootView from buildView
 * @property origin view that triggered the action
 * @property action is the action to be executed
 * @property eventName is the name of event to be referenced inside the @property action list
 * @property eventValue is the value that the eventName name has created,
 * this could be a primitive or a object that will be serialized to JSON
 */
@Deprecated(HANDLE_EVENT_DEPRECATED_MESSAGE, ReplaceWith(HANDLE_EVENT_POINTER))
fun Action.handleEvent(
    rootView: RootView,
    origin: View,
    action: Action,
    eventName: String,
    eventValue: Any? = null
) {
    eventValue?.let { handleEvent(rootView, origin, action, ContextData(eventName, eventValue)) }
        ?: handleEvent(rootView, origin, action)
}

/**
 * Evaluate the expression to a value
 * @property rootView from buildView
 * @property origin received on execute method
 * @property bind has the expression to be evaluated
 */
fun <T> Action.evaluateExpression(
    rootView: RootView,
    origin: View,
    bind: Bind<T>
): T? {
    return bind.evaluateForAction(rootView, origin, this)
}

internal fun Action.evaluateExpression(rootView: RootView, view: View, data: Any): Any? {
    return try {
        when {
            data is Bind.Value<*> -> data.value
            data is Bind.Expression<*> ->
                data.value.generateBindAndEvaluateForAction(rootView, view, this)
            data is JSONObject || data is JSONArray || data.isExpression() ->
                data.toString().generateBindAndEvaluateForAction(rootView, view, this)
            else -> data
        }
    } catch (ex: Exception) {
        BeagleMessageLogs.errorWhileTryingToEvaluateBinding(ex)
        null
    }
}

private fun String.generateBindAndEvaluateForAction(rootView: RootView, view: View, caller: Action): Any? {
    return expressionOf<String>(this)
        .evaluateForAction(rootView, view, caller)?.tryToDeserialize()
}