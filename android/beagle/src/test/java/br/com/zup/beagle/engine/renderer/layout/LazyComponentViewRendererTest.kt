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
import android.view.View
import androidx.core.view.get
import br.com.zup.beagle.BaseTest
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.engine.renderer.ViewRenderer
import br.com.zup.beagle.engine.renderer.ViewRendererFactory
import br.com.zup.beagle.extensions.once
import br.com.zup.beagle.testutil.RandomData
import br.com.zup.beagle.view.BeagleView
import br.com.zup.beagle.view.ViewFactory
import br.com.zup.beagle.widget.lazy.LazyComponent
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertTrue

private val URL = RandomData.httpUrl()

class LazyComponentViewRendererTest : BaseTest() {

    @MockK
    private lateinit var lazyComponent: LazyComponent
    @MockK
    private lateinit var viewRendererFactory: ViewRendererFactory
    @MockK
    private lateinit var viewFactory: ViewFactory
    @RelaxedMockK
    private lateinit var beagleView: BeagleView
    @MockK
    private lateinit var initialStateView: View
    @MockK
    private lateinit var rootView: RootView
    @MockK
    private lateinit var context: Context
    @MockK
    private lateinit var initialState: ServerDrivenComponent
    @MockK
    private lateinit var viewRenderer: ViewRenderer<*>

    @InjectMockKs
    private lateinit var lazyComponentViewRenderer: LazyComponentViewRenderer

    override fun setUp() {
        super.setUp()

        every { viewFactory.makeBeagleView(any()) } returns beagleView
        every { rootView.getContext() } returns context
        every { viewRendererFactory.make(any()) } returns viewRenderer
        every { lazyComponent.initialState } returns initialState
        every { beagleView[0] } returns initialStateView
        every { lazyComponent.path } returns URL
        every { beagleView.addServerDrivenComponent(any(), any()) } just Runs
        every { beagleView.updateView(any(), any(), any()) } just Runs
    }

    @Test
    fun build_should_call_make_a_BeagleView() {
        val actual = lazyComponentViewRenderer.build(rootView)

        assertTrue(actual is BeagleView)
    }

    @Test
    fun build_should_add_initialState_and_trigger_updateView() {
        lazyComponentViewRenderer.build(rootView)

        verify(exactly = once()) { beagleView.addServerDrivenComponent(initialState, rootView) }
        verify(exactly = once()) { beagleView.updateView(rootView, URL, initialStateView) }
    }
}