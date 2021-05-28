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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

data class User(
    val id: String,
)

@DisplayName("Given a GlobalContext")
class GlobalContextTest : BaseTest() {

    @BeforeEach
    override fun setUp() {
        super.setUp()

        GlobalContext.clear()
    }

    @Nested
    @DisplayName("When get is called")
    inner class GetContext {

        @Test
        @DisplayName("Then should return root if path is empty")
        fun getRootPath() {
            // When
            val value = GlobalContext.get()

            // Then
            assertEquals("", value)
        }

        @Test
        @DisplayName("Then should return value with path")
        fun getValueWithPath() {
            // Given
            val path = RandomData.string()
            val value = RandomData.string()
            GlobalContext.set(value = value, path = path)

            // When
            val result = GlobalContext.get(path)

            // Then
            assertEquals(value, result)
        }

        @Test
        @DisplayName("Then should return null if path does not exist")
        fun getNullInvalidPath() {
            // Given
            val path = RandomData.string()

            // Then
            val value = GlobalContext.get(path)

            // Then
            assertNull(value)
        }
    }

    @Nested
    @DisplayName("When set is called")
    inner class SetContext {

        @Test
        @DisplayName("Then should set value to root if path is null")
        fun setValueToRoot() {
            // Given
            val value = RandomData.string()

            // When
            GlobalContext.set(value = value)
            val result = GlobalContext.get()

            // Then
            assertEquals(value, result)
        }

        @Test
        @DisplayName("Then should set value to the given path")
        fun setValueToPath() {
            // Given
            val value = RandomData.string()
            val path = RandomData.string()

            // When
            GlobalContext.set(value = value, path = path)
            val result = GlobalContext.get(path)

            // Then
            assertEquals(value, result)
        }

        @Test
        @DisplayName("Then should not override other paths")
        fun setShouldNotOverride() {
            // Given
            val valueOne = RandomData.string()
            val pathOne = RandomData.string()
            GlobalContext.set(valueOne, pathOne)
            val valueTwo = RandomData.string()
            val pathTwo = RandomData.string()

            // When
            GlobalContext.set(value = valueTwo, path = pathTwo)
            val resultOne = GlobalContext.get(pathOne)
            val resultTwo = GlobalContext.get(pathTwo)

            // Then
            assertEquals(valueTwo, resultTwo)
            assertEquals(valueOne, resultOne)
        }

        @Test
        @DisplayName("Then should set objects and navigate them")
        fun setObject() {
            // Given
            val user = User(id = "identifier")

            // When
            GlobalContext.set(value = user, path = "user")

            // Then
            val result = GlobalContext.get("user.id")
            assertEquals(user.id, result)
        }
    }

    @Nested
    @DisplayName("When clear is called")
    inner class ClearContext {

        @Test
        @DisplayName("Then should set empty string to root if path is null")
        fun clearEmptyStringRoot() {
            // Given
            val pathForSet = RandomData.string()
            val value = RandomData.string()
            val valueAfterClear = ""
            GlobalContext.set(value = value, path = pathForSet)
            val path = null

            // When
            GlobalContext.clear(path)
            val result = GlobalContext.get()

            // Then
            assertEquals(valueAfterClear, result)
        }

        @Test
        @DisplayName("Then should set null to context with a path")
        fun clearContextWithPath() {
            // Given
            val path = RandomData.string()
            val value = RandomData.string()
            GlobalContext.set(value = value, path = path)

            // When
            GlobalContext.clear(path)
            val result = GlobalContext.get(path)

            // Then
            assertNull(result)
        }

        @Test
        @DisplayName("Then should remove attribute from Json Object")
        fun clearJsonObjectAttribute() {
            // Given
            val attributeContent = RandomData.string()
            GlobalContext.set(value = attributeContent, path = "a.b")
            GlobalContext.set(value = attributeContent, path = "a.c")

            // When
            GlobalContext.clear("a.b")
            val attr1 = GlobalContext.get("a.b")
            val attr2 = GlobalContext.get("a.c")
            val containsRemovedAttribute = GlobalContext.get().toString().contains("\"b\":", true)

            // Then
            assertNull(attr1)
            assertEquals(attributeContent, attr2)
            assertFalse(containsRemovedAttribute)
        }

        @Test
        @DisplayName("Then should not clear a path that does not exist")
        fun clearNonexistentPath() {
            // Given
            val objectPath = "a.b.c"

            // When
            GlobalContext.clear(objectPath)

            // Then
            assertNull(GlobalContext.get("a"))
            assertNull(GlobalContext.get("a.b"))
            assertNull(GlobalContext.get("a.b.c"))
        }

        @Test
        @DisplayName("Then should not clear a path that does not exist in Json Array")
        fun clearNonexistentJsonArrayPath() {
            // Given
            GlobalContext.set(true, "f.e")
            val objectPath = "a[0].c.e"

            // When
            GlobalContext.clear(objectPath)

            // Then
            assertNull(GlobalContext.get("c"))
            assertNull(GlobalContext.get("c.e"))
        }
    }
}
