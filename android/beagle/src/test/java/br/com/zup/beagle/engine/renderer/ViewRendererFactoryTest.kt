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
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

class ViewRendererFactoryTest {

    @MockK
    private lateinit var layoutViewRendererFactory: LayoutViewRendererFactory
    @MockK
    private lateinit var uiViewRendererFactory: UIViewRendererFactory

    @MockK
    private lateinit var component: ServerDrivenComponent

    private lateinit var viewRendererFactory: ViewRendererFactory

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewRendererFactory = ViewRendererFactory(layoutViewRendererFactory, uiViewRendererFactory)

        every { layoutViewRendererFactory.make(any()) } returns mockk<LayoutViewRenderer<*>>()
        every { uiViewRendererFactory.make(any()) } returns mockk<UIViewRenderer<*>>()
    }

    @Test
    fun make_should_return_a_LayoutViewRenderer() {
        val actual = viewRendererFactory.make(component)

        assertTrue(actual is LayoutViewRenderer)
    }

    @Test
    fun make_should_return_a_UIViewRenderer() {
        // Given
        every { viewRendererFactory.make(component) } throws IllegalArgumentException()

        // When
        val actual = viewRendererFactory.make(component)

        // Then
        assertTrue(actual is UIViewRenderer)
    }
}