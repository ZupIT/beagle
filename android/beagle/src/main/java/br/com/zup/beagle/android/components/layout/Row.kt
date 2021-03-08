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
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.ViewConvertable
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.core.MultiChildComponent
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.widget.core.AlignContent
import br.com.zup.beagle.widget.core.AlignItems
import br.com.zup.beagle.widget.core.AlignSelf
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.FlexDirection
import br.com.zup.beagle.widget.core.FlexWrap
import br.com.zup.beagle.widget.core.JustifyContent

/**
 *  The container component is a general container that can hold other components inside.
 *
 * @param children define a list of components that are part of the container.
 * when the Widget is displayed.
 */
data class Row(
    override val children: List<ServerDrivenComponent>,
    val reverse: Boolean? = false,
) : ViewConvertable, MultiChildComponent {

    @Transient
    private val viewFactory = ViewFactory()

    override fun buildView(rootView: RootView): View {
        val direction = if (reverse == true) FlexDirection.ROW_REVERSE else FlexDirection.ROW
        val flex = Flex(
            flexDirection = direction,
            alignSelf = AlignSelf.FLEX_START,
        )
        val view = viewFactory.makeBeagleFlexView(rootView, Style(flex = flex))
        return view.apply {
            addChildren(this)
        }
    }

    private fun addChildren(beagleFlexView: BeagleFlexView) {
        children.forEach { child ->
            beagleFlexView.addServerDrivenComponent(child)
        }
    }
}
