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

import android.view.View
import br.com.zup.beagle.android.components.form.core.Constants
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.utils.handleEvent
import br.com.zup.beagle.android.view.BeagleActivity
import br.com.zup.beagle.android.view.ServerDrivenState
import br.com.zup.beagle.android.widget.RootView


@Deprecated(Constants.FORM_DEPRECATED_MESSAGE)
data class FormLocalAction(
    val name: String,
    val data: Map<String, String>
) : Action {

    @Transient
    var formLocalActionHandler: FormLocalActionHandler? = BeagleEnvironment.beagleSdk.formLocalActionHandler

    override fun execute(rootView: RootView, origin: View) {
        formLocalActionHandler?.handle(rootView.getContext(), this, object : ActionListener {

            override fun onSuccess(action: Action) {
                changeActivityState(rootView, ServerDrivenState.Loading(false))
                handleEvent(rootView, origin, action)
            }

            override fun onError(e: Throwable) {
                changeActivityState(rootView, ServerDrivenState.Loading(false))
                changeActivityState(rootView, ServerDrivenState.Error(e))
            }

            override fun onStart() {
                changeActivityState(rootView, ServerDrivenState.Loading(true))
            }
        })
    }

    private fun changeActivityState(rootView: RootView, state: ServerDrivenState) {
        (rootView.getContext() as? BeagleActivity)?.onServerDrivenContainerStateChanged(state)
    }
}
