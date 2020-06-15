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
import br.com.zup.beagle.widget.action.Navigate
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.Widget
import br.com.zup.beagle.widget.layout.ComposeComponent
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScreenBuilder
import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass
import kotlin.test.assertTrue

internal class BeagleSerializerModifierTest {
    @Test
    fun modifySerializer_for_non_Beagle_type_should_return_the_input_serializer() =
        testModifySerializer(
            clazz = Any::class.java,
            expectedSerializerClass = BeanSerializerBase::class,
            compareSerializers = Assertions::assertSame
        )

    @Test
    fun modifySerializer_for_Action_should_return_BeagleTypeSerializer() =
        testModifySerializer(
            clazz = Action::class.java,
            expectedSerializerClass = BeagleTypeSerializer::class,
            compareSerializers = Assertions::assertNotSame
        )

    @Test
    fun modifySerializer_for_Action_subtype_should_return_BeagleTypeSerializer() =
        testModifySerializer(
            clazz = Navigate::class.java,
            expectedSerializerClass = BeagleTypeSerializer::class,
            compareSerializers = Assertions::assertNotSame
        )

    @Test
    fun modifySerializer_for_Screen_should_return_BeagleTypeSerializer() =
        testModifySerializer(
            clazz = Screen::class.java,
            expectedSerializerClass = BeagleTypeSerializer::class,
            compareSerializers = Assertions::assertNotSame
        )

    @Test
    fun modifySerializer_for_ServerDrivenComponent_should_return_BeagleTypeSerializer() =
        testModifySerializer(
            clazz = ServerDrivenComponent::class.java,
            expectedSerializerClass = BeagleTypeSerializer::class,
            compareSerializers = Assertions::assertNotSame
        )

    @Test
    fun modifySerializer_for_ServerDrivenComponent_subtype_should_return_BeagleTypeSerializer() =
        testModifySerializer(
            clazz = Widget::class.java,
            expectedSerializerClass = BeagleTypeSerializer::class,
            compareSerializers = Assertions::assertNotSame
        )

    private fun testModifySerializer(
        clazz: Class<*>,
        expectedSerializerClass: KClass<*>,
        compareSerializers: (BeanSerializerBase, JsonSerializer<*>) -> Unit
    ) {
        val description = mockk<BeanDescription>()
        val serializer = mockk<BeanSerializerBase>()

        every { description.beanClass } returns clazz

        val result = BeagleSerializerModifier(BeagleSerializerModifier::class.java.classLoader)
            .modifySerializer(mockk(), description, serializer)

        compareSerializers(serializer, result)
        assertTrue { expectedSerializerClass.isInstance(result) }
    }

    abstract class MyComposeComponent : ComposeComponent

    abstract class MyScreenBuilder : ScreenBuilder
}