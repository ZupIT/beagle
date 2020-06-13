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

import androidx.appcompat.app.AppCompatActivity
import br.com.zup.beagle.android.context.ContextComponentHandler
import br.com.zup.beagle.view.viewmodel.ScreenContextViewModel
import br.com.zup.beagle.core.Bind
import br.com.zup.beagle.core.ContextData
import br.com.zup.beagle.engine.renderer.ActivityRootView
import br.com.zup.beagle.mockdata.CustomWidget
import br.com.zup.beagle.utils.ViewModelProviderFactory
import br.com.zup.beagle.widget.layout.Container
import io.mockk.Runs
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class ContextComponentHandlerTest {

    private val rootView = mockk<ActivityRootView>()
    private val viewModel = mockk<ScreenContextViewModel>()

    private lateinit var contextComponentHandler: ContextComponentHandler

    @Before
    fun setUp() {
        contextComponentHandler = ContextComponentHandler()

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
        contextComponentHandler.handleContext(rootView, component)

        // Then
        verify(exactly = 1) { viewModel.contextDataManager.addBindingToContext(bindExpression) }
    }

    @Test
    fun startContextBinding_should_call_pushContext_when_component_is_ContextComponent() {
        // Given
        val component = mockk<Container>()
        val context = mockk<ContextData>()
        every { component.context } returns context
        every { viewModel.contextDataManager.addContext(any()) } just Runs

        // When
        contextComponentHandler.handleContext(rootView, component)

        // Then
        verify(exactly = 1) { viewModel.contextDataManager.addContext(context) }
    }
}