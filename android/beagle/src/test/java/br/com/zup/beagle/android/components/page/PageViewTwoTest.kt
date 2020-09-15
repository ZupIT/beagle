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

import androidx.viewpager.widget.ViewPager
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.components.BaseComponentTest
import br.com.zup.beagle.android.components.Button
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.utils.Observer
import br.com.zup.beagle.android.utils.handleEvent
import br.com.zup.beagle.android.utils.observeBindChanges
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeaglePageView
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.Style
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import org.junit.Test

class PageViewTwoTest : BaseComponentTest() {

    private val beaglePageView: BeaglePageView = mockk(relaxed = true)
    private val children = listOf<ServerDrivenComponent>(mockk<Button>())
    private val context: ContextData = mockk(relaxed = true)
    private val onPageChange = listOf<Action>(mockk())
    private val currentPage: Bind<Int> = mockk()
    private val currentPageSlot = slot<Observer<Int?>>()
    private val pageListenerSlot = slot<ViewPager.OnPageChangeListener>()

    private lateinit var pageView: PageViewTwo

    private val styleSlot = mutableListOf<Style>()

    override fun setUp() {
        super.setUp()
        mockkStatic("br.com.zup.beagle.android.utils.WidgetExtensionsKt")
        every { anyConstructed<ViewFactory>().makeViewPager(any()) } returns beaglePageView
        every { anyConstructed<ViewFactory>().makeBeagleFlexView(any(), capture(styleSlot)) } returns beagleFlexView
        every { beagleFlexView.addView(any(), capture(styleSlot)) } just Runs
        every { beaglePageView.addOnPageChangeListener(capture(pageListenerSlot)) } just Runs
    }

    @Test
    fun buildView_should_use_style_with_grow_1() {
        // Given
        pageView = PageViewTwo(children, context)

        // When
        pageView.buildView(rootView)

        // Then
        kotlin.test.assertEquals(1.0, styleSlot[0].flex?.grow)
        kotlin.test.assertEquals(1.0, styleSlot[1].flex?.grow)
    }

    @Test
    fun buildView_should_make_and_addView_once_when_current_page_and_current_page_is_null() {
        // GIVEN
        pageView = PageViewTwo(children, context)

        // WHEN
        pageView.buildView(rootView)

        // THEN
        verify(exactly = once()) { anyConstructed<ViewFactory>().makeViewPager(any()) }
        verify(atLeast = once()) { anyConstructed<ViewFactory>().makeBeagleFlexView(any(), styleSlot[0]) }
        verify(atLeast = once()) { beagleFlexView.addView(any(), styleSlot[1]) }
    }

    @Test
    fun buildView_should_call_addView_when_current_page_is_not_null() {
        // GIVEN
        pageView = PageViewTwo(
            children,
            context,
            currentPage = currentPage
        )
        every {
            pageView.observeBindChanges(
                rootView = rootView,
                view = beaglePageView,
                bind = currentPage,
                observes = capture(currentPageSlot)
            )
        } just Runs

        // WHEN
        pageView.buildView(rootView)

        // THEN
        verify(exactly = once()) { anyConstructed<ViewFactory>().makeViewPager(any()) }
        verify(atLeast = once()) { anyConstructed<ViewFactory>().makeBeagleFlexView(any(), styleSlot[0]) }
        verify(atLeast = once()) { beagleFlexView.addView(any(), styleSlot[1]) }
    }


    @Test
    fun buildView_should_call_addView_when_on_page_change_is_not_null() {
        // GIVEN
        pageView = PageViewTwo(
            children,
            context,
            onPageChange
        )
        every { pageView.handleEvent(rootView, beaglePageView, onPageChange, "onChange", any()) } just Runs

        // WHEN
        pageView.buildView(rootView)

        // THEN
        verify(exactly = once()) { anyConstructed<ViewFactory>().makeViewPager(any()) }
        verify(atLeast = once()) { anyConstructed<ViewFactory>().makeBeagleFlexView(any(), styleSlot[0]) }
        verify(atLeast = once()) { beagleFlexView.addView(any(), styleSlot[1]) }
    }

    @Test
    fun buildView_should_make_and_addView_once_when_on_page_change_and_current_page_is_not_null() {
        // GIVEN
        commonMock()

        // WHEN
        pageView.buildView(rootView)

        // THEN
        verify(exactly = once()) { anyConstructed<ViewFactory>().makeViewPager(any()) }
        verify(atLeast = once()) { anyConstructed<ViewFactory>().makeBeagleFlexView(any(), styleSlot[0]) }
        verify(atLeast = once()) { beagleFlexView.addView(any(), styleSlot[1]) }
    }

    @Test
    fun buildView_should_set_on_page_change_listener() {
        // GIVEN
        commonMock()

        // WHEN
        pageView.buildView(rootView)
        pageListenerSlot.captured.onPageSelected(1)

        // THEN
        kotlin.test.assertTrue { pageListenerSlot.isCaptured }
    }


    @Test
    fun buildView_should_call_handle_event_when_on_page_selected_is_change() {
        // GIVEN
        commonMock()

        // WHEN
        pageView.buildView(rootView)
        pageListenerSlot.captured.onPageSelected(1)

        // THEN
        verify(exactly = once()) {
            pageView.handleEvent(rootView, beaglePageView, onPageChange, ContextData("onPageChange", 1))
        }
    }


    @Test
    fun buildView_should_call_swap_to_page_when_current_page_change() {
        // GIVEN
        commonMock()

        // WHEN
        pageView.buildView(rootView)
        currentPageSlot.captured.invoke(1)

        // THEN
        verify(exactly = once()) {
            beaglePageView.swapToPage(1)
        }
    }

    private fun commonMock() {
        pageView = PageViewTwo(
            children,
            context,
            onPageChange,
            currentPage
        )

        every {
            pageView.observeBindChanges(
                rootView = rootView,
                view = beagleFlexView,
                bind = currentPage,
                observes = capture(currentPageSlot)
            )
        } just Runs

        every {
            pageView.handleEvent(
                rootView,
                beaglePageView,
                onPageChange,
                ContextData("onPageChange", 1)
            )
        } just Runs
    }
}