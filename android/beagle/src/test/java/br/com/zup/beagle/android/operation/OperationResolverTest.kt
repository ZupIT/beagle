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
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested

@DisplayName("Given an OperationResolver object")
internal class OperationResolverTest : BaseTest() {

    private lateinit var operationResolver: OperationResolver

    @BeforeEach
    override fun setUp() {
        super.setUp()
        operationResolver = OperationResolver()

        mockkObject(BeagleMessageLogs)
    }

    @DisplayName("When execute method is called passing divide operation")
    @Nested
    inner class DivideOperation {

        @DisplayName("Then it should execute the operation correctly")
        @Test
        fun shouldExecuteOperation() {
            assertNotNull(operationResolver.execute("divide", 1, 2))
        }
    }

    @DisplayName("When execute method is called passing multiply operation")
    @Nested
    inner class MultiplyOperation {

        @DisplayName("Then it should execute the operation correctly")
        @Test
        fun shouldExecuteOperation() {
            assertNotNull(operationResolver.execute("multiply", 1, 2))
        }
    }

    @DisplayName("When execute method is called passing subtract operation")
    @Nested
    inner class SubtractOperation {

        @DisplayName("Then it should execute the operation correctly")
        @Test
        fun shouldExecuteOperation() {
            assertNotNull(operationResolver.execute("subtract", 1, 2))
        }
    }

    @DisplayName("When execute method is called passing contains operation")
    @Nested
    inner class ContainsOperation {

        @DisplayName("Then it should execute the operation correctly")
        @Test
        fun shouldExecuteOperation() {
            assertNotNull(operationResolver.execute("contains", JSONArray(listOf(1)), 1))
        }
    }

    @DisplayName("When execute method is called passing insert operation")
    @Nested
    inner class InsertOperation {

        @DisplayName("Then it should execute the operation correctly")
        @Test
        fun shouldExecuteOperation() {
            assertNotNull(operationResolver.execute("insert", JSONArray(listOf(1)), 1))
        }
    }

    @DisplayName("When execute method is called passing remove operation")
    @Nested
    inner class RemoveOperation {

        @DisplayName("Then it should execute the operation correctly")
        @Test
        fun shouldExecuteOperation() {
            assertNotNull(operationResolver.execute("remove", JSONArray(listOf(1)), 1))
        }
    }

    @DisplayName("When execute method is called passing removeIndex operation")
    @Nested
    inner class RemoveIndexOperation {

        @DisplayName("Then it should execute the operation correctly")
        @Test
        fun shouldExecuteOperation() {
            assertNotNull(operationResolver.execute("removeIndex", JSONArray(listOf(1)), 0))
        }
    }

    @DisplayName("When execute method is called passing union operation")
    @Nested
    inner class UnionOperation {

        @DisplayName("Then it should execute the operation correctly")
        @Test
        fun shouldExecuteOperation() {
            assertNotNull(
                operationResolver.execute("union", JSONArray(listOf(1)), JSONArray(listOf(1, 2, 3, 4)))
            )
        }
    }

    @DisplayName("When execute method is called passing and operation")
    @Nested
    inner class AndOperation {

        @DisplayName("Then it should execute the operation correctly")
        @Test
        fun shouldExecuteOperation() {
            assertNotNull(operationResolver.execute("and", true))
        }
    }

    @DisplayName("When execute method is called passing or operation")
    @Nested
    inner class OrOperation {

        @DisplayName("Then it should execute the operation correctly")
        @Test
        fun shouldExecuteOperation() {
            assertNotNull(operationResolver.execute("or", true))
        }
    }

    @DisplayName("When execute method is called passing not operation")
    @Nested
    inner class NotOperation {

        @DisplayName("Then it should execute the operation correctly")
        @Test
        fun shouldExecuteOperation() {
            assertNotNull(operationResolver.execute("not", true))
        }
    }

    @DisplayName("When execute method is called passing condition operation")
    @Nested
    inner class ConditionOperation {

        @DisplayName("Then it should execute the operation correctly")
        @Test
        fun shouldExecuteOperation() {
            assertNotNull(operationResolver.execute("condition", true, true, true))
        }
    }

