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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a ScrollView")
class ScrollViewTest : BaseComponentTest() {

    private val scrollView: android.widget.ScrollView = mockk(relaxed = true)
    private val horizontalScrollView: HorizontalScrollView = mockk(relaxed = true)
    private val context: Context = mockk()

    private val components = listOf<ServerDrivenComponent>(mockk<Button>())

    private val scrollBarEnabled = slot<Boolean>()
    private val style = mutableListOf<Style>()

    private lateinit var scrollViewComponent: ScrollView

    @BeforeEach
    override fun setUp() {
        super.setUp()

        every { scrollView.addView(any()) } just Runs
        every { horizontalScrollView.addView(any()) } just Runs
        every { ViewFactory.makeBeagleFlexView(any(), capture(style)) } returns beagleFlexView
        every { beagleFlexView.addView(any<ServerDrivenComponent>(), false) } just Runs
        every { beagleFlexView.context } returns context
        every { ViewFactory.makeScrollView(any()) } returns scrollView
        every { ViewFactory.makeHorizontalScrollView(any()) } returns horizontalScrollView
    }

    @DisplayName("When buildView is called")
    @Nested
    inner class BuildView {

        @DisplayName("Then should create a vertical scrollView if scrollDirection is vertical")
        @Test
        fun testVerticalScrollView() {
            // Given
            every { scrollView.isVerticalScrollBarEnabled = capture(scrollBarEnabled) } just Runs
            scrollViewComponent = ScrollView(children = components, scrollDirection = ScrollAxis.VERTICAL,
                scrollBarEnabled = true)

            // When
            val view = scrollViewComponent.buildView(rootView)

            // Then
            verify {
                ViewFactory.makeBeagleFlexView(rootView, style.first())
                beagleFlexView.addView(components, false)
                beagleFlexView.setHeightAutoAndDirtyAllViews()
            }
            verify(exactly = 1) { ViewFactory.makeScrollView(context) }
            verify(exactly = 1) { scrollView.addView(beagleFlexView) }
            assertEquals(true, scrollBarEnabled.captured)
            assertEquals(1.0, style[0].flex?.grow)
            assertEquals(FlexDirection.COLUMN, style[1].flex?.flexDirection)
            assertTrue(view is BeagleFlexView)
        }

        @DisplayName("Then should create a horizontal scrollView if scrollDirection is horizontal")
        @Test
        fun testHorizontalScrollView() {
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
                ViewFactory.makeBeagleFlexView(rootView, style.first())
            }
            verify(exactly = 1) {
                ViewFactory.makeHorizontalScrollView(context)
                beagleFlexView.addView(components, false)
                beagleFlexView.setWidthAndHeightAutoAndDirtyAllViews()
            }
            assertEquals(false, scrollBarEnabled.captured)
            assertEquals(1.0, style[0].flex?.grow)
            assertEquals(FlexDirection.ROW, style[1].flex?.flexDirection)
        }

        @DisplayName("Then should return an empty BeagleFlexView if children is null")
        @Test
        fun testBeagleFlexViewEmpty() {
            // Given
            val children = null
            scrollViewComponent = ScrollView(children)

            // When
            val result = scrollViewComponent.buildView(rootView) as BeagleFlexView

            // Then
            assertTrue(result.childCount == 0)
        }
    }
}
