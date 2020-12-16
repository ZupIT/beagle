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

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import br.com.zup.beagle.appiumApp.config.BeagleMessageLogs
import br.com.zup.beagle.appiumApp.config.ContentValuesFactory
import br.com.zup.beagle.appiumApp.config.DatabaseLocalStore
import br.com.zup.beagle.appiumApp.config.ScreenEntry
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.Assert.*

private const val DATA_KEY = "data_key"

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

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)

        databaseLocalStore = DatabaseLocalStore(contentValuesFactory, database)

        mockkObject(BeagleMessageLogs)

        every { BeagleMessageLogs.logDataNotInsertedOnDatabase(any(), any()) } just Runs
        every { contentValuesFactory.make() } returns contentValues
        every { contentValues.put(any(), any<String>()) } just Runs
        every { database.query(any(), any(), any(), any(), any(), any(), any()) } returns cursor
        every { cursor.count } returns 0
        every { cursor.close() } just Runs
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `save should add new data on database`() {
        // Given
        val key = "key"
        val value = "value"
        val tableNameSlot = slot<String>()
        every { database.insertWithOnConflict(capture(tableNameSlot), any(), any(), any()) } returns 1

        // When
        databaseLocalStore.save(key, value)

        // Then
        val actualTableName = tableNameSlot.captured
        verify(exactly = 1) { database.insertWithOnConflict(actualTableName, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE) }
        assertEquals(ScreenEntry.TABLE_NAME, actualTableName)
        verify(exactly = 0) { BeagleMessageLogs.logDataNotInsertedOnDatabase(key, value) }
    }

    @Test
    fun `save should log error when return value is minus 1`() {
        // Given
        val key = "key"
        val value = "value"
        every { database.insertWithOnConflict(any(), any(), any(), any()) } returns -1

        // When
        databaseLocalStore.save(key, value)

        // Then
        verify(exactly = 1) { BeagleMessageLogs.logDataNotInsertedOnDatabase(key, value) }
    }


    @Test
    fun `restore should return value when query find key`() {
        // Given
        val value = "value"
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
    fun `restore should return null value when query doe not find key`() {
        // Given  When
        val value = databaseLocalStore.restore(DATA_KEY)

        // Then
        assertNull(value)
    }

    @Test
    fun `restore should query database by key column name`() {
        // Given
        val tableNameSlot = slot<String>()
        val columnsToReturnSlot = slot<Array<String>>()
        val columnsForWhereSlot = slot<String>()
        val valuesForWhereClauseSlot = slot<Array<String>>()
        every { database.query(
            capture(tableNameSlot),
            capture(columnsToReturnSlot),
            capture(columnsForWhereSlot),
            capture(valuesForWhereClauseSlot),
            any(),
            any(),
            any()
        ) } returns cursor


        // When
        databaseLocalStore.restore(DATA_KEY)

        // Then
        assertEquals(ScreenEntry.TABLE_NAME, tableNameSlot.captured)
        assertEquals(ScreenEntry.VALUE_COLUMN_NAME, columnsToReturnSlot.captured[0])
        assertEquals("${ScreenEntry.KEY_COLUMN_NAME}=?", columnsForWhereSlot.captured)
        assertEquals(DATA_KEY, valuesForWhereClauseSlot.captured[0])
    }
}