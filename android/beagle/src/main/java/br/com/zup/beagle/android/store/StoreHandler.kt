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

/**
 * Define the type of Store.
 */
enum class StoreType {
    /**
     * Represents disk.
     */
    DATABASE,

    /**
     * Represents memory.
     */
    MEMORY
}

interface StoreHandler {
    /**
     * Save the content in store.
     */
    fun save(storeType: StoreType, data: Map<String, String>)

    /**
     * Get content by keys
     */
    fun restore(storeType: StoreType, vararg keys: String): Map<String, String?>

    /**
     * Delete content has in store.
     */
    fun delete(storeType: StoreType, key: String)

    /**
     * Responsible to get all content
     */
    fun getAll(storeType: StoreType): Map<String, String>
}