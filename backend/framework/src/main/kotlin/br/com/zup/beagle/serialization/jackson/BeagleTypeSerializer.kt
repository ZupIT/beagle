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

import br.com.zup.beagle.widget.action.Action
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.layout.Screen
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase

internal class BeagleTypeSerializer : BeanSerializerBase {

    private lateinit var classLoader: ClassLoader
    private lateinit var actionClass: Class<*>
    private lateinit var screenClass: Class<*>
    private lateinit var serverDrivenComponentClass: Class<*>

    constructor(source: BeanSerializerBase) : super(source) {
        setup()
    }

    constructor(source: BeanSerializerBase, classLoader: ClassLoader) : super(source) {
        setup(classLoader)
    }

    constructor(
        source: BeagleTypeSerializer?,
        objectIdWriter: ObjectIdWriter?
    ) : super(source, objectIdWriter) {
        setup()
    }

    constructor(
        source: BeagleTypeSerializer?,
        toIgnore: MutableSet<String>?
    ) : super(source, toIgnore) {
        setup()
    }

    constructor(
        source: BeagleTypeSerializer?,
        objectIdWriter: ObjectIdWriter?,
        filterId: Any?
    ) : super(source, objectIdWriter, filterId) {
        setup()
    }

    constructor(
        source: BeanSerializerBase?,
        properties: Array<BeanPropertyWriter>,
        filteredProperties: Array<BeanPropertyWriter>
    ) : super(source, properties, filteredProperties) {
        setup()
    }

    override fun withObjectIdWriter(objectIdWriter: ObjectIdWriter?) = BeagleTypeSerializer(this, objectIdWriter)

    override fun withIgnorals(toIgnore: MutableSet<String>?) = BeagleTypeSerializer(this, toIgnore)

    override fun asArraySerializer() = BeagleTypeSerializer(this, this.classLoader)

    override fun withFilterId(filterId: Any?) = BeagleTypeSerializer(this, this.classLoader)

    override fun serialize(bean: Any, generator: JsonGenerator, provider: SerializerProvider) {
        generator.writeStartObject()
        getBeagleType(bean)?.apply { generator.writeStringField(getBeagleTypeFieldName(bean), this) }
        super.serializeFields(bean, generator, provider)
        generator.writeEndObject()
    }

    private fun getBeagleType(bean: Any): String? {
        val beanName = bean::class.simpleName?.decapitalize()
        val beanClass = bean::class.java
        return if (this.actionClass.isAssignableFrom(beanClass)) {
            "$BEAGLE_NAMESPACE:$beanName"
        } else if (this.screenClass.isAssignableFrom(beanClass)) {
            "$BEAGLE_NAMESPACE:$SCREEN_COMPONENT"
        } else if (this.serverDrivenComponentClass.isAssignableFrom(beanClass)) {
            if (beanClass.annotations.any { it.annotationClass.qualifiedName == RegisterWidget::class.qualifiedName }) {
                "$CUSTOM_WIDGET_BEAGLE_NAMESPACE:$beanName"
            } else {
                "$BEAGLE_NAMESPACE:$beanName"
            }
        } else {
            null
        }
    }

    private fun setup(classLoader: ClassLoader = BeagleTypeSerializer::class.java.classLoader) {
        this.classLoader = classLoader
        this.actionClass = getClass(Action::class, this.classLoader)
        this.screenClass = getClass(Screen::class, this.classLoader)
        this.serverDrivenComponentClass = getClass(ServerDrivenComponent::class, this.classLoader)
    }

    private fun getBeagleTypeFieldName(bean: Any): String {
        return if (bean is Action) {
            ACTION_TYPE
        } else {
            COMPONENT_TYPE
        }
    }
}