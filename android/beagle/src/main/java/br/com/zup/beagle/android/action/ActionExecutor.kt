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

import android.content.Context
import br.com.zup.beagle.action.CustomAction
import br.com.zup.beagle.action.FormValidation
import br.com.zup.beagle.action.Navigate
import br.com.zup.beagle.action.SendRequestAction
import br.com.zup.beagle.action.ShowNativeDialog
import br.com.zup.beagle.action.UpdateContext
import br.com.zup.beagle.android.engine.renderer.RootView
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.view.BeagleActivity
import br.com.zup.beagle.android.view.ServerDrivenState
import br.com.zup.beagle.widget.core.Action

internal class ActionExecutor(
    private val customActionHandler: CustomActionHandler? =
        BeagleEnvironment.beagleSdk.customActionHandler,
    private val navigationActionHandler: NavigationActionHandler =
        NavigationActionHandler(),
    private val showNativeDialogActionHandler: ShowNativeDialogActionHandler =
        ShowNativeDialogActionHandler(),
    private val formValidationActionHandler: DefaultActionHandler<FormValidation>? = null,
    private val sendRequestActionHandler: SendRequestActionHandler = SendRequestActionHandler(),
    private val updateContextActionHandler: UpdateContextActionHandler = UpdateContextActionHandler()
) {

    fun doAction(rootView: RootView, action: Action?) {
        val context = rootView.getContext()
        when (action) {
            is Navigate -> navigationActionHandler.handle(context, action)
            is ShowNativeDialog ->
                showNativeDialogActionHandler.handle(context, action)
            is FormValidation -> formValidationActionHandler?.handle(context, action)
            is SendRequestAction -> {
                sendRequestActionHandler.handle(rootView = rootView,
                    action = action) { actions ->
                    doAction(rootView, actions)
                }
            }
            is UpdateContext -> updateContextActionHandler.handle(context, action)
            is CustomAction -> customActionHandler?.handle(context, action, object : ActionListener {

                override fun onSuccess(action: Action) {
                    changeActivityState(context, ServerDrivenState.Loading(false))
                    doAction(rootView, action)
                }

                override fun onError(e: Throwable) {
                    changeActivityState(context, ServerDrivenState.Loading(false))
                    changeActivityState(context, ServerDrivenState.Error(e))
                }

                override fun onStart() {
                    changeActivityState(context, ServerDrivenState.Loading(true))
                }
            })
        }
    }

    fun doAction(rotView: RootView, actions: List<Action>?) {
        actions?.forEach { action ->
            doAction(rotView, action)
        }
    }

    private fun changeActivityState(context: Context, state: ServerDrivenState) {
        (context as? BeagleActivity)?.onServerDrivenContainerStateChanged(state)
    }
}

