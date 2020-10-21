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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.zup.beagle.R
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.engine.renderer.ActivityRootView
import br.com.zup.beagle.android.engine.renderer.FragmentRootView
import br.com.zup.beagle.android.utils.HandleEventDeprecatedConstants.HANDLE_EVENT_ACTIONS_POINTER
import br.com.zup.beagle.android.utils.HandleEventDeprecatedConstants.HANDLE_EVENT_DEPRECATED_MESSAGE
import br.com.zup.beagle.android.utils.HandleEventDeprecatedConstants.HANDLE_EVENT_POINTER
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.viewmodel.GenerateIdViewModel
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.core.ServerDrivenComponent

internal var viewFactory = ViewFactory()

/**
 * Execute a list of actions and create an implicit context with eventName and eventValue.
 * @property rootView from buildView
 * @property origin view that triggered the action
 * @property actions is the list of actions to be executed
 * @property context is the property that will contain the implicit context data, id and value in ContextData class
 * this could be a primitive or a object that will be serialized to JSON
 */
fun ServerDrivenComponent.handleEvent(
    rootView: RootView,
    origin: View,
    actions: List<Action>,
    context: ContextData? = null
) {
    contextActionExecutor.executeActions(rootView, origin, this, actions, context)
}

/**
 * Execute a list of actions and create an implicit context with eventName and eventValue.
 * @property rootView from buildView
 * @property origin view that triggered the action
 * @property actions is the list of actions to be executed
 * @property eventName is the name of event to be referenced inside the @property action list
 * @property eventValue is the value that the eventName name has created,
 * this could be a primitive or a object that will be serialized to JSON
 */
@Deprecated(HANDLE_EVENT_DEPRECATED_MESSAGE, ReplaceWith(HANDLE_EVENT_ACTIONS_POINTER))
fun ServerDrivenComponent.handleEvent(
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
fun ServerDrivenComponent.handleEvent(
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
fun ServerDrivenComponent.handleEvent(
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
 * Observe a specific Bind to changes. If the Bind is type of Value, then the actual value will be returned.
 * But if the value is an Expression, then the evaluation will be make.
 * @property rootView from buildView
 * @property view that will receive the binding
 * @property bind is the value that will retrieved or observed
 * @property observes is function that will be called when a expression is evaluated
 */
fun <T> ServerDrivenComponent.observeBindChanges(
    rootView: RootView,
    view: View,
    bind: Bind<T>,
    observes: Observer<T?>
) {
    val value = bind.observe(rootView, view, observes)
    if (bind is Bind.Value) {
        observes(value)
    }
}

/**
 * Transform your Component to a view.
 * @property activity <p>is the reference for your activity.
 * Make sure to use this method if you are inside a Activity because of the lifecycle</p>
 */
fun ServerDrivenComponent.toView(activity: AppCompatActivity, idView: Int = R.id.beagle_default_id): View =
    this.toView(ActivityRootView(activity, idView))

/**
 * Transform your Component to a view.
 * @property fragment <p>is the reference for your fragment.
 * Make sure to use this method if you are inside a Fragment because of the lifecycle</p>
 */
fun ServerDrivenComponent.toView(fragment: Fragment, idView: Int = R.id.beagle_default_id): View =
    this.toView(FragmentRootView(fragment, idView))


internal fun ServerDrivenComponent.toView(rootView: RootView): View {
    val viewModel = rootView.generateViewModelInstance<GenerateIdViewModel>()
    viewModel.createIfNotExisting(rootView.getParentId())
    val view = viewFactory.makeBeagleFlexView(rootView).apply {
        id = rootView.getParentId()
        addServerDrivenComponent(this@toView)
    }

    view.listenerOnViewDetachedFromWindow = {
        viewModel.setViewCreated(rootView.getParentId())
    }

    return view
}
