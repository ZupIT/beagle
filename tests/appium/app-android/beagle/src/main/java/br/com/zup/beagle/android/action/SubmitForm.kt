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
import android.view.ViewGroup
import android.view.ViewParent
import br.com.zup.beagle.android.components.form.SimpleForm
import br.com.zup.beagle.android.components.utils.beagleComponent
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.widget.RootView

/**
 * SubmitForm and a specific action of the SimpleForm component when executing it calls SimpleForm's onSubmit attribute.
 */
class SubmitForm : Action {

    override fun execute(rootView: RootView, origin: View) {
        var currentView: ViewParent? = origin.parent

        var foundSimpleForm = false

        while (!foundSimpleForm && currentView != null) {
            if (currentView is ViewGroup && currentView.beagleComponent is SimpleForm) {
                foundSimpleForm = true
            } else {
                currentView = currentView.parent
            }
        }

        if (foundSimpleForm) {
            ((currentView as? ViewGroup)?.beagleComponent as SimpleForm).submit(rootView, origin)
        } else {
            BeagleMessageLogs.logNotFoundSimpleForm()
        }
    }

}