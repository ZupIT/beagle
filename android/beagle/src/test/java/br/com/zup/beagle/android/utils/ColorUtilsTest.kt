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

package br.com.zup.beagle.android.utils


import android.graphics.Color
import br.com.zup.beagle.android.utils.ColorUtils
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.slot
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ColorUtilsTest {

    private val colorSlot = slot<String>()

    @Before
    fun setUp() {
        mockkObject(ColorUtils)
        mockkStatic(Color::class)
        every { Color.parseColor(capture(colorSlot)) } returns 0
    }

    @Test
    fun hexColor_should_return_11BB33_when_1B3_is_given() {
        // Given
        val colorRgb = "#1B3"

        // When
        ColorUtils.hexColor(colorRgb)

        // Then
        assertEquals("#11BB33",colorSlot.captured)

    }

    @Test
    fun hexColor_should_return_AA55CC_when_A5C_is_given() {
        // Given
        val colorRgb = "#2f3"

        // When
        ColorUtils.hexColor(colorRgb)

        // Then
        assertEquals("#22ff33",colorSlot.captured)

    }

    @Test
    fun hexColor_should_return_66EEFF22_when_6EF2_is_given() {
        // Given
        val colorRgba = "#6EF2"

        // When
        ColorUtils.hexColor(colorRgba)

        // Then
        assertEquals("#2266EEFF",colorSlot.captured)

    }

    @Test
    fun hexColor_should_return_cc9900bb_when_90bc_is_given() {
        // Given
        val colorRgba = "#90bc"

        // When
        ColorUtils.hexColor(colorRgba)

        // Then
        assertEquals("#cc9900bb",colorSlot.captured)

    }

    @Test
    fun hexColor_should_return_A3D256_when_A3D256_is_given() {
        // Given
        val colorRgb = "#A3D256"

        // When
        ColorUtils.hexColor(colorRgb)

        // Then
        assertEquals("#A3D256",colorSlot.captured)

    }

    @Test
    fun hexColor_should_return_584bcd_when_584bcd_is_given() {
        // Given
        val colorRgb = "#584bcd"

        // When
        ColorUtils.hexColor(colorRgb)

        // Then
        assertEquals("#584bcd",colorSlot.captured)

    }

    @Test
    fun hexColor_should_return_2018FAE3_when_18FAE320_is_given() {
        // Given
        val colorRgba = "#18FAE320"

        // When
        ColorUtils.hexColor(colorRgba)

        // Then
        assertEquals("#2018FAE3",colorSlot.captured)

    }

    @Test
    fun hexColor_should_return_50ae2g5c_when_ae2g5c50_is_given() {
        // Given
        val colorRgba = "#abc12330"

        // When
        ColorUtils.hexColor(colorRgba)

        // Then
        assertEquals("#30abc123",colorSlot.captured)

    }
}