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

package br.com.zup.beagle.context

import androidx.appcompat.app.AppCompatActivity
import br.com.zup.beagle.BaseTest
import br.com.zup.beagle.core.Bind
import br.com.zup.beagle.engine.renderer.ActivityRootView
import br.com.zup.beagle.setup.BindingAdapter
import br.com.zup.beagle.testutil.RandomData
import br.com.zup.beagle.utils.ViewModelProviderFactory
import br.com.zup.beagle.view.viewmodel.ScreenContextViewModel
import br.com.zup.beagle.widget.core.Action
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.Test

import kotlin.test.assertEquals
import kotlin.test.assertTrue

data class CustomAction(
    val a: Bind<String>,
    val b: Bind<Boolean>
) : Action, BindingAdapter {
    override fun getBindAttributes() = listOf(a, b)
}

class ContextActionExecutorTest : BaseTest() {

    private val rootView = mockk<ActivityRootView>()

    private val customAction = CustomAction(
        a = Bind.Expression.of("@{onChange.value}"),
        b = Bind.Value(true)
    )
    private lateinit var screenContextViewModel: ScreenContextViewModel
    private lateinit var contextActionExecutor: ContextActionExecutor

    override fun setUp() {
        super.setUp()

        mockkObject(ViewModelProviderFactory)

        every { beagleSdk.registeredWidgets() } returns listOf()
        every { rootView.activity } returns mockk()

        contextActionExecutor = ContextActionExecutor()
        screenContextViewModel = ScreenContextViewModel()

        every { ViewModelProviderFactory.of(any<AppCompatActivity>())[ScreenContextViewModel::class.java] } returns screenContextViewModel
    }

    @Test
    fun executeActions_should_call_bind_attributes() {
        // Given
        val eventId = "onChange"
        val value = RandomData.string()

        // When
        contextActionExecutor.executeActions(rootView, listOf(customAction), eventId, value)

        // Then
        assertEquals(value, customAction.a.get())
        assertTrue { customAction.b.get() }
    }
}