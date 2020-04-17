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

package br.com.zup.beagle.engine.renderer.layout

import br.com.zup.beagle.BaseTest
import br.com.zup.beagle.engine.renderer.ViewRendererFactory
import br.com.zup.beagle.view.ViewFactory
import br.com.zup.beagle.widget.core.FlexDirection
import br.com.zup.beagle.widget.layout.Horizontal
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Test

class HorizontalViewRendererTest : BaseTest() {

    @MockK
    private lateinit var viewRendererFactory: ViewRendererFactory
    @MockK
    private lateinit var viewFactory: ViewFactory

    @Test
    fun getYogaFlexDirection_should_return_ROW_REVERSE_when_reversed_is_true() {
        // Given
        val horizontalViewRenderer = HorizontalViewRenderer(
            Horizontal(listOf(), reversed = true),
            viewRendererFactory,
            viewFactory
        )

        // When
        val actual = horizontalViewRenderer.getYogaFlexDirection()

        // Then
        assertEquals(FlexDirection.ROW_REVERSE, actual)
    }

    @Test
    fun getYogaFlexDirection_should_return_ROW_when_reversed_is_false() {
        // Given
        val horizontalViewRenderer = HorizontalViewRenderer(
            Horizontal(listOf(), reversed = false),
            viewRendererFactory,
            viewFactory
        )

        // When
        val actual = horizontalViewRenderer.getYogaFlexDirection()

        // Then
        assertEquals(FlexDirection.ROW, actual)
    }
}