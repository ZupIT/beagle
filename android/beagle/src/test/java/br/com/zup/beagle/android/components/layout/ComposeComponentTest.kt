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

package br.com.zup.beagle.android.components.layout

import br.com.zup.beagle.android.components.BaseComponentTest
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.core.ServerDrivenComponent
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertEquals

class ComposeComponentTest : BaseComponentTest() {

    private val child: WidgetView = mockk()
    private lateinit var composeComponent: ComposeComponent

    override fun setUp() {
        super.setUp()
        composeComponent = object : ComposeComponent() {
            override fun build(): ServerDrivenComponent = child
        }
    }

    @Test
    fun build_should_create_view() {
        // WHEN
        val actual = composeComponent.buildView(rootView)

        // THEN
        assertEquals(beagleFlexView, actual)
    }

    @Test
    fun build_should_makeBeagleFlexView() {
        // WHEN
        composeComponent.buildView(rootView)

        // THEN
        verify(exactly = once()) { anyConstructed<ViewFactory>().makeBeagleFlexView(rootView) }
    }

    @Test
    fun build_should_addServerDrivenComponent() {
        // WHEN
        composeComponent.buildView(rootView)

        // THEN
        verify(exactly = once()) { beagleFlexView.addServerDrivenComponent(child) }
    }
}
