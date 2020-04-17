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

package br.com.zup.beagle.store

import br.com.zup.beagle.extensions.once
import br.com.zup.beagle.testutil.RandomData
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class StoreHandlerDefaultTest {

    @MockK
    private lateinit var memoryLocalStore: MemoryLocalStore
    @MockK
    private lateinit var databaseLocalStore: DatabaseLocalStore

    private lateinit var storeHandlerDefault: StoreHandlerDefault

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        storeHandlerDefault = StoreHandlerDefault(memoryLocalStore, databaseLocalStore)
    }

    @Test
    fun save_should_call_database_store_when_type_is_DATABASE() {
        // Given
        val dataKey = RandomData.string(3)
        val dataValue = RandomData.string()
        val data = mapOf(dataKey to dataValue)
        every { databaseLocalStore.save(any(), any()) } just Runs

        // When
        storeHandlerDefault.save(StoreType.DATABASE, data)

        // Then
        verify(exactly = once()) { databaseLocalStore.save(dataKey, dataValue) }
    }

    @Test
    fun save_should_call_memory_store_when_type_is_MEMORY() {
        // Given
        val dataKey = RandomData.string(3)
        val dataValue = RandomData.string()
        val data = mapOf(dataKey to dataValue)
        every { memoryLocalStore.save(any(), any()) } just Runs

        // When
        storeHandlerDefault.save(StoreType.MEMORY, data)

        // Then
        verify(exactly = once()) { memoryLocalStore.save(dataKey, dataValue) }
    }

    @Test
    fun restore_should_call_database_store_when_type_is_DATABASE() {
        // Given
        val dataKey = RandomData.string(3)
        val dataValue = RandomData.string()
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
        val dataKey = RandomData.string(3)
        val dataValue = RandomData.string()
        every { memoryLocalStore.restore(dataKey) } returns dataValue

        // When
        val value = storeHandlerDefault.restore(StoreType.MEMORY, dataKey)

        // Then
        assertEquals(dataKey, value.keys.first())
        assertEquals(dataValue, value.values.first())
    }
}