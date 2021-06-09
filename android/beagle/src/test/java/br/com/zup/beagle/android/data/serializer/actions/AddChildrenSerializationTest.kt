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

package br.com.zup.beagle.android.data.serializer.actions

import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.action.AddChildren
import br.com.zup.beagle.android.action.Mode
import br.com.zup.beagle.android.components.Text
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a Moshi Adapter")
class AddChildrenSerializationTest : BaseActionSerializationTest() {
    @DisplayName("When try to deserialize json AddChildren")
    @Nested
    inner class DeserializeJsonAddChildrenTest {

        @DisplayName("Then should return correct object")
        @Test
        fun testDeserializeJsonAddChildren() {
            // Given
            val expectedAction = makeObjectAddChildren()
            val json = makeAddChildrenJson()

            // When
            val actual = moshi.adapter(Action::class.java).fromJson(json)

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedAction, actual)
        }

    }

    @DisplayName("When try serialize object AddChildren")
    @Nested
    inner class SerializeObjectAddChildrenTest {

        @DisplayName("Then should return correct json")
        @Test
        fun testSerializeJsonAddChildren() {
            // Given
            val expectedJson = makeAddChildrenJson().replace("\\s".toRegex(), "")
            val action = makeObjectAddChildren()

            // When
            val actual = moshi.adapter(Action::class.java).toJson(action)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }
    }

    private fun makeAddChildrenJson() = """
    {
        "_beagleAction_":"beagle:addchildren",
        "componentId":"id",
        "value":[
           {
              "_beagleComponent_":"beagle:text",
              "text":"Test"
           }
        ],
        "mode":"APPEND"
    }
"""

    private fun makeObjectAddChildren() = AddChildren(
        componentId = "id",
        value = listOf(
            Text(
                text = "Test"
            )
        ),
        mode = Mode.APPEND
    )
}