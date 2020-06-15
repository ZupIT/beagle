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

package br.com.zup.beagle.android.engine.util

import androidx.appcompat.app.AppCompatActivity
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.context.ScreenContextViewModel
import br.com.zup.beagle.android.engine.renderer.ActivityRootView
import br.com.zup.beagle.android.mockdata.CustomWidget
import br.com.zup.beagle.android.utils.ViewModelProviderFactory
import br.com.zup.beagle.android.widget.core.Bind
import br.com.zup.beagle.core.ContextData
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.Before
import org.junit.Test


class ContextViewRendererTest {

    private val rootView = mockk<ActivityRootView>()
    private val viewModel = mockk<ScreenContextViewModel>()

    private lateinit var contextViewRenderer: ContextViewRenderer

    @Before
    fun setUp() {
        contextViewRenderer = ContextViewRenderer()

        mockkObject(ViewModelProviderFactory)

        every { rootView.activity } returns mockk()
        every { ViewModelProviderFactory.of(any<AppCompatActivity>())
            .get(ScreenContextViewModel::class.java) } returns viewModel
    }

    @Test
    fun startContextBinding_should_call_addBindingToContext_when_component_is_BindingAdapter() {
        // Given
        val component = mockk<CustomWidget>()
        val bindExpression = mockk<Bind.Expression<Any>>()
        every { component.getBindAttributes() } returns listOf(bindExpression)
        every { viewModel.contextDataManager.addBindingToContext(any()) } just Runs

        // When
        contextViewRenderer.startContextBinding(rootView, component)

        // Then
        verify(exactly = 1) { viewModel.contextDataManager.addBindingToContext(bindExpression) }
    }

    @Test
    fun startContextBinding_should_call_pushContext_when_component_is_ContextComponent() {
        // Given
        val component = mockk<Container>()
        val context = mockk<ContextData>()
        every { component.context } returns context
        every { viewModel.contextDataManager.pushContext(any()) } just Runs

        // When
        contextViewRenderer.startContextBinding(rootView, component)

        // Then
        verify(exactly = 1) { viewModel.contextDataManager.pushContext(context) }
    }

    @Test
    fun finishContextBinding_should_call_popContext_when_component_is_ContextComponent() {
        // Given
        val component = mockk<Container>()
        val context = mockk<ContextData>()
        every { viewModel.contextDataManager.popContext() } just Runs

        // When
        contextViewRenderer.finishContextBinding(rootView, component)

        // Then
        verify(exactly = 1) { viewModel.contextDataManager.popContext() }
    }
}
