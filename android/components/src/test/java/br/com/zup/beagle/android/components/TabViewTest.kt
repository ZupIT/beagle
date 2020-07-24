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
import br.com.zup.beagle.android.designsystem.constant.DesignSystemConstant
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.manager.StyleManager
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.view.BeaglePageView
import br.com.zup.beagle.android.view.BeagleTabLayout
import br.com.zup.beagle.android.view.ComponentsViewFactory
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Test

private const val DEFAULT_STYLE = "DummyStyle"

class TabViewTest : BaseComponentTest() {

    private val tabLayout: BeagleTabLayout = mockk(relaxed = true)
    private val viewPager: BeaglePageView = mockk()
    private val frameLayoutParams: FrameLayout.LayoutParams = mockk()
    private val tabItem: TabItem = mockk(relaxed = true)

    private lateinit var tabView: TabView

    override fun setUp() {
        super.setUp()

        DesignSystemConstant.context =  mockk {
            every { resources.displayMetrics } returns mockk {
                density = 10f
            }
        }
        every { BeagleEnvironment.beagleSdk.designSystem?.image(any()) } returns 10
        every { BeagleEnvironment.beagleSdk.designSystem?.tabViewStyle(any()) } returns 0

        every { anyConstructed<ComponentsViewFactory>().makeViewPager(rootView.getContext()) } returns viewPager
        every { anyConstructed<ComponentsViewFactory>().makeTabLayout(rootView.getContext(), any()) } returns tabLayout
        every { anyConstructed<ComponentsViewFactory>().makeFrameLayoutParams(any(), any()) } returns frameLayoutParams

        every { viewPager.adapter = any() } just runs
        every { viewPager.addOnPageChangeListener(any()) } just runs

        every { beagleFlexView.addView(any()) } just runs


        tabView = TabView(listOf(tabItem), DEFAULT_STYLE)
    }

    @Test
    fun build_should_return_view_with_tablayout_and_viewpager() {
        // When
        tabView.buildView(rootView)

        // Then
        verify(exactly = once()) { beagleFlexView.addView(tabLayout) }
        verify(exactly = once()) { beagleFlexView.addView(viewPager) }
    }

    @Test
    fun build_should_add_listeners_to_tablayout_and_viewpager() {
        // When
        tabView.buildView(rootView)

        // Then
        verify(exactly = once()) { tabLayout.addOnTabSelectedListener(any()) }
        verify(exactly = once()) { viewPager.addOnPageChangeListener(any()) }
    }

    @Test
    fun build_should_add_tabs() {
        // When
        tabView.buildView(rootView)

        // Then
        verify { tabLayout.setSelectedTabIndicatorColor(any()) }
        verify { tabLayout.tabIconTint = any() }
        verify { tabLayout.addTab(any()) }
    }
}