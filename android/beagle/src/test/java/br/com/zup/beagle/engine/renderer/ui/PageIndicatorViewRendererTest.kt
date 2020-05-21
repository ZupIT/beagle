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
import android.graphics.Color
import br.com.zup.beagle.extensions.once
import br.com.zup.beagle.testutil.RandomData
import br.com.zup.beagle.testutil.setPrivateField
import br.com.zup.beagle.view.BeaglePageIndicatorView
import br.com.zup.beagle.view.ViewFactory
import br.com.zup.beagle.widget.pager.PageIndicator
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class PageIndicatorViewTest {

    @MockK
    private lateinit var viewFactory: ViewFactory
    @MockK
    private lateinit var context: Context
    @MockK
    private lateinit var beaglePageIndicatorView: BeaglePageIndicatorView

    private lateinit var pageIndicator: PageIndicator

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        pageIndicator = PageIndicator(RandomData.string(), RandomData.string(), viewFactory).apply {
            setPrivateField("pageIndicator", beaglePageIndicatorView)
        }

        mockkStatic(Color::class)

        every { Color.parseColor(any()) } returns 0
        every { viewFactory.makePageIndicator(any()) } returns beaglePageIndicatorView
    }

    @Test
    fun toView_should_return_BeaglePageIndicatorView_and_set_colors() {
        val view = pageIndicator.buildView(context)

        assertEquals(beaglePageIndicatorView, view)
        verify(exactly = once()) { beaglePageIndicatorView.setSelectedColor(0) }
        verify(exactly = once()) { beaglePageIndicatorView.setUnselectedColor(0) }
    }

    @Test
    fun setCount_should_call_BeaglePageIndicatorView_setCount() {
        // Given
        val count = RandomData.int()

        // When
        pageIndicator.setCount(count)

        // Then
        verify(exactly = once()) { beaglePageIndicatorView.setCount(count) }
    }

    @Test
    fun onItemUpdated_should_call_BeaglePageIndicatorView_onItemUpdated() {
        // Given
        val count = RandomData.int()

        // When
        pageIndicator.onItemUpdated(count)

        // Then
        verify(exactly = once()) { beaglePageIndicatorView.setCurrentIndex(count) }
    }
}