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

package br.com.zup.beagle.android.operation

import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockkObject
import io.mockk.verify
import org.json.JSONArray
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach

class OperationResolverTest : BaseTest() {

    private lateinit var functionResolver: OperationResolver

    @BeforeEach
    override fun setUp() {
        super.setUp()
        functionResolver = OperationResolver()

        mockkObject(BeagleMessageLogs)
    }

    @Test
    fun should_execute_all_functions() {
        assertNotNull(functionResolver.execute("divide", 1, 2))
        assertNotNull(functionResolver.execute("multiply", 1, 2))
        assertNotNull(functionResolver.execute("subtract", 1, 2))
        assertNotNull(functionResolver.execute("contains", JSONArray(listOf(1)), 1))
        assertNotNull(functionResolver.execute("insert", JSONArray(listOf(1)), 1))
        assertNotNull(functionResolver.execute("remove", JSONArray(listOf(1)), 1))
        assertNotNull(functionResolver.execute("removeIndex", JSONArray(listOf(1)), 0))
        assertNotNull(functionResolver.execute("and", true))
        assertNotNull(functionResolver.execute("or", true))
        assertNotNull(functionResolver.execute("not", true))
        assertNotNull(functionResolver.execute("condition", true, true, true))
        assertNotNull(functionResolver.execute("eq", 1, 1))
        assertNotNull(functionResolver.execute("gt", 1, 1))
        assertNotNull(functionResolver.execute("gte", 1, 1))
        assertNotNull(functionResolver.execute("lt", 1, 1))
        assertNotNull(functionResolver.execute("lte", 1, 1))
        assertNotNull(functionResolver.execute("concat", "a", "b"))
        assertNotNull(functionResolver.execute("capitalize", "a"))
        assertNotNull(functionResolver.execute("uppercase", "a"))
        assertNotNull(functionResolver.execute("lowercase", "a"))
        assertNotNull(functionResolver.execute("substr", "aaaaaaa", 0, 2))
        assertNotNull(functionResolver.execute("isNull", "a"))
        assertNotNull(functionResolver.execute("isEmpty", "a"))
        assertNotNull(functionResolver.execute("length", "a"))
    }

    @Test
    fun execute_should_log_function_that_does_not_exists() {
        every { BeagleMessageLogs.functionWithNameDoesNotExist(any()) } just Runs

        assertNull(functionResolver.execute("aa", ""))

        verify { BeagleMessageLogs.functionWithNameDoesNotExist("aa") }
    }
}