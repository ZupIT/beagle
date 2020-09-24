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

package br.com.zup.beagle.android.cache

import android.util.LruCache
import br.com.zup.beagle.android.setup.BeagleEnvironment

internal class LruCacheStore(
    private val cache: LruCache<String, BeagleCache> = LruCache(
        BeagleEnvironment.beagleSdk.config.cache.memoryMaximumCapacity.takeIf { it > 0 }
            ?: BeagleEnvironment.beagleSdk.config.cache.size
    )
) {

    fun save(cacheKey: String, beagleCache: BeagleCache) {
        cache.put(cacheKey, beagleCache)
    }

    fun restore(cacheKey: String): BeagleCache? {
        return cache[cacheKey]
    }


    companion object {
        val instance: LruCacheStore by lazy { LruCacheStore() }
    }
}
