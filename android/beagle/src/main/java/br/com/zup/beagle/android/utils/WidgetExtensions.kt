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
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.engine.renderer.ActivityRootView
import br.com.zup.beagle.android.engine.renderer.FragmentRootView
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.viewmodel.ScreenContextViewModel
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.core.ServerDrivenComponent

internal var viewFactory = ViewFactory()

/**
 * Execute a list of actions and create an implicit context with eventName and eventValue.
 * @property rootView from buildView
 * @property actions is the list of actions to be executed
 * @property eventName is the name of event to be referenced inside the @property action list
 * @property eventValue is the value that the eventName name has created,
 * this could be a primitive or a object that will be serialized to JSON
 */
fun ServerDrivenComponent.handleEvent(
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
fun ServerDrivenComponent.handleEvent(
    rootView: RootView,
    action: Action,
    eventName: String,
    eventValue: Any? = null
) {
    contextActionExecutor.executeActions(rootView, this, listOf(action), eventName, eventValue)
}

/**
 * Observe a specific Bind to changes. If the Bind is type of Value, then the actual value will be returned.
 * But if the value is an Expression, then the evaluation will be make.
 * @property rootView from buildView
 * @property bind is the value that will retrieved or observed
 * @property observes is function that will be called when a expression is evaluated
 */
fun <T> ServerDrivenComponent.observeBindChanges(
    rootView: RootView,
    bind: Bind<T>,
    observes: Observer<T>? = null
) {
    bind.observe(rootView, observes)
}

/**
 * Transform your Component to a view.
 * @property activity <p>is the reference for your activity.
 * Make sure to use this method if you are inside a Activity because of the lifecycle</p>
 */
fun ServerDrivenComponent.toView(activity: AppCompatActivity) = this.toView(ActivityRootView(activity))

/**
 * Transform your Component to a view.
 * @property fragment <p>is the reference for your fragment.
 * Make sure to use this method if you are inside a Fragment because of the lifecycle</p>
 */
fun ServerDrivenComponent.toView(fragment: Fragment) = this.toView(FragmentRootView(fragment))

internal fun ServerDrivenComponent.toView(rootView: RootView): View {
    return viewFactory.makeBeagleFlexView(rootView.getContext()).apply {
        addServerDrivenComponent(this@toView, rootView)
        rootView.generateViewModelInstance<ScreenContextViewModel>().evaluateContexts()
    }
}