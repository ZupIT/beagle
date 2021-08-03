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

import android.graphics.Color
import br.com.zup.beagle.android.components.BaseComponentTest
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.utils.ColorUtils
import br.com.zup.beagle.android.utils.Observer
import br.com.zup.beagle.android.utils.observeBindChanges
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeaglePageIndicatorView
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

private const val SELECTED_COLOR = "#000000"
private const val UNSELECTED_COLOR = "#FF0000"

@DisplayName("Given Page Indicator")
class PageIndicatorTest : BaseComponentTest() {

    private val beaglePageIndicatorView: BeaglePageIndicatorView = mockk(relaxed = true, relaxUnitFun = true)
    private val numberOfPages: Int = RandomData.int()
    private val currentPage: Bind<Int> = mockk(relaxed = true, relaxUnitFun = true)
    private val currentPageSlot = slot<Observer<Int?>>()

    private lateinit var pageIndicator: PageIndicator

    @BeforeEach
    override fun setUp() {
        super.setUp()

        pageIndicator = PageIndicator(
            selectedColor = SELECTED_COLOR,
            unselectedColor = UNSELECTED_COLOR,
            numberOfPages = numberOfPages,
            currentPage = currentPage,
        )

        mockkStatic(Color::class)
        mockkStatic("br.com.zup.beagle.android.utils.WidgetExtensionsKt")
        mockkObject(ColorUtils::class)
        every { Color.parseColor(any()) } returns 0
        every { ViewFactory.makePageIndicator(any()) } returns beaglePageIndicatorView
        every {
            pageIndicator.observeBindChanges(
                rootView = rootView,
                view = beaglePageIndicatorView,
                bind = currentPage,
                observes = capture(currentPageSlot)
            )
        } just Runs
    }

    @DisplayName("When build view")
    @Nested
    inner class ColorTest {

        @DisplayName("Then should call correct color")
        @Test
        fun testCorrectColorCalled() {
            // When
            val view = pageIndicator.buildView(rootView)

            // Then
            assertEquals(beaglePageIndicatorView, view)
            verify(exactly = once()) {
                ColorUtils.hexColor(SELECTED_COLOR)
                ColorUtils.hexColor(UNSELECTED_COLOR)
                beaglePageIndicatorView.setSelectedColor(0)
                beaglePageIndicatorView.setUnselectedColor(0)
            }
        }

    }


    @DisplayName("When current page change")
    @Nested
    inner class OnItemUpdatedTest {

        @DisplayName("Then should call on item update")
        @Test
        fun testCallItemUpdate() {
            // Given
            val newPosition = RandomData.int()

            // When
            pageIndicator.buildView(rootView = rootView)
            currentPageSlot.captured.invoke(newPosition)

            // Then
            verify(exactly = once()) { pageIndicator.onItemUpdated(newPosition) }
        }

    }


    @DisplayName("When numbers of page is not null")
    @Nested
    inner class CountTest {

        @DisplayName("Then should call set count")
        @Test
        fun testCallSetCount() {
            // When
            pageIndicator.buildView(rootView = rootView)

            // Then
            verify(exactly = once()) { pageIndicator.setCount(numberOfPages) }
        }

    }


    @DisplayName("When set count")
    @Nested
    inner class CountCallTest {

        @DisplayName("Then should call set count from beagle page view")
        @Test
        fun testCallSetCount() {
            // Given
            val count = RandomData.int()

            // When
            pageIndicator.buildView(rootView)
            pageIndicator.setCount(count)

            // Then
            verify(exactly = once()) { beaglePageIndicatorView.setCount(count) }
        }

    }

    @DisplayName("When call item updated")
    @Nested
    inner class ItemUpdatedTest {

        @DisplayName("Then should set current index")
        @Test
        fun testSetCurrentIndex() {
            // Given
            val count = RandomData.int()

            // When
            pageIndicator.buildView(rootView)
            pageIndicator.onItemUpdated(count)

            // Then
            verify(exactly = once()) { beaglePageIndicatorView.setCurrentIndex(count) }
        }

    }
}