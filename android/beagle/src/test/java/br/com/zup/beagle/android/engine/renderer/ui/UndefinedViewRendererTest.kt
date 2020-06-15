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


package br.com.zup.beagle.android.engine.renderer.ui

import android.graphics.Color
import android.widget.TextView
import br.com.zup.beagle.android.components.BaseComponentTest
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.setup.Environment
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.UndefinedWidget
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UndefinedViewRendererTest : BaseComponentTest() {

    private val textView: TextView = mockk()

    private val textSlot = slot<String>()
    private val textColorSlot = slot<Int>()
    private val backgroundColorSlot = slot<Int>()

    private lateinit var undefinedViewRenderer: UndefinedWidget

    override fun setUp() {
        super.setUp()

        every { BeagleEnvironment.beagleSdk.config.environment } returns Environment.DEBUG
        every { anyConstructed<ViewFactory>().makeTextView(rootView.getContext()) } returns textView
        every { textView.text = capture(textSlot) } just Runs
        every { textView.setTextColor(capture(textColorSlot)) } just Runs
        every { textView.setBackgroundColor(capture(backgroundColorSlot)) } just Runs

        undefinedViewRenderer = UndefinedWidget()
    }

    @Test
    fun build_should_create_a_TexView_with_a_undefinedWidget_text() {
        val actual = undefinedViewRenderer.buildView(rootView)

        assertTrue(actual is TextView)
        assertEquals("undefined component", textSlot.captured)
    }

    @Test
    fun build_should_create_a_TexView_with_a_textColor_RED() {
        undefinedViewRenderer.buildView(rootView)

        assertEquals(Color.RED, textColorSlot.captured)
    }

    @Test
    fun build_should_create_a_TexView_with_a_backgroundColor_YELLOW() {
        undefinedViewRenderer.buildView(rootView)

        assertEquals(Color.YELLOW, backgroundColorSlot.captured)
    }

    @Test
    fun build_should_create_View_when_Environment_is_PRODUCTION() {
        // Given
        every { BeagleEnvironment.beagleSdk.config.environment } returns Environment.PRODUCTION
        every { anyConstructed<ViewFactory>().makeView(any()) } returns textView

        // When
        undefinedViewRenderer.buildView(rootView)

        // Then
        verify(exactly = once()) { anyConstructed<ViewFactory>().makeView(rootView.getContext()) }
    }
}
