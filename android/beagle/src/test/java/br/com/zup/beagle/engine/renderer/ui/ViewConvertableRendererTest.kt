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

package br.com.zup.beagle.engine.renderer.ui

import android.content.Context
import android.view.View
import br.com.zup.beagle.BaseTest
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.widget.core.WidgetView
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Test
import kotlin.test.assertEquals

class ViewConvertableRendererTest : BaseTest() {

    @RelaxedMockK
    private lateinit var widget: WidgetView
    @MockK
    private lateinit var context: Context
    @RelaxedMockK
    private lateinit var view: View
    @MockK
    private lateinit var rootView: RootView

    private lateinit var viewConvertableRenderer: ViewConvertableRenderer

    override fun setUp() {
        super.setUp()

        every { rootView.getContext() } returns context

        viewConvertableRenderer = ViewConvertableRenderer(widget)
    }

    @Test
    fun build_should_make_a_native_view() {
        // Given
        every { widget.buildView(rootView.getContext()) } returns view

        // When
        val actual = viewConvertableRenderer.buildView(rootView)

        // Then
        assertEquals(view, actual)
    }
}
