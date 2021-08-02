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

package br.com.zup.beagle.android.data.serializer.context

import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.android.context.valueOf
import br.com.zup.beagle.android.data.serializer.BaseSerializerTest
import br.com.zup.beagle.android.data.serializer.BeagleMoshi
import br.com.zup.beagle.android.mockdata.ComponentBinding
import br.com.zup.beagle.android.mockdata.InternalObject
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.core.ServerDrivenComponent
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@Suppress("UNCHECKED_CAST")
private val WIDGETS = listOf(
    ComponentBinding::class.java as Class<WidgetView>
)

@DisplayName("Given a Moshi Adapter")
class BindSerializationTest : BaseSerializerTest<ServerDrivenComponent>(ServerDrivenComponent::class.java) {

    @BeforeEach
    override fun setUp() {
        super.setUp()
        every { beagleSdk.registeredWidgets() } returns WIDGETS
        moshi = BeagleMoshi.createMoshi()
    }

    @DisplayName("When try to deserialize json Bind")
    @Nested
    inner class DeserializeJsonBindTest {

        @DisplayName("Then should return a ComponentBinding with expressions")
        @Test
        fun testDeserializeJsonComponentBindingWithExpressions() {
            testDeserializeJson(makeComponentBindingWithExpressionsJson(), makeObjectComponentBindingWithExpressions())
        }

        @DisplayName("Then should return a ComponentBinding without expressions")
        @Test
        fun testDeserializeJsonComponentBindingWithoutExpressions() {
            // Given
            val expectedComponent = makeObjectComponentBindingWithoutExpressions()
            val jsonComponent = makeComponentBindingWithoutExpressionsJson()

            // When
            val component = deserialize(jsonComponent)

            // Then
            Assertions.assertEquals(expectedComponent, component)
            val bindComponent = component as ComponentBinding
            Assertions.assertNull(bindComponent.value1)
            Assertions.assertEquals(expectedComponent.value2.value, bindComponent.value2.value)
            Assertions.assertTrue(bindComponent.value2 is Bind.Value<String>)
            Assertions.assertEquals(String::class.java, bindComponent.value2.type)
            Assertions.assertEquals(expectedComponent.value3.value, bindComponent.value3.value)
            Assertions.assertTrue(bindComponent.value3 is Bind.Value<Boolean>)
            Assertions.assertEquals(Boolean::class.javaObjectType, bindComponent.value3.type)
            Assertions.assertNotNull(bindComponent.value4.value)
            Assertions.assertEquals(InternalObject::class.java, bindComponent.value4.type)
            Assertions.assertEquals(expectedComponent.value5.value, bindComponent.value5.value)
            Assertions.assertEquals(expectedComponent.value6.value, bindComponent.value6.value)
        }

        @DisplayName("Then should return an InternalObject deserialized correctly")
        @Test
        fun testDeserializeInternalObjectUsingComponentTypeAttribute() {
            // Given
            val jsonComponent = makeComponentBindingWithoutExpressionsJson()
            val internalObjectJson = makeInternalObject()

            // When
            val bindComponent = deserialize(jsonComponent) as ComponentBinding
            val internalObject = moshi.adapter<Any>(bindComponent.value4.type).fromJson(internalObjectJson) as InternalObject

            // Then
            Assertions.assertEquals("hello", internalObject.value1)
            Assertions.assertEquals(123, internalObject.value2)
        }

    }

    @DisplayName("When try serialize object Bind")
    @Nested
    inner class SerializeObjectBindTest {

        @DisplayName("Then should return correct json with expressions")
        @Test
        fun testSerializeJsonComponentBindingWithExpressions() {
            testSerializeObject(
                makeComponentBindingWithExpressionsJson(),
                makeObjectComponentBindingWithExpressions()
            )
        }

        @DisplayName("Then should return correct json without expressions")
        @Test
        fun testSerializeJsonComponentBindingWithoutExpressions() {
            testSerializeObject(
                makeComponentBindingWithoutExpressionsJson(),
                makeObjectComponentBindingWithoutExpressions()
            )
        }
    }

    private fun makeComponentBindingWithExpressionsJson() = """
    {
        "_beagleComponent_": "custom:componentbinding",
        "value1": "@{intExpression}",
        "value2": "Hello @{context.name}",
        "value3": "@{booleanExpression}",
        "value4": "@{objectExpression}",
        "value5": "@{mapExpression}",
        "value6": "@{listExpression}"
    }
"""

    private fun makeComponentBindingWithoutExpressionsJson() = """
    {
        "_beagleComponent_": "custom:componentbinding",
        "value2": "Hello",
        "value3": true,
        "value4": ${makeInternalObject()},
        "value5": {"test1":"a","test2":"b"},
        "value6": ["test1", "test2"]
    }
"""

    private fun makeInternalObject() = """{"value1": "hello", "value2": 123}"""

    private fun makeObjectComponentBindingWithExpressions() = ComponentBinding(
        value1 = expressionOf("@{intExpression}"),
        value2 = expressionOf("Hello @{context.name}"),
        value3 = expressionOf("@{booleanExpression}"),
        value4 = expressionOf("@{objectExpression}"),
        value5 = expressionOf("@{mapExpression}"),
        value6 = expressionOf("@{listExpression}")
    )

    private fun makeObjectComponentBindingWithoutExpressions() = ComponentBinding(
        value1 = null,
        value2 = valueOf("Hello"),
        value3 = valueOf(true),
        value4 = valueOf(InternalObject(value1 = "hello", value2 = 123)),
        value5 = valueOf(mapOf("test1" to "a", "test2" to "b")),
        value6 = valueOf(listOf("test1", "test2"))
    )
}