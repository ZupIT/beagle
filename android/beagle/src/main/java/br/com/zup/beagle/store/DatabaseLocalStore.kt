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

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import br.com.zup.beagle.logger.BeagleMessageLogs
import br.com.zup.beagle.setup.BeagleEnvironment

internal object ScreenEntry : BaseColumns {
    const val TABLE_NAME = "KeyValueCache"
    const val KEY_COLUMN_NAME = "key"
    const val VALUE_COLUMN_NAME = "value"
}

internal class DatabaseLocalStore(
    private val contentValuesFactory: ContentValuesFactory = ContentValuesFactory(),
    private val database: SQLiteDatabase = BeagleDatabaseManager.getDatabase(
        BeagleEnvironment.application
    )
) : LocalStore {

    override fun save(key: String, value: String) {
        val values = contentValuesFactory.make().apply {
            put(ScreenEntry.KEY_COLUMN_NAME, key)
            put(ScreenEntry.VALUE_COLUMN_NAME, value)
        }

        val restoredValue = restore(key)
        if (restoredValue == null) {
            val newRowId = database.insert(ScreenEntry.TABLE_NAME, null, values)
            if (newRowId == -1L) {
                BeagleMessageLogs.logDataNotInsertedOnDatabase(key, value)
            }
        } else if (value != restoredValue) {
            database.update(ScreenEntry.TABLE_NAME, values, "${ScreenEntry.KEY_COLUMN_NAME}=?", arrayOf(key))
        }
    }

    override fun restore(key: String): String? {
        return executeRestoreQueryForKey(key).use { cursor ->
            if (cursor.count > 0) {
                cursor.moveToFirst()
                cursor.getString(cursor.getColumnIndexOrThrow(ScreenEntry.VALUE_COLUMN_NAME))
            } else {
                null
            }
        }
    }

    private fun executeRestoreQueryForKey(key: String): Cursor {
        val columnsToReturn = arrayOf(ScreenEntry.VALUE_COLUMN_NAME)
        val columnsForWhereClause = "${ScreenEntry.KEY_COLUMN_NAME}=?"
        val valuesForWhereClause = arrayOf(key)
        return database.query(
            ScreenEntry.TABLE_NAME,
            columnsToReturn,
            columnsForWhereClause,
            valuesForWhereClause,
            null,
            null,
            null
        )
    }
}

internal class ContentValuesFactory {
    fun make(): ContentValues = ContentValues()
}

internal object BeagleDatabaseManager {

    private const val DATABASE_NAME = "BeagleDefaultStore.db"
    private const val DATABASE_VERSION = 1

    private lateinit var database: SQLiteDatabase

    fun getDatabase(context: Context): SQLiteDatabase {
        if (!::database.isInitialized) {
            database = BeagleSQLiteDatabase(
                context,
                DATABASE_NAME,
                DATABASE_VERSION
            ).writableDatabase
        }
        return database
    }
}

internal open class BeagleSQLiteDatabase(
    context: Context,
    databaseName: String,
    databaseVersion: Int
) : SQLiteOpenHelper(
    context,
    databaseName,
    null,
    databaseVersion
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE ${ScreenEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${ScreenEntry.KEY_COLUMN_NAME} TEXT NOT NULL UNIQUE," +
            "${ScreenEntry.VALUE_COLUMN_NAME} TEXT NOT NULL" +
        ")"
        db?.execSQL(createTableQuery)
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val deleteTableQuery = "DROP TABLE IF EXISTS ${ScreenEntry.TABLE_NAME}"
        db?.execSQL(deleteTableQuery)
        onCreate(db)
    }
}