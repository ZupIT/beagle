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

package br.com.zup.beagle.action

import android.content.Context
import br.com.zup.beagle.view.BeagleNavigator

internal class NavigationActionHandler : DefaultActionHandler<Navigate> {

    override fun handle(context: Context, action: Navigate) {
        when (action) {
            is Navigate.OpenExternalURL -> openExternalURL(action, context)
            is Navigate.OpenNativeRoute -> openNativeRoute(action, context)
            is Navigate.PushStack -> pushStack(action, context)
            is Navigate.PopStack -> popStack(context)
            is Navigate.PushView -> pushView(action, context)
            is Navigate.PopView -> popView(context)
            is Navigate.PopToView -> popToView(action, context)
            is Navigate.ResetApplication -> resetApplication(action, context)
            is Navigate.ResetStack -> resetStack(action, context)
        }
    }

    private fun openExternalURL(action: Navigate.OpenExternalURL, context: Context) {
        BeagleNavigator.openExternalURL(context, action.url)
    }

    private fun openNativeRoute(action: Navigate.OpenNativeRoute, context: Context) {
        BeagleNavigator.openNativeRoute(context, action.route, action.data, action.shouldResetApplication)
    }

    private fun pushStack(action: Navigate.PushStack, context: Context) {
        BeagleNavigator.pushStack(context = context, route = action.route)
    }

    private fun popStack(context: Context) {
        BeagleNavigator.popStack(context = context)
    }

    private fun pushView(action: Navigate.PushView, context: Context) {
        BeagleNavigator.pushView(context = context, route = action.route)
    }

    private fun popView(context: Context) {
        BeagleNavigator.popView(context)
    }

    private fun popToView(action: Navigate.PopToView, context: Context) {
        BeagleNavigator.popToView(context, action.route)
    }

    private fun resetApplication(action: Navigate.ResetApplication, context: Context) {
        BeagleNavigator.resetApplication(context = context, route = action.route)
    }

    private fun resetStack(action: Navigate.ResetStack, context: Context) {
        BeagleNavigator.resetStack(context = context, route = action.route)
    }
}