    @DisplayName("When execute method is called passing eq operation")
    @Nested
    inner class EqOperation {

        @DisplayName("Then it should execute the operation correctly")
        @Test
        fun shouldExecuteOperation() {
            assertNotNull(operationResolver.execute("eq", 1, 1))
        }
    }

    @DisplayName("When execute method is called passing gt operation")
    @Nested
    inner class GtOperation {

        @DisplayName("Then it should execute the operation correctly")
        @Test
        fun shouldExecuteOperation() {
            assertNotNull(operationResolver.execute("gt", 1, 1))
        }
    }

    @DisplayName("When execute method is called passing gte operation")
    @Nested
    inner class GteOperation {

        @DisplayName("Then it should execute the operation correctly")
        @Test
        fun shouldExecuteOperation() {
            assertNotNull(operationResolver.execute("gte", 1, 1))
        }
    }

    @DisplayName("When execute method is called passing lt operation")
    @Nested
    inner class LtOperation {

        @DisplayName("Then it should execute the operation correctly")
        @Test
        fun shouldExecuteOperation() {
            assertNotNull(operationResolver.execute("lt", 1, 1))
        }
    }

    @DisplayName("When execute method is called passing lte operation")
    @Nested
    inner class LteOperation {

        @DisplayName("Then it should execute the operation correctly")
        @Test
        fun shouldExecuteOperation() {
            assertNotNull(operationResolver.execute("lte", 1, 1))
        }
    }

    @DisplayName("When execute method is called passing concat operation")
    @Nested
    inner class ConcatOperation {

        @DisplayName("Then it should execute the operation correctly")
        @Test
        fun shouldExecuteOperation() {
            assertNotNull(operationResolver.execute("concat", "a", "b"))
        }
    }

    @DisplayName("When execute method is called passing capitalize operation")
    @Nested
    inner class CapitalizeOperation {

        @DisplayName("Then it should execute the operation correctly")
        @Test
        fun shouldExecuteOperation() {
            assertNotNull(operationResolver.execute("capitalize", "a"))
        }
    }

    @DisplayName("When execute method is called passing uppercase operation")
    @Nested
    inner class UppercaseOperation {

        @DisplayName("Then it should execute the operation correctly")
        @Test
        fun shouldExecuteOperation() {
            assertNotNull(operationResolver.execute("uppercase", "a"))
        }
    }

    @DisplayName("When execute method is called passing lowercase operation")
    @Nested
    inner class LowercaseOperation {

        @DisplayName("Then it should execute the operation correctly")
        @Test
        fun shouldExecuteOperation() {
            assertNotNull(operationResolver.execute("lowercase", "a"))
        }
    }

    @DisplayName("When execute method is called passing substr operation")
    @Nested
    inner class SubstrOperation {

        @DisplayName("Then it should execute the operation correctly")
        @Test
        fun shouldExecuteOperation() {
            assertNotNull(operationResolver.execute("substr", "aaaaaaa", 0, 2))
        }
    }

    @DisplayName("When execute method is called passing isNull operation")
    @Nested
    inner class IsNullOperation {

        @DisplayName("Then it should execute the operation correctly")
        @Test
        fun shouldExecuteOperation() {
            assertNotNull(operationResolver.execute("isNull", "a"))
        }
    }

    @DisplayName("When execute method is called passing isEmpty operation")
    @Nested
    inner class IsEmptyOperation {

        @DisplayName("Then it should execute the operation correctly")
        @Test
        fun shouldExecuteOperation() {
            assertNotNull(operationResolver.execute("isEmpty", "a"))
        }
    }

    @DisplayName("When execute method is called passing length operation")
    @Nested
    inner class LengthOperation {

        @DisplayName("Then it should execute the operation correctly")
        @Test
        fun shouldExecuteOperation() {
            assertNotNull(operationResolver.execute("length", "a"))
        }
    }

    @DisplayName("When execute method is called passing a non existent operation")
    @Nested
    inner class NonExistentOperation {

        @DisplayName("Then it should log that this operation does not exist")
        @Test
        fun shouldLogThaOperationDoesNotExist() {
            every { BeagleMessageLogs.functionWithNameDoesNotExist(any()) } just Runs

            assertNull(operationResolver.execute("aa", ""))

            verify { BeagleMessageLogs.functionWithNameDoesNotExist("aa") }
        }
    }
}