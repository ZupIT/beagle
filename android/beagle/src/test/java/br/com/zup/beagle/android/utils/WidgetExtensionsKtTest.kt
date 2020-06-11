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

package br.com.zup.beagle.android.utils

import br.com.zup.beagle.core.Style
import br.com.zup.beagle.android.view.BeagleFlexView
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.android.components.layout.Screen
import br.com.zup.beagle.android.widget.core.RootView
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class WidgetExtensionsKtTest {

    @MockK
    private lateinit var rootView: RootView

    @RelaxedMockK
    private lateinit var viewFactoryMock: ViewFactory

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewFactory = viewFactoryMock
    }

    @Test
    fun toView() {
        // Given
        val component = mockk<ServerDrivenComponent>()
        val view = mockk<BeagleFlexView>(relaxed = true)
        every { viewFactory.makeBeagleFlexView(any()) } returns view
        every { rootView.getContext() } returns mockk()

        // When
        val actual = component.toView(rootView)

        // Then
        assertEquals(view, actual)
    }

    @Test
    fun toComponent_should_create_a_ScreenWidget() {
        // Given
        val navigationBar = mockk<NavigationBar>()
        val child = mockk<ServerDrivenComponent>()
        val style = mockk<Style>()
        val screen = Screen(
            navigationBar = navigationBar,
            child = child,
            style = style
        )

        // When
        val actual = screen.toComponent()

        // Then
        assertEquals(navigationBar, actual.navigationBar)
        assertEquals(child, actual.child)
        assertEquals(style, actual.style)
    }
}