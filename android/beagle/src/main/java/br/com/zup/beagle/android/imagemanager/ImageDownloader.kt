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

package br.com.zup.beagle.android.imagemanager

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import br.com.zup.beagle.android.utils.CoroutineDispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.URL


internal class ImageDownloader {
    suspend fun getRemoteImage(url: String?, contentWidth: Int, contentHeight: Int) : Bitmap {
        val cache= LruImagesCache.instance.memoryCache

        return withContext(CoroutineDispatchers.IO) {
            if (hasCache(url)) {
                cache.get(url)
            } else {
                val inputStream: InputStream = URL(url).openStream()
                val bitmapOptimized = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(inputStream), contentWidth, contentHeight, true)
                cache.put(url, bitmapOptimized)
                bitmapOptimized
            }
        }
    }

    private fun hasCache(url: String?) = LruImagesCache.instance.memoryCache.get(url) != null
}