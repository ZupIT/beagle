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
import br.com.zup.beagle.android.view.viewmodel.ScreenContextViewModel
import br.com.zup.beagle.android.widget.ActivityRootView
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ContextComponentHandlerTest {

    private val rootView = mockk<ActivityRootView>()
    private val view = mockk<View>(relaxed = true)
    private val viewModel = mockk<ScreenContextViewModel>(relaxed = true)
    private val component = mockk<Container>()
    private val context = mockk<ContextData>()

    private val contextComponentHandler = ContextComponentHandler()

    @BeforeEach
    fun setUp() {
        every { rootView.activity } returns mockk(relaxed = true)
        every { rootView.getViewModelStoreOwner() } returns rootView.activity
        every { component.context } returns context
        mockkConstructor(ViewModelProvider::class)
        every { anyConstructed<ViewModelProvider>().get(ScreenContextViewModel::class.java) } returns viewModel
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `GIVEN a context component WHEN handleComponent is called THEN should call addContext`() {
        // When
        contextComponentHandler.handleComponent(view, viewModel, component)

        // Then
        verify(exactly = 1) { viewModel.addContext(view, context) }
    }

    @Test
    fun `GIVEN a view with context WHEN onViewAttachedToWindow is called THEN should call linkBindingToContextAndEvaluateThem`() {
        // Given
        val listenerSlot = slot<View.OnAttachStateChangeListener>()
        every { view.addOnAttachStateChangeListener(capture(listenerSlot)) } just Runs

        // When
        contextComponentHandler.handleComponent(view, viewModel, component)
        listenerSlot.captured.onViewAttachedToWindow(view)

        // Then
        verify(exactly = 1) { viewModel.linkBindingToContextAndEvaluateThem(view) }
    }
}
