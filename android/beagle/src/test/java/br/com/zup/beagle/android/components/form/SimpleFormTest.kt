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

package br.com.zup.beagle.android.components.form

import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.components.BaseComponentTest
import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import br.com.zup.beagle.core.ServerDrivenComponent
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertTrue

internal class SimpleFormTest : BaseComponentTest() {

    private val simpleFormAction: Action = mockk()
    private val context: ContextData = mockk()
    private val onSubmit: List<Action> = listOf(simpleFormAction)
    private val children: List<ServerDrivenComponent> = listOf(Text(""))

    private lateinit var simpleForm: SimpleForm

    override fun setUp() {
        super.setUp()

        simpleForm = SimpleForm(context, onSubmit, children)
        every { simpleFormAction.execute(rootView, view) } just Runs
    }

    @Test
    fun `should construct a beagle flex view when build`() {
        // When
        val view = simpleForm.buildView(rootView)

        // Then
        assertTrue(view is BeagleFlexView)
    }

    @Test
    fun `should add server driven component when build`() {
        // When
        simpleForm.buildView(rootView)

        // Then
        verify(exactly = once()) { beagleFlexView.addServerDrivenComponent(children[0]) }
    }

    @Test
    fun `should execute on submit actions when call submit`() {
        // When
        simpleForm.submit(rootView, view)

        // Then
        verify(exactly = once()) {
            simpleFormAction.execute(rootView, view)
        }
    }
}
