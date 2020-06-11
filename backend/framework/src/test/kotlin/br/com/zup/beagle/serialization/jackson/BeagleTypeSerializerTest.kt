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

import br.com.zup.beagle.action.Navigate
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.widget.Widget
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.ui.Text
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertNotSame
import kotlin.test.assertTrue

internal class BeagleTypeSerializerTest {
    @Test
    fun withObjectIdWriter_should_return_new_BeagleTypeSerializer() {
        val serializer = BeagleTypeSerializer(mockk())

        val result = serializer.withObjectIdWriter(mockk())

        assertNotSame(serializer, result)
        assertTrue(block = result::usesObjectId)
    }

    @Test
    fun withFilterId_should_return_new_BeagleTypeSerializer() {
        val serializer = BeagleTypeSerializer(mockk())

        val result = serializer.withFilterId("Test")

        assertNotSame(serializer, result)
    }

    @Test
    fun serialize_non_beagle_type_should_have_beagleType_field_null() = testSerialize("Text") {
        verify(exactly = 0) { it.writeStringField(COMPONENT_TYPE, any()) }
    }

    @Test
    fun serialize_beagle_native_ServerDrivenComponent_should_have_component_beagleType_field_with_beagle_prefix() =
        testComponentSerialize(Text("test"), "$BEAGLE_NAMESPACE:text")

    @Test
    fun serialize_custom_ServerDrivenComponent_should_have_component_beagleType_field_with_custom_prefix() =
        testComponentSerialize(CustomWidget, "$CUSTOM_WIDGET_BEAGLE_NAMESPACE:customWidget")

    @Test
    fun serialize_Action_should_have_action_beagleType_field() = testSerialize(Navigate.PopStack()) {
        verify(exactly = 1) { it.writeStringField(ACTION_TYPE, "$BEAGLE_NAMESPACE:popStack") }
    }

    @Test
    fun serialize_Screen_should_have_screen_beagleType_field() =
        testComponentSerialize(Screen(child = CustomWidget), "$BEAGLE_NAMESPACE:$SCREEN_COMPONENT")

    private fun testSerialize(bean: Any, verify: (JsonGenerator) -> Unit) {
        val generator = mockk<JsonGenerator>(relaxUnitFun = true)
        val provider = mockk<SerializerProvider>()

        every { provider.activeView } returns Any::class.java

        BeagleTypeSerializer(mockk(relaxed = true), arrayOf(), arrayOf()).serialize(bean, generator, provider)

        verify(generator)
    }

    private fun testComponentSerialize(bean: Any, beagleType: String) = testSerialize(bean) {
        verify(exactly = 1) { it.writeStringField(COMPONENT_TYPE, beagleType) }
    }

    @RegisterWidget
    private object CustomWidget : Widget()
}
