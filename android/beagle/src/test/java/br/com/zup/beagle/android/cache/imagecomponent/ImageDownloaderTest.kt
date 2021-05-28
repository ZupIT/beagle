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

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.DisplayMetrics
import android.util.LruCache
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import java.io.InputStream
import java.net.URL

@DisplayName("Given ImageDownloader")
class ImageDownloaderTest {

    private val imageDownloader = ImageDownloader()
    private val bitmap: Bitmap = mockk(relaxed = true)
    private val bitmapImproved: Bitmap = mockk(relaxed = true)
    private val contentWidth = 300
    private val contentHeight = 200
    private val url = "https://www.stub.com/image.png"
    private val bitmapId = url + contentWidth + contentHeight
    private val inputStream: InputStream = mockk(relaxed = true)
    private val javaUrl: URL = mockk(relaxed = true)
    private val context: Context = mockk(relaxed = true)
    private val optionsSlot = slot<BitmapFactory.Options>()

    @BeforeEach
    fun setUp() {
        mockkStatic(BitmapFactory::class)
        mockkObject(LruImageCache)
        mockkStatic(LruCache::class)
        mockkStatic(Bitmap::class)
        mockkObject(URLFactory)
        every { bitmapImproved.width } returns contentWidth
        every { bitmapImproved.height } returns contentHeight
        every { LruImageCache.get(any()) } returns null
        every { LruImageCache.put(any(), any()) } just runs
        every { URLFactory.createURL(any()) } returns javaUrl
        every { javaUrl.openStream() } returns inputStream
        every { BitmapFactory.decodeStream(inputStream, null, capture(optionsSlot)) } returns bitmap
        every { Bitmap.createScaledBitmap(
            any(),
            contentWidth,
            contentHeight,
            any()
        ) } returns bitmapImproved
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @DisplayName("When download image")
    @Nested
    inner class Download {

        @DisplayName("Then should keep passed size and save the bitmap on cache")
        @Test
        fun keepSizeAndSaveOnCache() = runBlocking {
            // Given
            every { bitmap.width } returns contentWidth
            every { bitmap.height } returns contentHeight

            // When
            val bitmap = imageDownloader.getRemoteImage(url, contentWidth, contentHeight, context)

            // Then
            verify(exactly = 1) { LruImageCache.put(bitmapId, bitmap) }
            verify(exactly = 1) { LruImageCache.get(bitmapId) }
            assertEquals(contentWidth, bitmap?.width)
            assertEquals(contentHeight, bitmap?.height)
        }

        @DisplayName("Then should load bitmap from cache when exists")
        @Test
        fun loadBitmapFromCache() = runBlocking {
            // Given
            every { LruImageCache.get(any()) } returns bitmapImproved

            // When
            val bitmap = imageDownloader.getRemoteImage(url, contentWidth, contentHeight, context)

            // Then
            verify(exactly = 1) { LruImageCache.get(bitmapId) }
            assertEquals(contentWidth, bitmap?.width)
            assertEquals(contentHeight, bitmap?.height)
        }

        @DisplayName("Then should apply options to BitmapFactory")
        @Test
        fun loadBitmapWithOptions() = runBlocking {
            // Given
            val density = 440
            every { context.resources } returns mockk {
                every { displayMetrics } returns mockk {
                    densityDpi = density
                }
            }

            // When
            imageDownloader.getRemoteImage(url, contentWidth, contentHeight, context)

            // Then
            assertEquals(DisplayMetrics.DENSITY_DEFAULT, optionsSlot.captured.inDensity)
            assertEquals(density, optionsSlot.captured.inScreenDensity)
            assertEquals(density, optionsSlot.captured.inTargetDensity)
        }
    }
}

@DisplayName("Given URLFactory")
class ImageDownloaderUrlFactory {

    @DisplayName("When createURL method in URLFactory is called")
    @Nested
    inner class Factory {

        @DisplayName("Then should return a new instance of URL")
        @Test
        fun factoryUrl() {
            // Given
            val url = "https://www.stub.com"

            // When
            val result = URLFactory.createURL(url)

            // Then
            assertEquals("https", result.protocol)
            assertEquals("www.stub.com", result.host)
        }
    }
}
