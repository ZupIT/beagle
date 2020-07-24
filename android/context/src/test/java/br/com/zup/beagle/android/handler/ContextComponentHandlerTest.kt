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

package br.com.zup.beagle.android.handler

import androidx.appcompat.app.AppCompatActivity
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.fake.ContainerFake
import br.com.zup.beagle.android.viewmodel.ScreenContextViewModel
import br.com.zup.beagle.android.widget.ActivityRootView
import br.com.zup.beagle.android.widget.ViewModelProviderFactory
import io.mockk.*
import org.junit.After
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
        every {
            ViewModelProviderFactory.of(any<AppCompatActivity>())
                .get(ScreenContextViewModel::class.java)
        } returns viewModel
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun handleContext_should_call_addContext_when_component_is_ContextComponent() {
        // Given
        val context = mockk<ContextData>()
        every { viewModel.addContext(any()) } just Runs

        // When
        contextComponentHandler.handleContext(rootView, ContainerFake(context))

        // Then
        verify(exactly = 1) { viewModel.addContext(context) }
    }
}