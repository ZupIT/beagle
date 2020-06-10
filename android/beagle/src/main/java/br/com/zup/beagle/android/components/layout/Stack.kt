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
import br.com.zup.beagle.android.engine.renderer.ViewRendererFactory
import br.com.zup.beagle.android.view.BeagleFlexView
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.core.RootView
import br.com.zup.beagle.android.widget.core.ViewConvertable
import br.com.zup.beagle.core.FlexComponent
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.FlexPositionType
import br.com.zup.beagle.widget.layout.Stack

/**
 * component will hold a stack of components. It display its children relative to the stack.
 *
 * @param children define the components to be stacked on the Stack component view.
 *
 */
data class Stack(
    override val children: List<ServerDrivenComponent>
) : Stack(children), ViewConvertable {

    @Transient
    private val viewRendererFactory: ViewRendererFactory = ViewRendererFactory()

    @Transient
    private val viewFactory: ViewFactory = ViewFactory()

    override fun buildView(rootView: RootView): View {

        return viewFactory.makeBeagleFlexView(rootView.getContext()).apply {
            clipChildren = false
            addChildrenViews(children, this, rootView)
        }
    }

    private fun addChildrenViews(
        children: List<ServerDrivenComponent>,
        beagleFlexView: BeagleFlexView,
        rootView: RootView
    ) {
        children.forEach { component ->
            val absoluteFlex =
                (component as? FlexComponent)?.flex?.copy(positionType = FlexPositionType.ABSOLUTE)

            beagleFlexView.addView(
                viewRendererFactory.make(component).build(rootView),
                absoluteFlex ?: Flex(positionType = FlexPositionType.ABSOLUTE)
            )
        }
    }
}
