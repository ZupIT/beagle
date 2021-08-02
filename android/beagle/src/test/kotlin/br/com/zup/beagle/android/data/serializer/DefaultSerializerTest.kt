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

package br.com.zup.beagle.android.data.serializer

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

abstract class DefaultSerializerTest<T>(clazz: Class<T>) : BaseSerializerTest<T>(clazz){
    abstract fun testArguments(): List<Arguments>

    @DisplayName("When try to deserialize json")
    @ParameterizedTest(name = "[{index}] Then should return correct object {0} {1}")
    @MethodSource("testArguments")
    open fun executeDefaultDeserializationTests(json: String, component: T) {
        testDeserializeJson(json, component)
    }

    @DisplayName("When try to serialize object")
    @ParameterizedTest(name = "[{index}] Then should return correct json {0}, {1}")
    @MethodSource("testArguments")
    open fun executeDefaultSerializationTests(json: String, component: T) {
        testSerializeObject(json, component)
    }
}