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

import androidx.appcompat.app.AppCompatActivity
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.components.layout.NavigationBar
import br.com.zup.beagle.android.components.layout.Screen
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.android.engine.renderer.ActivityRootView
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import br.com.zup.beagle.android.view.viewmodel.ScreenContextViewModel
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.Style
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertEquals

class WidgetExtensionsKtTest : BaseTest() {

    private val component = mockk<ServerDrivenComponent>()
    private val rootView = mockk<ActivityRootView>()
    private val viewFactoryMock = mockk<ViewFactory>(relaxed = true)
    private val view = mockk<BeagleFlexView>(relaxed = true)


    private lateinit var viewModel: ScreenContextViewModel

    override fun setUp() {
        super.setUp()

        mockkObject(ViewModelProviderFactory)

        viewModel = ScreenContextViewModel()

        every { rootView.activity } returns mockk()

        viewFactory = viewFactoryMock
    }

    @Test
    fun observeBindChanges_should_evaluate_binding_from_context_and_implicit_context() {
        // Given
        val value = RandomData.string()
        val bind = expressionOf<String>("Hello @{context}")
        viewModel.addContext(ContextData(
            id = "context",
            value = value
        ))

        // When
        component.observeBindChanges(rootView, bind) { evaluated ->
            // Then
            val expected = "Hello $value"
            assertEquals(expected, evaluated)
        }
    }

    @Test
    fun toView() {
        // Given
        val viewModelMock = mockk<ScreenContextViewModel>(relaxed = true)
        every {
            ViewModelProviderFactory.of(any<AppCompatActivity>())[ScreenContextViewModel::class.java]
        } returns viewModelMock
        every { viewFactory.makeBeagleFlexView(any()) } returns view
        every { rootView.getContext() } returns mockk()

        // When
        val actual = component.toView(rootView)

        // Then
        assertEquals(view, actual)
        verify(exactly = once()) { viewModelMock.discoverAllContexts() }
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