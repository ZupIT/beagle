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
import android.util.Base64
import br.com.zup.beagle.android.cache.BeagleCache
import br.com.zup.beagle.android.cache.CACHE_HASH_KEY
import br.com.zup.beagle.android.cache.CACHE_KEY_DELIMITER
import br.com.zup.beagle.android.cache.CacheManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

internal class ImageDownloaderTest {

    private val cache: CacheManager = mockk(relaxed = true)
    private val beagleCache: BeagleCache = mockk(relaxed = true)
    private val imageDownloader = ImageDownloader(cache)
    private val bitmap: Bitmap = mockk(relaxed = true)
    private val byteArray  = ByteArray(10)
    private val bitmapBase64: String = "bitmapBase64"
    private val bitmapImproved: Bitmap = mockk(relaxed = true)
    private val contentWidth = 300
    private val contentHeight = 200
    private val url = "https://vitafelice.com.br/wp-content/uploads/2019/01/beagle.jpg"
    private val bitmapId = url + contentWidth + contentHeight

    @Before
    fun setUp() {
        mockkStatic(Base64::class)
        mockkStatic(BitmapFactory::class)
        mockkStatic(Bitmap::class)
        every { BitmapFactory.decodeByteArray(any(), any(), any()) } returns bitmap
        every { bitmapImproved.width } returns contentWidth
        every { bitmapImproved.height } returns contentHeight
        every { Base64.encodeToString(any(), any()) } returns bitmapBase64
        every { BitmapFactory.decodeStream(any()) } returns bitmap
        every { Base64.decode(any() as String, 0) } returns byteArray
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
        every { cache.restoreBeagleCache(any()) } returns null

        // When
        val bitmap = imageDownloader.getRemoteImage(url, contentWidth, contentHeight)

        // Then
        verify(exactly = 1) { cache.persistImageData(bitmapId, bitmapBase64) }
        verify(exactly = 1) { cache.restoreBeagleCache(bitmapId) }
        assertEquals(contentWidth, bitmap?.width)
        assertEquals(contentHeight, bitmap?.height)
    }

    @Test
    fun `GIVEN url and ImageViewSize WHEN download image bitmap THEN keep size and save on cache`() = runBlocking {
        // Given
        every { bitmap.width } returns contentWidth
        every { bitmap.height } returns contentHeight
        every { cache.restoreBeagleCache(any()) } returns null

        // When
        val bitmap = imageDownloader.getRemoteImage(url, contentWidth, contentHeight)

        // Then
        verify(exactly = 1) { cache.persistImageData(bitmapId, bitmapBase64) }
        verify(exactly = 1) { cache.restoreBeagleCache(bitmapId) }
        assertEquals(contentWidth, bitmap?.width)
        assertEquals(contentHeight, bitmap?.height)
    }

    @Test
    fun `GIVEN bitmap data WHEN download image THEN should load image bitmap from cache`() = runBlocking {
        // Given
        every { beagleCache.hash } returns bitmapId + CACHE_KEY_DELIMITER + CACHE_HASH_KEY
        every { beagleCache.data } returns bitmapBase64
        every { cache.restoreBeagleCache(any()) } returns beagleCache
        every { BitmapFactory.decodeByteArray(any(), any(), any()) } returns bitmapImproved

        // When
        val bitmap = imageDownloader.getRemoteImage(url, contentWidth, contentHeight)

        // Then
        verify(exactly = 1) { cache.restoreBeagleCache(bitmapId) }
        assertEquals(contentWidth, bitmap?.width)
        assertEquals(contentHeight, bitmap?.height)
    }
}