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

package br.com.zup.beagle.android.setup

/**
 * Enum responsible for informing Beagle about the current build status of the application.
 */
enum class Environment {
    /**
     * The debug mode has much more information available so that the debugging software can further help the
     * programmer to debug their code.
     */
    DEBUG,

    /**
     * Production mode provides more information about the software.
     */
    PRODUCTION
}

/**
 * Object responsible for managing the cache of Beagle requests.
 *
 * @param enabled Enables or disables memory and disk caching.
 * @param maxAge Time in seconds that memory cache will live.
 * @param memoryMaximumCapacity Memory LRU cache size. It represents number of screens that will be in memory.
 * @param size Memory LRU cache size.
 */
data class Cache(
    val enabled: Boolean,
    val maxAge: Long,
    @Deprecated("It was deprecated in version 1.2.2 and will be removed in a future version." +
        " Use size instead.",
        replaceWith = ReplaceWith("size"))
    val memoryMaximumCapacity: Int = 0,
    val size: Int = 0,
) {
    @Deprecated("It was deprecated in version 1.2.2 and will be removed in a future version.",
        replaceWith = ReplaceWith("Cache(enabled, maxAge, size=your_cache_size)"))
    constructor(enabled: Boolean, maxAge: Long, memoryMaximumCapacity: Int) :
        this(enabled, maxAge, memoryMaximumCapacity, 0)
}

/**
 * Interface that provides initial beagle configuration attributes.
 */
interface BeagleConfig {
    /**
     * Attribute responsible for informing Beagle about the current build status of the application.
     */
    val environment: Environment

    /**
     * Informs the base URL used in Beagle in the application.
     */
    val baseUrl: String

    /**
     * Object responsible for managing the cache of Beagle requests.
     */
    val cache: Cache

    /**
     * Attribute that enables or disables all logs that Beagle generates.
     */
    val isLoggingEnabled: Boolean
}
