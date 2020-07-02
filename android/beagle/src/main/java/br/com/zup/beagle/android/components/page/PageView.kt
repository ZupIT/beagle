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

package br.com.zup.beagle.android.components.page

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import br.com.zup.beagle.android.context.ContextComponent
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.engine.renderer.ViewRendererFactory
import br.com.zup.beagle.android.view.custom.BeaglePageView
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.core.UnitType
import br.com.zup.beagle.widget.core.UnitValue

data class PageView(
    val children: List<ServerDrivenComponent>,
    val pageIndicator: PageIndicatorComponent? = null,
    override val context: ContextData? = null
) : WidgetView(), ContextComponent {

    @Transient
    private val viewFactory: ViewFactory = ViewFactory()

    @Transient
    private val viewRendererFactory: ViewRendererFactory = ViewRendererFactory()

    override fun buildView(rootView: RootView): View {
        val style = Style(flex = Flex(grow = 1.0))

        val viewPager = viewFactory.makeViewPager(rootView.getContext()).apply {
            adapter = PageViewAdapter(rootView, children, viewFactory)
        }

        val container = viewFactory.makeBeagleFlexView(rootView.getContext(), style).apply {
            addView(viewPager, style)
        }

        pageIndicator?.let {
            val pageIndicatorView = viewRendererFactory.make(it).build(rootView)
            setupPageIndicator(children.size, viewPager, pageIndicator)
            container.addView(pageIndicatorView)
        }

        return container
    }

    private fun setupPageIndicator(
        pages: Int,
        viewPager: BeaglePageView,
        pageIndicator: PageIndicatorComponent?
    ) {
        pageIndicator?.initPageView(viewPager)
        pageIndicator?.setCount(pages)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                pageIndicator?.onItemUpdated(position)
            }
        })
    }
}

internal class PageViewAdapter(
    private val rootView: RootView,
    private val children: List<ServerDrivenComponent>,
    private val viewFactory: ViewFactory
) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = viewFactory.makeBeagleFlexView(rootView.getContext()).also {
            it.addServerDrivenComponent(children[position], rootView)
        }
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    override fun getCount(): Int = children.size

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
