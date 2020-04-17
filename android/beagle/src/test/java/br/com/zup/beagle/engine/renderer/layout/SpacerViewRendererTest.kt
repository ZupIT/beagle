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

import android.content.Context
import br.com.zup.beagle.BaseTest
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.engine.renderer.ViewRendererFactory
import br.com.zup.beagle.view.BeagleFlexView
import br.com.zup.beagle.view.ViewFactory
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.layout.Spacer
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.slot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class SpacerViewRendererTest : BaseTest() {

    @MockK
    private lateinit var viewRendererFactory: ViewRendererFactory
    @MockK
    private lateinit var viewFactory: ViewFactory
    @MockK
    private lateinit var rootView: RootView
    @MockK
    private lateinit var context: Context

    private lateinit var spacerViewRenderer: SpacerViewRenderer

    override fun setUp() {
        super.setUp()

        spacerViewRenderer = SpacerViewRenderer(
            Spacer(10.0),
            viewRendererFactory,
            viewFactory
        )

        every { rootView.getContext() } returns context
    }

    @Test
    fun build() {
        // Given
        val beagleFlexView = mockk<BeagleFlexView>()
        val flexSlot = slot<Flex>()
        every { viewFactory.makeBeagleFlexView(context, capture(flexSlot)) } returns beagleFlexView

        // When
        val actual = spacerViewRenderer.build(rootView)

        // Then
        assertNotNull(actual)
        assertEquals(10.0, flexSlot.captured.size?.width?.value)
        assertEquals(10.0, flexSlot.captured.size?.height?.value)
    }
}