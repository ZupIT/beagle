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

import android.database.sqlite.SQLiteDatabase
import br.com.zup.beagle.android.setup.BeagleEnvironment
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class StoreHandlerFactoryTest {

    @MockK
    private lateinit var beagleEnvironment: BeagleEnvironment

    private lateinit var storeHandlerFactory: StoreHandlerFactory

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        mockkObject(BeagleDatabaseManager)

        storeHandlerFactory = StoreHandlerFactory(beagleEnvironment)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun make_should_return_default_value_when_storeHandler_is_null() {
        // Given
        val sqliteDatabase = mockk<SQLiteDatabase>()
        every { beagleEnvironment.application } returns mockk()
        every { beagleEnvironment.beagleSdk.storeHandler } returns null
        every { BeagleDatabaseManager.getDatabase(any()) } returns sqliteDatabase

        // When
        val storeHandler = storeHandlerFactory.make()

        // Then
        assertTrue(storeHandler is StoreHandlerDefault)
    }

    @Test
    fun make_should_return_application_storeHandler() {
        // Given
        val appStoreHandler = mockk<StoreHandler>()
        every { beagleEnvironment.beagleSdk.storeHandler } returns appStoreHandler

        // When
        val storeHandler = storeHandlerFactory.make()

        // Then
        assertEquals(appStoreHandler, storeHandler)
    }
}