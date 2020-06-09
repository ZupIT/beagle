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

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito.spy

class BeagleSQLiteDatabaseTest {

    private val querySlot = mutableListOf<String>()

    @MockK
    private lateinit var context: Context
    @MockK
    private lateinit var sqLiteDatabase: SQLiteDatabase

    private lateinit var beagleSQLiteDatabase: BeagleSQLiteDatabase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        beagleSQLiteDatabase = BeagleSQLiteDatabase(context, "", 0)

        every { sqLiteDatabase.execSQL(capture(querySlot)) } just Runs
    }

    @Test
    fun onCreate_should_call_database_to_create_tables() {
        // Given When
        beagleSQLiteDatabase.onCreate(sqLiteDatabase)

        // Then
        val actualQuery = querySlot[0]
        verify(exactly = 1) { sqLiteDatabase.execSQL(actualQuery) }
        val expectedQuery = "CREATE TABLE ${ScreenEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${ScreenEntry.KEY_COLUMN_NAME} TEXT NOT NULL UNIQUE," +
            "${ScreenEntry.VALUE_COLUMN_NAME} TEXT NOT NULL" +
        ")"
        assertEquals(expectedQuery, actualQuery)
    }

    @Test
    fun onUpgrade_should_drop_existing_tables_and_create_again() {
        // Given
        val spyDatabase = spy(beagleSQLiteDatabase)

        // When
        spyDatabase.onUpgrade(sqLiteDatabase, 1, 1)

        // Then
        val actualQuery = querySlot[0]
        verify(exactly = 1) { sqLiteDatabase.execSQL(actualQuery) }
        verify(exactly = 1) { spyDatabase.onCreate(sqLiteDatabase) }
        val expectedQuery = "DROP TABLE IF EXISTS ${ScreenEntry.TABLE_NAME}"
        assertEquals(expectedQuery, actualQuery)
    }
}