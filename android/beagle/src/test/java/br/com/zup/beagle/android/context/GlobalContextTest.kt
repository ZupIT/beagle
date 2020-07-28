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

package br.com.zup.beagle.android.context

import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.testutil.RandomData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GlobalContextTest : BaseTest() {

    override fun setUp() {
        super.setUp()
        GlobalContext.clearContext()
    }

    @Test
    fun get_should_return_context_root_when_no_path_is_listed() {
        // Given When
        val value = GlobalContext.get()

        //Then
        assertEquals("", value)
    }

    @Test
    fun get_should_return_context_with_path() {
        // Given
        val path = RandomData.string()
        val value = RandomData.string()
        GlobalContext.set(path = path, value = value)

        // When
        val result = GlobalContext.get(path)

        //Then
        assertEquals(value, result)
    }

    @Test
    fun get_should_return_null_when_path_does_not_exist() {
        // Given
        val path = RandomData.string()

        // When
        val value = GlobalContext.get(path)

        //Then
        assertNull(value)
    }

    @Test
    fun set_should_set_global_context_value_at_root_when_path_is_null() {
        // Given
        val value = RandomData.string()

        // When
        GlobalContext.set(value = value)
        val result = GlobalContext.get()

        //Then
        assertEquals(value, result)
    }

    @Test
    fun set_should_set_global_context_value_at_a_specific_path() {
        // Given
        val value = RandomData.string()
        val path = RandomData.string()

        // When
        GlobalContext.set(path = path, value = value)
        val result = GlobalContext.get(path)

        //Then
        assertEquals(value, result)
    }

    @Test
    fun set_should_not_override_other_paths_in_global_context_root() {
        // Given
        val valueOne = RandomData.string()
        val pathOne = RandomData.string()
        GlobalContext.set(pathOne, valueOne)
        val valueTwo = RandomData.string()
        val pathTwo = RandomData.string()

        // When
        GlobalContext.set(pathTwo, value = valueTwo)
        val resultOne = GlobalContext.get(pathOne)
        val resultTwo = GlobalContext.get(pathTwo)

        //Then
        assertEquals(valueTwo, resultTwo)
        assertEquals(valueOne, resultOne)
    }

    @Test
    fun clear_should_set_empty_string_value_at_global_context_root_when_path_is_null() {
        // Given
        val pathForSet = RandomData.string()
        val value = RandomData.string()
        val valueAfterClear = ""
        GlobalContext.set(path = pathForSet, value = value)
        val path = null
        // When
        GlobalContext.clear(path)
        val result = GlobalContext.get()

        //Then
        assertEquals(valueAfterClear, result)
    }

    @Test
    fun clear_sould_set_empty_string_value_at_global_context_with_path() {
        // Given
        val path = RandomData.string()
        val value = RandomData.string()
        val valueAfterClear = ""
        GlobalContext.set(path = path, value = value)

        // When
        GlobalContext.clear(path)
        val result = GlobalContext.get(path)

        //Then
        assertEquals(valueAfterClear, result)
    }
}