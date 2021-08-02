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
import br.com.zup.beagle.android.components.page.PageView
import br.com.zup.beagle.android.widget.UndefinedWidget
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given an UndefinedComponent")
class UndefinedComponentSerializerTest : BaseServerDrivenComponentSerializerTest() {

    @DisplayName("When try to deserialize json")
    @Nested
    inner class UndefinedComponentDeserializationTests {

        @DisplayName("Then should return correct object")
        @Test
        fun testUndefinedComponentDeserialization() {
            // Given
            val json = makeUndefinedComponentJson()

            // When
            val deserializedComponent = deserialize(json)

            // Then
            Assertions.assertNotNull(deserializedComponent)
            Assertions.assertTrue(deserializedComponent is UndefinedWidget)
        }

        @DisplayName("Then should return correct object with UndefinedWidget of type InputWidget")
        @Test
        fun testUndefinedComponentDeserializationWithInputWidget() {
            // Given
            val json = makeUndefinedComponentOfTypeInputWidgetJson()

            // When
            val deserializedComponent = deserialize(json) as FormInput

            // Then
            Assertions.assertNotNull(deserializedComponent)
            Assertions.assertTrue(deserializedComponent.child is UndefinedWidget)
        }

        @DisplayName("Then should return correct object with UndefinedWidget of type PageIndicator")
        @Test
        fun testUndefinedComponentDeserializationWithPageIndicator() {
            // Given
            val json = makeUndefinedComponentOfTypePageIndicatorComponentJson()

            // When
            val deserializedComponent = deserialize(json) as PageView

            // Then
            Assertions.assertNotNull(deserializedComponent)
            Assertions.assertTrue(deserializedComponent.pageIndicator is UndefinedWidget)
        }
    }

    @DisplayName("When try to serialize object")
    @Nested
    inner class UndefinedComponentSerializationTests {

        @DisplayName("Then should return correct json")
        @Test
        fun testUndefinedComponentSerialization() {
            testSerializeObject(
                makeUndefinedWidgetJson(),
                makeObjectUndefinedComponent()
            )
        }

        @DisplayName("Then should return correct json with UndefinedWidget of type InputWidget")
        @Test
        fun testUndefinedComponentSerializationWithInputWidget() {
            testSerializeObject(
                makeUndefinedComponentOfTypeInputWidgetJson(),
                makeObjectUndefinedComponentOfTypeInputWidget()
            )
        }

        @DisplayName("Then should return correct json with UndefinedWidget of type PageIndicator")
        @Test
        fun testUndefinedComponentSerializationWithPageIndicator() {
            testSerializeObject(
                makeUndefinedComponentOfTypePageIndicatorComponentJson(),
                makeObjectUndefinedComponentOfTypePageIndicatorComponent()
            )
        }
    }

    private fun makeUndefinedComponentJson() = """
    {
        "_beagleComponent_": "custom:new"
    }
"""

    private fun makeUndefinedWidgetJson() = """
    {
        "_beagleComponent_": "beagle:undefinedwidget"
    }
"""

    private fun makeUndefinedComponentOfTypeInputWidgetJson() = """
    {
        "_beagleComponent_":"beagle:forminput",
        "name":"name",
        "child":{
            "_beagleComponent_":"beagle:undefinedwidget"
        }
    }
"""

    private fun makeUndefinedComponentOfTypePageIndicatorComponentJson() = """
    {
        "_beagleComponent_":"beagle:pageview",
        "children":[],
        "pageIndicator":{
            "_beagleComponent_":"beagle:undefinedwidget"
        }
    }
"""

    private fun makeObjectUndefinedComponent() = UndefinedWidget()

    private fun makeObjectUndefinedComponentOfTypeInputWidget() = FormInput(
        name = "name",
        child = UndefinedWidget()
    )

    private fun makeObjectUndefinedComponentOfTypePageIndicatorComponent() = PageView(
        children = listOf(),
        pageIndicator = UndefinedWidget()
    )
}