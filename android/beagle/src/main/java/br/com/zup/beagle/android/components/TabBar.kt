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
import android.icu.text.CaseMap
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import br.com.zup.beagle.R
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.components.utils.styleManagerFactory
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.ContextComponent
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.utils.dp
import br.com.zup.beagle.android.utils.handleEvent
import br.com.zup.beagle.android.utils.observeBindChanges
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.widget.core.Flex
import com.google.android.material.tabs.TabLayout

private val TAB_BAR_HEIGHT = 48.dp()

data class TabBar(
    val children: List<TabBarItem>,
    val styleId: String? = null,
    override val context: ContextData? = null,
    val currentTab: Bind<Int>? = null,
    val onTabSelection: List<Action>? = null
) : WidgetView(), ContextComponent {

    @Transient
    private val viewFactory: ViewFactory = ViewFactory()

    override fun buildView(rootView: RootView): View {
        val containerFlex = Style(flex = Flex(grow = 1.0))
        val tabBar = makeTabLayout(rootView.getContext())
        val container = viewFactory.makeBeagleFlexView(rootView.getContext(), containerFlex)
        configTabSelectedListener(tabBar, rootView)
        configCurrentTabObserver(tabBar, rootView)
        container.addView(tabBar)
        return container
    }

    private fun makeTabLayout(context: Context): TabLayout {
        return viewFactory.makeTabLayout(context, styleManagerFactory.getTabViewStyle(styleId)).apply {
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

    private fun configTabSelectedListener(tabBar: TabLayout, rootView: RootView) {
        tabBar.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(p0: TabLayout.Tab?) {
                onTabSelection?.let {
                    handleEvent(rootView, tabBar, it, "onChange")
                }
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {}

            override fun onTabUnselected(p0: TabLayout.Tab?) {}

        })
    }

    private fun configCurrentTabObserver(tabBar: TabLayout, rootView: RootView) {
        currentTab?.let {
            observeBindChanges(rootView, it) { position ->
                position?.let { newPosition ->
                    tabBar.getTabAt(newPosition)?.select()
                }
            }
        }

    }

}

data class TabBarItem(
    val title: String? = null,
    val icon: PathType.Local? = null
)