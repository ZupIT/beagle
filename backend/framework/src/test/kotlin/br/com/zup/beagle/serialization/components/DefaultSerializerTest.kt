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

package br.com.zup.beagle.serialization.components

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal abstract class DefaultSerializerTest<T> : BaseSerializerTest<T>() {
    abstract fun testArguments(): List<Arguments>

    @ParameterizedTest(name = "[{index}] When try to serialize component Then should return correct json {0}, {1}")
    @MethodSource("testArguments")
    open fun executeDefaultSerializationTests(expectedJson: String, component: T) {
        testSerialization(component, expectedJson)
    }
}