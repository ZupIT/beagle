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

package br.com.zup.beagle.android.store

internal object MemoryLocalStore : LocalStore {

    private val cache = mutableMapOf<String, String>()

    override fun save(key: String, value: String) {
        cache[key] = value
    }

    override fun restore(key: String): String? {
        return cache[key]
    }

    override fun delete(key: String) {
        cache.remove(key)
    }

    override fun getAll(): Map<String, String> {
        return cache.toMap()
    }
}