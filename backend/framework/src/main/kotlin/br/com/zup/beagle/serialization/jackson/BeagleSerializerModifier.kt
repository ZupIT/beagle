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
import br.com.zup.beagle.core.Bind
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.core.ComposeComponent
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScreenBuilder
import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializationConfig
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase
import kotlin.reflect.KClass

internal object BeagleSerializerModifier : BeanSerializerModifier() {
    private val beagleBaseClasses = arrayOf(Action::class, Screen::class, ServerDrivenComponent::class)
    private val beagleBuilders = arrayOf(ComposeComponent::class, ScreenBuilder::class)

    override fun modifySerializer(
        config: SerializationConfig,
        description: BeanDescription,
        serializer: JsonSerializer<*>
    ) = when {
        serializer is BeanSerializerBase && isBeagleBuilder(description) -> BeagleBuilderSerializer(serializer)
        serializer is BeanSerializerBase && isBeagleBase(description) -> BeagleTypeSerializer(serializer)
        serializer is BeanSerializerBase && isBeagleBinding(description) -> BeagleBindSerializer(serializer)
        else -> serializer
    }

    private fun isBeagleBase(description: BeanDescription) = beagleBaseClasses.findAssignableFrom(description)

    private fun isBeagleBuilder(description: BeanDescription) = beagleBuilders.findAssignableFrom(description)

    private fun isBeagleBinding(description: BeanDescription) = Bind::class.isAssignableFrom(description)

    private fun Array<KClass<*>>.findAssignableFrom(description: BeanDescription) =
        this.any { it.isAssignableFrom(description) }

    private fun KClass<*>.isAssignableFrom(description: BeanDescription) =
        this.java.isAssignableFrom(description.beanClass)
}
