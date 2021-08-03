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

package br.com.zup.beagle.android.view.custom

import android.view.View
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.Style
import com.facebook.yoga.YogaNode
import com.facebook.yoga.YogaNodeFactory
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a BeagleFlexView")
internal class BeagleFlexViewTest : BaseTest() {

    private val styleMock = mockk<Style>(relaxUnitFun = true, relaxed = true)

    private lateinit var beagleFlexView: BeagleFlexView

    @BeforeEach
    override fun setUp() {
        super.setUp()

        mockYoga()

        beagleFlexView = spyk(BeagleFlexView(rootView, styleMock))
    }

    @DisplayName("When call add view with style")
    @Nested
    inner class TestAddView {

        @DisplayName("Then it should call internal view")
        @Test
        fun testInternalViewCalled() {
            // Given
            val view = mockk<View>()
            val style = Style()
            every { beagleFlexView.addViewWithStyle(view, style) } just Runs

            // When
            beagleFlexView.addView(view, style)

            // Then
            verify {
                beagleFlexView.addViewWithStyle(view, style)
            }

        }
    }

    @DisplayName("When call add view with component")
    @Nested
    inner class TestAddComponentView {

        @DisplayName("Then it should call internal view")
        @Test
        fun testInternalComponentViewCalled() {
            // Given
            val view = mockk<ServerDrivenComponent>()
            every {
                beagleFlexView
                    .addServerDrivenComponent(view, true)
            } just Runs

            // When
            beagleFlexView.addView(view)

            // Then
            verify {
                beagleFlexView.addServerDrivenComponent(view, true)
            }

        }
    }

    @DisplayName("When call add view with list of component")
    @Nested
    inner class TestAddListComponentView {

        @DisplayName("Then it should call internal view")
        @Test
        fun testInternalComponentViewCalled() {
            // Given
            val list = listOf<ServerDrivenComponent>(mockk(), mockk())
            every {
                beagleFlexView
                    .addServerDrivenComponent(any(), true)
            } just Runs

            // When
            beagleFlexView.addView(list)

            // Then
            verifyOrder {
                beagleFlexView.addServerDrivenComponent(list[0], true)
                beagleFlexView.addServerDrivenComponent(list[1], true)
            }
        }

        @DisplayName("Then it should not call internal view if it's null")
        @Test
        fun testInternalComponentViewNotCalled() {
            // Given
            val list = null
            val addLayoutChangeListener = true

            // When
            beagleFlexView.addView(list, addLayoutChangeListener)

            // Then
            verify(exactly = 0) { beagleFlexView.addServerDrivenComponent(any(), addLayoutChangeListener) }
        }
    }

    @DisplayName("When call addListenerOnViewDetachedFromWindow")
    @Nested
    inner class TestAddListenerOnViewDetachedFromWindow {

        @DisplayName("Then it should call internal view")
        @Test
        fun testCallAddListenerOnViewDetachedFromWindow() {
            // Given
            val listenerMock = mockk<() -> Unit>()
            every {
                beagleFlexView.listenerOnViewDetachedFromWindow = any()
            } just Runs

            // When
            beagleFlexView.addListenerOnViewDetachedFromWindow(listenerMock)

            // Then
            verify {
                beagleFlexView.listenerOnViewDetachedFromWindow = listenerMock
            }

        }
    }


    private fun mockYoga() {
        val yogaNode = mockk<YogaNode>(relaxed = true, relaxUnitFun = true)
        val view = View(mockk())
        mockkStatic(YogaNode::class)
        mockkStatic(YogaNodeFactory::class)
        every { YogaNodeFactory.create() } returns yogaNode
        every { yogaNode.data } returns view
    }
}