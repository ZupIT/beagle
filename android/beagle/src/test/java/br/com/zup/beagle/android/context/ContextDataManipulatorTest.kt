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

import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.testutil.RandomData
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.json.JSONArray
import org.json.JSONObject
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNull
import kotlin.test.assertTrue

private val CONTEXT_ID = RandomData.string()

class ContextDataManipulatorTest {

    private val contextDataManipulator = ContextDataManipulator()

    @Before
    fun setUp() {
        mockkObject(BeagleMessageLogs)

        every { BeagleMessageLogs.errorWhileTryingToChangeContext(any()) } just Runs
        every { BeagleMessageLogs.errorWhileTryingToAccessContext(any()) } just Runs
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `set should replace context value when path is empty`() {
        // Given
        val context = ContextData(
            id = CONTEXT_ID,
            value = true
        )
        val value = false

        // When
        val result = contextDataManipulator.set(context = context, value = value)

        // Then
        assertTrue { result is ContextSetResult.Succeed }
        val newContext = (result as ContextSetResult.Succeed).newContext
        assertEquals(CONTEXT_ID, newContext.id)
        assertEquals(value, newContext.value)
    }

    @Test
    fun `set should ignore contextId and replace context value when path is empty`() {
        // Given
        val context = ContextData(
            id = CONTEXT_ID,
            value = JSONObject()
        )
        val value = false
        val path = "$CONTEXT_ID.a"

        // When
        val result = contextDataManipulator.set(context = context, value = value, path = path)

        // Then
        val newValue = (result as ContextSetResult.Succeed).newContext.value.toString()
        assertEquals("{\"a\":false}", newValue)
    }

    @Test
    fun `set should create extra keys before set value`() {
        // Given
        val context = ContextData(
            id = CONTEXT_ID,
            value = JSONObject()
        )
        val path = "a.b.c"
        val value = false

        // When
        val result = contextDataManipulator.set(context, path, value)

        // Then
        assertTrue { result is ContextSetResult.Succeed }
        val newContext = (result as ContextSetResult.Succeed).newContext
        assertEquals(CONTEXT_ID, newContext.id)
        val resultValue = (newContext.value as JSONObject)
            .getJSONObject("a")
            .getJSONObject("b")
            .getBoolean("c")
        assertEquals(value, resultValue)
    }

    @Test
    fun `set should create extra keys and replace root`() {
        // Given
        val context = ContextData(
            id = CONTEXT_ID,
            value = JSONObject()
        )
        val path = "[0].a.b.c"
        val value = false

        // When
        val result = contextDataManipulator.set(context, path, value)

        // Then
        assertTrue { result is ContextSetResult.Succeed }
        val newContext = (result as ContextSetResult.Succeed).newContext
        assertEquals(CONTEXT_ID, newContext.id)
        val resultValue = (newContext.value as JSONArray)
            .getJSONObject(0)
            .getJSONObject("a")
            .getJSONObject("b")
            .getBoolean("c")
        assertEquals(value, resultValue)
    }

    @Test
    fun `set should throw exception and log error`() {
        // Given
        val context = ContextData(
            id = CONTEXT_ID,
            value = JSONObject()
        )
        val path = "["
        val value = false

        // When
        val result = contextDataManipulator.set(context, path, value)

        // Then
        assertTrue { result is ContextSetResult.Failure }
        verify(exactly = once()) { BeagleMessageLogs.errorWhileTryingToChangeContext(any()) }
    }

    @Test
    fun `clear should create new context when path is null`() {
        // Given
        val context = ContextData(
            id = CONTEXT_ID,
            value = true
        )

        // When
        val result = contextDataManipulator.clear(context)

        // Then
        assertTrue { result is ContextSetResult.Succeed }
        assertEquals("", (result as ContextSetResult.Succeed).newContext.value)
    }

    @Test
    fun `clear should throw exception and log error`() {
        // Given
        val context = ContextData(
            id = CONTEXT_ID,
            value = JSONArray()
        )
        val path = "["

        // When
        val result = contextDataManipulator.clear(context, path)

        // Then
        assertTrue { result is ContextSetResult.Failure }
        verify(exactly = once()) { BeagleMessageLogs.errorWhileTryingToChangeContext(any()) }
    }

    @Test
    fun `clear should delete specific JSON node when path is given`() {
        // Given
        val context = ContextData(
            id = CONTEXT_ID,
            value = createMockJSONObject()
        )
        val path = "a"

        // When
        val result = contextDataManipulator.clear(context, path)

        // Then
        assertTrue { result is ContextSetResult.Succeed }
        val succeed = result as ContextSetResult.Succeed
        assertFails {
            (succeed.newContext.value as JSONObject).getJSONObject("a")
        }
    }

    @Test
    fun `clear should ignore contextId on path`() {
        // Given
        val context = ContextData(
            id = CONTEXT_ID,
            value = createMockJSONObject()
        )
        val path = "$CONTEXT_ID.a"

        // When
        val result = contextDataManipulator.clear(context, path)

        // Then
        assertTrue { result is ContextSetResult.Succeed }
        val succeed = result as ContextSetResult.Succeed
        assertFails {
            (succeed.newContext.value as JSONObject).getJSONObject("a")
        }
    }

    @Test
    fun `clear should delete specific JSONArray node when path is given`() {
        // Given
        val context = ContextData(
            id = CONTEXT_ID,
            value = JSONArray().apply {
                put(0)
            }
        )
        val path = "[0]"

        // When
        val result = contextDataManipulator.clear(context, path)

        // Then
        assertTrue { result is ContextSetResult.Succeed }
        val succeed = result as ContextSetResult.Succeed
        assertFails {
            (succeed.newContext.value as JSONArray).get(0)
        }
    }

    @Test
    fun `clear should not remove if value is not a JSON`() {
        // Given
        val context = ContextData(
            id = CONTEXT_ID,
            value = true
        )
        val path = "a"

        // When
        val result = contextDataManipulator.clear(context, path)

        // Then
        assertTrue { result is ContextSetResult.Failure }
    }

    @Test
    fun `get should return value from specified path`() {
        // Given
        val path = "a.b.c.d"
        val contextData = ContextData(
            id = CONTEXT_ID,
            value = createMockJSONObject()
        )

        // When
        val value = contextDataManipulator.get(contextData, path)

        // Then
        assertEquals(true, value)
    }

    @Test
    fun `get should return value from array path`() {
        // Given
        val path = "[0]"
        val contextData = ContextData(
            id = CONTEXT_ID,
            value = JSONArray().apply {
                put("0")
                put("1")
            }
        )

        // When
        val value = contextDataManipulator.get(contextData, path)

        // Then
        assertEquals("0", value)
    }

    @Test
    fun `get should return value from array`() {
        // Given
        val path = "$CONTEXT_ID[0]"
        val contextData = ContextData(
            id = CONTEXT_ID,
            value = JSONArray().apply {
                put("0")
                put("1")
            }
        )

        // When
        val value = contextDataManipulator.get(contextData, path)

        // Then
        assertEquals("0", value)
    }

    @Test
    fun `get should return value from array inside object`() {
        // Given
        val path = "$CONTEXT_ID.a[0]"
        val contextData = ContextData(
            id = CONTEXT_ID,
            value = JSONObject().apply {
                put("a", JSONArray().apply {
                    put("0")
                    put("1")
                })
            }
        )

        // When
        val value = contextDataManipulator.get(contextData, path)

        // Then
        assertEquals("0", value)
    }

    @Test
    fun `get should throw exception and log error`() {
        // Given
        val path = "[0]"
        val contextData = ContextData(
            id = CONTEXT_ID,
            value = createMockJSONObject()
        )

        // When
        val result = contextDataManipulator.get(contextData, path)

        // Then
        assertNull(result)
        verify(exactly = once()) { BeagleMessageLogs.errorWhileTryingToAccessContext(any()) }
    }

    private fun createMockJSONObject(): JSONObject {
        return JSONObject().apply {
            put("a", JSONObject().apply {
                put("b", JSONObject().apply {
                    put("c", JSONObject().apply {
                        put("d", true)
                    })
                })
            })
        }
    }
}