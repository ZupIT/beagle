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

package br.com.zup.beagle.cache

import br.com.zup.beagle.networking.ResponseData
import br.com.zup.beagle.setup.BeagleEnvironment
import br.com.zup.beagle.store.StoreHandler
import br.com.zup.beagle.store.StoreHandlerFactory
import br.com.zup.beagle.store.StoreType
import br.com.zup.beagle.utils.nanoTimeInSeconds
import br.com.zup.beagle.view.ScreenRequest
import java.lang.NumberFormatException

private const val BEAGLE_HASH = "beagle-hash"
private const val CACHE_CONTROL_HEADER = "cache-control"

internal data class BeagleCache(
    val isHot: Boolean,
    val hash: String,
    val json: String
)

internal class CacheManager(
    private val storeHandler: StoreHandler = StoreHandlerFactory().make(),
    private val timerCacheStore: LruCacheStore = LruCacheStore,
    private val beagleEnvironment: BeagleEnvironment = BeagleEnvironment
) {

    fun restoreBeagleCacheForUrl(url: String): BeagleCache? {
        if (!isEnabled()) {
            return null
        }

        val beagleHashKey = url.toBeagleHashKey()
        val timerCache = timerCacheStore.restore(beagleHashKey)
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
        val cachedData = storeHandler.restore(StoreType.DATABASE, beagleHashKey, beagleJsonKey)
        val beagleHashValue = cachedData[beagleHashKey]
        val beagleJson = cachedData[beagleJsonKey]

        return if (beagleHashValue != null && beagleJson != null) {
            persistCacheOnMemory(url, beagleJson, beagleHashValue, null)

            return BeagleCache(
                isHot = false,
                hash = beagleHashValue,
                json = beagleJson
            )
        } else {
            null
        }
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
            beagleCache.json
        } else {
            return String(responseData.data).apply {
                val beagleHash = responseData.headers[BEAGLE_HASH]
                if (isEnabled() && beagleHash != null) {
                    persistCacheDataOnDisk(url, this, beagleHash)
                    val cacheControl = responseData.headers[CACHE_CONTROL_HEADER]
                    persistCacheOnMemory(url, this, beagleHash, cacheControl)
                }
            }
        }
    }

    private fun persistCacheDataOnDisk(url: String, responseBody: String, beagleHash: String) {
        val cacheKey = url.toBeagleHashKey()
        val data = mapOf(
            cacheKey to beagleHash,
            url.toBeagleJsonKey() to responseBody
        )
        storeHandler.save(StoreType.DATABASE, data)
    }

    private fun persistCacheOnMemory(
        url: String,
        responseBody: String,
        beagleHash: String,
        cacheControl: String?
    ) {
        val cacheKey = url.toBeagleHashKey()
        val maxTime = getMaxAgeFromCacheControl(cacheControl)
        timerCacheStore.save(cacheKey, TimerCache(
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

    private fun String.toBeagleHashKey(): String = "$this#hash"
    private fun String.toBeagleJsonKey(): String = "$this#json"
}