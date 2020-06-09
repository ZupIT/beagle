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

package br.com.zup.beagle.engine.renderer.ui

import android.content.Context
import android.widget.FrameLayout
import br.com.zup.beagle.BaseTest
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.extensions.once
import br.com.zup.beagle.setup.BeagleEnvironment
import br.com.zup.beagle.utils.StyleManager
import br.com.zup.beagle.view.BeagleFlexView
import br.com.zup.beagle.view.BeaglePageView
import br.com.zup.beagle.view.BeagleTabLayout
import br.com.zup.beagle.view.ViewFactory
import br.com.zup.beagle.widget.ui.TabItem
import br.com.zup.beagle.widget.ui.TabView
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.Test

class TabViewRendererTest : BaseTest() {

    @InjectMockKs
    private lateinit var tabViewRenderer: TabViewRenderer

    @MockK
    private lateinit var rootView: RootView
    @RelaxedMockK
    private lateinit var context: Context
    @MockK
    private lateinit var viewFactory: ViewFactory
    @RelaxedMockK
    private lateinit var tabLayout: BeagleTabLayout
    @MockK
    private lateinit var viewPager: BeaglePageView
    @MockK
    private lateinit var beagleFlexView: BeagleFlexView
    @MockK
    private lateinit var tabView: TabView
    @MockK
    private lateinit var frameLayoutParams: FrameLayout.LayoutParams
    @RelaxedMockK
    private lateinit var tabItem: TabItem
    @RelaxedMockK
    private lateinit var styleManager: StyleManager

    override fun setUp() {
        super.setUp()

        every { BeagleEnvironment.application } returns mockk {
            every { resources.displayMetrics } returns mockk {
                density = 10f
            }
        }
        styleManagerFactory = styleManager
        every { BeagleEnvironment.beagleSdk.designSystem?.image(any()) } returns 10
        every { BeagleEnvironment.beagleSdk.designSystem?.tabViewStyle(any()) } returns 0

        every { rootView.getContext() } returns context

        every { viewFactory.makeBeagleFlexView(context, any()) } returns beagleFlexView
        every { viewFactory.makeBeagleFlexView(context) } returns beagleFlexView
        every { viewFactory.makeViewPager(context) } returns viewPager
        every { viewFactory.makeTabLayout(context) } returns tabLayout
        every { viewFactory.makeFrameLayoutParams(any(), any()) } returns frameLayoutParams

        every { viewPager.adapter = any() } just runs
        every { viewPager.addOnPageChangeListener(any()) } just runs

        every { beagleFlexView.addView(any()) } just runs

        every { tabView.tabItems } returns listOf(tabItem)
        every { tabView.style } returns ""
    }

    override fun tearDown() {
        super.tearDown()
        unmockkAll()
    }

    @Test
    fun build_should_return_view_with_tablayout_and_viewpager() {
        // When
        tabViewRenderer.build(rootView)

        // Then
        verify(exactly = once()) { beagleFlexView.addView(tabLayout) }
        verify(exactly = once()) { beagleFlexView.addView(viewPager) }
    }

    @Test
    fun build_should_add_listeners_to_tablayout_and_viewpager() {
        // When
        tabViewRenderer.build(rootView)

        // Then
        verify(exactly = once()) { tabLayout.addOnTabSelectedListener(any()) }
        verify(exactly = once()) { viewPager.addOnPageChangeListener(any()) }
    }

    @Test
    fun build_should_add_tabs() {
        // When
        tabViewRenderer.build(rootView)

        // Then
        verify { tabLayout.setTabTextColors(any(), any()) }
        verify { tabLayout.setSelectedTabIndicatorColor(any()) }
        verify { tabLayout.background = any() }
        verify { tabLayout.tabIconTint = any() }
        verify { tabLayout.addTab(any()) }
    }
}