/*
 * Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
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

    private fun checkMemoryCacheAndReturn(timerCache: TimerCache): BeagleCache? {
        return if (timerCache.isValid()) {
            return BeagleCache(
                hash = timerCache.hash,
                json = timerCache.json
            )
        } else {
            null
        }
    }

    private fun restoreCacheFromDisk(url: String, beagleHashKey: String): BeagleCache? {
        val beagleJsonKey = url.toBeagleJsonKey()
        val cachedData = storeHandler.restore(StoreType.DATABASE, beagleHashKey, beagleJsonKey)
        val beagleHashValue = cachedData[beagleHashKey]
        val beagleJson = cachedData[beagleJsonKey]

        return if (beagleHashValue != null && beagleJson != null) {
            return BeagleCache(
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
            val responseBody = String(responseData.data)
            if (isEnabled()) {
                persistCacheData(url, responseData)
            }
            responseBody
        }
    }

    private fun persistCacheData(url: String, responseData: ResponseData) {
        val responseBody = String(responseData.data)
        val newBeagleHash = responseData.headers[BEAGLE_HASH]
        if (newBeagleHash != null) {
            val cacheKey = url.toBeagleHashKey()
            val data = mapOf(
                cacheKey to newBeagleHash,
                url.toBeagleJsonKey() to responseBody
            )
            storeHandler.save(StoreType.DATABASE, data)
            val maxTime = getMaxAgeFromCacheControl(responseData.headers[CACHE_CONTROL_HEADER])
            timerCacheStore.save(cacheKey, TimerCache(
                maxTime = maxTime,
                cachedTime = nanoTimeInSeconds(),
                hash = newBeagleHash,
                json = responseBody
            ))
        }
    }

    private fun getMaxAgeFromCacheControl(cacheControl: String?): Long {
        val maxAgeName = "max-age"
        val maxAge = cacheControl?.split(",")?.find { it == maxAgeName } ?: ""
        return try {
            maxAge.replace("$maxAgeName=", "").toLong()
        } catch (ex: NumberFormatException) {
            beagleEnvironment.beagleSdk.config.cache.maxAge
        }
    }

    private fun isEnabled(): Boolean = beagleEnvironment.beagleSdk.config.cache.enabled

    private fun String.toBeagleHashKey(): String = "$this#hash"
    private fun String.toBeagleJsonKey(): String = "$this#json"
}