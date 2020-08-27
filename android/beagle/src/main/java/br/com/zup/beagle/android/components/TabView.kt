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

package br.com.zup.beagle.android.components

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
import br.com.zup.beagle.android.components.utils.styleManagerFactory
import br.com.zup.beagle.android.context.ContextComponent
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.utils.DeprecationMessages.DEPRECATED_TAB_VIEW
import br.com.zup.beagle.android.utils.dp
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.core.MultiChildComponent
import br.com.zup.beagle.widget.core.Flex
import com.google.android.material.tabs.TabLayout

private val TAB_BAR_HEIGHT = 48.dp()

@RegisterWidget
@Deprecated(DEPRECATED_TAB_VIEW)
data class TabView(
    override val children: List<TabItem>,
    val styleId: String? = null,
    override val context: ContextData? = null
) : WidgetView(), ContextComponent, MultiChildComponent {

    @Transient
    private val viewFactory: ViewFactory = ViewFactory()

    override fun buildView(rootView: RootView): View {
        val containerFlex = Style(flex = Flex(grow = 1.0))

        val container = viewFactory.makeBeagleFlexView(rootView, containerFlex)

        val tabLayout = makeTabLayout(rootView)

        val viewPager = viewFactory.makeViewPager(rootView.getContext()).apply {
            adapter = ContentAdapter(
                viewFactory = viewFactory,
                children = children,
                rootView = rootView
            )
        }

        val containerViewPager = viewFactory.makeBeagleFlexView(rootView).apply {
            addView(viewPager)
        }

        tabLayout.addOnTabSelectedListener(getTabSelectedListener(viewPager))
        viewPager.addOnPageChangeListener(getViewPagerChangePageListener(tabLayout))

        container.addView(tabLayout)
        container.addView(containerViewPager)

        return container
    }

    private fun makeTabLayout(rootView: RootView): TabLayout {
        val context = rootView.getContext()
        return viewFactory.makeTabLayout(context, styleManagerFactory.getTabViewStyle(styleId)).apply {
            layoutParams =
                viewFactory.makeFrameLayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    TAB_BAR_HEIGHT
                )
            tabMode = TabLayout.MODE_SCROLLABLE
            tabGravity = TabLayout.GRAVITY_FILL
            setData()
            addTabs(context)
        }
    }

    private fun TabLayout.setData() {
        styleManagerFactory.getTabBarTypedArray(context, styleId).apply {
            setSelectedTabIndicatorColor(
                getColor(
                    R.styleable.BeagleTabBarStyle_tabIndicatorColor,
                    styleManagerFactory.getTypedValueByResId(R.attr.colorAccent, context).data
                )
            )
            tabIconTint = getColorStateList(R.styleable.BeagleTabBarStyle_tabIconTint)
            recycle()
        }
    }

    private fun TabLayout.addTabs(context: Context) {
        for (i in children.indices) {
            addTab(newTab().apply {
                text = children[i].title
                children[i].icon?.let {
                    icon = getIconFromResources(context, it.mobileId)
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
    private val children: List<TabItem>,
    private val viewFactory: ViewFactory,
    private val rootView: RootView
) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    override fun getCount(): Int = children.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = viewFactory.makeBeagleFlexView(rootView).also {
            it.addServerDrivenComponent(children[position].child)
        }
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
