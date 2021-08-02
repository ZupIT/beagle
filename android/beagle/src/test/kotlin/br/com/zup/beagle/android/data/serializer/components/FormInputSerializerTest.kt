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

import br.com.zup.beagle.android.components.form.FormInput
import br.com.zup.beagle.android.data.serializer.BeagleMoshi
import br.com.zup.beagle.android.mockdata.CustomInputWidget
import br.com.zup.beagle.android.widget.WidgetView
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@Suppress("UNCHECKED_CAST")
private val WIDGETS = listOf(
    CustomInputWidget::class.java as Class<WidgetView>,
)

@DisplayName("Given a FormInput")
class FormInputSerializerTest : BaseServerDrivenComponentSerializerTest() {

    @BeforeEach
    override fun setUp() {
        super.setUp()
        every { beagleSdk.registeredWidgets() } returns WIDGETS
        moshi = BeagleMoshi.createMoshi()
    }

    @DisplayName("When try to deserialize json")
    @Nested
    inner class FormInputDeserializationTests {

        @DisplayName("Then should return correct object")
        @Test
        fun testFormInputDeserialization() {
            // Given
            val json = makeFormInputJson()

            // When
            val deserializedComponent = deserialize(json)

            // Then
            Assertions.assertNotNull(deserializedComponent)
            Assertions.assertTrue(deserializedComponent is FormInput)
        }
    }

    @DisplayName("When try to serialize object")
    @Nested
    inner class FormInputSerializationTests {

        @DisplayName("Then should return correct json")
        @Test
        fun testFormInputSerialization() {
            testSerializeObject(makeFormInputJson(), makeObjectFormInput())
        }
    }

    private fun makeFormInputJson() = """
    {
        "_beagleComponent_": "beagle:forminput",
        "name": "An input name",
        "required": false,
        "validator": "A validator",
        "errorMessage": "An error message",
        "child": ${makeCustomInputWidgetJson()}
    }
"""

    private fun makeCustomInputWidgetJson() = """
    {
        "_beagleComponent_": "custom:custominputwidget"
    }        
"""

    private fun makeObjectFormInput() = FormInput(
        name = "An input name",
        required = false,
        validator = "A validator",
        errorMessage = "An error message",
        child = makeObjectCustomInputWidget()
    )

    private fun makeObjectCustomInputWidget() = CustomInputWidget()
}