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
import android.graphics.BitmapFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

internal class ImageDownloaderTest {

    private val imageCache: LruImageCache = mockk(relaxed = true)
    private val imageDownloader = ImageDownloader(imageCache)
    private val bitmap: Bitmap = mockk(relaxed = true)
    private val bitmapImproved: Bitmap = mockk(relaxed = true)
    private val contentWidth = 300
    private val contentHeight = 200
    private val url = "https://vitafelice.com.br/wp-content/uploads/2019/01/beagle.jpg"

    @Before
    fun setUp() {
        mockkStatic(BitmapFactory::class)
        mockkStatic(Bitmap::class)
        every { BitmapFactory.decodeStream(any()) } returns bitmap
        every { bitmapImproved.width } returns contentWidth
        every { bitmapImproved.height } returns contentHeight
        every { Bitmap.createScaledBitmap(
            any(),
            contentWidth,
            contentHeight,
            any()
        ) } returns bitmapImproved
    }

    @Test
    fun `should download image bitmap and resize and save on cache`() = runBlocking {
        // Given
        every { bitmap.width } returns 715
        every { bitmap.height } returns 715
        every { imageCache.get(any()) } returns null

        // When
        val bitmap = imageDownloader.getRemoteImage(url, contentWidth, contentHeight)

        // Then
        verify(exactly = 1) { imageCache.put(any(), any()) }
        verify(exactly = 1) { imageCache.get(any()) }
        verify(exactly = 1) { Bitmap.createScaledBitmap(any(), any(), any(), any()) }
        assertEquals(contentWidth, bitmap?.width)
        assertEquals(contentHeight, bitmap?.height)
    }

    @Test
    fun `should download image bitmap keep size and resize and save on cache`() = runBlocking {
        // Given
        every { bitmap.width } returns contentWidth
        every { bitmap.height } returns contentHeight
        every { imageCache.get(any()) } returns null

        // When
        val bitmap = imageDownloader.getRemoteImage(url, contentWidth, contentHeight)

        // Then
        verify(exactly = 1) { imageCache.put(any(), any()) }
        verify(exactly = 1) { imageCache.get(any()) }
        verify(exactly = 0) { Bitmap.createScaledBitmap(any(), any(), any(), any()) }
        assertEquals(contentWidth, bitmap?.width)
        assertEquals(contentHeight, bitmap?.height)
    }

    @Test
    fun `should load image bitmap from cache`() = runBlocking {
        // Given
        every { imageCache.get(any()) } returns bitmapImproved

        // When
        val bitmap = imageDownloader.getRemoteImage(url, contentWidth, contentHeight)

        // Then
        verify(exactly = 2) { imageCache.get(any()) }
        assertEquals(contentWidth, bitmap?.width)
        assertEquals(contentHeight, bitmap?.height)
    }
}