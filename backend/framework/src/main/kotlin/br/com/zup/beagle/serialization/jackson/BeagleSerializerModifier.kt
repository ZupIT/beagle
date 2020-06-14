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
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.layout.Screen
import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializationConfig
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase

internal class BeagleSerializerModifier(
    private val classLoader: ClassLoader
) : BeanSerializerModifier() {
    private var beagleBaseClasses = listOf(Action::class.java, Screen::class.java, ServerDrivenComponent::class.java)

    init {
        this.beagleBaseClasses = this.beagleBaseClasses.map { Class.forName(it.name, true, this.classLoader) }
    }

    override fun modifySerializer(
        config: SerializationConfig,
        description: BeanDescription,
        serializer: JsonSerializer<*>
    ) =
        if (serializer is BeanSerializerBase && this.beagleBaseClasses.findAssignableFrom(description)) {
            BeagleTypeSerializer(serializer, this.classLoader)
        } else {
            serializer
        }

    private fun List<Class<*>>.findAssignableFrom(description: BeanDescription) =
        this.find { it.isAssignableFrom(description.beanClass) } != null
}
