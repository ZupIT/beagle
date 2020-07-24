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

import android.view.View
import br.com.zup.beagle.android.handler.ComponentStylization
import br.com.zup.beagle.android.handler.ContextComponentHandler
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.widget.Widget
import io.mockk.mockk
import io.mockk.verifySequence
import org.junit.Before
import org.junit.Test

private class AbstractViewRenderer(
    override val component: Widget,
    componentStylization: ComponentStylization<Widget>,
    contextComponentHandler: ContextComponentHandler
) : ViewRenderer<Widget>(componentStylization, contextComponentHandler) {
    override fun buildView(rootView: RootView): View {
        return mockk()
    }
}

class AbstractViewRendererTest {

    private val component = mockk<Widget>(relaxed = true)
    private val rootView = mockk<RootView>(relaxed = true)
    private val componentStylization = mockk<ComponentStylization<Widget>>(relaxed = true)
    private val contextViewRenderer = mockk<ContextComponentHandler>(relaxed = true)

    private lateinit var viewRenderer: AbstractViewRenderer

    @Before
    fun setUp() {
        viewRenderer = AbstractViewRenderer(
            component,
            componentStylization,
            contextViewRenderer
        )
    }

    @Test
    fun build_should_call_contextViewRenderer_and_componentStylization() {
        // Given When
        viewRenderer.build(rootView)

        // Then
        verifySequence {
            contextViewRenderer.handleContext(rootView, component)
            componentStylization.apply(any(), component)
        }
    }
}