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

import br.com.zup.beagle.android.utils.CacheDeprecatedConstants

enum class Environment {
    DEBUG,
    PRODUCTION
}

data class Cache(
    val enabled: Boolean,
    val maxAge: Long,
    @Deprecated(CacheDeprecatedConstants.MEMORY_MAXIMUM_CAPACITY, replaceWith = ReplaceWith("size"))
    val memoryMaximumCapacity: Int = 0,
    val size: Int = 0
) {
    @Deprecated(CacheDeprecatedConstants.MEMORY_MAXIMUM_CAPACITY,
        replaceWith = ReplaceWith("Cache(enabled, maxAge, size=your_cache_size)"))
    constructor(enabled: Boolean, maxAge: Long, memoryMaximumCapacity: Int) :
        this(enabled, maxAge, memoryMaximumCapacity, 0)
}

interface BeagleConfig {
    val environment: Environment
    val baseUrl: String
    val cache: Cache
    val isLoggingEnabled: Boolean
}
