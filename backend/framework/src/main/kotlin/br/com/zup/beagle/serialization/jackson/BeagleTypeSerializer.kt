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

import br.com.zup.beagle.action.Action
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.layout.Screen
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter
import com.fasterxml.jackson.databind.ser.impl.BeanAsArraySerializer
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase
import kotlin.reflect.full.findAnnotation

internal class BeagleTypeSerializer : BeanSerializerBase {
    constructor(source: BeanSerializerBase?) : super(source)

    constructor(
        source: BeagleTypeSerializer?,
        objectIdWriter: ObjectIdWriter?
    ) : super(source, objectIdWriter)

    constructor(
        source: BeagleTypeSerializer?,
        toIgnore: MutableSet<String>?
    ) : super(source, toIgnore)

    constructor(
        source: BeagleTypeSerializer?,
        objectIdWriter: ObjectIdWriter?,
        filterId: Any?
    ) : super(source, objectIdWriter, filterId)

    constructor(
        source: BeanSerializerBase?,
        properties: Array<BeanPropertyWriter>,
        filteredProperties: Array<BeanPropertyWriter>
    ) : super(source, properties, filteredProperties)

    override fun withObjectIdWriter(objectIdWriter: ObjectIdWriter) =
        BeagleTypeSerializer(this, objectIdWriter)

    override fun withIgnorals(toIgnore: MutableSet<String>) =
        BeagleTypeSerializer(this, toIgnore)

    override fun asArraySerializer() =
        BeanAsArraySerializer(this)

    override fun withFilterId(filterId: Any?) =
        BeagleTypeSerializer(this, this._objectIdWriter, filterId)

    override fun serialize(bean: Any, generator: JsonGenerator, provider: SerializerProvider) {
        generator.writeStartObject()
        getBeagleTypeValue(bean)?.apply {
            generator.writeStringField(getBeagleTypeFieldName(bean), this)
        }
        super.serializeFields(bean, generator, provider)
        generator.writeEndObject()
    }

    private fun getBeagleTypeValue(bean: Any): String? {
        val beanName = bean::class.simpleName?.decapitalize()

        return when (bean) {
            is Action -> "$BEAGLE_NAMESPACE:$beanName"
            is Screen -> "$BEAGLE_NAMESPACE:$SCREEN_COMPONENT"
            is ServerDrivenComponent ->
                if (bean::class.findAnnotation<RegisterWidget>() != null) {
                    "$CUSTOM_WIDGET_BEAGLE_NAMESPACE:$beanName"
                } else {
                    "$BEAGLE_NAMESPACE:$beanName"
                }
            else -> null
        }
    }

    private fun getBeagleTypeFieldName(bean: Any): String {
        return if (bean is Action) {
            ACTION_TYPE
        } else {
            COMPONENT_TYPE
        }
    }
}
