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
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import br.com.zup.beagle.R
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.components.utils.styleManagerFactory
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.utils.dp
import br.com.zup.beagle.android.utils.handleEvent
import br.com.zup.beagle.android.utils.observeBindChanges
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.widget.core.Flex
import com.google.android.material.tabs.TabLayout

private val TAB_BAR_HEIGHT = 48.dp()

@RegisterWidget
data class TabBar(
    val items: List<TabBarItem>,
    val styleId: String? = null,
    val currentTab: Bind<Int>? = null,
    val onTabSelection: List<Action>? = null
) : WidgetView() {

    @Transient
    private val viewFactory: ViewFactory = ViewFactory()

    override fun buildView(rootView: RootView): View {
        val containerFlex = Style(flex = Flex(grow = 1.0))
        val tabBar = makeTabLayout(rootView.getContext())
        val container = viewFactory.makeBeagleFlexView(rootView, containerFlex)
        configTabSelectedListener(tabBar, rootView)
        configCurrentTabObserver(tabBar, rootView)
        container.addView(tabBar)
        return container
    }

    private fun makeTabLayout(context: Context): TabLayout = viewFactory.makeTabLayout(
        context,
        styleManagerFactory.getTabViewStyle(styleId)
    ).apply {
        layoutParams =
            viewFactory.makeFrameLayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                TAB_BAR_HEIGHT
            )
        tabMode = TabLayout.MODE_SCROLLABLE
        tabGravity = TabLayout.GRAVITY_FILL
        configTabBarStyle()
        addTabs(context)
    }

    private fun TabLayout.configTabBarStyle() {
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
        for (i in items.indices) {
            addTab(newTab().apply {
                text = items[i].title
                items[i].icon?.let {
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

    private fun configTabSelectedListener(tabBar: TabLayout, rootView: RootView) {
        tabBar.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                onTabSelection?.let {
                    tab?.let { tab ->
                        handleEvent(rootView, tabBar, it, ContextData("onTabSelection", value = tab.position))
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun configCurrentTabObserver(tabBar: TabLayout, rootView: RootView) {
        currentTab?.let {
            observeBindChanges(rootView, tabBar, it) { position ->
                position?.let { newPosition ->
                    tabBar.getTabAt(newPosition)?.select()
                }
            }
        }
    }
}

data class TabBarItem(
    val title: String? = null,
    val icon: ImagePath.Local? = null
)
