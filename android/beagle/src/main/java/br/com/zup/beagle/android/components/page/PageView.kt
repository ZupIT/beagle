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
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.ContextComponent
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.engine.renderer.ViewRendererFactory
import br.com.zup.beagle.android.utils.handleEvent
import br.com.zup.beagle.android.utils.observeBindChanges
import br.com.zup.beagle.android.view.custom.BeaglePageView
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.core.UnitType
import br.com.zup.beagle.widget.core.UnitValue

@RegisterWidget
data class PageView(
    val children: List<ServerDrivenComponent>,
    val pageIndicator: PageIndicatorComponent? = null,
    override val context: ContextData? = null,
    val onPageChange: List<Action>? = null,
    val currentPage: Bind<Int>? = null
) : WidgetView(), ContextComponent {

    @Deprecated(
        message = " This will be removed in a future version; " +
            "please refactor this component using new context features."
    )
    constructor(
        children: List<ServerDrivenComponent>,
        pageIndicator: PageIndicatorComponent? = null,
        context: ContextData? = null
    ) : this(
        children,
        pageIndicator,
        context,
        null,
        null
    )

    constructor(
        children: List<ServerDrivenComponent>,
        context: ContextData? = null,
        onPageChange: List<Action>? = null,
        currentPage: Bind<Int>? = null
    ) : this(
        children,
        null,
        context,
        onPageChange,
        currentPage
    )

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
        configPageChangeListener(
            viewPager,
            {
                pageIndicator?.onItemUpdated(it)
            },
            {
                executeActions(viewPager, rootView, it)
            }
        )

        observerCurrentPage(viewPager, rootView)
        return container
    }

    private fun executeActions(viewPager: BeaglePageView, rootView: RootView, position: Int){
        onPageChange?.let {listAction ->
               handleEvent(rootView, viewPager, listAction, "onChange", position)
        }
    }

    private fun configPageChangeListener(
        viewPager: BeaglePageView,
        onPageSelectedIndicator: (Int) -> Unit,
        onPageSelectedContext: (Int) -> Unit
    ) {
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                onPageSelectedIndicator(position)
                onPageSelectedContext(position)
            }
        })
    }

    private fun observerCurrentPage(viewPager: BeaglePageView, rootView: RootView){
        currentPage?.let {
            observeBindChanges(rootView = rootView, bind = it){position ->
                viewPager.swapToPage(position)
            }
        }
    }

    private fun setupPageIndicator(
        pages: Int,
        viewPager: BeaglePageView,
        pageIndicator: PageIndicatorComponent?
    ) {
        pageIndicator?.initPageView(viewPager)
        pageIndicator?.setCount(pages)
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
