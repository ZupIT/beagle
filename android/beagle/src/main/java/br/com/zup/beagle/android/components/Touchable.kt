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

package br.com.zup.beagle.android.components

import android.view.View
import br.com.zup.beagle.action.Action
import br.com.zup.beagle.analytics.ClickEvent
import br.com.zup.beagle.android.action.ActionExecutor
import br.com.zup.beagle.android.data.PreFetchHelper
import br.com.zup.beagle.android.engine.renderer.ViewRendererFactory
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.widget.core.RootView
import br.com.zup.beagle.android.widget.core.ViewConvertable
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.navigation.Touchable

data class Touchable(
    override val action: Action,
    override val child: ServerDrivenComponent,
    override val clickAnalyticsEvent: ClickEvent? = null
) : Touchable(action, child, clickAnalyticsEvent), ViewConvertable {

    @Transient
    private val actionExecutor: ActionExecutor = ActionExecutor()

    @Transient
    private val preFetchHelper: PreFetchHelper = PreFetchHelper()

    @Transient
    private val viewRendererFactory: ViewRendererFactory = ViewRendererFactory()

    override fun buildView(rootView: RootView): View {
        preFetchHelper.handlePreFetch(rootView, action)

        return viewRendererFactory.make(child).build(rootView).apply {
            setOnClickListener {
                actionExecutor.doAction(context, action)
                clickAnalyticsEvent?.let {
                    BeagleEnvironment.beagleSdk.analytics?.sendClickEvent(it)
                }
            }
        }
    }
}
