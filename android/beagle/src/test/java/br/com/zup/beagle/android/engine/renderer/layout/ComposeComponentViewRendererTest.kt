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

package br.com.zup.beagle.android.engine.renderer.layout

import android.content.Context
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.engine.renderer.RootView
import br.com.zup.beagle.android.engine.renderer.ViewRendererFactory
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.widget.layout.ComposeComponent
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertEquals

class ComposeComponentViewRendererTest : BaseTest() {

    @RelaxedMockK
    private lateinit var component: ComposeComponent
    @MockK
    private lateinit var viewRendererFactory: ViewRendererFactory
    @MockK
    private lateinit var viewFactory: ViewFactory
    @InjectMockKs
    private lateinit var viewRenderer: ComposeComponentViewRenderer

    @MockK
    private lateinit var rootView: RootView
    @RelaxedMockK
    private lateinit var beagleFlexView: BeagleFlexView
    @MockK
    private lateinit var context: Context

    override fun setUp() {
        super.setUp()

        every { viewFactory.makeBeagleFlexView(any()) } returns beagleFlexView
        every { rootView.getContext() } returns context
    }

    @Test
    fun build_should_create_view() {
        // WHEN
        val actual = viewRenderer.buildView(rootView)

        // THEN
        assertEquals(beagleFlexView, actual)
    }

    @Test
    fun build_should_makeBeagleFlexView() {
        // WHEN
        viewRenderer.buildView(rootView)

        // THEN
        verify(exactly = once()) { viewFactory.makeBeagleFlexView(context) }
    }

    @Test
    fun build_should_addServerDrivenComponent() {
        // WHEN
        viewRenderer.buildView(rootView)

        // THEN
        verify(exactly = once()) { component.build() }
    }
}