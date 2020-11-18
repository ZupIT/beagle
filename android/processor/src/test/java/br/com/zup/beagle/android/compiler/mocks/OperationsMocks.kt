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

package br.com.zup.beagle.android.compiler.mocks

const val VALID_OPERATION =
    """
        import br.com.zup.beagle.annotation.RegisterOperation
        import br.com.zup.beagle.android.operation.Operation
        
        @RegisterOperation("OperationTestName")
        class OperationTest: Operation { }
    """

const val VALID_SECOND_OPERATION =
    """
        @RegisterOperation("OperationTwoTestName")
        class OperationTwoTest: Operation { }
    """

const val VALID_LIST_OPERATIONS = VALID_OPERATION + VALID_SECOND_OPERATION

const val INTERNAL_SINGLE_OPERATION_GENERATED_EXPECTED: String =
    """
        @file:Suppress("OverridingDeprecatedMember", "DEPRECATION", "UNCHECKED_CAST", "UNUSED_EXPRESSION")
        package br.com.test.beagle
        import br.com.zup.beagle.android.operation.Operation
        import kotlin.String
        import kotlin.Suppress
        import kotlin.collections.Map
        
        public final object RegisteredOperations { 
            public fun registeredOperations() : Map<String,Operation> {
                val operations = mapOf<String , Operation>(
                    "OperationTestName" to br.com.test.beagle.OperationTest(),
                )
                return operations 
            }
        }

    """

const val INTERNAL_LIST_OPERATION_GENERATED_EXPECTED: String =
    """
        @file:Suppress("OverridingDeprecatedMember", "DEPRECATION", "UNCHECKED_CAST", "UNUSED_EXPRESSION")
        package br.com.test.beagle
        import br.com.zup.beagle.android.operation.Operation
        import kotlin.String
        import kotlin.Suppress
        import kotlin.collections.Map
        
        public final object RegisteredOperations { 
            public fun registeredOperations() : Map<String, Operation> { 
                val operations = mapOf<String, Operation>(
                    "OperationTwoTestName" to br.com.test.beagle.OperationTwoTest(),
                    "OperationTestName" to br.com.test.beagle.OperationTest(),
                )
                return operations 
            }
        }

    """

const val INVALID_OPERATION =
    """
        import br.com.zup.beagle.annotation.RegisterOperation

        @RegisterOperation("test")
        class InvalidOperation { }
    """

const val INVALID_OPERATION_TWO =
    """
        import br.com.zup.beagle.annotation.RegisterOperation

        @RegisterOperation("test")
        class InvalidOperationTwo { }
    """

const val INVALID_OPERATION_WITH_INHERITANCE =
    """
        import br.com.zup.beagle.annotation.RegisterOperation

        @RegisterOperation("testTwo")
        class InvalidOperation : WidgetView { }
    """