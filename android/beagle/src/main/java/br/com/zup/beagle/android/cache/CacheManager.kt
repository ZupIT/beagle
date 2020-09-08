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

import br.com.zup.beagle.android.networking.ResponseData
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.store.StoreHandler
import br.com.zup.beagle.android.store.StoreHandlerFactory
import br.com.zup.beagle.android.store.StoreType
import br.com.zup.beagle.android.utils.nanoTimeInSeconds
import br.com.zup.beagle.android.utils.toLowerKeys
import br.com.zup.beagle.android.view.ScreenRequest

private const val BEAGLE_HASH = "beagle-hash"
private const val CACHE_CONTROL_HEADER = "cache-control"
private const val CACHE_KEY_DELIMITER = "#"
private const val CACHE_TIME_KEY = "time"
private const val CACHE_HASH_KEY = "hash"
private const val CACHE_JSON_KEY = "json"

internal data class BeagleCache(
    val isHot: Boolean,
    val hash: String,
    val json: String
)

internal class CacheManager(
    private val storeHandler: StoreHandler = StoreHandlerFactory().make(),
    private val beagleEnvironment: BeagleEnvironment = BeagleEnvironment,
    private val timerCacheStore: LruCacheStore? =
        if (beagleEnvironment.beagleSdk.config.cache.enabled) LruCacheStore.instance else null
) {

    fun restoreBeagleCacheForUrl(url: String): BeagleCache? {
        if (!isEnabled()) {
            return null
        }

        val beagleHashKey = url.toBeagleHashKey()
        val timerCache = timerCacheStore?.restore(beagleHashKey)
        return if (timerCache != null) {
            checkMemoryCacheAndReturn(timerCache)
        } else {
            restoreCacheFromDisk(url, beagleHashKey)
        }
    }

    private fun checkMemoryCacheAndReturn(timerCache: TimerCache): BeagleCache {
        return BeagleCache(
            isHot = timerCache.isValid(),
            hash = timerCache.hash,
            json = timerCache.json
        )
    }

    private fun restoreCacheFromDisk(url: String, beagleHashKey: String): BeagleCache? {
        val beagleJsonKey = url.toBeagleJsonKey()
        val beagleTimeKey = url.toBeagleTimeKey()
        val cachedData = storeHandler.restore(StoreType.DATABASE, beagleHashKey, beagleJsonKey, beagleTimeKey)
        val beagleHashValue = cachedData[beagleHashKey]
        val beagleJson = cachedData[beagleJsonKey]
        val beagleCachedTime = cachedData[beagleTimeKey]

        return if (!isCacheValid(beagleCachedTime)) {
            deleteDiskCacheForUrl(url)
            null
        } else if (beagleHashValue != null && beagleJson != null) {
            BeagleCache(
                isHot = false,
                hash = beagleHashValue,
                json = beagleJson
            )
        } else {
            null
        }
    }

    private fun isCacheValid(cacheTime: String?): Boolean {
        val cacheTimeInNano = cacheTime?.toLong() ?: 0
        val maxTime = getMaxAgeFromCacheControl(null)
        val stepTime = nanoTimeInSeconds() - cacheTimeInNano

        return stepTime < maxTime
    }

    fun screenRequestWithCache(
        screenRequest: ScreenRequest,
        beagleCache: BeagleCache?
    ): ScreenRequest {
        return if (beagleCache != null) {
            val headers = screenRequest.headers.toMutableMap().apply {
                put(BEAGLE_HASH, beagleCache.hash)
            }
            screenRequest.copy(headers = headers)
        } else {
            screenRequest
        }
    }

    fun handleResponseData(
        url: String,
        beagleCache: BeagleCache?,
        responseData: ResponseData
    ): String {
        return if (responseData.statusCode == 304 && beagleCache != null) {
            persistCacheOnMemory(url, beagleCache.json, beagleCache.hash, null)
            beagleCache.json
        } else {
            val headers = responseData.headers.toLowerKeys()
            return String(responseData.data).apply {
                val beagleHash = headers[BEAGLE_HASH]
                if (isEnabled() && beagleHash != null) {
                    persistCacheDataOnDisk(url, this, beagleHash)
                    val cacheControl = headers[CACHE_CONTROL_HEADER]
                    persistCacheOnMemory(url, this, beagleHash, cacheControl)
                }
            }
        }
    }

    private fun persistCacheDataOnDisk(url: String, responseBody: String, beagleHash: String) {
        val cacheKey = url.toBeagleHashKey()
        val jsonKey = url.toBeagleJsonKey()
        val timeKey = url.toBeagleTimeKey()
        val data = mapOf(
            cacheKey to beagleHash,
            jsonKey to responseBody,
            timeKey to nanoTimeInSeconds().toString()
        )
        storeHandler.save(StoreType.DATABASE, data)

        clearOldCacheDataOnDisk()
    }

    private fun clearOldCacheDataOnDisk() {
        val cacheMaxRegisters = beagleEnvironment.beagleSdk.config.cache.memoryMaximumCapacity
        val cachedKeys = storeHandler.getAll(StoreType.DATABASE)
        val timeKeys = cachedKeys.filter { mapEntry ->
            mapEntry.key.contains(CACHE_TIME_KEY)
        }

        if (timeKeys.size > cacheMaxRegisters) {
            val orderedTimeKeysList = timeKeys.toList().sortedByDescending { (_, value) -> value }
            for (index in cacheMaxRegisters..orderedTimeKeysList.size) {
                val url = orderedTimeKeysList[index].first.split(CACHE_KEY_DELIMITER)[0]
                deleteDiskCacheForUrl(url)
            }
        }
    }

    private fun deleteDiskCacheForUrl(url: String){
        val cacheKey = url.toBeagleHashKey()
        val jsonKey = url.toBeagleJsonKey()
        val timeKey = url.toBeagleTimeKey()
        storeHandler.delete(StoreType.DATABASE, cacheKey)
        storeHandler.delete(StoreType.DATABASE, jsonKey)
        storeHandler.delete(StoreType.DATABASE, timeKey)
    }

    private fun persistCacheOnMemory(
        url: String,
        responseBody: String,
        beagleHash: String,
        cacheControl: String?
    ) {
        val cacheKey = url.toBeagleHashKey()
        val maxTime = getMaxAgeFromCacheControl(cacheControl)
        timerCacheStore?.save(cacheKey, TimerCache(
            maxTime = maxTime,
            cachedTime = nanoTimeInSeconds(),
            hash = beagleHash,
            json = responseBody
        ))
    }

    private fun getMaxAgeFromCacheControl(cacheControl: String?): Long {
        val maxAgeName = "max-age"
        val maxAge = cacheControl?.split(",")?.find { it.contains(maxAgeName) } ?: cacheControl ?: ""
        return try {
            maxAge.replace("$maxAgeName=", "").trim().toLong()
        } catch (ex: NumberFormatException) {
            beagleEnvironment.beagleSdk.config.cache.maxAge
        }
    }

    private fun isEnabled(): Boolean = beagleEnvironment.beagleSdk.config.cache.enabled

    private fun String.toBeagleHashKey(): String = "$this$CACHE_KEY_DELIMITER$CACHE_HASH_KEY"
    private fun String.toBeagleJsonKey(): String = "$this$CACHE_KEY_DELIMITER$CACHE_JSON_KEY"
    private fun String.toBeagleTimeKey(): String = "$this$CACHE_KEY_DELIMITER$CACHE_TIME_KEY"
}