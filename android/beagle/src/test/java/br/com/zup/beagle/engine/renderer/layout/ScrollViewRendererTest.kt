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

package br.com.zup.beagle.engine.renderer.layout

import android.content.Context
import android.widget.HorizontalScrollView
import br.com.zup.beagle.BaseTest
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.engine.renderer.ViewRendererFactory
import br.com.zup.beagle.extensions.once
import br.com.zup.beagle.view.BeagleFlexView
import br.com.zup.beagle.view.ViewFactory
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.FlexDirection
import br.com.zup.beagle.widget.layout.ScrollAxis
import br.com.zup.beagle.widget.layout.ScrollView
import br.com.zup.beagle.widget.ui.Button
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ScrollViewRendererTest : BaseTest() {

    @RelaxedMockK
    private lateinit var scroll: ScrollView
    @MockK
    private lateinit var viewRendererFactory: ViewRendererFactory
    @MockK
    private lateinit var viewFactory: ViewFactory

    @MockK
    private lateinit var rootView: RootView
    @MockK
    private lateinit var context: Context
    @RelaxedMockK
    private lateinit var scrollView: android.widget.ScrollView
    @RelaxedMockK
    private lateinit var horizontalScrollView: HorizontalScrollView
    @RelaxedMockK
    private lateinit var beagleFlexView: BeagleFlexView

    private val components = listOf<ServerDrivenComponent>(Button(""))

    private val scrollBarEnabled = slot<Boolean>()
    private val flex = slot<Flex>()

    @InjectMockKs
    private lateinit var scrollViewRenderer: ScrollViewRenderer

    override fun setUp() {
        super.setUp()

        every { scrollView.addView(any()) } just Runs
        every { horizontalScrollView.addView(any()) } just Runs
        every { viewFactory.makeBeagleFlexView(any(), capture(flex)) } returns beagleFlexView
        every { viewFactory.makeBeagleFlexView(any()) } returns beagleFlexView
        every { beagleFlexView.addServerDrivenComponent(any(), any()) } just Runs
        every { rootView.getContext() } returns context
        every { scroll.children } returns components
        every { viewFactory.makeScrollView(any()) } returns scrollView
        every { viewFactory.makeHorizontalScrollView(any()) } returns horizontalScrollView
    }

    @Test
    fun when_scrollDirection_VERTICAL_and_scrollBarEnabled_true_build_should_create_a_scrollView() {
        // Given
        every { scroll.scrollDirection } returns ScrollAxis.VERTICAL
        every { scroll.scrollBarEnabled } returns true
        every { scrollView.isVerticalScrollBarEnabled = capture(scrollBarEnabled) } just Runs

        // When
        val view = scrollViewRenderer.build(rootView)

        // Then
        verify(exactly = once()) { viewFactory.makeScrollView(context) }
        verify(exactly = once()) { viewFactory.makeBeagleFlexView(context, flex.captured) }
        verify(exactly = once()) { scrollView.addView(beagleFlexView) }
        assertEquals(true, scrollBarEnabled.captured)
        assertEquals(FlexDirection.COLUMN, flex.captured.flexDirection)
        assertTrue(view is BeagleFlexView)
    }

    @Test
    fun when_scrollDirection_HORIZONTAL_and_scrollBarEnabled_false_build_should_create_a_scrollView() {
        // Given
        every { scroll.scrollDirection } returns ScrollAxis.HORIZONTAL
        every { scroll.scrollBarEnabled } returns false
        every {
            horizontalScrollView.isHorizontalScrollBarEnabled = capture(scrollBarEnabled)
        } just Runs

        // When
        scrollViewRenderer.build(rootView)

        // Then
        verify(exactly = once()) { viewFactory.makeHorizontalScrollView(context) }
        verify(exactly = once()) { beagleFlexView.addServerDrivenComponent(components[0], rootView) }
        assertEquals(false, scrollBarEnabled.captured)
        assertEquals(FlexDirection.ROW, flex.captured.flexDirection)
    }
}
