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

package br.com.zup.beagle.utils

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.zup.beagle.context.ContextActionExecutor
import br.com.zup.beagle.core.Appearance
import br.com.zup.beagle.core.LayoutComponent
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.engine.renderer.ActivityRootView
import br.com.zup.beagle.engine.renderer.FragmentRootView
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.engine.renderer.ViewRendererFactory
import br.com.zup.beagle.view.viewmodel.ScreenContextViewModel
import br.com.zup.beagle.widget.core.Action
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScreenComponent

internal var viewRenderer = ViewRendererFactory()
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

fun br.com.zup.beagle.android.action.Action.handleEvent(
    rootView: RootView,
    actions: List<Action>,
    eventName: String,
    eventValue: Any? = null
) {
    contextActionExecutor.executeActions(rootView, actions, eventName, eventValue)
}

fun br.com.zup.beagle.android.action.Action.handleEvent(
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
    navigationBar = this.navigationBar,
    child = this.child,
    screenAnalyticsEvent = screenAnalyticsEvent
).applyAppearance(appearance ?: Appearance())

internal fun ServerDrivenComponent.toView(rootView: RootView): View {
    val view = if (this is LayoutComponent) {
        viewRenderer.make(this).build(rootView)
    } else {
        val container = Container(listOf(this))
        viewRenderer.make(container).build(rootView)
    }

    rootView.generateViewModelInstance<ScreenContextViewModel>().contextDataManager.evaluateAllContext()

    return view
}
