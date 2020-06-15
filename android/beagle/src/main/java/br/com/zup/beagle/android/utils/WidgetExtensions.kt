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

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.components.layout.Screen
import br.com.zup.beagle.android.components.layout.ScreenComponent
import br.com.zup.beagle.android.context.ContextActionExecutor
import br.com.zup.beagle.android.engine.renderer.ActivityRootView
import br.com.zup.beagle.android.engine.renderer.FragmentRootView
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.core.ServerDrivenComponent

internal var viewFactory = ViewFactory()
internal var contextActionExecutor = ContextActionExecutor()

fun ServerDrivenComponent.toView(context: Context) = this.toView(context as AppCompatActivity)

fun ServerDrivenComponent.toView(activity: AppCompatActivity) = this.toView(ActivityRootView(activity))

fun ServerDrivenComponent.toView(fragment: Fragment) = this.toView(FragmentRootView(fragment))

fun ServerDrivenComponent.handleEvent(
    rootView: RootView,
    actions: List<Action>,
    eventName: String,
    eventValue: Any? = null
) {
    contextActionExecutor.executeActions(rootView, actions, eventName, eventValue)
}

fun ServerDrivenComponent.handleEvent(
    rootView: RootView,
    action: Action,
    eventName: String,
    eventValue: Any? = null
) {
    contextActionExecutor.executeAction(rootView, action, eventName, eventValue)
}

fun Action.handleEvent(
    rootView: RootView,
    actions: List<Action>,
    eventName: String,
    eventValue: Any? = null
) {
    contextActionExecutor.executeActions(rootView, actions, eventName, eventValue)
}

fun Action.handleEvent(
    rootView: RootView,
    action: Action,
    eventName: String,
    eventValue: Any? = null
) {
    contextActionExecutor.executeAction(rootView, action, eventName, eventValue)
}

fun Screen.toView(activity: AppCompatActivity) = this.toComponent().toView(activity)

fun Screen.toView(fragment: Fragment) = this.toComponent().toView(fragment)

internal fun Screen.toComponent() = ScreenComponent(
    identifier = this.identifier,
    safeArea = this.safeArea,
    navigationBar = this.navigationBar,
    child = this.child,
    style = this.style,
    screenAnalyticsEvent = this.screenAnalyticsEvent
)

internal fun ServerDrivenComponent.toView(rootView: RootView): View =
    viewFactory.makeBeagleFlexView(rootView.getContext()).apply {
        addServerDrivenComponent(this@toView, rootView)
    }