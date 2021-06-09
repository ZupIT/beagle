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

import br.com.zup.beagle.android.action.FormMethodType
import br.com.zup.beagle.android.action.FormRemoteAction
import br.com.zup.beagle.android.components.Button
import br.com.zup.beagle.android.components.form.Form
import br.com.zup.beagle.core.ServerDrivenComponent
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a Moshi Adapter")
class FormSerializationTest : BaseComponentSerializationTest() {

    @DisplayName("When try to deserialize json Form")
    @Nested
    inner class DeserializeJsonFormTest {

        @DisplayName("Then should return correct object")
        @Test
        fun testDeserializeJsonForm() {
            // Given
            val expectedComponent = makeObjectForm()
            val json = makeFormJson()

            // When
            val actual = moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedComponent, actual)
        }
    }

    @DisplayName("When try serialize object Form")
    @Nested
    inner class SerializeObjectFormTest {

        @DisplayName("Then should return correct json")
        @Test
        fun testSerializeJsonForm() {
            // Given
            val expectedJson = makeFormJson().replace("\\s".toRegex(), "")
            val component = makeObjectForm()

            // When
            val actual = moshi.adapter(ServerDrivenComponent::class.java).toJson(component)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }
    }

    private fun makeFormJson() = """
    {
        "_beagleComponent_": "beagle:form",
        "child": {
            "_beagleComponent_": "beagle:button",
            "text": "Test"
        },
        "onSubmit": [{
            "_beagleAction_": "beagle:formremoteaction",
            "path": "http://test.com",
            "method": "POST"
        }],
        "group": "A group",
        "additionalData":{"test" : "test"},
        "shouldStoreFields": true
    }
"""

    private fun makeObjectForm() = Form(
        child = Button(
            text = "Test"
        ),
        onSubmit = listOf(
            FormRemoteAction(
                path = "http://test.com",
                method = FormMethodType.POST
            )
        ),
        group = "A group",
        additionalData = mapOf("test" to "test"),
        shouldStoreFields = true
    )
}