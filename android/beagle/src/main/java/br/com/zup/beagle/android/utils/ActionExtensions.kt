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

import android.util.MalformedJsonException
import android.view.View
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.android.context.ContextActionExecutor
import br.com.zup.beagle.android.context.ContextDataValueResolver
import br.com.zup.beagle.android.context.isExpression
import br.com.zup.beagle.android.context.normalizeContextValue
import br.com.zup.beagle.android.data.serializer.BeagleMoshi
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.widget.RootView
import org.json.JSONArray
import org.json.JSONObject
import java.io.EOFException
import java.lang.NumberFormatException

internal var contextActionExecutor = ContextActionExecutor()
internal var contextDataValueResolver = ContextDataValueResolver()

/**
 * Execute a list of actions and create the implicit context with eventName and eventValue (optional).
 * @property rootView from buildView
 * @property origin view that triggered the action
 * @property actions is the list of actions to be executed
 * @property eventName is the name of event to be referenced inside the @property action list
 * @property eventValue is the value that the eventName name has created,
 * this could be a primitive or a object that will be serialized to JSON
 */
fun Action.handleEvent(
    rootView: RootView,
    origin: View,
    actions: List<Action>,
    eventName: String,
    eventValue: Any? = null
) {
    contextActionExecutor.executeActions(rootView, origin, this, actions, eventName, eventValue)
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
fun Action.handleEvent(
    rootView: RootView,
    origin: View,
    action: Action,
    eventName: String,
    eventValue: Any? = null
) {
    contextActionExecutor.executeActions(rootView, origin, this, listOf(action), eventName, eventValue)
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
    data: Any
): Any? {
    return try {
        return if (data is JSONObject || data is JSONArray || data.isExpression()) {
            val value = expressionOf<String>(data.toString()).evaluateForAction(rootView, this)
            value.tryToDeserialize()
        } else {
            data
        }
    } catch (ex: Exception) {
        BeagleMessageLogs.errorWhileTryingToEvaluateBinding(ex)
        null
    }
}

private fun String?.tryToDeserialize(): Any? {
    return try {
        val number = this?.tryToConvertToNumber()
        if (number != null) {
            number
        } else {
            val newValue = BeagleMoshi.moshi.adapter(Any::class.java).fromJson(this)
            contextDataValueResolver.parse(newValue)
        }
    } catch (ex: Exception) {
        if (this?.isNotEmpty() == true) {
            this
        } else {
            null
        }
    }
}

private fun String.tryToConvertToNumber(): Number? {
    return try {
        this.toInt()
    } catch (ex: NumberFormatException) {
        try {
            this.toDouble()
        } catch (ex: NumberFormatException) {
            null
        }
    }
}