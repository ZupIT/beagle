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

import br.com.zup.beagle.android.action.Alert
import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.android.components.Touchable
import br.com.zup.beagle.core.ServerDrivenComponent
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a Moshi Adapter")
class TouchableSerializationTest : BaseComponentSerializationTest() {

    @DisplayName("When try to deserialize json Touchable")
    @Nested
    inner class DeserializeJsonTouchableTest {

        @DisplayName("Then should return correct object")
        @Test
        fun testDeserializeJsonTouchable() {
            // Given
            val expectedComponent = makeObjectTouchable()
            val json = makeTouchableJson()

            // When
            val actual = moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedComponent, actual)
        }
    }

    @DisplayName("When try serialize object Touchable")
    @Nested
    inner class SerializeObjectTouchableTest {

        @DisplayName("Then should return correct json")
        @Test
        fun testSerializeJsonTouchable() {
            // Given
            val expectedJson = makeTouchableJson().replace("\\s".toRegex(), "")
            val component = makeObjectTouchable()

            // When
            val actual = moshi.adapter(ServerDrivenComponent::class.java).toJson(component)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }
    }

    private fun makeTouchableJson() = """
    {
        "_beagleComponent_": "beagle:touchable",
        "onPress": [{
            "_beagleAction_": "beagle:alert",
            "title": "A title",
            "message": "A message",
            "onPressOk": {
                 "_beagleAction_": "beagle:alert",
                 "title": "Another title",
                 "message": "Another message",
                 "labelOk": "Ok"
            },
            "labelOk": "Ok"
        }],
        "child": {
            "_beagleComponent_": "beagle:text",
            "text": "Test"
        }
    }
"""

    private fun makeObjectTouchable() = Touchable(
        onPress = listOf(
            Alert(
                title = "A title",
                message = "A message",
                labelOk = "Ok",
                onPressOk = Alert(
                    title = "Another title",
                    message = "Another message",
                    labelOk = "Ok"
                )
            )
        ),
        child = Text(text = "Test")
    )
}