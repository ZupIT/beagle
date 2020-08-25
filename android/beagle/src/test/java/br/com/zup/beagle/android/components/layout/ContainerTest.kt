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
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyStyle
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class ContainerTest : BaseComponentTest() {

    private val style: Style = mockk(relaxed = true)

    private val containerChildren = listOf<ServerDrivenComponent>(mockk<Container>())

    private lateinit var container: Container

    override fun setUp() {
        super.setUp()

        every { style.copy(flex = any()) } returns style

        container = Container(containerChildren).applyStyle(style)
    }

    @Test
    fun build_should_makeBeagleFlexView() {
        // WHEN
        container.buildView(rootView)

        // THEN
        verify(exactly = once()) { anyConstructed<ViewFactory>().makeBeagleFlexView(rootView, style) }
    }

    @Test
    fun build_should_addServerDrivenComponent() {
        // WHEN
        container.buildView(rootView)

        // THEN
        verify(exactly = once()) { beagleFlexView.addServerDrivenComponent(containerChildren[0]) }
    }
}
