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
import br.com.zup.beagle.android.utils.CoroutineDispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.URL

internal class ImageDownloader(
    private val cache: LruImageCache,
    private val beagleBitmapFactory: BeagleBitmapFactory = BeagleBitmapFactory()
) {

    suspend fun getRemoteImage(url: String?, contentWidth: Int, contentHeight: Int) : Bitmap? {
        val cacheId = generateId(url, contentWidth, contentHeight)

        return withContext(CoroutineDispatchers.IO) {
            if (hasBitmapOnCacheForId(cacheId)) {
                cache.get(cacheId)
            } else {
                url?.let {
                    downloadBitmap(it, contentWidth, contentHeight).apply {
                        saveOnCache(cacheId, this)
                    }
                }
            }
        }
    }

    private fun downloadBitmap(url: String?, contentWidth: Int, contentHeight: Int) : Bitmap {
        val inputStream: InputStream = URL(url).openStream()
        val bitmap = BitmapFactory.decodeStream(inputStream)

        return beagleBitmapFactory.getBitmap(bitmap, contentWidth, contentHeight)
    }

    private fun saveOnCache(cacheId: String, bitmap: Bitmap?) {
        if (bitmap != null) {
            cache.put(cacheId, bitmap)
        }
    }

    private fun hasBitmapOnCacheForId(url: String?) : Boolean =
        cache.get(url) != null

    private fun generateId(url: String?, contentWidth: Int, contentHeight: Int) = url + contentWidth + contentHeight
}
