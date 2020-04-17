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

import br.com.zup.beagle.widget.core.ComposeComponent
import br.com.zup.beagle.widget.layout.ScreenBuilder
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter
import com.fasterxml.jackson.databind.ser.impl.BeanAsArraySerializer
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase

internal class BeagleBuilderSerializer : BeanSerializerBase {
    constructor(source: BeanSerializerBase?) : super(source)

    constructor(
        source: BeagleBuilderSerializer?,
        objectIdWriter: ObjectIdWriter?
    ) : super(source, objectIdWriter)

    constructor(
        source: BeagleBuilderSerializer?,
        toIgnore: MutableSet<String>?
    ) : super(source, toIgnore)

    constructor(
        source: BeagleBuilderSerializer?,
        objectIdWriter: ObjectIdWriter?,
        filterId: Any?
    ) : super(source, objectIdWriter, filterId)

    constructor(
        source: BeanSerializerBase?,
        properties: Array<BeanPropertyWriter>,
        filteredProperties: Array<BeanPropertyWriter>
    ) : super(source, properties, filteredProperties)

    override fun withObjectIdWriter(objectIdWriter: ObjectIdWriter) =
        BeagleBuilderSerializer(this, objectIdWriter)

    override fun withIgnorals(toIgnore: MutableSet<String>) =
        BeagleBuilderSerializer(this, toIgnore)

    override fun asArraySerializer() =
        BeanAsArraySerializer(this)

    override fun withFilterId(filterId: Any?) =
        BeagleBuilderSerializer(this, this._objectIdWriter, filterId)

    override fun serialize(bean: Any, generator: JsonGenerator, provider: SerializerProvider) {
        generator.writeObject(
            when (bean) {
                is ComposeComponent -> bean.build()
                is ScreenBuilder -> bean.build()
                else -> bean
            }
        )
    }
}