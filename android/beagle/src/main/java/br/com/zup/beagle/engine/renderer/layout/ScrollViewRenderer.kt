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
import android.view.ViewGroup
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.engine.renderer.LayoutViewRenderer
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.engine.renderer.ViewRendererFactory
import br.com.zup.beagle.view.ViewFactory
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.FlexDirection
import br.com.zup.beagle.widget.layout.ScrollAxis
import br.com.zup.beagle.widget.layout.ScrollView

internal class ScrollViewRenderer(
    override val component: ScrollView,
    viewRendererFactory: ViewRendererFactory = ViewRendererFactory(),
    viewFactory: ViewFactory = ViewFactory()
) : LayoutViewRenderer<ScrollView>(viewRendererFactory, viewFactory) {

    override fun buildView(rootView: RootView): View {
        val scrollDirection = component.scrollDirection ?: ScrollAxis.VERTICAL
        val scrollBarEnabled = component.scrollBarEnabled ?: true

        val flexDirection = if (scrollDirection == ScrollAxis.VERTICAL) {
            FlexDirection.COLUMN
        } else {
            FlexDirection.ROW
        }

        val flexChild = Flex(flexDirection = flexDirection)
        val flexParent = Flex(grow = 1.0)

        return viewFactory.makeBeagleFlexView(rootView.getContext(), flexParent).apply {
            addView(if (scrollDirection == ScrollAxis.HORIZONTAL) {
                viewFactory.makeHorizontalScrollView(rootView.getContext()).apply {
                    isHorizontalScrollBarEnabled = scrollBarEnabled
                    addChildrenViews(this, component.children, rootView, flexChild)
                }
            } else {
                viewFactory.makeScrollView(rootView.getContext()).apply {
                    isVerticalScrollBarEnabled = scrollBarEnabled
                    addChildrenViews(this, component.children, rootView, flexChild)
                }
            }, flexParent)
        }
    }

    private fun addChildrenViews(
        scrollView: ViewGroup,
        children: List<ServerDrivenComponent>,
        rootView: RootView,
        flexChild: Flex
    ) {
        val viewGroup = viewFactory.makeBeagleFlexView(rootView.getContext(), flexChild)
        children.forEach { component ->
            viewGroup.addServerDrivenComponent(component, rootView)
        }
        scrollView.addView(viewGroup)
    }
}
