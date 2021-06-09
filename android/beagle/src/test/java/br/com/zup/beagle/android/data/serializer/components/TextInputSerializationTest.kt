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
import br.com.zup.beagle.android.components.TextInput
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.core.TextInputType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a Moshi Adapter")
class TextInputInputSerializationTest : BaseComponentSerializationTest() {

    @DisplayName("When try to deserialize json TextInput")
    @Nested
    inner class DeserializeJsonTextInputTest {

        @DisplayName("Then should return correct object")
        @Test
        fun testDeserializeJsonTextInput() {
            // Given
            val expectedComponent = makeObjectTextInput()
            val json = makeTextInputJson()

            // When
            val actual = moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedComponent, actual)
        }

        @DisplayName("Then should return correct object with expression")
        @Test
        fun testDeserializeJsonTextInputWithExpression() {
            // Given
            val expectedComponent = makeObjectTextInputWithExpression()
            val json = makeTextInputWithExpressionJson()

            // When
            val actual = moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedComponent, actual)
        }
    }

    @DisplayName("When try serialize object TextInput")
    @Nested
    inner class SerializeObjectTextInputTest {

        @DisplayName("Then should return correct json")
        @Test
        fun testSerializeJsonTextInput() {
            // Given
            val expectedJson = makeTextInputJson().replace("\\s".toRegex(), "")
            val component = makeObjectTextInput()

            // When
            val actual = moshi.adapter(ServerDrivenComponent::class.java).toJson(component)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }

        @DisplayName("Then should return correct json with expression")
        @Test
        fun testSerializeJsonTextInputWithExpression() {
            // Given
            val expectedJson = makeTextInputWithExpressionJson().replace("\\s".toRegex(), "")
            val component = makeObjectTextInputWithExpression()

            // When
            val actual = moshi.adapter(ServerDrivenComponent::class.java).toJson(component)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }
    }

    private fun makeTextInputJson() = """
    {
        "_beagleComponent_": "beagle:textinput",
        "value": "value",
        "placeholder": "placeholder",
        "readOnly": false,
        "type": "EMAIL",
        "error": "error",
        "showError": true,
        "styleId": "styleId",
        "onChange": [${makeAlertActionJson()}],
        "onFocus": [${makeAlertActionJson()}],
        "onBlur": [${makeAlertActionJson()}],
        "enabled": false
    }
"""

    private fun makeTextInputWithExpressionJson() = """
    {
        "_beagleComponent_": "beagle:textinput",
        "value": "@{value}",
        "placeholder": "@{placeholder}",
        "readOnly": "@{false}",
        "type": "@{EMAIL}",
        "error": "@{error}",
        "showError": "@{true}",
        "styleId": "styleId",
        "onChange": [${makeAlertActionJson()}],
        "onFocus": [${makeAlertActionJson()}],
        "onBlur": [${makeAlertActionJson()}],
        "enabled": "@{false}"
    }
"""

    private fun makeAlertActionJson() = """
    {
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
    }
"""

    private fun makeObjectTextInput() = TextInput(
        value = "value",
        placeholder = "placeholder",
        readOnly = false,
        type = TextInputType.EMAIL,
        error = "error",
        showError = true,
        styleId = "styleId",
        onChange = listOf(
            makeObjectAlert()
        ),
        onFocus = listOf(
            makeObjectAlert()
        ),
        onBlur = listOf(
            makeObjectAlert()
        ),
        enabled = false
    )

    private fun makeObjectTextInputWithExpression() = TextInput(
        value = expressionOf("@{value}"),
        placeholder = expressionOf("@{placeholder}"),
        readOnly = expressionOf("@{false}"),
        type = expressionOf("@{EMAIL}"),
        error = expressionOf("@{error}"),
        showError = expressionOf("@{true}"),
        styleId = "styleId",
        onChange = listOf(
            makeObjectAlert()
        ),
        onFocus = listOf(
            makeObjectAlert()
        ),
        onBlur = listOf(
            makeObjectAlert()
        ),
        enabled = expressionOf("@{false}")
    )

    private fun makeObjectAlert() = Alert(
        title = "A title",
        message = "A message",
        labelOk = "Ok",
        onPressOk = Alert(
            title = "Another title",
            message = "Another message",
            labelOk = "Ok"
        )
    )
}