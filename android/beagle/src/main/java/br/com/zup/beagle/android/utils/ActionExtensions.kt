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
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.core.ServerDrivenComponent

fun Action.handleEvent(
    rootView: RootView,
    actions: List<Action>,
    eventName: String,
    eventValue: Any? = null
) {
    contextActionExecutor.execute(rootView, actions, eventName, eventValue)
}

fun Action.handleEvent(
    rootView: RootView,
    action: Action,
    eventName: String,
    eventValue: Any? = null
) {
    contextActionExecutor.execute(rootView, action, eventName, eventValue)
}

fun ServerDrivenComponent.handleEvent(
    rootView: RootView,
    actions: List<Action>,
    eventName: String,
    eventValue: Any? = null
) {
    contextActionExecutor.execute(rootView, actions, eventName, eventValue)
}

fun ServerDrivenComponent.handleEvent(
    rootView: RootView,
    action: Action,
    eventName: String,
    eventValue: Any? = null
) {
    contextActionExecutor.execute(rootView, action, eventName, eventValue)
}