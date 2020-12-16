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

import br.com.zup.beagle.android.data.serializer.adapter.generic.BeagleTypeAdapter
import br.com.zup.beagle.android.setup.BeagleSdk
import br.com.zup.beagle.appiumApp.adapters.Person
import br.com.zup.beagle.appiumApp.adapters.PersonAdapter
import br.com.zup.beagle.appiumApp.adapters.PersonImpl
import br.com.zup.beagle.appiumApp.config.BeagleSQLiteDatabase
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BeagleSetupTest {

    private lateinit var beagleSetup: BeagleSetup

    @BeforeEach
    fun setUp() {
        mockkConstructor(BeagleSQLiteDatabase::class)

        every { anyConstructed<BeagleSQLiteDatabase>().writableDatabase } returns mockk()
        AppApplication.APPLICATION = mockk()
        beagleSetup = BeagleSetup()
    }

    @Test
    fun beagleSetup_should_be_instance_of_BeagleSdk() {
        assertTrue(beagleSetup is BeagleSdk)
    }

    @Test
    fun registeredWidgets_should_have_3_elements_in_list() {
        assertTrue(beagleSetup.registeredWidgets().isNotEmpty())
    }

    @Test
    fun formLocalActionHandler_should_have_a_valid_instance() {
        assertNotNull(beagleSetup.formLocalActionHandler)
    }

    @Test
    fun deepLinkHandler_should_have_a_valid_instance() {
        assertNotNull(beagleSetup.deepLinkHandler)
    }

    @Test
    fun httpClient_should_not_be_null() {
        assertNotNull(beagleSetup.httpClient)
    }

    @Test
    fun validatorHandler_should_have_a_valid_instance() {
        assertNotNull(beagleSetup.validatorHandler)
    }

    @Test
    fun config_should_have_a_valid_instance() {
        assertNotNull(beagleSetup.config)
    }

    @Test
    fun designSystem_should_have_a_valid_instance() {
        assertNotNull(beagleSetup.designSystem)
    }

    @Test
    fun `should has valid instance when get type adapter`() {
        //WHEN
        val result = beagleSetup.typeAdapterResolver.getAdapter<PersonAdapter>(Person::class.java)

        //THEN
        assertNotNull(beagleSetup.typeAdapterResolver)
        assertTrue(result is BeagleTypeAdapter<PersonAdapter>)
    }

    @Test
    fun `should get null when get type adapter with not has type mapped`() {
        //WHEN
        val result = beagleSetup.typeAdapterResolver.getAdapter<PersonImpl>(PersonImpl::class.java)

        //THEN
        assertNotNull(beagleSetup.typeAdapterResolver)
        assertNull(result)
    }
}
