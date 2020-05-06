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
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScreenBuilder
import br.com.zup.beagle.widget.ui.Text
import org.junit.jupiter.api.Test

internal class BeagleBuilderSerializerTest {
    @Test
    fun withObjectIdWriter_should_return_new_BeagleBuilderSerializer() =
        withObjectIdWriterShouldReturnNewSerializer(::BeagleBuilderSerializer)

    @Test
    fun withFilterId_should_return_new_BeagleBuilderSerializer() =
        withFilterIdShouldReturnNewSerializer(::BeagleBuilderSerializer)

    @Test
    fun serialize_non_beagle_builder_should_write_itself() = "Test".let { this.testSerialize(it, it) }

    @Test
    fun serialize_ComposeComponent_should_write_the_component_from_build() =
        Text("Test").let {
            this.testSerialize(
                object : ComposeComponent() {
                    override fun build() = it
                },
                it
            )
        }

    @Test
    fun serialize_ScreenBuilder_should_write_the_component_from_build() =
        Screen(child = Text("Test")).let {
            this.testSerialize(
                object : ScreenBuilder {
                    override fun build() = it
                },
                it
            )
        }

    private fun testSerialize(bean: Any, built: Any) = testSerialize(bean, built, ::BeagleBuilderSerializer)
}
