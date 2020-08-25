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
import android.view.ViewGroup
import br.com.zup.beagle.android.context.ContextComponent
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.core.MultiChildComponent
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.FlexDirection
import br.com.zup.beagle.widget.core.ScrollAxis

@RegisterWidget
data class ScrollView(
    override val children: List<ServerDrivenComponent>,
    val scrollDirection: ScrollAxis? = null,
    val scrollBarEnabled: Boolean? = null,
    override val context: ContextData? = null
) : WidgetView(), ContextComponent, MultiChildComponent {

    @Transient
    private val viewFactory: ViewFactory = ViewFactory()

    override fun buildView(rootView: RootView): View {
        val scrollDirection = scrollDirection ?: ScrollAxis.VERTICAL
        val scrollBarEnabled = scrollBarEnabled ?: true

        val flexDirection = if (scrollDirection == ScrollAxis.VERTICAL) {
            FlexDirection.COLUMN
        } else {
            FlexDirection.ROW
        }

        val styleParent = Style(flex = Flex(grow = 1.0))
        val styleChild = Style(flex = Flex(flexDirection = flexDirection))

        return viewFactory.makeBeagleFlexView(rootView, styleParent).apply {

            addView(if (scrollDirection == ScrollAxis.HORIZONTAL) {
                viewFactory.makeHorizontalScrollView(context).apply {
                    isHorizontalScrollBarEnabled = scrollBarEnabled
                    addChildrenViews(this, children, rootView, styleChild)
                }
            } else {
                viewFactory.makeScrollView(context).apply {
                    isVerticalScrollBarEnabled = scrollBarEnabled
                    addChildrenViews(this, children, rootView, styleChild)
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
        val viewGroup = viewFactory.makeBeagleFlexView(rootView, styleChild)
        children.forEach { component ->
            viewGroup.addServerDrivenComponent(component)
        }
        scrollView.addView(viewGroup)
    }
}
