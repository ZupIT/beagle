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

package br.com.zup.beagle.android.engine.renderer.layout

import android.view.View
import android.view.ViewGroup
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.android.engine.renderer.LayoutViewRenderer
import br.com.zup.beagle.android.engine.renderer.RootView
import br.com.zup.beagle.android.engine.renderer.ViewRendererFactory
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.core.Style
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

        val styleParent = Style(flex = Flex(flexDirection = flexDirection))
        val styleChild = Style(flex = Flex(grow = 1.0))

        return viewFactory.makeBeagleFlexView(rootView.getContext(), styleParent).apply {
            addView(if (scrollDirection == ScrollAxis.HORIZONTAL) {
                viewFactory.makeHorizontalScrollView(rootView.getContext()).apply {
                    isHorizontalScrollBarEnabled = scrollBarEnabled
                    addChildrenViews(this, component.children, rootView, styleChild)
                }
            } else {
                viewFactory.makeScrollView(rootView.getContext()).apply {
                    isVerticalScrollBarEnabled = scrollBarEnabled
                    addChildrenViews(this, component.children, rootView, styleChild)
                }
            }, styleParent)
        }
    }

    private fun addChildrenViews(
        scrollView: ViewGroup,
        children: List<ServerDrivenComponent>,
        rootView: RootView,
        styleChild: Style
    ) {
        val viewGroup = viewFactory.makeBeagleFlexView(rootView.getContext(), styleChild)
        children.forEach { component ->
            viewGroup.addServerDrivenComponent(component, rootView)
        }
        scrollView.addView(viewGroup)
    }
}
