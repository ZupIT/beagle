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
import br.com.zup.beagle.setup.BeagleEnvironment
import br.com.zup.beagle.view.BeagleActivity
import br.com.zup.beagle.view.ServerDrivenState

internal class ActionExecutor(
    private val customActionHandler: CustomActionHandler? =
        BeagleEnvironment.beagleSdk.customActionHandler,
    private val navigationActionHandler: NavigationActionHandler =
        NavigationActionHandler(),
    private val showNativeDialogActionHandler: ShowNativeDialogActionHandler =
        ShowNativeDialogActionHandler(),
    private val formValidationActionHandler: DefaultActionHandler<FormValidation>? = null
) {

    fun doAction(context: Context, action: Action?) {
        when (action) {
            is Navigate -> navigationActionHandler.handle(context, action)
            is ShowNativeDialog -> showNativeDialogActionHandler.handle(context, action)
            is FormValidation -> formValidationActionHandler?.handle(context, action)

            is CustomAction -> customActionHandler?.handle(context, action, object : ActionListener {

                override fun onSuccess(action: Action) {
                    changeActivityState(context, ServerDrivenState.Loading(false))
                    doAction(context, action)
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

    private fun changeActivityState(context: Context, state: ServerDrivenState) {
        (context as? BeagleActivity)?.onServerDrivenContainerStateChanged(state)
    }
}

