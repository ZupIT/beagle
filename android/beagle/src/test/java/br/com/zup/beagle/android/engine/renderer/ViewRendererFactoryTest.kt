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

package br.com.zup.beagle.android.engine.renderer

import android.content.Context
import android.view.View
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.ViewConvertable
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.core.ServerDrivenComponent
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Given a ViewRenderFactory")
class ViewRendererFactoryTest : BaseTest() {

    private val component: ServerDrivenComponent = mockk<Text>()
    private val viewConvertableRenderer = mockk<ViewConvertableRenderer>()
    private val viewConvertable = mockk<ViewConvertable>()

    private lateinit var viewRendererFactory: ViewRendererFactory

    @BeforeEach
    override fun setUp() {
        super.setUp()

        viewRendererFactory = ViewRendererFactory()

//        every { ViewConvertableRenderer(component as ViewConvertable) } returns viewConvertableRenderer
    }

    @Test
    @DisplayName("Then the component should be render correctly")
    fun makeView() {
        // When
        val viewActual = viewRendererFactory.make(component)

        // Then
        Assertions.assertEquals(viewActual, viewConvertableRenderer)
    }

}
