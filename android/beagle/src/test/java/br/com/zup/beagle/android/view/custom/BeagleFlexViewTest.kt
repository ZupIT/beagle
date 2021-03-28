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
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a Beagle Flex View")
internal class BeagleFlexViewTest : BaseTest() {

    private val styleMock = mockk<Style>(relaxUnitFun = true, relaxed = true)

    private lateinit var beagleFlexView: BeagleFlexView

    @BeforeEach
    override fun setUp() {
        super.setUp()

        mockkConstructor(InternalBeagleFlexView::class)
        mockYoga()

        every { anyConstructed<InternalBeagleFlexView>().addView(any()) } just Runs
        every { anyConstructed<InternalBeagleFlexView>().addView(any(), any<Style>()) } just Runs

        beagleFlexView = BeagleFlexView(rootView, styleMock)
    }

    @DisplayName("When call add view")
    @Nested
    inner class TestAddView {

        @DisplayName("Then it should call internal view")
        @Test
        fun testInternalViewCalled() {
            // Given
            val view = mockk<View>()

            // When
            beagleFlexView.addView(view)

            // Then
            verify {
                anyConstructed<InternalBeagleFlexView>().addView(view, Style())
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
                anyConstructed<InternalBeagleFlexView>()
                    .addServerDrivenComponent(view, true)
            } just Runs

            // When
            beagleFlexView.addView(view)

            // Then
            verify {
                anyConstructed<InternalBeagleFlexView>().addServerDrivenComponent(view, true)
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
                anyConstructed<InternalBeagleFlexView>()
                    .addServerDrivenComponent(any(), true)
            } just Runs

            // When
            beagleFlexView.addView(list)

            // Then
            verifySequence {
                anyConstructed<InternalBeagleFlexView>().addServerDrivenComponent(list[0], true)
                anyConstructed<InternalBeagleFlexView>().addServerDrivenComponent(list[1], true)
            }

        }
    }

    @DisplayName("When call setHeightAutoAndDirtyAllViews")
    @Nested
    inner class TestSetHeightAutoAndDirtyAllViews {

        @DisplayName("Then it should call internal view")
        @Test
        fun testCallSetHeightAutoAndDirtyAllViews() {
            // Given
            every {
                anyConstructed<InternalBeagleFlexView>()
                    .setHeightAutoAndDirtyAllViews()
            } just Runs

            // When
            beagleFlexView.setHeightAutoAndDirtyAllViews()

            // Then
            verify {
                anyConstructed<InternalBeagleFlexView>().setHeightAutoAndDirtyAllViews()
            }

        }
    }

    @DisplayName("When call setWidthAndHeightAutoAndDirtyAllViews")
    @Nested
    inner class TestSetWidthAndHeightAutoAndDirtyAllViews {

        @DisplayName("Then it should call internal view")
        @Test
        fun testCallSetWidthAndHeightAutoAndDirtyAllViews() {
            // Given
            every {
                anyConstructed<InternalBeagleFlexView>()
                    .setWidthAndHeightAutoAndDirtyAllViews()
            } just Runs

            // When
            beagleFlexView.setWidthAndHeightAutoAndDirtyAllViews()

            // Then
            verify {
                anyConstructed<InternalBeagleFlexView>().setWidthAndHeightAutoAndDirtyAllViews()
            }

        }
    }

    @DisplayName("When call setWidthAutoAndDirtyAllViews")
    @Nested
    inner class TestSetWidthAutoAndDirtyAllViews {

        @DisplayName("Then it should call internal view")
        @Test
        fun testCallSetWidthAutoAndDirtyAllViews() {
            // Given
            every {
                anyConstructed<InternalBeagleFlexView>()
                    .setWidthAutoAndDirtyAllViews()
            } just Runs

            // When
            beagleFlexView.setWidthAutoAndDirtyAllViews()

            // Then
            verify {
                anyConstructed<InternalBeagleFlexView>().setWidthAutoAndDirtyAllViews()
            }

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
                anyConstructed<InternalBeagleFlexView>()
                    .listenerOnViewDetachedFromWindow = any()
            } just Runs

            // When
            beagleFlexView.addListenerOnViewDetachedFromWindow(listenerMock)

            // Then
            verify {
                anyConstructed<InternalBeagleFlexView>().listenerOnViewDetachedFromWindow = listenerMock
            }

        }
    }


    private fun mockYoga() {
        val yogaNode = mockk<YogaNode>(relaxed = true, relaxUnitFun = true)
        val view = View(mockk())
        mockkStatic(YogaNode::class)
        every { YogaNode.create() } returns yogaNode
        every { yogaNode.data } returns view
    }
}