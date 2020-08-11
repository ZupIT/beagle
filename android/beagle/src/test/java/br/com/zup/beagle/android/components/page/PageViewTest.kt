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

import android.support.v4.view.ViewPager
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.components.BaseComponentTest
import br.com.zup.beagle.android.components.Button
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.utils.handleEvent
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import br.com.zup.beagle.android.view.custom.BeaglePageView
import br.com.zup.beagle.core.Style
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PageViewTest : BaseComponentTest() {

    private val beaglePageView: BeaglePageView = mockk(relaxed = true)
    private val pageIndicatorComponent: PageIndicatorComponent = mockk(relaxed = true)
    private val children = listOf<ServerDrivenComponent>(mockk<Button>())
    private val context: ContextData = mockk(relaxed = true)
    private val actions = listOf<Action>(mockk(relaxed = true, relaxUnitFun = true))
    private val currentPage: Bind<Int> = mockk(relaxed = true, relaxUnitFun = true)
    private val pageListenerSlot = slot<ViewPager.OnPageChangeListener>()


    private lateinit var pageView: PageView

    private val styleSlot = mutableListOf<Style>()

    override fun setUp() {
        super.setUp()
        mockkStatic("br.com.zup.beagle.android.utils.WidgetExtensionsKt")
        every { anyConstructed<ViewFactory>().makeViewPager(any()) } returns beaglePageView
        every { anyConstructed<ViewFactory>().makeBeagleFlexView(any(), capture(styleSlot)) } returns beagleFlexView
        every { beagleFlexView.addView(any(), capture(styleSlot)) } just Runs
    }

    @Test
    fun buildView_should_use_style_with_grow_1() {
        // Given
        pageView = PageView(children)

        // When
        pageView.buildView(rootView)

        // Then
        assertEquals(1.0, styleSlot[0].flex?.grow)
        assertEquals(1.0, styleSlot[1].flex?.grow)
    }

    @Test
    fun buildView_should_make_and_addView_once_when_indicator_is_null() {
        // GIVEN
        pageView = PageView(children, pageIndicator = null)

        // WHEN
        pageView.buildView(rootView)

        // THEN
        verify(exactly = once()) { anyConstructed<ViewFactory>().makeViewPager(any()) }
        verify(atLeast = once()) { anyConstructed<ViewFactory>().makeBeagleFlexView(any(), styleSlot[0]) }
        verify(atLeast = once()) { beagleFlexView.addView(any(), styleSlot[1]) }
    }

    @Test
    fun buildView_should_call_addView_when_indicator_is_not_null() {
        // GIVEN
        pageView = PageView(children, pageIndicatorComponent)

        // WHEN
        pageView.buildView(rootView)

        // THEN
        verify(exactly = once()) { beagleFlexView.addView(any()) }
    }

    @Test
    fun buildView_should_set_on_page_change_listener() {
        // GIVEN
        mockOnChangePage()

        // WHEN
        pageView.buildView(rootView)
        pageListenerSlot.captured.onPageSelected(1)

        // THEN
        assertTrue { pageListenerSlot.isCaptured }
    }

    private fun mockOnChangePage() {
        every { beaglePageView.addOnPageChangeListener(capture(pageListenerSlot)) } just Runs
        pageView = PageView(children, pageIndicatorComponent, context, actions)
        every { pageView.handleEvent(rootView, beaglePageView, actions, "onChange", any()) } just Runs
    }

    @Test
    fun buildView_should_return_page_view_two_when_current_page_is_not_null() {
        // GIVEN
        val mockedView: BeagleFlexView = mockk()
        mockkConstructor(PageViewTwo::class)
        every { anyConstructed<PageViewTwo>().buildView(any()) } returns mockedView
        pageView = PageView(children, context, actions, currentPage)

        // WHEN
        val result = pageView.buildView(rootView)

        // THEN
        assertEquals(mockedView, result)
    }
}
