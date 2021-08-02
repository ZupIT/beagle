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
import br.com.zup.beagle.android.action.Navigate
import br.com.zup.beagle.android.data.serializer.BaseSerializerTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a PopView Action")
class PopViewSerializerTest : BaseActionSerializerTest() {

    @DisplayName("When try to deserialize json PopView")
    @Nested
    inner class DeserializeJsonPopViewTest {

        @DisplayName("Then should return correct object")
        @Test
        fun testDeserializeJsonPopView() {
            // Given
            val json = makePopViewJson()

            // When
            val actual = deserialize(json)

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertTrue(actual is Navigate.PopView)
        }
    }

    @DisplayName("When try serialize object PopView")
    @Nested
    inner class SerializeObjectPopViewTest {

        @DisplayName("Then should return correct json")
        @Test
        fun testSerializeJsonPopView() {
            testSerializeObject(makePopViewJson(), makeObjectPopView())
        }
    }

    private fun makePopViewJson() = """
    {
        "_beagleAction_": "beagle:popview"
    }
"""

    private fun makeObjectPopView() = Navigate.PopView()
}