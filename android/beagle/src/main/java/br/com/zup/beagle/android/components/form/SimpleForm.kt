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

package br.com.zup.beagle.android.components.form

import android.view.View
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.components.utils.beagleComponent
import br.com.zup.beagle.android.context.ContextComponent
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.data.PreFetchHelper
import br.com.zup.beagle.android.utils.handleEvent
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.core.MultiChildComponent
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.Style

/**
 * Component will define a submit handler for a SimpleForm.
 *
 * @param context define the contextData that be set to form
 *
 * @param children define the items on the simple form.
 *
 * @param onSubmit define the actions you want to execute when action submit form
 *
 */
@RegisterWidget
data class SimpleForm(
    override val context: ContextData? = null,
    val onSubmit: List<Action>,
    override val children: List<ServerDrivenComponent>
) : WidgetView(), ContextComponent, MultiChildComponent {

    @Transient
    private val viewFactory: ViewFactory = ViewFactory()

    @Transient
    private val preFetchHelper: PreFetchHelper = PreFetchHelper()

    override fun buildView(rootView: RootView): View {
        preFetchHelper.handlePreFetch(rootView, onSubmit)
        return viewFactory.makeBeagleFlexView(rootView, style ?: Style())
            .apply {
                beagleComponent = this@SimpleForm
                addChildrenForm(this)
            }
    }

    private fun addChildrenForm(beagleFlexView: BeagleFlexView) {
        children.forEach { child ->
            beagleFlexView.addServerDrivenComponent(child)
        }
    }

    fun submit(rootView: RootView, view: View) {
        onSubmit.forEach { action ->
            handleEvent(rootView, view, action)
        }
    }
}
