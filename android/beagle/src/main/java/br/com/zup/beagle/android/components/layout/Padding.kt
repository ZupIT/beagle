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

import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.ViewConvertable
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.SingleChildComponent
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.widget.core.AlignContent
import br.com.zup.beagle.widget.core.AlignSelf
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.JustifyContent

/**
 *  The container component is a general container that can hold other components inside.
 *
 * when the Widget is displayed.
 */
data class Padding(
    override val child: ServerDrivenComponent,
) : ViewConvertable, SingleChildComponent {

    @Transient
    private val viewFactory = ViewFactory()

    override fun buildView(rootView: RootView): View {
        val view = viewFactory.makeBeagleFlexView(
            rootView,
            Style(
                flex = Flex(
                    justifyContent = JustifyContent.CENTER,
                    alignContent = AlignContent.CENTER,
                    alignSelf = AlignSelf.CENTER,
                    grow = 1.0
                ),
            ),
        )
        return view.apply {
            addChildren(this)
        }
    }

    private fun addChildren(beagleFlexView: BeagleFlexView) {
        beagleFlexView.addServerDrivenComponent(child)
    }
}
