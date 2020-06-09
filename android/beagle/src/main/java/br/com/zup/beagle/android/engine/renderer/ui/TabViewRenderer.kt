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

package br.com.zup.beagle.android.engine.renderer.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import br.com.zup.beagle.R
import br.com.zup.beagle.android.engine.renderer.RootView
import br.com.zup.beagle.android.engine.renderer.UIViewRenderer
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.utils.StyleManager
import br.com.zup.beagle.android.utils.dp
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.ui.TabItem
import br.com.zup.beagle.widget.ui.TabView
import com.google.android.material.tabs.TabLayout

private val TABBAR_HEIGHT = 48.dp()
internal var styleManagerFactory = StyleManager()

internal class TabViewRenderer(
    override val component: TabView,
    private val viewFactory: ViewFactory = ViewFactory()
) : UIViewRenderer<TabView>() {

    override fun buildView(rootView: RootView): View {
        val containerFlex = Flex(grow = 1.0)

        val container = viewFactory.makeBeagleFlexView(rootView.getContext(), containerFlex)

        val tabLayout = makeTabLayout(rootView.getContext())

        val viewPager = viewFactory.makeViewPager(rootView.getContext()).apply {
            adapter = ContentAdapter(
                rootView = rootView,
                viewFactory = viewFactory,
                tabList = component.tabItems
            )
        }

        val containerViewPager =
            viewFactory.makeBeagleFlexView(rootView.getContext()).apply {
                addView(viewPager)
            }

        tabLayout.addOnTabSelectedListener(getTabSelectedListener(viewPager))
        viewPager.addOnPageChangeListener(getViewPagerChangePageListener(tabLayout))

        container.addView(tabLayout)
        container.addView(containerViewPager)
        return container
    }

    private fun makeTabLayout(context: Context): TabLayout {
        return viewFactory.makeTabLayout(context).apply {
            layoutParams =
                viewFactory.makeFrameLayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    TABBAR_HEIGHT
                )

            tabMode = TabLayout.MODE_SCROLLABLE
            tabGravity = TabLayout.GRAVITY_FILL
            setData()
            addTabs(context)
        }
    }

    private fun TabLayout.setData() {
        val typedArray = styleManagerFactory.getTabBarTypedArray(context, component.style)
        typedArray?.let {
            setTabTextColors(
                it.getColor(R.styleable.BeagleTabBarStyle_tabTextColor, Color.BLACK),
                it.getColor(R.styleable.BeagleTabBarStyle_tabSelectedTextColor, Color.GRAY)
            )
            setSelectedTabIndicatorColor(
                it.getColor(
                    R.styleable.BeagleTabBarStyle_tabIndicatorColor,
                    styleManagerFactory.getTypedValueByResId(R.attr.colorAccent, context).data
                )
            )
            background = it.getDrawable(R.styleable.BeagleTabBarStyle_tabBackground)
            tabIconTint = it.getColorStateList(R.styleable.BeagleTabBarStyle_tabIconTint)
            it.recycle()
        }
    }

    private fun TabLayout.addTabs(context: Context) {
        for (i in component.tabItems.indices) {
            addTab(newTab().apply {
                text = component.tabItems[i].title
                component.tabItems[i].icon?.let {
                    icon = getIconFromResources(context, it)
                }
            })
        }
    }

    private fun getIconFromResources(context: Context, icon: String): Drawable? {
        return BeagleEnvironment.beagleSdk.designSystem?.image(icon)?.let {
            ContextCompat.getDrawable(context, it)
        }
    }

    private fun getTabSelectedListener(viewPager: ViewPager): TabLayout.OnTabSelectedListener {
        return object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        }
    }

    private fun getViewPagerChangePageListener(tabLayout: TabLayout): ViewPager.OnPageChangeListener {
        return object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                tabLayout.getTabAt(position)?.select()
            }
        }
    }
}

internal class ContentAdapter(
    private val tabList: List<TabItem>,
    private val viewFactory: ViewFactory,
    private val rootView: RootView
) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    override fun getCount(): Int = tabList.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = viewFactory.makeBeagleFlexView(rootView.getContext()).also {
            it.addServerDrivenComponent(tabList[position].content, rootView)
        }
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}