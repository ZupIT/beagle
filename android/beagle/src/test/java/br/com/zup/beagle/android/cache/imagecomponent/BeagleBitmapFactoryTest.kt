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

package br.com.zup.beagle.android.cache.imagecomponent

import android.graphics.Bitmap
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals


internal class BeagleBitmapFactoryTest {
    
    private val beagleBitmapFactory: BeagleBitmapFactory = BeagleBitmapFactory()
    private val bitmap: Bitmap = mockk(relaxed = true)
    private val imageViewWidthAndHeight = 50

    @Before
    fun setUp() {
        mockkStatic(Bitmap::class)
    }

    @Test
    fun `GIVEN a bitmap greater than ImageView WHEN getBitmap THEN should return a resized bitmap`() {
        // Given
        val bitmapScaled: Bitmap = mockk(relaxed = true)
        every { bitmapScaled.height } returns imageViewWidthAndHeight
        every { bitmapScaled.width } returns imageViewWidthAndHeight
        every { bitmap.width } returns 100
        every { bitmap.height } returns 100
        every { Bitmap.createScaledBitmap(
            bitmap,
            imageViewWidthAndHeight,
            imageViewWidthAndHeight,
            any()
        ) } returns bitmapScaled

        // When
        val bitmapResult =  beagleBitmapFactory.getBitmap(bitmap, imageViewWidthAndHeight, imageViewWidthAndHeight)
        
        // Then
        verify(exactly = 1) {
            Bitmap.createScaledBitmap(
                bitmap,
                imageViewWidthAndHeight,
                imageViewWidthAndHeight,
                any()
            )
        }
        assertEquals(imageViewWidthAndHeight, bitmapResult.height)
        assertEquals(imageViewWidthAndHeight, bitmapResult.width)
    }

    @Test
    fun `GIVEN a bitmap lessEquals than ImageView WHEN getBitmap THEN should return the same bitmap`() {
        // Given
        every { bitmap.width } returns imageViewWidthAndHeight
        every { bitmap.height } returns imageViewWidthAndHeight

        // When
        val bitmapResult =  beagleBitmapFactory.getBitmap(bitmap, imageViewWidthAndHeight, imageViewWidthAndHeight)

        // Then
        assertEquals(imageViewWidthAndHeight, bitmapResult.height)
        assertEquals(imageViewWidthAndHeight, bitmapResult.width)
        assertEquals(bitmapResult, bitmap)
    }
    
}