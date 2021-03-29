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
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.jupiter.api.Test

class InternalBeagleFlexViewTest {

    private val rootViewMock = mockk<RootView>(relaxed = true, relaxUnitFun = true)
    private val flexMapperMock = mockk<FlexMapper>(relaxUnitFun = true, relaxed = true)
    private val styleMock = mockk<Style>()
    private val viewRendererFactoryMock = mockk<ViewRendererFactory>()
    private val screenContextViewModelMock = mockk<ScreenContextViewModel>()
    private val generateIdManagerMock = mockk<GenerateIdManager>(relaxed = true)

    @Test
    fun `GIVEN a BeagleFlexView WHEN instance the class THEN should call bind changes`() {
        // When
        val beagleFlexView = InternalBeagleFlexView(
            rootView = rootViewMock,
            style = styleMock,
            flexMapper = flexMapperMock,
            viewRendererFactory = viewRendererFactoryMock,
            viewModel = screenContextViewModelMock
        )

        // Then
        verify {
            flexMapperMock.observeBindChangesFlex(styleMock, rootViewMock, beagleFlexView, beagleFlexView.yogaNode)
        }
    }

    @Test
    fun `GIVEN beagle flex view WHEN call addView THEN should call bind changes`() {
        // Given
        val viewAddChild = mockk<View>()
        val styleAddChild = mockk<Style>()
        val yogaNodeChild = mockk<YogaNode>()

        every { flexMapperMock.makeYogaNode(styleAddChild) } returns yogaNodeChild

        val beagleFlexView = spyk(
            InternalBeagleFlexView(
                rootView = rootViewMock,
                style = styleMock,
                flexMapper = flexMapperMock,
                viewRendererFactory = viewRendererFactoryMock,
                viewModel = screenContextViewModelMock
            )
        )
        every { beagleFlexView.addView(viewAddChild, yogaNodeChild) } just Runs

        // When
        beagleFlexView.addViewWithStyle(viewAddChild, styleAddChild)

        // Then
        verify {
            flexMapperMock.observeBindChangesFlex(styleAddChild, rootViewMock, beagleFlexView, yogaNodeChild)
        }
    }

    @Test
    fun `GIVEN beagle flex view WHEN call addServerDrivenComponent THEN should call bind changes`() {
        // Given
        val viewAddChild = mockk<View>()
        val yogaNodeChild = mockk<YogaNode>()
        val style = Style()
        val serverDrivenComponent = mockk<ServerDrivenComponent>()

        every { flexMapperMock.makeYogaNode(style) } returns yogaNodeChild
        every { viewRendererFactoryMock.make(serverDrivenComponent).build(rootViewMock) } returns viewAddChild

        val beagleFlexView = spyk(
            InternalBeagleFlexView(
                rootView = rootViewMock,
                style = styleMock,
                flexMapper = flexMapperMock,
                viewRendererFactory = viewRendererFactoryMock,
                viewModel = screenContextViewModelMock
            )
        )
        every { beagleFlexView.addView(viewAddChild, yogaNodeChild) } just Runs

        // When
        beagleFlexView.addServerDrivenComponent(serverDrivenComponent, false)

        // Then
        verify {
            flexMapperMock.observeBindChangesFlex(style, rootViewMock, viewAddChild, yogaNodeChild)
        }
    }

    @Test
    fun `GIVEN a BeagleFlexView WHEN addServerDrivenComponent THEN should call manageId before make and build`() {
        // Given
        val beagleFlexView = InternalBeagleFlexView(
            rootView = rootViewMock,
            style = styleMock,
            flexMapper = flexMapperMock,
            viewRendererFactory = viewRendererFactoryMock,
            viewModel = screenContextViewModelMock,
            generateIdManager = generateIdManagerMock
        )

        val serverDrivenComponent = mockk<ServerDrivenComponent>()
        val viewRenderer = mockk<ViewRenderer<*>>(relaxed = true)
        every { viewRendererFactoryMock.make(serverDrivenComponent) } returns viewRenderer
        val beagleFlexViewSpy = spyk(beagleFlexView)
        every { beagleFlexViewSpy.addView(any(), any<YogaNode>()) } just Runs

        // When
        beagleFlexViewSpy.addServerDrivenComponent(serverDrivenComponent)

        // Then
        verifySequence {
            generateIdManagerMock.manageId(serverDrivenComponent, beagleFlexViewSpy)
            viewRendererFactoryMock.make(serverDrivenComponent)
            viewRenderer.build(rootViewMock)
        }
    }
}
