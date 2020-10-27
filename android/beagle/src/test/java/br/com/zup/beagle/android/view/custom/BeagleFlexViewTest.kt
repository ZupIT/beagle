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

import br.com.zup.beagle.android.engine.mapper.FlexMapper
import br.com.zup.beagle.android.engine.renderer.ViewRenderer
import br.com.zup.beagle.android.engine.renderer.ViewRendererFactory
import br.com.zup.beagle.android.utils.GenerateIdManager
import br.com.zup.beagle.android.view.viewmodel.ScreenContextViewModel
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.Style
import com.facebook.yoga.YogaNode
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verifySequence
import org.junit.After
import org.junit.Before
import org.junit.Test

class BeagleFlexViewTest {

    private val rootView = mockk<RootView>(relaxed = true)
    private val style = mockk<Style>()
    private val flexMapper = mockk<FlexMapper>(relaxed = true)
    private val viewRendererFactory = mockk<ViewRendererFactory>(relaxed = true)
    private val viewModel = mockk<ScreenContextViewModel>()
    private val generateIdManager = mockk<GenerateIdManager>(relaxed = true)
    private lateinit var beagleFlexView: BeagleFlexView

    @Before
    fun setUp() {
        beagleFlexView = BeagleFlexView(rootView, style, flexMapper, viewRendererFactory, viewModel, generateIdManager)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `GIVEN a BeagleFlexView WHEN addServerDrivenComponent THEN should call manageId before make and build`() {
        // Given
        val serverDrivenComponent = mockk<ServerDrivenComponent>()
        val viewRenderer = mockk<ViewRenderer<*>>(relaxed = true)
        every { viewRendererFactory.make(serverDrivenComponent) } returns viewRenderer
        val beagleFlexViewSpy = spyk(beagleFlexView)
        every { beagleFlexViewSpy.addView(any(), any<YogaNode>()) } just Runs

        // When
        beagleFlexViewSpy.addServerDrivenComponent(serverDrivenComponent)

        // Then
        verifySequence {
            generateIdManager.manageId(serverDrivenComponent, beagleFlexViewSpy)
            viewRendererFactory.make(serverDrivenComponent)
            viewRenderer.build(rootView)
        }
    }
}
