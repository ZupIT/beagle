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

package br.com.zup.beagle.android.action

import br.com.zup.beagle.action.Route
import br.com.zup.beagle.android.engine.renderer.RootView
import br.com.zup.beagle.android.view.BeagleNavigator

internal sealed class Navigate : Action {

    data class OpenExternalURL(val url: String) : Navigate() {
        override fun execute(rootView: RootView) {
            BeagleNavigator.openExternalURL(rootView.getContext(), url)
        }
    }

    class OpenNativeRoute(val route: String,
                          val shouldResetApplication: Boolean = false,
                          val data: Map<String, String>? = null) : Navigate() {
        override fun execute(rootView: RootView) {
            BeagleNavigator.openNativeRoute(rootView.getContext(), route, data, shouldResetApplication)
        }
    }

    data class PushStack(val route: Route) : Navigate() {
        override fun execute(rootView: RootView) {
            BeagleNavigator.pushStack(rootView.getContext(), route)
        }
    }

    class PopStack : Navigate() {
        override fun execute(rootView: RootView) {
            BeagleNavigator.popStack(rootView.getContext())
        }
    }

    data class PushView(val route: Route) : Navigate() {
        override fun execute(rootView: RootView) {
            BeagleNavigator.pushView(rootView.getContext(), route)
        }
    }

    class PopView : Navigate() {
        override fun execute(rootView: RootView) {
            BeagleNavigator.popView(rootView.getContext())
        }
    }

    data class PopToView(val route: String) : Navigate() {
        override fun execute(rootView: RootView) {
            BeagleNavigator.popToView(rootView.getContext(), route)
        }
    }

    data class ResetApplication(val route: Route) : Navigate() {
        override fun execute(rootView: RootView) {
            BeagleNavigator.resetApplication(rootView.getContext(), route)
        }
    }

    data class ResetStack(val route: Route) : Navigate() {
        override fun execute(rootView: RootView) {
            BeagleNavigator.resetStack(rootView.getContext(), route)
        }
    }
}
