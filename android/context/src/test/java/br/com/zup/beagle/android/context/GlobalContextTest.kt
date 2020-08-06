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

import br.com.zup.beagle.android.logger.BeagleContextLogs
import br.com.zup.beagle.android.testutil.RandomData
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockkObject
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GlobalContextTest {

    @Before
    fun setUp() {
        mockkObject(BeagleContextLogs)
        every { BeagleContextLogs.errorWhileTryingToChangeContext(any()) } just Runs
        every { BeagleContextLogs.errorWhileTryingToAccessContext(any()) } just Runs


        GlobalContext.clear()
    }

    @Test
    fun get_should_return_context_root_when_no_path_is_listed() {
        val value = GlobalContext.get()

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

        // Then
        assertEquals(value, result)
    }

    @Test
    fun get_should_return_null_when_path_does_not_exist() {
        // Given
        val path = RandomData.string()

        // Then
        val value = GlobalContext.get(path)

        // Then
        assertNull(value)
    }

    @Test
    fun set_should_set_global_context_value_at_root_when_path_is_null() {
        // Given
        val value = RandomData.string()

        // When
        GlobalContext.set(value = value)
        val result = GlobalContext.get()

        // Then
        assertEquals(value, result)
    }

    @Test
    fun set_should_set_global_context_value_at_a_specific_path() {

        val value = RandomData.string()
        val path = RandomData.string()

        GlobalContext.set(path = path, value = value)
        val result = GlobalContext.get(path)

        assertEquals(value, result)
    }

    @Test
    fun set_should_not_override_other_paths_in_global_context_root() {

        val valueOne = RandomData.string()
        val pathOne = RandomData.string()
        GlobalContext.set(pathOne, valueOne)
        val valueTwo = RandomData.string()
        val pathTwo = RandomData.string()

        GlobalContext.set(pathTwo, value = valueTwo)
        val resultOne = GlobalContext.get(pathOne)
        val resultTwo = GlobalContext.get(pathTwo)

        assertEquals(valueTwo, resultTwo)
        assertEquals(valueOne, resultOne)
    }

    @Test
    fun clear_should_set_empty_string_value_at_global_context_root_when_path_is_null() {

        val pathForSet = RandomData.string()
        val value = RandomData.string()
        val valueAfterClear = ""
        GlobalContext.set(path = pathForSet, value = value)
        val path = null

        GlobalContext.clear(path)
        val result = GlobalContext.get()

        assertEquals(valueAfterClear, result)
    }

    @Test
    fun clear_should_set_null_to_context_with_a_path() {
        // Given
        val path = RandomData.string()
        val value = RandomData.string()
        GlobalContext.set(path = path, value = value)

        // When
        GlobalContext.clear(path)
        val result = GlobalContext.get(path)

        // Then
        assertNull(result)
    }

    @Test
    fun clear_should_remove_attribute_from_JSON_object() {
        //Given
        val attributeContent = RandomData.string()
        GlobalContext.set(path = "a.b", value = attributeContent)
        GlobalContext.set(path = "a.c", value = attributeContent)

        //When
        GlobalContext.clear("a.b")

        //Then
        val attr1 = GlobalContext.get("a.b")
        val attr2 = GlobalContext.get("a.c")
        val containsRemovedAttribute = GlobalContext.get().toString().contains("\"b\":", true)

        assertNull(attr1)
        assertEquals(attributeContent, attr2)
        assertFalse(containsRemovedAttribute)
    }

    @Test
    fun clear_should_not_clear_a_path_that_does_not_exist() {
        //Given
        val objectPath = "a.b.c"

        //When
        GlobalContext.clear(objectPath)

        //Then
        assertNull(GlobalContext.get("a"))
        assertNull(GlobalContext.get("a.b"))
        assertNull(GlobalContext.get("a.b.c"))
    }

    @Test
    fun clear_should_not_clear_a_path_that_does_not_exist_in_JSONObject() {
        //Given
        GlobalContext.set("f.e", true)
        val objectPath = "a[0].c.e"

        //When
        GlobalContext.clear(objectPath)

        //Then
        assertNull(GlobalContext.get("c"))
        assertNull(GlobalContext.get("c.e"))
    }
}