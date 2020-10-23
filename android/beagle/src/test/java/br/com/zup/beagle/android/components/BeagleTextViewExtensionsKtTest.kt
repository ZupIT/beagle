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


package br.com.zup.beagle.android.components

import android.graphics.Color
import android.support.v4.widget.TextViewCompat
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.TextView
import br.com.zup.beagle.android.components.utils.styleManagerFactory
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.setup.DesignSystem
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.utils.StyleManager
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.widget.core.TextAlignment
import io.mockk.*
import org.junit.Test
import kotlin.test.assertEquals

private val STYLE_RES = RandomData.int()
private val IMAGE_RES = RandomData.int()

class BeagleTextViewExtensionsKtTest : BaseComponentTest() {

    private val designSystem: DesignSystem = mockk()
    private val activity: AppCompatActivity = mockk(relaxed = true)
    private val textView: TextView = mockk(relaxed = true)
    private val styleManager: StyleManager = mockk(relaxed = true)

    private val textValueSlot = slot<String>()
    private val textAlignment = slot<Int>()

    private lateinit var text: Text

    override fun setUp() {
        super.setUp()

        mockkStatic(TextViewCompat::class)
        mockkStatic(Color::class)

        styleManagerFactory = styleManager
        every { styleManager.getTextStyle(any()) } returns STYLE_RES

        every { BeagleEnvironment.beagleSdk.designSystem } returns designSystem
        every { TextViewCompat.setTextAppearance(any(), any()) } just Runs
        every { anyConstructed<ViewFactory>().makeTextView(any(), STYLE_RES) } returns textView
        every { textView.context } returns activity
        every { textView.text = capture(textValueSlot) } just Runs
        every { textView.gravity = capture(textAlignment) } just Runs
        every { designSystem.image(any()) } returns IMAGE_RES
    }

    @Test
    fun setTextWidget_with_text_should_call_TextViewCompat_setTextAppearance() {
        // Given
        val textValue = RandomData.string()
        val style = RandomData.string()
        text = Text(text = textValue, styleId = style, alignment = null)

        // When
        text.buildView(rootView)

        // Then
        assertEquals(textValue, textValueSlot.captured)
    }

    @Test
    fun setTextWidget_with_text_should_not_call_TextViewCompat_setTextAppearance_when_style_is_null() {
        // Given
        val textValue = RandomData.string()
        text = Text(text = textValue, styleId = null, alignment = null)

        // When
        text.buildView(rootView)

        // Then
        verify(exactly = 0) { designSystem.textStyle("") }
    }

    @Test
    fun setTextWidget_with_text_should_set_alignment_when_is_center() {
        // Given
        val textValue = RandomData.string()
        text = Text(text = textValue, styleId = null, alignment = TextAlignment.CENTER)

        // When
        text.buildView(rootView)

        // Then
        assertEquals(Gravity.CENTER, textAlignment.captured)
    }

    @Test
    fun setTextWidget_with_text_should_set_alignment_when_is_right() {
        // Given
        val textValue = RandomData.string()
        text = Text(text = textValue, styleId = null, alignment = TextAlignment.RIGHT)

        // When
        text.buildView(rootView)

        // Then
        assertEquals(Gravity.END, textAlignment.captured)
    }

    @Test
    fun setTextWidget_with_text_should_set_alignment_when_is_left() {
        // Given
        val textValue = RandomData.string()
        text = Text(text = textValue, styleId = null, alignment = TextAlignment.LEFT)

        // When
        text.buildView(rootView)

        // Then
        assertEquals(Gravity.START, textAlignment.captured)
    }

    @Test
    fun setTextWidget_with_text_should_not_call_TextViewCompat_setTextAppearance_when_designSystem_is_null() {
        // Given
        val textValue = RandomData.string()
        val styleId = RandomData.string()

        every { BeagleEnvironment.beagleSdk.designSystem } returns null

        text = Text(text = textValue, styleId = styleId, alignment = null)

        // When
        text.buildView(rootView)

        // Then
        verify(exactly = 0) { TextViewCompat.setTextAppearance(textView, STYLE_RES) }
    }

    @Test
    fun setTextWidget_should_call_setTextColor() {
        // Given
        val textColor = "#000000"
        val colorInt = 0x000000
        every { Color.parseColor(textColor) } returns colorInt

        text = Text(text = "", textColor = textColor)

        // When
        text.buildView(rootView)

        // Then
        verify(exactly = once()) { textView.setTextColor(colorInt) }
    }

    @Test
    fun setTextWidget_should_not_call_setTextColor_when_color_is_null() {
        // Given
        val textColor = null
        val colorInt = 0x000000
        text = Text(text = "", textColor = textColor)

        // When
        text.buildView(rootView)

        // Then
        verify(exactly = 0) { textView.setTextColor(colorInt) }
    }
}
