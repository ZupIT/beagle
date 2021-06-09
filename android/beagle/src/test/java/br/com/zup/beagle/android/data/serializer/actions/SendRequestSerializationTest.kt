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
import br.com.zup.beagle.android.action.RequestActionMethod
import br.com.zup.beagle.android.action.SendRequest
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.android.context.valueOf
import org.json.JSONObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a Moshi Adapter")
class SendRequestSerializationTest : BaseActionSerializationTest() {
    @DisplayName("When try to deserialize json SendRequest")
    @Nested
    inner class DeserializeJsonSendRequestTest {

        @DisplayName("Then should return correct object")
        @Test
        fun testDeserializeJsonSendRequest() {
            // Given
            val expectedAction = makeObjectSendRequest()
            val json = makeSendRequestJson()

            // When
            val actual = moshi.adapter(Action::class.java).fromJson(json) as SendRequest

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedAction.url, actual.url)
            Assertions.assertEquals(expectedAction.method, actual.method)
            Assertions.assertEquals(expectedAction.headers, actual.headers)
            val expectedData = expectedAction.data as JSONObject
            val actualData = actual.data as JSONObject
            Assertions.assertEquals(expectedData.getBoolean("a"), actualData.getBoolean("a"))
            Assertions.assertEquals(expectedData.getString("b"), actualData.getString("b"))
            Assertions.assertEquals(expectedAction.onSuccess, actual.onSuccess)
            Assertions.assertEquals(expectedAction.onError, actual.onError)
            Assertions.assertEquals(expectedAction.onFinish, actual.onFinish)
        }

        @DisplayName("Then should return correct object without expression")
        @Test
        fun testDeserializeJsonSendRequestWithoutExpression() {
            // Given
            val expectedAction = makeObjectSendRequestWithoutExpression()
            val json = makeSendRequestWithoutExpressionJson()

            // When
            val actual = moshi.adapter(Action::class.java).fromJson(json) as SendRequest

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedAction.url, actual.url)
            Assertions.assertEquals(expectedAction.method, actual.method)
            Assertions.assertEquals(expectedAction.headers, actual.headers)
            val expectedData = expectedAction.data as JSONObject
            val actualData = actual.data as JSONObject
            Assertions.assertEquals(expectedData.getBoolean("a"), actualData.getBoolean("a"))
            Assertions.assertEquals(expectedData.getString("b"), actualData.getString("b"))
            Assertions.assertEquals(expectedAction.onSuccess, actual.onSuccess)
            Assertions.assertEquals(expectedAction.onError, actual.onError)
            Assertions.assertEquals(expectedAction.onFinish, actual.onFinish)
        }

    }

    @DisplayName("When try serialize object SendRequest")
    @Nested
    inner class SerializeObjectSendRequestTest {

        @DisplayName("Then should return correct json")
        @Test
        fun testSerializeJsonSendRequest() {
            // Given
            val expectedJson = makeSendRequestJson().replace("\\s".toRegex(), "")
            val action = makeObjectSendRequest()

            // When
            val actual = moshi.adapter(Action::class.java).toJson(action)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }

        @DisplayName("Then should return correct json without expression")
        @Test
        fun testSerializeJsonSendRequestWithoutExpression() {
            // Given
            val expectedJson = makeSendRequestWithoutExpressionJson().replace("\\s".toRegex(), "")
            val action = makeObjectSendRequestWithoutExpression()

            // When
            val actual = moshi.adapter(Action::class.java).toJson(action)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }
    }

    private fun makeSendRequestJson() = """
    {
        "_beagleAction_": "beagle:sendrequest",
        "url": "@{test.url}",
        "method": "@{test.method}",
        "headers": "@{test.headers}",
        "data": {
            "a": true,
            "b": "a"
        },
        "onSuccess":[${makeAlertActionJson()}],
        "onError":[${makeAlertActionJson()}],
        "onFinish":[${makeAlertActionJson()}]
    }
"""

    private fun makeSendRequestWithoutExpressionJson() = """
    {
        "_beagleAction_": "beagle:sendrequest",
        "url": "http://test.com",
        "method": "GET",
        "headers": {"test" : "test"},
        "data": {
            "a": true,
            "b": "a"
        },
        "onSuccess":[${makeAlertActionJson()}],
        "onError":[${makeAlertActionJson()}],
        "onFinish":[${makeAlertActionJson()}]
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

    private fun makeObjectSendRequest() = SendRequest(
        url = expressionOf("@{test.url}"),
        method = expressionOf("@{test.method}"),
        headers = expressionOf("@{test.headers}"),
        data = JSONObject()
            .put("a", true)
            .put("b", "a"),
        onSuccess = listOf(
            makeObjectAlert()
        ),
        onError = listOf(
            makeObjectAlert()
        ),
        onFinish = listOf(
            makeObjectAlert()
        )
    )

    private fun makeObjectSendRequestWithoutExpression() = SendRequest(
        url = valueOf("http://test.com"),
        method = valueOf(RequestActionMethod.GET),
        headers = valueOf(mapOf("test" to "test")),
        data = JSONObject()
            .put("a", true)
            .put("b", "a"),
        onSuccess = listOf(
            makeObjectAlert()
        ),
        onError = listOf(
            makeObjectAlert()
        ),
        onFinish = listOf(
            makeObjectAlert()
        )
    )

    private fun makeObjectAlert() = Alert(
        title = "A title",
        message = "A message",
        onPressOk = Alert(
            title = "Another title",
            message = "Another message",
            labelOk = "Ok"
        ),
        labelOk = "Ok"
    )
}