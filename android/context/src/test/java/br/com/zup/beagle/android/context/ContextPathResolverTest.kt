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

import br.com.zup.beagle.android.testutil.RandomData
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFails

private val CONTEXT_ID = RandomData.string()

class ContextPathResolverTest {

    private lateinit var contextPathResolver: ContextPathResolver

    @Before
    fun setUp() {
        contextPathResolver =
            ContextPathResolver();
    }

    @Test
    fun getKeysFromPath_should_return_key_paths_without_context() {
        // Given
        val path = "a.b"

        // When
        val keys = contextPathResolver.getKeysFromPath(CONTEXT_ID, "$CONTEXT_ID.$path")

        // Then
        assertEquals("a", keys.poll())
        assertEquals("b", keys.poll())
        assertEquals(0, keys.size)
    }

    @Test
    fun getKeysFromPath_should_throw_exception_when_path_is_invalid() {
        assertFails {
            contextPathResolver.getKeysFromPath(
                CONTEXT_ID,
                CONTEXT_ID
            )
        }
    }

    @Test
    fun getKeysFromPath_should_return_keys_only() {
        // Given
        val contextId = RandomData.string()

        // When
        val keys = contextPathResolver.getKeysFromPath(contextId, "a.b")

        // Then
        assertEquals("a", keys.poll())
        assertEquals("b", keys.poll())
        assertEquals(0, keys.size)
    }
}