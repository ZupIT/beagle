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

package br.com.zup.beagle.android.components.layout

import br.com.zup.beagle.android.components.BaseComponentTest
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.utils.StyleManager
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyStyle
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

private const val STYLE_ID = "TEST"
private const val STYLE_ID_INTEGER = 123

@DisplayName("Given a Container")
class ContainerTest : BaseComponentTest() {

    private val style: Style = mockk(relaxed = true)

    private val containerChildren = listOf<ServerDrivenComponent>(mockk<Container>())

    private lateinit var container: Container

    @BeforeEach
    override fun setUp() {
        super.setUp()

        every { ViewFactory.makeBeagleFlexView(any(), any(), any()) } returns beagleFlexView
        every { style.copy(flex = any()) } returns style
        mockkConstructor(StyleManager::class)

        every { anyConstructed<StyleManager>().getContainerStyle(STYLE_ID) } returns STYLE_ID_INTEGER

        container = Container(
            children = containerChildren,
            styleId = STYLE_ID,
        ).applyStyle(style)
    }

    @DisplayName("When build view")
    @Nested
    inner class BeagleFlexViewTest {

        @Test
        @DisplayName("Then should create correct beagle flex view")
        fun testBuildCorrectBeagleFlexView() {
            // When
            container.buildView(rootView)

            // Then
            verify {
                ViewFactory.makeBeagleFlexView(
                    rootView = rootView,
                    style = style,
                    styleId = STYLE_ID_INTEGER,
                )
            }
        }

    }

    @DisplayName("When build view with children")
    @Nested
    inner class ChildrenTest {

        @Test
        @DisplayName("Then should create correct beagle flex view with children")
        fun testCorrectCallChildren() {
            // When
            container.buildView(rootView)

            // Then
            verify(exactly = once()) { beagleFlexView.addView(containerChildren) }
        }

    }
}
