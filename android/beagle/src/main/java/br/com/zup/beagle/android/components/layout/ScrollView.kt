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
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.core.MultiChildComponent
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.FlexDirection
import br.com.zup.beagle.widget.core.ScrollAxis

/**
 * Component is a specialized container that will display its components in a Scroll
 *
 * @param children define a list of components to be displayed on this view.
 * @param scrollDirection define the scroll roll direction on screen.
 * @param scrollBarEnabled determine if the Scroll bar is displayed or not. It is displayed by default.
 * @param context define the contextData that be set to scrollView.
 */
@RegisterWidget("scrollview")
data class ScrollView(
    override val children: List<ServerDrivenComponent>? = null,
    val scrollDirection: ScrollAxis? = null,
    val scrollBarEnabled: Boolean? = null,
    override val context: ContextData? = null,
) : WidgetView(), ContextComponent, MultiChildComponent {

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

        return ViewFactory.makeBeagleFlexView(rootView, styleParent).apply {
            children?.let {
                addView(
                    if (scrollDirection == ScrollAxis.HORIZONTAL) {
                        ViewFactory.makeHorizontalScrollView(context).apply {
                            isHorizontalScrollBarEnabled = scrollBarEnabled
                            addChildrenViews(this, children, rootView, styleChild, true)
                        }
                    } else {
                        ViewFactory.makeScrollView(context).apply {
                            isVerticalScrollBarEnabled = scrollBarEnabled
                            addChildrenViews(this, children, rootView, styleChild, false)
                        }
                    }, styleParent
                )
            }
        }
    }

    private fun addChildrenViews(
        scrollView: ViewGroup,
        children: List<ServerDrivenComponent>,
        rootView: RootView,
        styleChild: Style,
        isHorizontal: Boolean,
    ) {
        val viewGroup = ViewFactory.makeBeagleFlexView(rootView, styleChild)

        viewGroup.addView(children, false)

        scrollView.addView(viewGroup)
        if (isHorizontal) {
            viewGroup.setWidthAndHeightAutoAndDirtyAllViews()
        } else {
            viewGroup.setHeightAutoAndDirtyAllViews()
        }
    }
}
