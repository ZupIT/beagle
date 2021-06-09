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

import br.com.zup.beagle.android.components.Button
import br.com.zup.beagle.android.components.form.FormSubmit
import br.com.zup.beagle.core.ServerDrivenComponent
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a Moshi Adapter")
class FormSubmitSerializationTest : BaseComponentSerializationTest() {

    @DisplayName("When try to deserialize json FormSubmit")
    @Nested
    inner class DeserializeJsonFormSubmitTest {

        @DisplayName("Then should return correct object")
        @Test
        fun testDeserializeJsonFormSubmit() {
            // Given
            val expectedComponent = makeObjectFormSubmit()
            val json = makeFormSubmitJson()

            // When
            val actual = moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedComponent, actual)
        }
    }

    @DisplayName("When try serialize object FormSubmit")
    @Nested
    inner class SerializeObjectFormSubmitTest {

        @DisplayName("Then should return correct json")
        @Test
        fun testSerializeJsonFormSubmit() {
            // Given
            val expectedJson = makeFormSubmitJson().replace("\\s".toRegex(), "")
            val component = makeObjectFormSubmit()

            // When
            val actual = moshi.adapter(ServerDrivenComponent::class.java).toJson(component)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }
    }

    private fun makeFormSubmitJson() = """
    {
        "_beagleComponent_": "beagle:formsubmit",
        "child": {
            "_beagleComponent_": "beagle:button",
            "text": "Test"
        },
        "enabled": true
    }
"""

    private fun makeObjectFormSubmit() = FormSubmit(
        enabled = true,
        child = Button(
            text = "Test"
        )
    )
}