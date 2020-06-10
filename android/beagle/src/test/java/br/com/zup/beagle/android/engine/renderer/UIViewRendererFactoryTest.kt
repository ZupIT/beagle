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

package br.com.zup.beagle.android.engine.renderer

import br.com.zup.beagle.android.engine.renderer.layout.ComposeComponentViewRenderer
import br.com.zup.beagle.android.engine.renderer.ui.ButtonViewRenderer
import br.com.zup.beagle.android.engine.renderer.ui.ImageViewRenderer
import br.com.zup.beagle.android.engine.renderer.ui.ListViewRenderer
import br.com.zup.beagle.android.engine.renderer.ui.ViewConvertableRenderer
import br.com.zup.beagle.android.engine.renderer.ui.NetworkImageViewRenderer
import br.com.zup.beagle.android.engine.renderer.ui.TextViewRenderer
import br.com.zup.beagle.widget.core.ComposeComponent
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.widget.core.WidgetView
import br.com.zup.beagle.widget.form.FormInput
import br.com.zup.beagle.widget.form.FormSubmit
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.Image
import br.com.zup.beagle.widget.ui.ListView
import br.com.zup.beagle.widget.ui.NetworkImage
import br.com.zup.beagle.widget.ui.Text
import br.com.zup.beagle.widget.ui.WebView
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

class UIViewRendererFactoryTest {

    private lateinit var viewRendererFactory: UIViewRendererFactory

    @Before
    fun setUp() {
        mockkObject(BeagleEnvironment)

        every { BeagleEnvironment.beagleSdk } returns mockk(relaxed = true)

        viewRendererFactory = UIViewRendererFactory()
    }

    @After
    fun tearDown() {
        unmockkObject(BeagleEnvironment)
    }

    @Test
    fun make_should_return_ButtonViewRenderer_when_component_is_a_Button() {
        // Given
        val component = Button(text = "")

        // When
        val actual = viewRendererFactory.make(component)

        // Then
        assertTrue(actual is ButtonViewRenderer)
    }

    @Test
    fun make_should_return_TextViewRenderer_when_component_is_a_Text() {
        // Given
        val component = Text(text = "")

        // When
        val actual = viewRendererFactory.make(component)

        // Then
        assertTrue(actual is TextViewRenderer)
    }

    @Test
    fun make_should_return_ImageViewRenderer_when_component_is_a_Image() {
        // Given
        val component = Image(name = "")

        // When
        val actual = viewRendererFactory.make(component)

        // Then
        assertTrue(actual is ImageViewRenderer)
    }

    @Test
    fun make_should_return_NetworkImageViewRenderer_when_component_is_a_NetworkImage() {
        // Given
        val component = NetworkImage(path = "")

        // When
        val actual = viewRendererFactory.make(component)

        // Then
        assertTrue(actual is NetworkImageViewRenderer)
    }

    @Test
    fun make_should_return_ListViewRenderer_when_component_is_a_ListView() {
        // Given
        val component = mockk<ListView>()

        // When
        val actual = viewRendererFactory.make(component)

        // Then
        assertTrue(actual is ListViewRenderer)
    }

    @Test
    fun make_should_return_CustomWidgetViewRenderer_when_component_is_a_NativeWidget() {
        // Given
        val component = mockk<WidgetView>()

        // When
        val actual = viewRendererFactory.make(component)

        // Then
        assertTrue(actual is ViewConvertableRenderer)
    }

    @Test
    fun make_should_return_BuildableWidgetViewRenderer_when_component_is_not_a_ComposeWidget() {
        // Given
        val component = mockk<ComposeComponent>()

        // When
        val actual = viewRendererFactory.make(component)

        // Then
        assertTrue(actual is ComposeComponentViewRenderer)
    }

    @Test
    fun make_should_return_FormInputViewRenderer_when_component_is_a_FormInput() {
        // Given
        val component = mockk<FormInput>()

        // When
        val actual = viewRendererFactory.make(component)

        // Then
        assertTrue(actual is FormInputViewRenderer)
    }

    @Test
    fun make_should_return_FormSubmitViewRenderer_when_component_is_a_FormSubmit() {
        // Given
        val component = mockk<FormSubmit>()

        // When
        val actual = viewRendererFactory.make(component)

        // Then
        assertTrue(actual is FormSubmitViewRenderer)
    }

    @Test
    fun make_should_return_WebViewRenderer_when_component_is_a_WebView() {
        // Given
        val component = mockk<WebView>()

        // When
        val actual = viewRendererFactory.make(component)

        // Then
        assertTrue(actual is WebViewRenderer)
    }
}