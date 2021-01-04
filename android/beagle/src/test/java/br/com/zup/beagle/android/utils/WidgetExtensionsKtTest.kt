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
import androidx.lifecycle.ViewModelProvider
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.components.layout.NavigationBar
import br.com.zup.beagle.android.components.layout.Screen
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.android.mockdata.createViewForContext
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import br.com.zup.beagle.android.view.viewmodel.GenerateIdViewModel
import br.com.zup.beagle.android.view.viewmodel.ScreenContextViewModel
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.widget.Widget
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a widget extension")
class WidgetExtensionsKtTest : BaseTest() {

    private val component = mockk<ServerDrivenComponent>()
    private val widgetComponent = mockk<Widget>()

    private val viewFactoryMock = mockk<ViewFactory>(relaxed = true)
    private val view = createViewForContext()
    private val generateIdViewModel: GenerateIdViewModel = mockk(relaxed = true)
    private val contextViewModel: ScreenContextViewModel = mockk(relaxed = true)
    private val activity: AppCompatActivity = mockk(relaxed = true)

    @BeforeEach
    override fun setUp() {
        super.setUp()
        viewFactory = viewFactoryMock

        prepareViewModelMock(generateIdViewModel)
        every { anyConstructed<ViewModelProvider>().get(contextViewModel::class.java) } returns contextViewModel
    }

    @DisplayName("When observeBindChanges")
    @Nested
    inner class ObserveBindChanges {

        @DisplayName("Then should evaluate  binding from context and implicit context")
        @Test
        fun testObserveBindChangesShouldEvaluateBindingFromContextAndImplicitContext() {
            // Given
            val value = RandomData.string()
            val bind = expressionOf<String>("Hello @{context}")
            contextViewModel.addContext(view, ContextData(
                id = "context",
                value = value
            ))

            // When Then
            component.observeBindChanges(rootView, view, bind) { evaluated ->
                // Then
                val expected = "Hello $value"
                assertEquals(expected, evaluated)
            }

            contextViewModel.linkBindingToContextAndEvaluateThem(view)
        }

    }

    @DisplayName("When toView")
    @Nested
    inner class ToView {

        @DisplayName("Then should call some funs on sequence")
        @Test
        fun testToViewShouldCallSomeFunOnSequence() {
            // Given
            val beagleFlexView = mockk<BeagleFlexView>(relaxed = true, relaxUnitFun = true)

            every { viewFactory.makeBeagleFlexView(any()) } returns beagleFlexView
            every { rootView.getContext() } returns mockk()

            // When
            val actual = component.toView(rootView)

            // Then
            verifySequence {
                generateIdViewModel.createIfNotExisting(0)
                beagleFlexView.id = 0
                beagleFlexView.addServerDrivenComponent(component)
                beagleFlexView.listenerOnViewDetachedFromWindow = any()
            }

            assertEquals(beagleFlexView, actual)
        }


        @DisplayName("Then assert the identifier is getting right")
        @Test
        fun testToViewShouldGetScreenIdentifierFromParameterFirst() {
            //given
            val slot = commonMock()
            val screenId = "screenId"

            //when
            widgetComponent.toView(activity = activity, screenIdentifier = screenId)

            //then
            assertEquals(screenId, slot.captured.getScreenId())
        }

        @DisplayName("Then assert the identifier is getting right")
        @Test
        fun testToViewWithIdOnComponent() {
            //given
            val slot = commonMock()
            val componentId = "componentId"

            //when
            widgetComponent.toView(activity = activity, screenIdentifier = null)

            //then
            assertEquals(componentId, slot.captured.getScreenId())
        }

        private fun commonMock(): CapturingSlot<RootView> {
            mockkStatic("br.com.zup.beagle.android.utils.WidgetExtensionsKt")
            val slot = slot<RootView>()
            every { widgetComponent.toView(capture(slot), any()) } returns mockk()
            every { widgetComponent.id } returns "componentId"
            return slot
        }
    }

    @DisplayName("When toComponent")
    @Nested
    inner class ToComponent {

        @DisplayName("Then should create screen widget")
        @Test
        fun testToComponentShouldCreateScreenWidget() {
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
}
