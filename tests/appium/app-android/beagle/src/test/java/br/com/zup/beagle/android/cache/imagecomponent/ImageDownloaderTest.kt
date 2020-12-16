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
import android.util.LruCache
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

internal class ImageDownloaderTest {

    private val imageDownloader = ImageDownloader()
    private val bitmap: Bitmap = mockk(relaxed = true)
    private val bitmapImproved: Bitmap = mockk(relaxed = true)
    private val contentWidth = 300
    private val contentHeight = 200
    private val url = "https://vitafelice.com.br/wp-content/uploads/2019/01/beagle.jpg"
    private val bitmapId = url + contentWidth + contentHeight

    @BeforeEach
    fun setUp() {
        mockkStatic(BitmapFactory::class)
        mockkObject(LruImageCache)
        mockkStatic(LruCache::class)
        mockkStatic(Bitmap::class)
        every { bitmapImproved.width } returns contentWidth
        every { bitmapImproved.height } returns contentHeight
        every { LruImageCache.get(any()) } returns null
        every { LruImageCache.put(any(), any()) } just runs
        every { BitmapFactory.decodeStream(any()) } returns bitmap
        every { Bitmap.createScaledBitmap(
            any(),
            contentWidth,
            contentHeight,
            any()
        ) } returns bitmapImproved
    }

    @Test
    fun `GIVEN url and ImageViewSize WHEN download image bitmap THEN keep size and save on cache`() = runBlocking {
        // Given
        every { bitmap.width } returns contentWidth
        every { bitmap.height } returns contentHeight

        // When
        val bitmap = imageDownloader.getRemoteImage(url, contentWidth, contentHeight)

        // Then
        verify(exactly = 1) { LruImageCache.put(bitmapId, bitmap) }
        verify(exactly = 1) { LruImageCache.get(bitmapId) }
        assertEquals(contentWidth, bitmap?.width)
        assertEquals(contentHeight, bitmap?.height)
    }

    @Test
    fun `GIVEN bitmap data WHEN download image THEN should load image bitmap from cache`() = runBlocking {
        // Given
        every { LruImageCache.get(any()) } returns bitmapImproved

        // When
        val bitmap = imageDownloader.getRemoteImage(url, contentWidth, contentHeight)

        // Then
        verify(exactly = 1) { LruImageCache.get(bitmapId) }
        assertEquals(contentWidth, bitmap?.width)
        assertEquals(contentHeight, bitmap?.height)
    }
}