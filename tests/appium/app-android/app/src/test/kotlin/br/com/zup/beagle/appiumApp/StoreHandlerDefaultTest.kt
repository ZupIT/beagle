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

package br.com.zup.beagle.appiumApp

import br.com.zup.beagle.android.store.StoreType
import br.com.zup.beagle.appiumApp.config.DatabaseLocalStore
import br.com.zup.beagle.appiumApp.config.MemoryLocalStore
import br.com.zup.beagle.appiumApp.config.StoreHandlerDefault
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class StoreHandlerDefaultTest {

    @MockK
    private lateinit var memoryLocalStore: MemoryLocalStore
    @MockK
    private lateinit var databaseLocalStore: DatabaseLocalStore

    private lateinit var storeHandlerDefault: StoreHandlerDefault

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)

        storeHandlerDefault = StoreHandlerDefault(memoryLocalStore, databaseLocalStore)
    }

    @Test
    fun save_should_call_database_store_when_type_is_DATABASE() {
        // Given
        val dataKey = "key"
        val dataValue = "value"
        val data = mapOf(dataKey to dataValue)
        every { databaseLocalStore.save(any(), any()) } just Runs

        // When
        storeHandlerDefault.save(StoreType.DATABASE, data)

        // Then
        verify(exactly = 1) { databaseLocalStore.save(dataKey, dataValue) }
    }

    @Test
    fun save_should_call_memory_store_when_type_is_MEMORY() {
        // Given
        val dataKey = "key"
        val dataValue = "value"
        val data = mapOf(dataKey to dataValue)
        every { memoryLocalStore.save(any(), any()) } just Runs

        // When
        storeHandlerDefault.save(StoreType.MEMORY, data)

        // Then
        verify(exactly = 1) { memoryLocalStore.save(dataKey, dataValue) }
    }

    @Test
    fun restore_should_call_database_store_when_type_is_DATABASE() {
        // Given
        val dataKey = "key"
        val dataValue = "value"
        every { databaseLocalStore.restore(dataKey) } returns dataValue

        // When
        val value = storeHandlerDefault.restore(StoreType.DATABASE, dataKey)

        // Then
        assertEquals(dataKey, value.keys.first())
        assertEquals(dataValue, value.values.first())
    }

    @Test
    fun restore_should_call_memory_store_when_type_is_MEMORY() {
        // Given
        val dataKey = "key"
        val dataValue = "value"
        every { memoryLocalStore.restore(dataKey) } returns dataValue

        // When
        val value = storeHandlerDefault.restore(StoreType.MEMORY, dataKey)

        // Then
        assertEquals(dataKey, value.keys.first())
        assertEquals(dataValue, value.values.first())
    }
}