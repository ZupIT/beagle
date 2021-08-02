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
import io.mockk.unmockkStatic
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class ColorUtilsTest {

    private val colorSlot = slot<String>()

    @BeforeEach
    fun setUp() {
        mockkStatic(Color::class)
        every { Color.parseColor(capture(colorSlot)) } returns 0
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(Color::class)
    }

    @Test
    fun `GIVEN #1B3 WHEN hexColor is called THEN consider #11BB33`() {
        // Given
        val colorRgb = "#1B3"

        // When
        ColorUtils.hexColor(colorRgb)

        // Then
        assertEquals("#11BB33", colorSlot.captured)
    }

    @Test
    fun `GIVEN #2F3 WHEN hexColor is called THEN consider #22FF33`() {
        // Given
        val colorRgb = "#2f3"

        // When
        ColorUtils.hexColor(colorRgb)

        // Then
        assertEquals("#22ff33", colorSlot.captured)
    }

    @Test
    fun `GIVEN #6EF2 WHEN hexColor is called THEN consider #2266EEFF`() {
        // Given
        val colorRgba = "#6EF2"

        // When
        ColorUtils.hexColor(colorRgba)

        // Then
        assertEquals("#2266EEFF", colorSlot.captured)
    }

    @Test
    fun `GIVEN #90BC WHEN hexColor is called THEN consider #CC9900BB`() {
        // Given
        val colorRgba = "#90bc"

        // When
        ColorUtils.hexColor(colorRgba)

        // Then
        assertEquals("#cc9900bb", colorSlot.captured)
    }

    @Test
    fun `GIVEN #A3D256 WHEN hexColor is called THEN consider #A3D256`() {
        // Given
        val colorRgb = "#A3D256"

        // When
        ColorUtils.hexColor(colorRgb)

        // Then
        assertEquals("#A3D256", colorSlot.captured)
    }

    @Test
    fun `GIVEN #584BCD WHEN hexColor is called THEN consider #584BCD`() {
        // Given
        val colorRgb = "#584bcd"

        // When
        ColorUtils.hexColor(colorRgb)

        // Then
        assertEquals("#584bcd", colorSlot.captured)
    }

    @Test
    fun `GIVEN #18FAE320 WHEN hexColor is called THEN consider #2018FAE3`() {
        // Given
        val colorRgba = "#18FAE320"

        // When
        ColorUtils.hexColor(colorRgba)

        // Then
        assertEquals("#2018FAE3", colorSlot.captured)
    }

    @Test
    fun `GIVEN #ABC12330 WHEN hexColor is called THEN consider #30ABC123`() {
        // Given
        val colorRgba = "#abc12330"

        // When
        ColorUtils.hexColor(colorRgba)

        // Then
        assertEquals("#30abc123", colorSlot.captured)
    }
}
