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
import br.com.zup.beagle.android.view.BeagleFlexView
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.core.RootView
import br.com.zup.beagle.android.widget.core.ViewConvertable
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.FlexDirection
import br.com.zup.beagle.widget.layout.Vertical

data class Vertical(
    override val children: List<ServerDrivenComponent>,
    override val reversed: Boolean? = null
) : Vertical(children, reversed), ViewConvertable {

    @Transient
    private val viewFactory = ViewFactory()

    override fun buildView(rootView: RootView): View {
        return viewFactory.makeBeagleFlexView(rootView.getContext(),
            Flex(flexDirection = getYogaFlexDirection()))
            .apply {
                addChildren(this, rootView)
            }
    }

    private fun getYogaFlexDirection(): FlexDirection {
        return if (reversed == true) {
            FlexDirection.COLUMN_REVERSE
        } else {
            FlexDirection.COLUMN
        }
    }

    private fun addChildren(beagleFlexView: BeagleFlexView, rootView: RootView) {
        children.forEach { child ->
            beagleFlexView.addServerDrivenComponent(child, rootView)
        }
    }
}
