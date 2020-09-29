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

import android.widget.FrameLayout
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.components.utils.styleManagerFactory
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.utils.*
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeagleTabLayout
import br.com.zup.beagle.core.Style
import com.google.android.material.tabs.TabLayout
import io.mockk.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private const val DEFAULT_STYLE = "DummyStyle"

class TabBarTest : BaseComponentTest() {

    private val TAB_BAR_ITEM_TITLE = "Beagle Tab"

    private val tabLayout: BeagleTabLayout = mockk(relaxed = true)
    private val frameLayoutParams: FrameLayout.LayoutParams = mockk()
    private val tabBarItem: TabBarItem = mockk()
    private val styleManager: StyleManager = mockk(relaxed = true)
    private val currentTab: Bind<Int> = mockk(relaxed = true)
    private val onTabSelection: List<Action> = mockk(relaxed = true)
    private val icon: ImagePath.Local = mockk(relaxed = true)
    private val styleSlot = mutableListOf<Style>()
    private val onTabSelectedSlot = slot<TabLayout.OnTabSelectedListener>()

    private lateinit var tabBar: TabBar

    override fun setUp() {
        super.setUp()
        mockkStatic("br.com.zup.beagle.android.utils.WidgetExtensionsKt")
        every { BeagleEnvironment.application } returns mockk {
            every { resources.displayMetrics } returns mockk {
                density = 10f
            }
        }
        styleManagerFactory = styleManager
        every { BeagleEnvironment.beagleSdk.designSystem?.image(any()) } returns 10
        every { BeagleEnvironment.beagleSdk.designSystem?.tabViewStyle(any()) } returns 0

        every { anyConstructed<ViewFactory>().makeTabLayout(rootView.getContext(), any()) } returns tabLayout
        every { anyConstructed<ViewFactory>().makeFrameLayoutParams(any(), any()) } returns frameLayoutParams

        every { anyConstructed<ViewFactory>().makeBeagleFlexView(any(), capture(styleSlot)) } returns beagleFlexView
        every { beagleFlexView.addView(any(), capture(styleSlot)) } just Runs

        every { tabBarItem.title } returns TAB_BAR_ITEM_TITLE
        every { tabBarItem.icon } returns icon

        every { tabLayout.addOnTabSelectedListener(capture(onTabSelectedSlot)) } just Runs
        tabBar = TabBar(
            items = listOf(tabBarItem),
            styleId = DEFAULT_STYLE,
            currentTab = currentTab,
            onTabSelection = onTabSelection
        )
    }

    @Test
    fun buildView_should_use_style_with_grow_1() {
        //GIVEN

        // When
        tabBar.buildView(rootView)

        // Then
        assertEquals(1.0, styleSlot[0].flex?.grow)
    }

    @Test
    fun buildView_should_set_layout_params() {
        //GIVEN

        //WHEN
        val tabBarView = tabBar.buildView(rootView)

        //THEN
        assertEquals(frameLayoutParams.width, tabBarView.layoutParams.width)
        assertEquals(frameLayoutParams.height, tabBarView.layoutParams.height)
    }

    @Test
    fun buildView_should_set_tab_mode_as_scrollable() {
        //GIVEN

        //WHEN
        tabBar.buildView(rootView)

        //THEN
        assertEquals(TabLayout.MODE_SCROLLABLE, tabLayout.tabMode)
    }

    @Test
    fun buildView_should_set_tab_gravity_as_fill() {
        //GIVEN

        //WHEN
        tabBar.buildView(rootView)

        //THEN
        assertEquals(TabLayout.GRAVITY_FILL, tabLayout.tabGravity)
    }

    @Test
    fun build_view_should_add_tab_layout_listener() {
        //GIVEN

        //WHEN
        tabBar.buildView(rootView)

        //THEN
        assertTrue { onTabSelectedSlot.isCaptured }
    }

    @Test
    fun build_view_should_observers_current_tab() {
        //GIVEN
        val currentTabSlot = slot<Observer<Int?>>()
        every {
            tabBar.observeBindChanges(
                rootView = rootView,
                view = beagleFlexView,
                bind = currentTab,
                observes = capture(currentTabSlot)
            )
        } just Runs

        //WHEN
        tabBar.buildView(rootView)

        //THEN
        assertTrue { currentTabSlot.isCaptured }
    }

    @Test
    fun handle_event_should_be_called_when_tab_is_selected() {
        //GIVEN
        val tabMocked = mockk<TabLayout.Tab>()
        every { tabMocked.position } returns 0
        every {
            tabBar.handleEvent(rootView, tabLayout, onTabSelection, ContextData("onTabSelection", 0))
        } just Runs

        //WHEN
        tabBar.buildView(rootView)
        onTabSelectedSlot.captured.onTabSelected(tabMocked)

        //THEN
        verify(exactly = once()) {
            tabBar.handleEvent(rootView, tabLayout, onTabSelection, ContextData("onTabSelection", 0))
        }
    }

    @Test
    fun observers_bind_change_should_be_called_when_current_page_change() {
        //GIVEN
        val currentTabSlot = slot<Observer<Int?>>()

        every {
            tabBar.observeBindChanges(
                rootView = rootView,
                view = beagleFlexView,
                bind = currentTab,
                observes = capture(currentTabSlot)
            )
        } just Runs

        //WHEN
        tabBar.buildView(rootView)
        currentTabSlot.captured.invoke(1)

        //THEN
        verify(exactly = once()) { tabLayout.getTabAt(1)?.select() }
    }
}