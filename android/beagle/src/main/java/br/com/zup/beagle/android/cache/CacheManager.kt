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

import br.com.zup.beagle.android.networking.RequestData
import br.com.zup.beagle.android.networking.ResponseData
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.store.StoreHandler
import br.com.zup.beagle.android.store.StoreType
import br.com.zup.beagle.android.utils.nanoTimeInSeconds
import br.com.zup.beagle.android.utils.toLowerKeys

private const val BEAGLE_HASH = "beagle-hash"
private const val CACHE_CONTROL_HEADER = "cache-control"
private const val CACHE_KEY_DELIMITER = "#"
private const val CACHE_TIME_KEY = "time"
private const val CACHE_HASH_KEY = "hash"
private const val CACHE_JSON_KEY = "json"

internal data class BeagleCache(
    val hash: String,
    val json: String,
    val maxTime: Long,
    val cachedTime: Long,
) {
    fun isExpired(): Boolean {
        val stepTime = nanoTimeInSeconds() - cachedTime
        return stepTime > maxTime
    }
}

internal class CacheManager(
    private val storeHandler: StoreHandler? = BeagleEnvironment.beagleSdk.storeHandler,
    private val beagleEnvironment: BeagleEnvironment = BeagleEnvironment,
    private val memoryCacheStore: LruCacheStore? =
        if (beagleEnvironment.beagleSdk.config.cache.enabled) LruCacheStore.instance else null,
) {

    fun restoreBeagleCacheForUrl(url: String): BeagleCache? {
        if (!isCacheEnabled()) {
            return null
        }

        val beagleHashKey = url.toBeagleHashKey()
        return getBeagleCacheFromMemory(beagleHashKey) ?: restoreCacheFromDisk(url, beagleHashKey)
    }

    private fun getBeagleCacheFromMemory(beagleHashKey: String): BeagleCache? {
        return memoryCacheStore?.restore(beagleHashKey)
    }

    private fun restoreCacheFromDisk(url: String, beagleHashKey: String): BeagleCache? {

        val beagleCache = getBeagleCacheFromDisk(url, beagleHashKey)

        return when {
            beagleCache == null -> {
                null
            }
            beagleCache.isExpired() -> {
                deleteDiskCacheForUrl(url)
                null
            }
            else -> {
                beagleCache
            }
        }
    }

    private fun getBeagleCacheFromDisk(url: String, beagleHashKey: String): BeagleCache? {
        val beagleJsonKey = url.toBeagleJsonKey()
        val beagleTimeKey = url.toBeagleTimeKey()

        val cachedData = storeHandler!!.restore(StoreType.DATABASE, beagleHashKey, beagleJsonKey, beagleTimeKey)

        val beagleHash = cachedData[beagleHashKey]
        val beagleJson = cachedData[beagleJsonKey]
        val beagleCachedTime = cachedData[beagleTimeKey]?.toLong() ?: 0

        val maxTime = getMaxAgeFromCacheControl(null)

        return if (beagleHash != null && beagleJson != null) {
            BeagleCache(
                hash = beagleHash,
                json = beagleJson,
                maxTime = maxTime,
                cachedTime = beagleCachedTime
            )
        } else {
            null
        }
    }

    fun requestDataWithCache(
        requestData: RequestData,
        beagleCache: BeagleCache?,
    ): RequestData {
        return if (beagleCache != null) {
            var headers = requestData.httpAdditionalData.headers ?: hashMapOf()
            headers = headers.toMutableMap().apply {
                put(BEAGLE_HASH, beagleCache.hash)
            }
            val httpAdditionalData = requestData.httpAdditionalData.copy(headers = headers)
            requestData.copy(headers = headers, httpAdditionalData = httpAdditionalData)
        } else {
            requestData
        }
    }

    fun handleResponseData(
        url: String,
        beagleCache: BeagleCache?,
        responseData: ResponseData,
    ): String {
        return if (responseData.statusCode == 304 && beagleCache != null) {
            persistCacheOnMemory(url, beagleCache.json, beagleCache.hash, null)
            persistCacheDataOnDisk(url, beagleCache.json, beagleCache.hash)
            beagleCache.json
        } else {
            val headers = responseData.headers.toLowerKeys()
            return String(responseData.data).apply {
                val beagleHash = headers[BEAGLE_HASH]
                if (isCacheEnabled() && beagleHash != null) {
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
        storeHandler!!.save(StoreType.DATABASE, data)

        clearOldCacheDataOnDisk()
    }

    private fun clearOldCacheDataOnDisk() {
        val cacheMaxRegisters = beagleEnvironment.beagleSdk.config.cache.memoryMaximumCapacity.takeIf { it > 0 }
            ?: beagleEnvironment.beagleSdk.config.cache.size

        val cachedKeys = storeHandler!!.getAll(StoreType.DATABASE)
        val cachedTimeKeys = cachedKeys.filter { mapEntry ->
            mapEntry.key.contains(CACHE_TIME_KEY)
        }

        if (cachedTimeKeys.size > cacheMaxRegisters) {
            val orderedCachedTimeKeysList = cachedTimeKeys.toList().sortedByDescending { (_, value) -> value }
            for (index in cacheMaxRegisters until orderedCachedTimeKeysList.size) {
                val url = orderedCachedTimeKeysList[index].first.split(CACHE_KEY_DELIMITER)[0]
                deleteDiskCacheForUrl(url)
            }
        }
    }

    private fun deleteDiskCacheForUrl(url: String) {
        val cacheKey = url.toBeagleHashKey()
        val jsonKey = url.toBeagleJsonKey()
        val timeKey = url.toBeagleTimeKey()
        storeHandler!!.delete(StoreType.DATABASE, cacheKey)
        storeHandler.delete(StoreType.DATABASE, jsonKey)
        storeHandler.delete(StoreType.DATABASE, timeKey)
    }

    private fun persistCacheOnMemory(
        url: String,
        responseBody: String,
        beagleHash: String,
        cacheControl: String?,
    ) {
        val cacheKey = url.toBeagleHashKey()
        val maxTime = getMaxAgeFromCacheControl(cacheControl)
        memoryCacheStore?.save(cacheKey, BeagleCache(
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

    private fun isCacheEnabled(): Boolean = beagleEnvironment.beagleSdk.config.cache.enabled && storeHandler != null

    private fun String.toBeagleHashKey(): String = "$this$CACHE_KEY_DELIMITER$CACHE_HASH_KEY"
    private fun String.toBeagleJsonKey(): String = "$this$CACHE_KEY_DELIMITER$CACHE_JSON_KEY"
    private fun String.toBeagleTimeKey(): String = "$this$CACHE_KEY_DELIMITER$CACHE_TIME_KEY"
}