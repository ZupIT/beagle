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

import br.com.zup.beagle.core.Bind
import org.junit.jupiter.api.Test
import java.util.*

internal class BeagleBindSerializerTest {
    @Test
    fun withObjectIdWriter_should_return_new_BeagleBindSerializer() =
        withObjectIdWriterShouldReturnNewSerializer(::BeagleBindSerializer)

    @Test
    fun withFilterId_should_return_new_BeagleBindSerializer() =
        withFilterIdShouldReturnNewSerializer(::BeagleBindSerializer)

    @Test
    fun serialize_non_beagle_bind_should_write_itself() = UUID.randomUUID().let { this.testSerialize(it, it) }

    @Test
    fun serialize_Bind_Expression_should_write_just_its_string_value() =
        UUID.randomUUID().toString().let { this.testSerialize(Bind.Expression<Any>(it), it) }

    @Test
    fun serialize_Bind_Value_should_write_just_its_value() =
        UUID.randomUUID().let { this.testSerialize(Bind.Value(it), it) }

    private fun testSerialize(bean: Any, value: Any) = testSerialize(bean, value, ::BeagleBindSerializer)
}