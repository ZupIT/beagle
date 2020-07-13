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
import br.com.zup.beagle.android.utils.observe
import br.com.zup.beagle.android.utils.observeBindChanges
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeaglePageIndicatorView
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class PageIndicatorTest : BaseComponentTest() {

    private val beaglePageIndicatorView: BeaglePageIndicatorView = mockk(relaxed = true, relaxUnitFun = true)

    private lateinit var pageIndicator: PageIndicator
    private val numberOfPages = RandomData.int()
    private val currentPage : Bind<Int> = mockk()
    private val bindSlot = slot<Observer<Int>>()

    override fun setUp() {
        super.setUp()

        mockkStatic(Color::class)
        mockkStatic("br.com.zup.beagle.android.utils.BindExtensionsKt")
        every { Color.parseColor(any()) } returns 0
        every { anyConstructed<ViewFactory>().makePageIndicator(any()) } returns beaglePageIndicatorView
        every { currentPage.observe(rootView, capture(bindSlot)) } returns 1
        pageIndicator = PageIndicator(RandomData.string(), RandomData.string(), numberOfPages, currentPage)

    }

    @Test
    fun toView_should_return_BeaglePageIndicatorView_set_colors_and_setCount() {
        val view = pageIndicator.buildView(rootView)

        assertEquals(beaglePageIndicatorView, view)
        verify(exactly = once()) { beaglePageIndicatorView.setSelectedColor(0) }
        verify(exactly = once()) { beaglePageIndicatorView.setUnselectedColor(0) }
        verify(exactly = once()) { beaglePageIndicatorView.setCount(numberOfPages) }

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

    @Test
    fun when_bind_change_should_call_onItemUpdated() {
        // Given
        // When
        pageIndicator.buildView(rootView)
        bindSlot.captured.invoke(2)

        // Then
        verify(exactly = once()) { pageIndicator.onItemUpdated(2) }
    }

}