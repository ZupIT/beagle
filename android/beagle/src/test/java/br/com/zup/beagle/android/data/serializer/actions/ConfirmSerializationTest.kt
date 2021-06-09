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
import br.com.zup.beagle.android.action.Alert
import br.com.zup.beagle.android.action.Confirm
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a Moshi Adapter")
class ConfirmSerializationTest : BaseActionSerializationTest() {
    @DisplayName("When try to deserialize json Confirm")
    @Nested
    inner class DeserializeJsonConfirmTest {

        @DisplayName("Then should return correct object")
        @Test
        fun testDeserializeJsonConfirm() {
            // Given
            val expectedAction = makeObjectConfirm()
            val json = makeConfirmJson()

            // When
            val actual = moshi.adapter(Action::class.java).fromJson(json)

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedAction, actual)
        }

    }

    @DisplayName("When try serialize object Confirm")
    @Nested
    inner class SerializeObjectConfirmTest {

        @DisplayName("Then should return correct json")
        @Test
        fun testSerializeJsonConfirm() {
            // Given
            val expectedJson = makeConfirmJson().replace("\\s".toRegex(), "")
            val action = makeObjectConfirm()

            // When
            val actual = moshi.adapter(Action::class.java).toJson(action)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }
    }

    private fun makeConfirmJson() = """
    {
        "_beagleAction_": "beagle:confirm",
        "title": "A title",
        "message": "A message",
        "onPressOk": {
             "_beagleAction_": "beagle:alert",
             "title": "Another title",
             "message": "Another message",
             "labelOk": "Ok"
        },
        "onPressCancel": {
             "_beagleAction_": "beagle:alert",
             "title": "Some title",
             "message": "Some message",
             "labelOk": "Ok"
        },
        "labelOk": "Ok",
        "labelCancel": "Cancel"
    }
"""

    private fun makeObjectConfirm() = Confirm(
        title = "A title",
        message = "A message",
        onPressOk = Alert(
            title = "Another title",
            message = "Another message",
            labelOk = "Ok"
        ),
        labelOk = "Ok",
        onPressCancel = Alert(
            title = "Some title",
            message = "Some message",
            labelOk = "Ok"
        ),
        labelCancel = "Cancel",
    )
}