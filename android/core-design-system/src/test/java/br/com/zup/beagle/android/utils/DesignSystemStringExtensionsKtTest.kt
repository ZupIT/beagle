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
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class DesignSystemStringExtensionsKtTest {

    private val colorSlot = slot<String>()

    @Before
    fun setUp() {
        mockkStatic(Color::class)
        every { Color.parseColor(capture(colorSlot)) } returns 0
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun toHexColor_should_return_00BB22_when_0B2_is_given() {
        // Given
        val rgb = "#0B2"

        // When
        rgb.toAndroidColor()

        // Then
        assertEquals("#00BB22", colorSlot.captured)
    }

    @Test
    fun toHexColor_should_return_aa55cc_when_a5c_is_given() {
        // Given
        val rgb = "#a5c"

        // When
        rgb.toAndroidColor()

        // Then
        assertEquals("#aa55cc", colorSlot.captured)
    }

    @Test
    fun toHexColor_should_return_AA00BB22_when_0B2A_is_given() {
        // Given
        val rgba = "#0B2A"

        // When
        rgba.toAndroidColor()

        // Then
        assertEquals("#AA00BB22", colorSlot.captured)
    }

    @Test
    fun toHexColor_should_return_11bb44dd_when_1b4d_is_given() {
        // Given
        val rgba = "#1b4d"

        // When
        rgba.toAndroidColor()

        // Then
        assertEquals("#dd11bb44", colorSlot.captured)
    }

    @Test
    fun toHexColor_should_return_A5C321_when_A5C321_is_given() {
        // Given
        val rgb = "#A5C321"

        // When
        rgb.toAndroidColor()

        // Then
        assertEquals("#A5C321", colorSlot.captured)
    }

    @Test
    fun toHexColor_should_return_ffffff_when_ffffff_is_given() {
        // Given
        val rgb = "#ffffff"

        // When
        rgb.toAndroidColor()

        // Then
        assertEquals("#ffffff", colorSlot.captured)
    }

    @Test
    fun toHexColor_should_return_5042FD7A_when_42FD7A50_is_given() {
        // Given
        val rgb = "#42FD7A50"

        // When
        rgb.toAndroidColor()

        // Then
        assertEquals("#5042FD7A", colorSlot.captured)
    }

    @Test
    fun toHexColor_should_return_60A218DC_when_A218DC60_is_given() {
        // Given
        val rgb = "#a218dc60"

        // When
        rgb.toAndroidColor()

        // Then
        assertEquals("#60a218dc", colorSlot.captured)
    }

}
