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
import br.com.zup.beagle.android.components.form.SimpleForm
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.core.ServerDrivenComponent
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a Moshi Adapter")
class SimpleFormSerializationTest : BaseComponentSerializationTest() {

    @DisplayName("When try to deserialize json SimpleForm")
    @Nested
    inner class DeserializeJsonSimpleFormTest {

        @DisplayName("Then should return correct object")
        @Test
        fun testDeserializeJsonSimpleForm() {
            // Given
            val expectedComponent = makeObjectSimpleForm()
            val json = makeSimpleFormJson()

            // When
            val actual = moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedComponent, actual)
        }
    }

    @DisplayName("When try serialize object SimpleForm")
    @Nested
    inner class SerializeObjectSimpleFormTest {

        @DisplayName("Then should return correct json")
        @Test
        fun testSerializeJsonSimpleForm() {
            // Given
            val expectedJson = makeSimpleFormJson().replace("\\s".toRegex(), "")
            val component = makeObjectSimpleForm()

            // When
            val actual = moshi.adapter(ServerDrivenComponent::class.java).toJson(component)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }
    }

    private fun makeSimpleFormJson() = """
    {
        "_beagleComponent_": "beagle:simpleform",
        "context": {
            "id": "contextId",
            "value": true
        },
        "onSubmit": [${makeAlertActionJson()}],
        "children": [
        {
            "_beagleComponent_": "beagle:text",
            "text": "Test"
        }],
        "onValidationError": [${makeAlertActionJson()}]
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

    private fun makeObjectSimpleForm() = SimpleForm(
        context = ContextData(id = "contextId", value = true),
        onSubmit = listOf(makeObjectAlert()),
        children = listOf(Text(text = "Test")),
        onValidationError = listOf(makeObjectAlert()),
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