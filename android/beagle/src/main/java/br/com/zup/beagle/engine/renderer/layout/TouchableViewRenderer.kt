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

package br.com.zup.beagle.engine.renderer.layout

import android.view.View
import br.com.zup.beagle.action.ActionExecutor
import br.com.zup.beagle.data.PreFetchHelper
import br.com.zup.beagle.engine.renderer.LayoutViewRenderer
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.engine.renderer.ViewRendererFactory
import br.com.zup.beagle.setup.BeagleEnvironment
import br.com.zup.beagle.view.ViewFactory
import br.com.zup.beagle.widget.navigation.Touchable

internal class TouchableViewRenderer(
    override val component: Touchable,
    private val actionExecutor: ActionExecutor = ActionExecutor(),
    private val preFetchHelper: PreFetchHelper = PreFetchHelper(),
    viewRendererFactory: ViewRendererFactory = ViewRendererFactory(),
    viewFactory: ViewFactory = ViewFactory()
) : LayoutViewRenderer<Touchable>(viewRendererFactory, viewFactory) {

    override fun buildView(rootView: RootView): View {
        preFetchHelper.handlePreFetch(rootView, component.action)
        return viewRendererFactory.make(component.child).build(rootView).apply {
            setOnClickListener {
                actionExecutor.doAction(context, component.action)
                component.clickAnalyticsEvent?.let {
                    BeagleEnvironment.beagleSdk.analytics?.
                    sendClickEvent(it)
                }
            }
        }
    }
}
