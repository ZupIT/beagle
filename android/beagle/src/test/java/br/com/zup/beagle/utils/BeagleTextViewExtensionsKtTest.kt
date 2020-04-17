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

package br.com.zup.beagle.utils

import android.graphics.Color
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.TextViewCompat
import br.com.zup.beagle.extensions.once
import br.com.zup.beagle.setup.BeagleEnvironment
import br.com.zup.beagle.setup.DesignSystem
import br.com.zup.beagle.testutil.RandomData
import br.com.zup.beagle.view.BeagleTextView
import br.com.zup.beagle.view.setTextWidget
import br.com.zup.beagle.widget.ui.Text
import br.com.zup.beagle.widget.ui.TextAlignment
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

private val STYLE_RES = RandomData.int()
private val IMAGE_RES = RandomData.int()

class BeagleTextViewExtensionsTest {

    private val textValueSlot = slot<String>()
    private val textAlignment = slot<Int>()

    @RelaxedMockK
    private lateinit var beagleTextView: BeagleTextView
    @MockK
    private lateinit var designSystem: DesignSystem
    @RelaxedMockK
    private lateinit var activity: AppCompatActivity
    @RelaxedMockK
    private lateinit var text: Text

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        mockkObject(BeagleEnvironment)
        mockkStatic(TextViewCompat::class)
        mockkStatic(Color::class)

        every { BeagleEnvironment.beagleSdk.designSystem } returns designSystem
        every { TextViewCompat.setTextAppearance(any(), any()) } just Runs
        every { beagleTextView.context } returns activity
        every { beagleTextView.text = capture(textValueSlot) } just Runs
        every { beagleTextView.gravity = capture(textAlignment) } just Runs
        every { designSystem.textAppearance(any()) } returns STYLE_RES
        every { designSystem.buttonStyle(any()) } returns STYLE_RES
        every { designSystem.image(any()) } returns IMAGE_RES
        every { text.textColor } returns null
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun setTextWidget_with_text_should_call_TextViewCompat_setTextAppearance() {
        // Given
        val textValue = RandomData.string()
        val style = RandomData.string()
        every { text.text } returns textValue
        every { text.style } returns style
        every { text.alignment } returns null

        // When
        beagleTextView.setTextWidget(text)

        // Then
        assertEquals(textValue, textValueSlot.captured)
        verify(exactly = 1) { TextViewCompat.setTextAppearance(beagleTextView, STYLE_RES) }
    }

    @Test
    fun setTextWidget_with_text_should_not_call_TextViewCompat_setTextAppearance_when_style_is_null() {
        // Given
        val textValue = RandomData.string()
        every { text.text } returns textValue
        every { text.style } returns null
        every { text.alignment } returns null

        // When
        beagleTextView.setTextWidget(text)

        // Then
        verify(exactly = 1) { designSystem.textAppearance("") }
    }

    @Test
    fun setTextWidget_with_text_should_set_alignment_when_is_center() {
        // Given
        val textValue = RandomData.string()
        every { text.text } returns textValue
        every { text.style } returns null
        every { text.alignment } returns TextAlignment.CENTER

        // When
        beagleTextView.setTextWidget(text)

        // Then
        assertEquals(Gravity.CENTER, textAlignment.captured)
    }

    @Test
    fun setTextWidget_with_text_should_set_alignment_when_is_right() {
        // Given
        val textValue = RandomData.string()
        every { text.text } returns textValue
        every { text.style } returns null
        every { text.alignment } returns TextAlignment.RIGHT

        // When
        beagleTextView.setTextWidget(text)

        // Then
        assertEquals(Gravity.END, textAlignment.captured)
    }

    @Test
    fun setTextWidget_with_text_should_set_alignment_when_is_left() {
        // Given
        val textValue = RandomData.string()
        every { text.text } returns textValue
        every { text.style } returns null
        every { text.alignment } returns TextAlignment.LEFT

        // When
        beagleTextView.setTextWidget(text)

        // Then
        assertEquals(Gravity.START, textAlignment.captured)
    }

    @Test
    fun setTextWidget_with_text_should_not_call_TextViewCompat_setTextAppearance_when_designSystem_is_null() {
        // Given
        val textValue = RandomData.string()
        every { text.text } returns textValue
        every { text.style } returns RandomData.string()
        every { BeagleEnvironment.beagleSdk.designSystem } returns null
        every { text.alignment } returns null

        // When
        beagleTextView.setTextWidget(text)

        // Then
        verify(exactly = 0) { TextViewCompat.setTextAppearance(beagleTextView, STYLE_RES) }
    }

    @Test
    fun setTextWidget_should_call_setTextColor() {
        // Given
        val textColor = "#000000"
        val colorInt = 0x000000
        every { Color.parseColor(textColor) } returns colorInt
        every { text.textColor } returns textColor

        // When
        beagleTextView.setTextWidget(text)

        // Then
        verify(exactly = once()) { beagleTextView.setTextColor(colorInt) }
    }

    @Test
    fun setTextWidget_should_not_call_setTextColor_when_color_is_null() {
        // Given
        val textColor = null
        val colorInt = 0x000000
        every { text.textColor } returns textColor

        // When
        beagleTextView.setTextWidget(text)

        // Then
        verify(exactly = 0) { beagleTextView.setTextColor(colorInt) }
    }
}