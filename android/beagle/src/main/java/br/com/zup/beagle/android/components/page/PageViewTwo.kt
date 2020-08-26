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
import br.com.zup.beagle.android.utils.handleEvent
import br.com.zup.beagle.android.utils.observeBindChanges
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeaglePageView
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.core.MultiChildComponent
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.widget.core.Flex

internal data class PageViewTwo(
    override val children: List<ServerDrivenComponent>,
    override val context: ContextData? = null,
    val onPageChange: List<Action>? = null,
    val currentPage: Bind<Int>? = null
) : WidgetView(), ContextComponent, MultiChildComponent {

    @Transient
    private val viewFactory: ViewFactory = ViewFactory()

    override fun buildView(rootView: RootView): View {
        val style = Style(flex = Flex(grow = 1.0))
        val viewPager = viewFactory.makeViewPager(rootView.getContext()).apply {
            adapter = PageViewAdapterTwo(rootView, children, viewFactory)
        }

        val container = viewFactory.makeBeagleFlexView(rootView, style).apply {
            addView(viewPager, style)
        }

        configPageChangeListener(viewPager, rootView)
        observerCurrentPage(viewPager, rootView)

        return container
    }

    private fun configPageChangeListener(viewPager: BeaglePageView, rootView: RootView) {
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                executeActions(viewPager, rootView, position)
            }
        })
    }

    private fun executeActions(viewPager: BeaglePageView, rootView: RootView, position: Int) {
        onPageChange?.let { listAction ->
            handleEvent(rootView, viewPager, listAction, ContextData("onPageChange", position))
        }
    }

    private fun observerCurrentPage(viewPager: BeaglePageView, rootView: RootView){
        currentPage?.let {
            observeBindChanges(rootView = rootView, view = viewPager,  bind = it){position ->
                position?.let{
                    viewPager.swapToPage(position)
                }
            }
        }
    }
}

internal class PageViewAdapterTwo(
    private val rootView: RootView,
    private val children: List<ServerDrivenComponent>,
    private val viewFactory: ViewFactory
) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = viewFactory.makeBeagleFlexView(rootView).also {
            it.addServerDrivenComponent(children[position])
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
