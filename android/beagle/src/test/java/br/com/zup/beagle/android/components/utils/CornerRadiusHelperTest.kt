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

import android.util.DisplayMetrics
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.core.CornerRadius
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a CornerRadiusHelper")
class CornerRadiusHelperTest {

    @BeforeEach
    fun setUp() {
        mockkObject(BeagleEnvironment)
        every { BeagleEnvironment.application } returns mockk() {
            every { resources } returns mockk() {
                every { displayMetrics } returns DisplayMetrics().apply {
                    density = 1f
                }
            }
        }
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(BeagleEnvironment)
    }

    @Nested
    @DisplayName("When getFloatArray is called")
    inner class ApplyRadius {

        @Test
        @DisplayName("Then should set zeros when CornerRadius is empty")
        fun testEmptyCornerRadius() {
            // Given
            val cornerRadius = CornerRadius()

            // When
            val result = cornerRadius.getFloatArray()

            // Then
            result.forEach {
                assertEquals(0f, it)
            }
        }

        @Test
        @DisplayName("Then should set the radius value to every array position")
        fun testRadius() {
            // Given
            val cornerRadius = CornerRadius(radius = 10.0)

            // When
            val result = cornerRadius.getFloatArray()

            // Then
            result.forEach {
                assertEquals(10f, it)
            }
        }

        @Test
        @DisplayName("Then should set the radius value to positions 0 and 1")
        fun testTopLeftRadius() {
            // Given
            val cornerRadius = CornerRadius(topLeft = 10.0)

            // When
            val result = cornerRadius.getFloatArray()

            // Then
            for (i in result.indices) {
                when (i) {
                    0, 1 -> assertEquals(10f, result[i])
                    else -> assertEquals(0f, result[i])
                }
            }
        }

        @Test
        @DisplayName("Then should set the radius value to positions 2 and 3")
        fun testTopRightRadius() {
            // Given
            val cornerRadius = CornerRadius(topRight = 10.0)

            // When
            val result = cornerRadius.getFloatArray()

            // Then
            for (i in result.indices) {
                when (i) {
                    2, 3 -> assertEquals(10f, result[i])
                    else -> assertEquals(0f, result[i])
                }
            }
        }

        @Test
        @DisplayName("Then should set the radius value to positions 4 and 5")
        fun testBottomRightRadius() {
            // Given
            val cornerRadius = CornerRadius(bottomRight = 10.0)

            // When
            val result = cornerRadius.getFloatArray()

            // Then
            for (i in result.indices) {
                when (i) {
                    4, 5 -> assertEquals(10f, result[i])
                    else -> assertEquals(0f, result[i])
                }
            }
        }

        @Test
        @DisplayName("Then should set the radius value to positions 6 and 7")
        fun testBottomLeftRadius() {
            // Given
            val cornerRadius = CornerRadius(bottomLeft = 10.0)

            // When
            val result = cornerRadius.getFloatArray()

            // Then
            for (i in result.indices) {
                when (i) {
                    6, 7 -> assertEquals(10f, result[i])
                    else -> assertEquals(0f, result[i])
                }
            }
        }

        @Test
        @DisplayName("Then should set the radius to all position and the corners to 6 and 7 indices")
        fun testRadiusAndOtherRadius() {
            // Given
            val cornerRadius = CornerRadius(radius = 10.0, bottomLeft = 20.0)

            // When
            val result = cornerRadius.getFloatArray()

            // Then
            for (i in result.indices) {
                when (i) {
                    6, 7 -> assertEquals(20f, result[i])
                    else -> assertEquals(10f, result[i])
                }
            }
        }
    }
}
