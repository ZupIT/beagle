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
import android.util.LruCache

internal object LruImageCache {

    private val cache: LruCache<String, Bitmap> by lazy {
        LruCache<String, Bitmap>(anEighthOfMemory())
    }

    fun put(key: String, bitmap: Bitmap?) {
        if (bitmap != null) {
            cache.put(key, bitmap)
        }
    }

    fun get(key: String?): Bitmap? = cache.get(key)

    private fun anEighthOfMemory() = ((Runtime.getRuntime().maxMemory() / 1024).toInt() / 8)

    fun generateBitmapId(
        url: String?,
        contentWidth: Int,
        contentHeight: Int
    ) = StringBuilder().append(url).append(contentWidth).append(contentHeight).toString()
}
