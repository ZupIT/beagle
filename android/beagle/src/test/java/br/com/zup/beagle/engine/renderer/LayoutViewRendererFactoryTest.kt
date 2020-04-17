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

package br.com.zup.beagle.engine.renderer

import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.engine.renderer.layout.ContainerViewRenderer
import br.com.zup.beagle.engine.renderer.layout.FormViewRenderer
import br.com.zup.beagle.engine.renderer.layout.HorizontalViewRenderer
import br.com.zup.beagle.engine.renderer.layout.LazyComponentViewRenderer
import br.com.zup.beagle.engine.renderer.layout.ScreenViewRenderer
import br.com.zup.beagle.engine.renderer.layout.ScrollViewRenderer
import br.com.zup.beagle.engine.renderer.layout.SpacerViewRenderer
import br.com.zup.beagle.engine.renderer.layout.StackViewRenderer
import br.com.zup.beagle.engine.renderer.layout.TouchableViewRenderer
import br.com.zup.beagle.engine.renderer.layout.VerticalViewRender
import br.com.zup.beagle.setup.BeagleEnvironment
import br.com.zup.beagle.widget.form.Form
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.Horizontal
import br.com.zup.beagle.widget.layout.ScreenComponent
import br.com.zup.beagle.widget.layout.ScrollView
import br.com.zup.beagle.widget.layout.Spacer
import br.com.zup.beagle.widget.layout.Stack
import br.com.zup.beagle.widget.layout.Vertical
import br.com.zup.beagle.widget.lazy.LazyComponent
import br.com.zup.beagle.widget.navigation.Touchable
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFails
import kotlin.test.assertTrue

class LayoutViewRendererFactoryTest {

    private lateinit var viewRendererFactory: LayoutViewRendererFactory

    @Before
    fun setUp() {
        mockkObject(BeagleEnvironment)

        every { BeagleEnvironment.beagleSdk } returns mockk(relaxed = true)

        viewRendererFactory = LayoutViewRendererFactory()
    }

    @After
    fun tearDown() {
        unmockkObject(BeagleEnvironment)
    }

    @Test
    fun make_should_return_ContainerViewRenderer_when_component_is_a_Screen() {
        // Given
        val component = mockk<ScreenComponent>()

        // When
        val actual = viewRendererFactory.make(component)

        // Then
        assertTrue(actual is ScreenViewRenderer)
    }

    @Test
    fun make_should_return_VerticalViewRender_when_widget_is_a_Vertical() {
        // Given
        val component = mockk<Vertical>()
        every { component.children } returns listOf()

        // When
        val actual = viewRendererFactory.make(component)

        // Then
        assertTrue(actual is VerticalViewRender)
    }

    @Test
    fun make_should_return_HorizontalViewRenderer_when_widget_is_a_Horizontal() {
        // Given
        val component = mockk<Horizontal>()
        every { component.children } returns listOf()

        // When
        val actual = viewRendererFactory.make(component)

        // Then
        assertTrue(actual is HorizontalViewRenderer)
    }

    @Test
    fun make_should_return_StackViewRenderer_when_widget_is_a_Stack() {
        // Given
        val component = mockk<Stack>()

        // When
        val actual = viewRendererFactory.make(component)

        // Then
        assertTrue(actual is StackViewRenderer)
    }

    @Test
    fun make_should_return_SpacerViewRenderer_when_widget_is_a_Spacer() {
        // Given
        val component = mockk<Spacer>()

        // When
        val actual = viewRendererFactory.make(component)

        // Then
        assertTrue(actual is SpacerViewRenderer)
    }

    @Test
    fun make_should_return_ContainerViewRenderer_when_widget_is_a_Container() {
        // Given
        val component = mockk<Container>()

        // When
        val actual = viewRendererFactory.make(component)

        // Then
        assertTrue(actual is ContainerViewRenderer)
    }

    @Test
    fun make_should_return_NavigatorViewRenderer_when_widget_is_a_Touchable() {
        // Given
        val component = mockk<Touchable>()

        // When
        val actual = viewRendererFactory.make(component)

        // Then
        assertTrue(actual is TouchableViewRenderer)
    }

    @Test
    fun make_should_return_a_FormViewRenderer_when_widget_is_a_layout_Form() {
        // Given
        val component = mockk<Form>()

        // When
        val actual = viewRendererFactory.make(component)

        assertTrue(actual is FormViewRenderer)
    }

    @Test
    fun make_should_return_ScrollViewRenderer_when_widget_is_a_ScrollView() {
        // Given
        val component = mockk<ScrollView>()

        // When
        val actual = viewRendererFactory.make(component)

        // Then
        assertTrue(actual is ScrollViewRenderer)
    }

    @Test
    fun make_should_return_a_LazyWidgetViewRenderer_when_widget_is_a_layout_LazyWidget() {
        // Given
        val component = mockk<LazyComponent>()

        // When
        val actual = viewRendererFactory.make(component)

        assertTrue(actual is LazyComponentViewRenderer)
    }

    @Test
    fun make_should_throw_IllegalArgumentException_when_widget_is_not_a_layout_Widget() {
        // Given
        val component = mockk<ServerDrivenComponent>()

        // When
        val exception = assertFails("$component is not a Layout Widget.") {
            viewRendererFactory.make(component)
        }

        assertTrue(exception is IllegalArgumentException)
    }
}