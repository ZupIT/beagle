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
import br.com.zup.beagle.analytics2.ActionAnalyticsConfig
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.core.ServerDrivenComponent

/**
 * This abstract class represents an Action Analytics on Beagle
*/
abstract class ActionAnalytics : Action {

    /**
     * This attribute is an ActionAnalyticsConfig.
     * When this attribute is not null, this ActionAnalyticsConfig will override the AnalyticsConfig provided on
     * AnalyticsProvider.
    */
    abstract var analytics: ActionAnalyticsConfig?

    /**
     * Method executed when the function is triggered
     * @property rootView component that holds context, lifeCycleOwner and ViewModelStoreOwner
     * @property origin view that triggered the action
     * @property originComponent ServerDrivenComponent where the action was executed.
    */
    abstract fun execute(rootView: RootView, origin: View, originComponent: ServerDrivenComponent? = null)

    override fun execute(rootView: RootView, origin: View) {
        this.execute(rootView, origin, null)
    }
}
