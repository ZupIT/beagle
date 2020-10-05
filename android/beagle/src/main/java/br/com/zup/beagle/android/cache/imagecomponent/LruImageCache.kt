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

class LruImageCache(private val memoryCache: LruCache<String, Bitmap> =
                            LruCache<String, Bitmap>(anEighthOfMemory())) {

    fun put(key: String, bitmap: Bitmap) {
        memoryCache.put(key, bitmap)
    }

    fun get(key: String?): Bitmap? = memoryCache.get(key)

    companion object {
        val instance: LruImageCache by lazy { LruImageCache() }
    }
}

private fun anEighthOfMemory() : Int = ((Runtime.getRuntime().maxMemory() / 1024).toInt() / 8)
