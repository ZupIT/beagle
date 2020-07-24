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

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.store.logger.BeagleStoreLogs
import br.com.zup.beagle.android.testutil.RandomData
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

private val DATA_KEY = RandomData.string()

class DatabaseLocalStoreTest {

    @MockK
    private lateinit var database: SQLiteDatabase

    @MockK
    private lateinit var cursor: Cursor

    @MockK
    private lateinit var contentValuesFactory: ContentValuesFactory

    @MockK
    private lateinit var contentValues: ContentValues

    private lateinit var databaseLocalStore: DatabaseLocalStore

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        databaseLocalStore = DatabaseLocalStore(contentValuesFactory, database)

        mockkObject(BeagleStoreLogs)

        every { BeagleStoreLogs.logDataNotInsertedOnDatabase(any(), any()) } just Runs
        every { contentValuesFactory.make() } returns contentValues
        every { contentValues.put(any(), any<String>()) } just Runs
        every { database.query(any(), any(), any(), any(), any(), any(), any()) } returns cursor
        every { cursor.count } returns 0
        every { cursor.close() } just Runs
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun save_should_add_new_data_on_database() {
        // Given
        val key = RandomData.string()
        val value = RandomData.string()
        val tableNameSlot = slot<String>()
        every { database.insert(capture(tableNameSlot), any(), any()) } returns 1

        // When
        databaseLocalStore.save(key, value)

        // Then
        val actualTableName = tableNameSlot.captured
        verify(exactly = once()) { database.insert(actualTableName, null, contentValues) }
        verify(exactly = 0) { BeagleStoreLogs.logDataNotInsertedOnDatabase(key, value) }
        assertEquals(ScreenEntry.TABLE_NAME, actualTableName)
    }

    @Test
    fun save_should_log_error_when_return_value_is_minus_1() {
        // Given
        val key = RandomData.string()
        val value = RandomData.string()
        every { database.insert(any(), any(), any()) } returns -1

        // When
        databaseLocalStore.save(key, value)

        // Then
        verify(exactly = once()) { BeagleStoreLogs.logDataNotInsertedOnDatabase(key, value) }
    }

    @Test
    fun restore_should_return_value_when_query_find_key() {
        // Given
        val value = RandomData.string()
        every { cursor.count } returns 1
        every { cursor.moveToFirst() } returns true
        every { cursor.getColumnIndexOrThrow(ScreenEntry.VALUE_COLUMN_NAME) } returns 0
        every { cursor.getString(any()) } returns value

        // When
        val actualValue = databaseLocalStore.restore(DATA_KEY)

        // Then
        assertEquals(actualValue, value)
    }

    @Test
    fun restore_should_return_null_value_when_query_doe_not_find_key() {
        // Given  When
        val value = databaseLocalStore.restore(DATA_KEY)

        // Then
        assertNull(value)
    }

    @Test
    fun restore_should_query_database_by_key_column_name() {
        // Given
        val tableNameSlot = slot<String>()
        val columnsToReturnSlot = slot<Array<String>>()
        val columnsForWhereSlot = slot<String>()
        val valuesForWhereClauseSlot = slot<Array<String>>()
        every {
            database.query(
                capture(tableNameSlot),
                capture(columnsToReturnSlot),
                capture(columnsForWhereSlot),
                capture(valuesForWhereClauseSlot),
                any(),
                any(),
                any()
            )
        } returns cursor


        // When
        databaseLocalStore.restore(DATA_KEY)

        // Then
        assertEquals(ScreenEntry.TABLE_NAME, tableNameSlot.captured)
        assertEquals(ScreenEntry.VALUE_COLUMN_NAME, columnsToReturnSlot.captured[0])
        assertEquals("${ScreenEntry.KEY_COLUMN_NAME}=?", columnsForWhereSlot.captured)
        assertEquals(DATA_KEY, valuesForWhereClauseSlot.captured[0])
    }
}