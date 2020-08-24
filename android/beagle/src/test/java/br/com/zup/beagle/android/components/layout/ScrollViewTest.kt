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

package br.com.zup.beagle.android.components.layout

import android.content.Context
import android.widget.HorizontalScrollView
import br.com.zup.beagle.android.components.BaseComponentTest
import br.com.zup.beagle.android.components.Button
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.widget.core.FlexDirection
import br.com.zup.beagle.widget.core.ScrollAxis
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ScrollViewTest : BaseComponentTest() {

    private val scrollView: android.widget.ScrollView = mockk(relaxed = true)
    private val horizontalScrollView: HorizontalScrollView = mockk(relaxed = true)
    private val context: Context = mockk()

    private val components = listOf<ServerDrivenComponent>(mockk<Button>())

    private val scrollBarEnabled = slot<Boolean>()
    private val style = mutableListOf<Style>()

    private lateinit var scrollViewComponent: ScrollView

    override fun setUp() {
        super.setUp()

        every { scrollView.addView(any()) } just Runs
        every { horizontalScrollView.addView(any()) } just Runs
        every { anyConstructed<ViewFactory>().makeBeagleFlexView(any(), capture(style)) } returns beagleFlexView
        every { beagleFlexView.addServerDrivenComponent(any()) } just Runs
        every { beagleFlexView.context } returns context
        every { anyConstructed<ViewFactory>().makeScrollView(any()) } returns scrollView
        every { anyConstructed<ViewFactory>().makeHorizontalScrollView(any()) } returns horizontalScrollView
    }

    @Test
    fun when_scrollDirection_VERTICAL_and_scrollBarEnabled_true_build_should_create_a_scrollView() {
        // Given
        every { scrollView.isVerticalScrollBarEnabled = capture(scrollBarEnabled) } just Runs
        scrollViewComponent = ScrollView(children = components, scrollDirection = ScrollAxis.VERTICAL,
            scrollBarEnabled = true)

        // When
        val view = scrollViewComponent.buildView(rootView)

        // Then
        verify {
            anyConstructed<ViewFactory>().makeBeagleFlexView(rootView, style.first())
        }
        verify(exactly = once()) { anyConstructed<ViewFactory>().makeScrollView(context) }
        verify(exactly = once()) { scrollView.addView(beagleFlexView) }
        assertEquals(true, scrollBarEnabled.captured)
        assertEquals(1.0, style[0].flex?.grow)
        assertEquals(FlexDirection.COLUMN, style[1].flex?.flexDirection)
        assertTrue(view is BeagleFlexView)
    }

    @Test
    fun when_scrollDirection_HORIZONTAL_and_scrollBarEnabled_false_build_should_create_a_scrollView() {
        // Given
        every {
            horizontalScrollView.isHorizontalScrollBarEnabled = capture(scrollBarEnabled)
        } just Runs
        scrollViewComponent = ScrollView(children = components, scrollDirection = ScrollAxis.HORIZONTAL,
            scrollBarEnabled = false)

        // When
        scrollViewComponent.buildView(rootView)

        // Then
        verify {
            anyConstructed<ViewFactory>().makeBeagleFlexView(rootView, style.first())
        }
        verify(exactly = once()) { anyConstructed<ViewFactory>().makeHorizontalScrollView(context) }
        verify(exactly = once()) { beagleFlexView.addServerDrivenComponent(components[0]) }
        assertEquals(false, scrollBarEnabled.captured)
        assertEquals(1.0, style[0].flex?.grow)
        assertEquals(FlexDirection.ROW, style[1].flex?.flexDirection)
    }
}
