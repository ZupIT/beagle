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

package br.com.zup.beagle.android.components.layout

import android.view.View
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.components.OnInitiableComponent
import br.com.zup.beagle.android.components.OnInitiableComponentImpl
import br.com.zup.beagle.android.context.ContextComponent
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.utils.StyleManager
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.core.MultiChildComponent
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.Style

/**
 *  The container component is a general container that can hold other components inside.
 *
 * @param children define a list of components that are part of the container.
 * @param context define the contextData that be set to container.
 * @param onInit it is a parameter that allows you to define a list of actions to be performed
 * when the Widget is displayed.
 * @param styleId reference a native style in your local styles file to be applied on this container
 */
@RegisterWidget("container")
data class Container(
    override val children: List<ServerDrivenComponent>? = null,
    override val context: ContextData? = null,
    override val onInit: List<Action>? = null,
    val styleId: String? = null,
) : WidgetView(), OnInitiableComponent by OnInitiableComponentImpl(onInit),
    ContextComponent, MultiChildComponent {

    @Transient
    private val styleManager: StyleManager = StyleManager()

    override fun buildView(rootView: RootView): View {
        val styleId = styleManager.getContainerStyle(styleId)
        val view = if (styleId == 0) ViewFactory.makeBeagleFlexView(rootView, style ?: Style())
        else ViewFactory.makeBeagleFlexView(rootView, style ?: Style(), styleId)

        handleOnInit(rootView, view)
        return view.apply {
            addView(children)
        }
    }
}
