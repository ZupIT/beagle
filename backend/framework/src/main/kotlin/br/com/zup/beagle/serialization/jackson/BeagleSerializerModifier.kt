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
import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializationConfig
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase

internal object BeagleSerializerModifier : BeanSerializerModifier() {
    private val beagleBuilders = listOf(ComposeComponent::class.java, ScreenBuilder::class.java)

    override fun modifySerializer(
        config: SerializationConfig,
        description: BeanDescription,
        serializer: JsonSerializer<*>
    ) =
        when {
            serializer is BeanSerializerBase && this.isBeagleBuilder(description) -> BeagleBuilderSerializer(serializer)
            serializer is BeanSerializerBase && this.isBeagleBase(description) -> BeagleTypeSerializer(serializer)
            else -> serializer
        }

    private fun isBeagleBase(description: BeanDescription) = getBeagleType(description.beanClass) != null

    private fun isBeagleBuilder(description: BeanDescription) =
        this.beagleBuilders.any { it.isAssignableFrom(description.beanClass) }
}
