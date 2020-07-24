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

package br.com.zup.beagle.android.components.form.core

import br.com.zup.beagle.android.utils.BaseTest
import org.junit.Test
import kotlin.test.assertEquals

private const val FORM_GROUP_VALUE = "GROUP"
private const val FORM_KEY = "KEY"
private const val FORM_VALUE = "VALUE"

class FormDataStoreHandlerTest : BaseTest() {

    private lateinit var formDataStoreHandler: FormDataStoreHandler

    override fun setUp() {
        super.setUp()
        formDataStoreHandler = FormDataStoreHandler()
    }

    @Test
    fun should_store_value_to_group_after_put() {
        // When
        formDataStoreHandler.put(FORM_GROUP_VALUE, FORM_KEY, FORM_VALUE)

        // Then
        assertEquals(formDataStoreHandler.getAllValues(FORM_GROUP_VALUE)[FORM_KEY], FORM_VALUE)
    }

    @Test
    fun should_clear_all_values_after_clear_called() {
        // When
        formDataStoreHandler.put(FORM_GROUP_VALUE, "key1", "value1")
        formDataStoreHandler.put(FORM_GROUP_VALUE, "key2", "value2")
        formDataStoreHandler.put(FORM_GROUP_VALUE, "key3", "value3")
        formDataStoreHandler.clear(FORM_GROUP_VALUE)

        // Then
        assert(formDataStoreHandler.getAllValues(FORM_GROUP_VALUE).keys.isEmpty())
    }
}