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

package br.com.zup.beagle.serialization.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verifyAll
import kotlin.test.assertNotSame
import kotlin.test.assertTrue

private typealias SerializerCreator = (BeanSerializerBase) -> BeanSerializerBase

internal fun withObjectIdWriterShouldReturnNewSerializer(create: SerializerCreator) {
    val serializer = create(mockk())

    val result = serializer.withObjectIdWriter(mockk())

    assertNotSame(serializer, result)
    assertTrue(block = result::usesObjectId)
}

internal fun withFilterIdShouldReturnNewSerializer(create: SerializerCreator) {
    val serializer = create(mockk())

    val result = serializer.withFilterId("Test")

    assertNotSame(serializer, result)
}

internal fun testSerialize(bean: Any, written: Any, create: SerializerCreator) {
    val generator = mockk<JsonGenerator>()

    every { generator.writeObject(any()) } just Runs

    create(mockk()).serialize(bean, generator, mockk())

    verifyAll { generator.writeObject(written) }
}