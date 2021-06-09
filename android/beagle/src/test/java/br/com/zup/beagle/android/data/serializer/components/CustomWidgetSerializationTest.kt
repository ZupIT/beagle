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

package br.com.zup.beagle.android.data.serializer.components

import br.com.zup.beagle.android.data.serializer.BeagleMoshi
import br.com.zup.beagle.android.mockdata.CustomWidget
import br.com.zup.beagle.android.mockdata.Person
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
    CustomWidget::class.java as Class<WidgetView>,
)

@DisplayName("Given a Moshi Adapter")
class CustomWidgetSerializationTest : BaseComponentSerializationTest() {

    @BeforeEach
    override fun setUp() {
        super.setUp()
        every { beagleSdk.registeredWidgets() } returns WIDGETS
        moshi = BeagleMoshi.createMoshi()
    }

    @DisplayName("When try to deserialize json CustomWidget")
    @Nested
    inner class DeserializeJsonCustomWidgetTest {

        @DisplayName("Then should return correct object")
        @Test
        fun testDeserializeJsonCustomWidget() {
            // Given
            val expectedComponent = makeObjectCustomWidget()
            val json = makeCustomWidgetJson()

            // When
            val actual = moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedComponent, actual)
        }
    }

    @DisplayName("When try serialize object CustomWidget")
    @Nested
    inner class SerializeObjectCustomWidgetTest {

        @DisplayName("Then should return correct json")
        @Test
        fun testSerializeJsonCustomWidget() {
            // Given
            val expectedJson = makeCustomWidgetJson().replace("\\s".toRegex(), "")
            val component = makeObjectCustomWidget()

            // When
            val actual = moshi.adapter(ServerDrivenComponent::class.java).toJson(component)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }
    }

    private fun makeCustomWidgetJson() = """
     {
     "_beagleComponent_": "custom:customwidget",
          "arrayList": [
                {
                  "names": [
                    "text"
                  ]
                }
          ],
          "pair": {
                "first": {
                  "names": [
                    "text"
                  ]
                },
                "second": "second"
          },
          "charSequence": "charSequence",
          "personInterface": "{
                  \"names\": [
                    \"text\"
                  ]
          }"
    }
"""

    private fun makeObjectCustomWidget() = CustomWidget(arrayListOf(Person(names = arrayListOf("text"))),
        Pair(Person(names = arrayListOf("text")), "second"), "charSequence",
        Person(names = arrayListOf("text")))
}