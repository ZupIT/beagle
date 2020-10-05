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
    private val bitmapId = url + contentWidth + contentHeight

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
    fun `GIVEN url and ImageViewSize WHEN download image bitmap THEN resize and save on cache`() = runBlocking {
        // Given
        val widthAndHeight = 715
        every { bitmap.width } returns widthAndHeight
        every { bitmap.height } returns widthAndHeight
        every { imageCache.get(bitmapId) } returns null

        // When
        val bitmap = imageDownloader.getRemoteImage(url, contentWidth, contentHeight)

        // Then
        verify(exactly = 1) { imageCache.put(url + contentWidth + contentHeight, any()) }
        verify(exactly = 1) { imageCache.get(bitmapId) }
        assertEquals(contentWidth, bitmap?.width)
        assertEquals(contentHeight, bitmap?.height)
    }

    @Test
    fun `GIVEN url and ImageViewSize WHEN download image bitmap THEN keep size and save on cache`() = runBlocking {
        // Given
        every { bitmap.width } returns contentWidth
        every { bitmap.height } returns contentHeight
        every { imageCache.get(bitmapId) } returns null

        // When
        val bitmap = imageDownloader.getRemoteImage(url, contentWidth, contentHeight)

        // Then
        verify(exactly = 1) { imageCache.put(url + contentWidth + contentHeight, any()) }
        verify(exactly = 1) { imageCache.get(bitmapId) }
        assertEquals(contentWidth, bitmap?.width)
        assertEquals(contentHeight, bitmap?.height)
    }

    @Test
    fun `GIVEN Bitmap resized WHEN download image THEN should load image bitmap from cache`() = runBlocking {
        // Given
        every { imageCache.get(any()) } returns bitmapImproved

        // When
        val bitmap = imageDownloader.getRemoteImage(url, contentWidth, contentHeight)

        // Then
        verify(exactly = 2) { imageCache.get(bitmapId) }
        assertEquals(contentWidth, bitmap?.width)
        assertEquals(contentHeight, bitmap?.height)
    }
}