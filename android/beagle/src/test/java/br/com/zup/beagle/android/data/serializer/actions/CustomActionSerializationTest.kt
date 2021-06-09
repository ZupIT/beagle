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
import br.com.zup.beagle.android.data.serializer.BeagleMoshi
import br.com.zup.beagle.android.mockdata.CustomAndroidAction
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@Suppress("UNCHECKED_CAST")
private val ACTIONS = listOf(
    CustomAndroidAction::class.java as Class<Action>
)

@DisplayName("Given a Moshi Adapter")
class CustomActionSerializationTest : BaseActionSerializationTest() {

    @BeforeEach
    override fun setUp() {
        super.setUp()
        every { beagleSdk.registeredActions() } returns ACTIONS
        moshi = BeagleMoshi.createMoshi()
    }

    @DisplayName("When try to deserialize json CustomAction")
    @Nested
    inner class DeserializeJsonCustomActionTest {

        @DisplayName("Then should return correct object")
        @Test
        fun testDeserializeJsonCustomAction() {
            // Given
            val expectedAction = makeObjectCustomAction()
            val json = makeCustomActionJson()

            // When
            val actual = moshi.adapter(Action::class.java).fromJson(json)

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedAction, actual)
        }

    }

    @DisplayName("When try serialize object CustomAction")
    @Nested
    inner class SerializeObjectCustomActionTest {

        @DisplayName("Then should return correct json")
        @Test
        fun testSerializeJsonCustomAction() {
            // Given
            val expectedJson = makeCustomActionJson().replace("\\s".toRegex(), "")
            val action = makeObjectCustomAction()

            // When
            val actual = moshi.adapter(Action::class.java).toJson(action)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }
    }

    private fun makeCustomActionJson() = """
    {
        "_beagleAction_": "custom:customandroidaction",
        "value": "A value",
        "intValue": 123
    }
"""

    private fun makeObjectCustomAction() = CustomAndroidAction(
        value = "A value",
        intValue = 123
    )
}