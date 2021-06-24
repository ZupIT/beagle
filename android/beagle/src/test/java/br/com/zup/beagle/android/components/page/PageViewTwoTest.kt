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
import br.com.zup.beagle.android.utils.Observer
import br.com.zup.beagle.android.utils.handleEvent
import br.com.zup.beagle.android.utils.observeBindChanges
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeagleFlexView
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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a PageViewTwo")
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

    @BeforeEach
    override fun setUp() {
        super.setUp()
        mockkStatic("br.com.zup.beagle.android.utils.WidgetExtensionsKt")
        every { ViewFactory.makeViewPager(any()) } returns beaglePageView
        every { ViewFactory.makeBeagleFlexView(any(), capture(styleSlot)) } returns beagleFlexView
        every { beagleFlexView.addView(any(), capture(styleSlot)) } just Runs
        every { beaglePageView.addOnPageChangeListener(capture(pageListenerSlot)) } just Runs
    }

    @DisplayName("When buildView is called")
    @Nested
    inner class BuildView {

        @DisplayName("Then should use style with grow 1")
        @Test
        fun testStyleWithGrow1() {
            // Given
            pageView = PageViewTwo(children, context)

            // When
            pageView.buildView(rootView)

            // Then
            assertEquals(1.0, styleSlot[0].flex?.grow)
            assertEquals(1.0, styleSlot[1].flex?.grow)
        }

        @DisplayName("Then should build one view if current page is null")
        @Test
        fun testOneViewWithCurrentPageNull() {
            // GIVEN
            pageView = PageViewTwo(children, context)

            // WHEN
            pageView.buildView(rootView)

            // THEN
            verify(exactly = 1) { ViewFactory.makeViewPager(any()) }
            verify(atLeast = 1) { ViewFactory.makeBeagleFlexView(any(), styleSlot[0]) }
            verify(atLeast = 1) { beagleFlexView.addView(any(), styleSlot[1]) }
        }

        @DisplayName("Then should build one view with current page")
        @Test
        fun testOneViewWithCurrentPage() {
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
            verify(exactly = 1) { ViewFactory.makeViewPager(any()) }
            verify(atLeast = 1) { ViewFactory.makeBeagleFlexView(any(), styleSlot[0]) }
            verify(atLeast = 1) { beagleFlexView.addView(any(), styleSlot[1]) }
        }

        @DisplayName("Then should addView with onPageChange")
        @Test
        fun testOneViewWithOnPageChange() {
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
            verify(exactly = 1) { ViewFactory.makeViewPager(any()) }
            verify(atLeast = 1) { ViewFactory.makeBeagleFlexView(any(), styleSlot[0]) }
            verify(atLeast = 1) { beagleFlexView.addView(any(), styleSlot[1]) }
        }

        @DisplayName("Then should build one view with onPageChange and currentPage")
        @Test
        fun testOnPageChangeAndCurrentPage() {
            // GIVEN
            commonMock()

            // WHEN
            pageView.buildView(rootView)

            // THEN
            verify(exactly = 1) { ViewFactory.makeViewPager(any()) }
            verify(atLeast = 1) { ViewFactory.makeBeagleFlexView(any(), styleSlot[0]) }
            verify(atLeast = 1) { beagleFlexView.addView(any(), styleSlot[1]) }
        }

        @DisplayName("Then should set onPageChange")
        @Test
        fun testSetOnPageChangeListener() {
            // GIVEN
            commonMock()

            // WHEN
            pageView.buildView(rootView)
            pageListenerSlot.captured.onPageSelected(1)

            // THEN
            assertTrue { pageListenerSlot.isCaptured }
        }

        @DisplayName("Then should call handleEvent when page is changed")
        @Test
        fun testHandleEvent() {
            // GIVEN
            commonMock()

            // WHEN
            pageView.buildView(rootView)
            pageListenerSlot.captured.onPageSelected(1)

            // THEN
            verify(exactly = 1) {
                pageView.handleEvent(rootView, beaglePageView, onPageChange, ContextData("onPageChange", 1), "onPageChange")
            }
        }

        @DisplayName("Then should swapToPage when currentPage change")
        @Test
        fun testSwapToPage() {
            // GIVEN
            commonMock()

            // WHEN
            pageView.buildView(rootView)
            currentPageSlot.captured.invoke(1)

            // THEN
            verify(exactly = 1) {
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
                    ContextData("onPageChange", 1),
                    "onPageChange"
                )
            } just Runs
        }

        @DisplayName("Then should return an empty BeagleFlexView if children is null")
        @Test
        fun testBeagleFlexViewEmpty() {
            // Given
            val children = null
            pageView = PageViewTwo(children)

            // When
            val result = pageView.buildView(rootView) as BeagleFlexView

            // Then
            assertTrue(result.childCount == 0)
        }
    }
}
