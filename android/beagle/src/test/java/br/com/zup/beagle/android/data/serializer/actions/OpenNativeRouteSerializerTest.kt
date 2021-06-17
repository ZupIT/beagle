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
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.android.data.serializer.BaseSerializerTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given an OpenNativeRoute Action")
class OpenNativeRouteSerializerTest : BaseActionSerializerTest() {

    @DisplayName("When try to deserialize json OpenNativeRoute")
    @Nested
    inner class DeserializeJsonOpenNativeRouteTest {

        @DisplayName("Then should return correct object")
        @Test
        fun testDeserializeJsonOpenNativeRoute() {
            // Given
            val expectedAction = makeObjectOpenNativeRoute()
            val json = makeOpenNativeRouteJson()

            // When
            val actual = deserialize(json) as Navigate.OpenNativeRoute

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedAction.route, actual.route)
            Assertions.assertEquals(expectedAction.shouldResetApplication, actual.shouldResetApplication)
            Assertions.assertEquals(expectedAction.data, actual.data)
        }

        @DisplayName("Then should return correct object with expression")
        @Test
        fun testDeserializeJsonOpenNativeRouteWithExpression() {
            // Given
            val expectedAction = makeObjectOpenNativeRouteWithExpression()
            val json = makeOpenNativeRouteJsonWithExpression()

            // When
            val actual = deserialize(json) as Navigate.OpenNativeRoute

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedAction.route, actual.route)
            Assertions.assertEquals(expectedAction.shouldResetApplication, actual.shouldResetApplication)
            Assertions.assertEquals(expectedAction.data, actual.data)
        }
    }

    @DisplayName("When try serialize object OpenNativeRoute")
    @Nested
    inner class SerializeObjectOpenNativeRouteTest {

        @DisplayName("Then should return correct json")
        @Test
        fun testSerializeJsonOpenNativeRoute() {
            testSerializeObject(makeOpenNativeRouteJson(), makeObjectOpenNativeRoute())
        }

        @DisplayName("Then should return correct json with expression")
        @Test
        fun testSerializeJsonOpenNativeRouteWithExpression() {
            testSerializeObject(
                makeOpenNativeRouteJsonWithExpression(),
                makeObjectOpenNativeRouteWithExpression()
            )
        }
    }

    private fun makeOpenNativeRouteJson() = """
    {
        "_beagleAction_": "beagle:opennativeroute",
        "route": "test",
        "shouldResetApplication": true,
        "data": {"test" : "test"}
    }
"""

    private fun makeOpenNativeRouteJsonWithExpression() = """
    {
        "_beagleAction_": "beagle:opennativeroute",
        "route": "@{test}",
        "shouldResetApplication": true,
        "data": {"test" : "test"}
    }
"""

    private fun makeObjectOpenNativeRoute() = Navigate.OpenNativeRoute(
        route = "test",
        shouldResetApplication = true,
        data = mapOf("test" to "test")
    )

    private fun makeObjectOpenNativeRouteWithExpression() = Navigate.OpenNativeRoute(
        route = expressionOf("@{test}"),
        shouldResetApplication = true,
        data = mapOf("test" to "test")
    )
}