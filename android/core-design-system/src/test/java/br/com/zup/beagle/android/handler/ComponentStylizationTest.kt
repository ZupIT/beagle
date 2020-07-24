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

import android.view.View
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.manager.StyleManager
import br.com.zup.beagle.android.utils.AccessibilitySetup
import br.com.zup.beagle.android.utils.styleManagerFactory
import br.com.zup.beagle.android.utils.toAndroidId
import br.com.zup.beagle.core.IdentifierComponent
import br.com.zup.beagle.core.ServerDrivenComponent
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ComponentStylizationTest {

    @RelaxedMockK
    private lateinit var accessibilitySetup: AccessibilitySetup

    @RelaxedMockK
    private lateinit var view: View

    @RelaxedMockK
    private lateinit var widget: IdentifierComponent

    @RelaxedMockK
    private lateinit var styleManager: StyleManager

    @InjectMockKs
    private lateinit var componentStylization: ComponentStylization<ServerDrivenComponent>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        styleManagerFactory = styleManager
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun afterBuildView_when_is_widget() {
        // GIVEN
        val widgetId = "123"
        val slotId = slot<Int>()

        every { widget.id } returns widgetId
        every { view.id = capture(slotId) } just Runs

        // WHEN
        componentStylization.apply(view, widget)

        // THEN
        assertEquals(widgetId.toAndroidId(), slotId.captured)
        verify(exactly = once()) { accessibilitySetup.applyAccessibility(view, widget) }
    }
}
