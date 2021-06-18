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

import android.view.View
import androidx.viewpager.widget.ViewPager
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.components.BaseComponentTest
import br.com.zup.beagle.android.components.Button
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.utils.handleEvent
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import br.com.zup.beagle.android.view.custom.BeaglePageView
import br.com.zup.beagle.android.view.custom.InternalBeagleFlexView
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.Style
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a PageView")
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

    @BeforeEach
    override fun setUp() {
        super.setUp()
        mockkStatic("br.com.zup.beagle.android.utils.WidgetExtensionsKt")
        every { ViewFactory.makeViewPager(any()) } returns beaglePageView
        every { ViewFactory.makeBeagleFlexView(any(), capture(styleSlot)) } returns beagleFlexView
        every { beagleFlexView.addView(any(), capture(styleSlot)) } just Runs
    }

    @DisplayName("When buildView is called")
    @Nested
    inner class BuildView {

        @DisplayName("Then should use style with grow 1")
        @Test
        fun testStyleWithGrow1() {
            // Given
            pageView = PageView(children)

            // When
            pageView.buildView(rootView)

            // Then
            assertEquals(1.0, styleSlot[0].flex?.grow)
            assertEquals(1.0, styleSlot[1].flex?.grow)
        }

        @DisplayName("Then should build one view if indicator is null")
        @Test
        fun testOneViewIfIndicatorIsNull() {
            // GIVEN
            pageView = PageView(children, pageIndicator = null)

            // WHEN
            pageView.buildView(rootView)

            // THEN
            verify(exactly = 1) { ViewFactory.makeViewPager(any()) }
            verify(atLeast = 1) { ViewFactory.makeBeagleFlexView(any(), styleSlot[0]) }
            verify(atLeast = 1) { beagleFlexView.addView(any(), styleSlot[1]) }
        }

        @DisplayName("Then should call addView with indicator")
        @Test
        fun testAddViewCallWithIndicator() {
            // GIVEN
            pageView = PageView(children, pageIndicatorComponent)

            // WHEN
            pageView.buildView(rootView)

            // THEN
            verify(exactly = 1) { beagleFlexView.addView(any<View>()) }
        }

        @DisplayName("Then should set onPageChangeListener")
        @Test
        fun testSetOnPageChangeListener() {
            // GIVEN
            every { beaglePageView.addOnPageChangeListener(capture(pageListenerSlot)) } just Runs
            pageView = PageView(children, pageIndicatorComponent, context, actions)
            every { pageView.handleEvent(rootView, beaglePageView, actions, "onChange", any()) } just Runs

            // WHEN
            pageView.buildView(rootView)
            pageListenerSlot.captured.onPageSelected(1)

            // THEN
            assertTrue { pageListenerSlot.isCaptured }
        }

        @DisplayName("Then should return PageViewTwo if current page is not null")
        @Test
        fun testPageViewTwoReturn() {
            // GIVEN
            val mockedView: InternalBeagleFlexView = mockk()
            mockkConstructor(PageViewTwo::class)
            every { anyConstructed<PageViewTwo>().buildView(any()) } returns mockedView
            pageView = PageView(children, context, actions, currentPage)

            // WHEN
            val result = pageView.buildView(rootView)

            // THEN
            assertEquals(mockedView, result)
        }

        @DisplayName("Then should return an empty BeagleFlexView if children is null")
        @Test
        fun testBeagleFlexViewEmpty() {
            // Given
            val children = null
            pageView = PageView(children, pageIndicator = null)

            // When
            val result = pageView.buildView(rootView) as BeagleFlexView

            // Then
            assertTrue(result.childCount == 0)
        }
    }
}
