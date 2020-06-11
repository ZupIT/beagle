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

internal class StoreHandlerDefault(
    private val memoryLocalStore: MemoryLocalStore = MemoryLocalStore,
    private val databaseLocalStore: DatabaseLocalStore = DatabaseLocalStore()
) : StoreHandler {

    override fun save(storeType: StoreType, data: Map<String, String>) {
        data.forEach {
            if (storeType == StoreType.DATABASE) {
                databaseLocalStore.save(it.key, it.value)
            } else {
                memoryLocalStore.save(it.key, it.value)
            }
        }
    }

    override fun restore(storeType: StoreType, vararg keys: String): Map<String, String?> {
        val values = mutableMapOf<String, String?>()
        keys.forEach {
            val value = if (storeType == StoreType.DATABASE) {
                databaseLocalStore.restore(it)
            } else {
                memoryLocalStore.restore(it)
            }
            values[it] = value
        }
        return values
    }
}