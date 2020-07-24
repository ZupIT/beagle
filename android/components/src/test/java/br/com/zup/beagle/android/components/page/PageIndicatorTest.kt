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
import br.com.zup.beagle.android.utils.Observer
import br.com.zup.beagle.android.utils.observeBindChanges
import br.com.zup.beagle.android.view.BeaglePageIndicatorView
import br.com.zup.beagle.android.view.ComponentsViewFactory
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertEquals

class PageIndicatorTest : BaseComponentTest() {

    private val beaglePageIndicatorView: BeaglePageIndicatorView = mockk(relaxed = true, relaxUnitFun = true)
    private val numberOfPages: Int = RandomData.int()
    private val currentPage: Bind<Int> = mockk(relaxed = true, relaxUnitFun = true)
    private val currentPageSlot = slot<Observer<Int?>>()


    private lateinit var pageIndicator: PageIndicator

    override fun setUp() {
        super.setUp()

        mockkStatic(Color::class)
        mockkStatic("br.com.zup.beagle.android.utils.WidgetExtensionsKt")
        every { Color.parseColor(any()) } returns 0
        every { anyConstructed<ComponentsViewFactory>().makePageIndicator(any()) } returns beaglePageIndicatorView

        pageIndicator = PageIndicator(RandomData.string(), RandomData.string(), numberOfPages, currentPage)
    }

    @Test
    fun buildView_should_return_BeaglePageIndicatorView_and_set_colors() {
        val view = pageIndicator.buildView(rootView)

        assertEquals(beaglePageIndicatorView, view)
        verify(exactly = once()) { beaglePageIndicatorView.setSelectedColor(0) }
        verify(exactly = once()) { beaglePageIndicatorView.setUnselectedColor(0) }
    }

    @Test
    fun buildView_should_call_onItemUpdate_when_currentPage_change() {
        //GIVEN
        val newPosition = RandomData.int()
        every {
            pageIndicator.observeBindChanges(
                rootView = rootView,
                bind = currentPage,
                observes = capture(currentPageSlot)
            )
        } just Runs
        //WHEN
        pageIndicator.buildView(rootView = rootView)
        currentPageSlot.captured.invoke(newPosition)

        //THEN
        verify(exactly = once()) { pageIndicator.onItemUpdated(newPosition) }
    }

    @Test
    fun buildView_should_call_setCount_when_numberOfPage_is_not_null() {
        //GIVEN

        //WHEN
        pageIndicator.buildView(rootView = rootView)

        //THEN
        verify(exactly = once()) { pageIndicator.setCount(numberOfPages) }
    }

    @Test
    fun setCount_should_call_BeaglePageIndicatorView_setCount() {
        // Given
        val count = RandomData.int()

        // When
        pageIndicator.buildView(rootView)
        pageIndicator.setCount(count)

        // Then
        verify(exactly = once()) { beaglePageIndicatorView.setCount(count) }
    }

    @Test
    fun onItemUpdated_should_call_BeaglePageIndicatorView_onItemUpdated() {
        // Given
        val count = RandomData.int()

        // When
        pageIndicator.buildView(rootView)
        pageIndicator.onItemUpdated(count)

        // Then
        verify(exactly = once()) { beaglePageIndicatorView.setCurrentIndex(count) }
    }
}