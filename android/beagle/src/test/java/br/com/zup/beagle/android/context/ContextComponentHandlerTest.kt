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

package br.com.zup.beagle.android.context

import android.view.View
import androidx.lifecycle.ViewModelProvider
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.engine.renderer.ActivityRootView
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.view.viewmodel.ScreenContextViewModel
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class ContextComponentHandlerTest {

    private val rootView = mockk<ActivityRootView>()
    private val view = mockk<View>()
    private val viewModel = mockk<ScreenContextViewModel>(relaxed = true)

    private lateinit var contextComponentHandler: ContextComponentHandler

    @Before
    fun setUp() {
        contextComponentHandler = ContextComponentHandler()

        every { rootView.activity } returns mockk(relaxed = true)
        every { rootView.getViewModelStoreOwner() } returns rootView.activity
        mockkConstructor(ViewModelProvider::class)
        every { anyConstructed<ViewModelProvider>().get(ScreenContextViewModel::class.java) } returns viewModel
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun addContext_should_call_addContext_when_component_is_ContextComponent() {
        // Given
        val component = mockk<Container>()
        val context = mockk<ContextData>()
        every { component.context } returns context

        // When
        contextComponentHandler.addContext(viewModel, view, component)

        // Then
        verify(exactly = 1) { viewModel.addContext(view, context) }
    }

    @Test
    fun onViewAttachedToWindow_should_call_linkBindingToContextAndEvaluateThem() {
        // Given
        val listenerSlot = slot<View.OnAttachStateChangeListener>()
        every { view.addOnAttachStateChangeListener(capture(listenerSlot)) } just Runs

        // When
        contextComponentHandler.addListenerToHandleContext(viewModel, view)
        listenerSlot.captured.onViewAttachedToWindow(view)

        // Then
        verify(exactly = once()) { viewModel.linkBindingToContextAndEvaluateThem(view) }
    }
}
