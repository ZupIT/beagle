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

package br.com.zup.beagle.android.components.utils

import android.view.View
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.utils.StyleManager
import br.com.zup.beagle.android.utils.dp
import br.com.zup.beagle.android.utils.toAndroidColor
import br.com.zup.beagle.android.utils.toAndroidId
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifyOrder
import io.mockk.verifySequence
import org.junit.Test
import kotlin.test.assertEquals

class ComponentStylizationTest : BaseTest() {

    @RelaxedMockK
    private lateinit var accessibilitySetup: AccessibilitySetup

    @MockK
    private lateinit var view: View

    @RelaxedMockK
    private lateinit var widget: Text

    @RelaxedMockK
    private lateinit var styleManager: StyleManager

    @InjectMockKs
    private lateinit var componentStylization: ComponentStylization<Text>

    override fun setUp() {
        super.setUp()
        styleManagerFactory = styleManager
        mockkStatic("br.com.zup.beagle.android.components.utils.ViewExtensionsKt")
    }

    @Test
    fun afterBuildView_when_is_widget() {
        // GIVEN
        val widgetId = "123"
        val slotId = slot<Int>()

        every { widget.id } returns widgetId
        every { view.id = capture(slotId) } just Runs
        every { view.applyStyle(widget) } just Runs

        // WHEN
        componentStylization.apply(view, widget)

        // THEN
        assertEquals(widgetId.toAndroidId(), slotId.captured)
        verifyOrder {
            view.applyStyle(widget)
            accessibilitySetup.applyAccessibility(view, widget)
        }
    }
}
