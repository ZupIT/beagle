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
import com.fasterxml.jackson.core.JsonGenerator
import io.mockk.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotSame
import kotlin.test.assertTrue

internal class BeagleBuilderSerializerTest {
    @Test
    fun withObjectIdWriter_should_return_new_BeagleBuilderSerializer() {
        val serializer = BeagleBuilderSerializer(mockk())

        val result = serializer.withObjectIdWriter(mockk())

        assertNotSame(serializer, result)
        assertTrue(block = result::usesObjectId)
    }

    @Test
    fun withFilterId_should_return_new_BeagleBuilderSerializer() {
        val serializer = BeagleBuilderSerializer(mockk())

        val result = serializer.withFilterId("Test")

        assertNotSame(serializer, result)
    }

    @Test
    fun serialize_non_beagle_builder_should_write_itself() = testSerialize("Test", "Test")

    @Test
    fun serialize_ComposeComponent_should_write_the_component_from_build() =
        testSerialize(
            object : ComposeComponent() {
                override fun build() = Text("Test")
            },
            Text("Test")
        )

    @Test
    fun serialize_ScreenBuilder_should_write_the_component_from_build() =
        testSerialize(
            object : ScreenBuilder {
                override fun build() = Screen(child = Text("Test"))
            },
            Screen(child = Text("Test"))
        )

    private inline fun <reified T : Any> testSerialize(bean: Any, built: T) {
        val generator = mockk<JsonGenerator>()
        val builtSlot = slot<T>()

        every { generator.writeObject(capture(builtSlot)) } just Runs

        BeagleBuilderSerializer(mockk(relaxed = true), arrayOf(), arrayOf())
            .serialize(bean, generator, mockk())

        verify(exactly = 1) { generator.writeObject(any()) }
        assertEquals(built, builtSlot.captured)
    }
}
