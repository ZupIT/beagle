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

import br.com.zup.beagle.android.components.WebView
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.core.ServerDrivenComponent
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a Moshi Adapter")
class WebViewSerializationTest : BaseComponentSerializationTest() {

    @DisplayName("When try to deserialize json WebView")
    @Nested
    inner class DeserializeJsonWebViewTest {

        @DisplayName("Then should return correct object")
        @Test
        fun testDeserializeJsonWebView() {
            // Given
            val expectedComponent = makeObjectWebView()
            val json = makeWebViewJson()

            // When
            val actual = moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedComponent, actual)
        }

        @DisplayName("Then should return correct object with expression")
        @Test
        fun testDeserializeJsonWebViewWithExpression() {
            // Given
            val expectedComponent = makeObjectWebViewWithExpression()
            val json = makeWebViewWithExpressionJson()

            // When
            val actual = moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedComponent, actual)
        }
    }

    @DisplayName("When try serialize object WebView")
    @Nested
    inner class SerializeObjectWebViewTest {

        @DisplayName("Then should return correct json")
        @Test
        fun testSerializeJsonWebView() {
            // Given
            val expectedJson = makeWebViewJson().replace("\\s".toRegex(), "")
            val component = makeObjectWebView()

            // When
            val actual = moshi.adapter(ServerDrivenComponent::class.java).toJson(component)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }

        @DisplayName("Then should return correct json with expression")
        @Test
        fun testSerializeJsonWebViewWithExpression() {
            // Given
            val expectedJson = makeWebViewWithExpressionJson().replace("\\s".toRegex(), "")
            val component = makeObjectWebViewWithExpression()

            // When
            val actual = moshi.adapter(ServerDrivenComponent::class.java).toJson(component)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }
    }

    private fun makeWebViewJson() = """
    {
        "_beagleComponent_": "beagle:webview",
        "url": "https://www.test.com"
    }
"""

    private fun makeWebViewWithExpressionJson() = """
    {
        "_beagleComponent_": "beagle:webview",
        "url": "@{test}"
    }
"""

    private fun makeObjectWebView() = WebView(
        url = "https://www.test.com"
    )

    private fun makeObjectWebViewWithExpression() = WebView(
        url = expressionOf("@{test}")
    )
}