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

package br.com.zup.beagle.android.engine.renderer.layout

import android.content.Context
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.android.engine.renderer.RootView
import br.com.zup.beagle.android.engine.renderer.ViewRendererFactory
import br.com.zup.beagle.android.view.BeagleFlexView
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.widget.layout.Stack
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.slot
import org.junit.Test
import kotlin.test.assertEquals

class StackViewRendererTest : BaseTest() {

    @MockK
    private lateinit var viewRendererFactory: ViewRendererFactory
    @MockK
    private lateinit var viewFactory: ViewFactory
    @MockK
    private lateinit var rootView: RootView

    @MockK
    private lateinit var stack: Stack
    @RelaxedMockK
    private lateinit var context: Context

    @RelaxedMockK
    private lateinit var children: List<ServerDrivenComponent>

    @RelaxedMockK
    private lateinit var beagleFlexView:BeagleFlexView

    @InjectMockKs
    private lateinit var stackViewRenderer: StackViewRenderer

    private val clipChildren = slot<Boolean>()
    private val style = slot<Style>()

    @Test
    fun build() {
        // Given
        every { viewFactory.makeBeagleFlexView(any()) } returns beagleFlexView
        every { rootView.getContext() } returns context
        every { beagleFlexView.clipChildren = capture(clipChildren) } just Runs
        every { beagleFlexView.addView(any(), capture(style)) } just Runs
        every { stack.children } returns children

        // When
        stackViewRenderer.build(rootView)

        // Then
        assertEquals(false, clipChildren.captured)
        //assertEquals(FlexPositionType.ABSOLUTE, flex.captured.positionType)
        //verify(exactly = once()) { beagleFlexView.addView(any()) }
    }
}