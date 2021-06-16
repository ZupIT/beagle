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

package br.com.zup.beagle.android.data.serializer.analytics

import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.action.Navigate
import br.com.zup.beagle.android.data.serializer.BeagleMoshi
import br.com.zup.beagle.newanalytics.ActionAnalyticsConfig
import br.com.zup.beagle.newanalytics.ActionAnalyticsProperties
import com.squareup.moshi.Moshi
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a Moshi Adapter")
class ActionAnalyticsConfigSerializationTest : BaseTest() {

    private lateinit var moshi: Moshi

    @BeforeEach
    override fun setUp() {
        super.setUp()

        moshi = BeagleMoshi.createMoshi()
    }

    @DisplayName("When try to deserialize json ActionAnalyticsConfig")
    @Nested
    inner class DeserializeJsonActionAnalyticsConfigTest {

        @DisplayName("Then should create JSON with false string")
        @Test
        fun testDeserializeJsonActionAnalyticsConfigDisabledReturnFalse() {
            val actionAnalyticsConfigExpected = ActionAnalyticsConfig.Disabled()

            val actionAnalyticsConfigActual = moshi.adapter(ActionAnalyticsConfig::class.java).fromJson("false")

            Assertions.assertNotNull(actionAnalyticsConfigActual)
            Assertions.assertEquals(
                actionAnalyticsConfigExpected::class.java,
                actionAnalyticsConfigActual!!::class.java
            )
            Assertions.assertFalse(actionAnalyticsConfigActual.value as Boolean)

        }

        @DisplayName("Then should create JSON with true string")
        @Test
        fun testDeserializeJsonActionAnalyticsConfigEnabledWithAnalyticsNullReturnFalse() {
            val actionAnalyticsConfigExpected = ActionAnalyticsConfig.Enabled()

            val actionAnalyticsConfigActual = moshi.adapter(ActionAnalyticsConfig::class.java).fromJson("true")

            Assertions.assertNotNull(actionAnalyticsConfigActual)
            Assertions.assertEquals(
                actionAnalyticsConfigExpected::class.java,
                actionAnalyticsConfigActual!!::class.java
            )
            Assertions.assertEquals(null, actionAnalyticsConfigActual.value)
        }

        @DisplayName("Then should create JSON with Attributes should return right string")
        @Test
        fun testDeserializeJsonActionAnalyticsConfigEnabledWithAnalyticsReturnRightString() {
            val actionAnalyticsConfig = ActionAnalyticsConfig.Enabled(
                ActionAnalyticsProperties(attributes = listOf("attributes"))
            )

            val actual = moshi
                .adapter(ActionAnalyticsConfig::class.java)
                .fromJson(makeActionAnalyticsPropertiesWithAttributeJson())

            Assertions.assertNotNull(actual)
            Assertions.assertEquals(actionAnalyticsConfig::class.java, actual!!::class.java)
            Assertions.assertEquals(
                (actionAnalyticsConfig.value as ActionAnalyticsProperties).attributes,
                (actual.value as ActionAnalyticsProperties).attributes
            )
            Assertions.assertEquals(
                (actionAnalyticsConfig.value as ActionAnalyticsProperties).additionalEntries,
                (actual.value as ActionAnalyticsProperties).additionalEntries
            )

        }

        @DisplayName("Then should create JSON with AdditionalEntries should return right string")
        @Test
        fun testDeserializeJsonActionAnalyticsConfigEnabledWithAdditionalEntriesReturnRightString() {
            val actionAnalyticsConfig = ActionAnalyticsConfig.Enabled(
                ActionAnalyticsProperties(additionalEntries = mapOf("attributes" to "test"))
            )

            val actual = moshi
                .adapter(ActionAnalyticsConfig::class.java)
                .fromJson(makeActionAnalyticsPropertiesWithAdditionalEntriesJson())

            Assertions.assertNotNull(actual)
            Assertions.assertEquals(actionAnalyticsConfig::class.java, actual!!::class.java)
            Assertions.assertEquals(
                (actionAnalyticsConfig.value as ActionAnalyticsProperties).attributes,
                (actual.value as ActionAnalyticsProperties).attributes
            )
            Assertions.assertEquals(1, (actual.value as ActionAnalyticsProperties).additionalEntries?.size)
            Assertions.assertEquals(
                (actionAnalyticsConfig.value as ActionAnalyticsProperties).additionalEntries?.get("attributes"),
                (actual.value as ActionAnalyticsProperties).additionalEntries?.get("attributes")
            )
        }

        @DisplayName("Then should create JSON with AdditionalEntries and attributes should return right string")
        @Test
        fun testDeserializeJsonActionAnalyticsConfigEnabledWithAdditionalEntriesAndAttributesReturnRightString() {
            val actionAnalyticsConfig = ActionAnalyticsConfig.Enabled(
                ActionAnalyticsProperties(listOf("attributes"), mapOf("attributes" to "test"))
            )

            val actual = moshi
                .adapter(ActionAnalyticsConfig::class.java)
                .fromJson(makeActionAnalyticsPropertiesWithAttributesAndAdditionalEntriesJson())

            Assertions.assertNotNull(actual)
            Assertions.assertEquals(actionAnalyticsConfig::class.java, actual!!::class.java)
            Assertions.assertEquals(
                (actionAnalyticsConfig.value as ActionAnalyticsProperties).attributes,
                (actual.value as ActionAnalyticsProperties).attributes
            )
            Assertions.assertEquals(
                (actionAnalyticsConfig.value as ActionAnalyticsProperties).additionalEntries,
                (actual.value as ActionAnalyticsProperties).additionalEntries
            )
        }

        @DisplayName("Then should return a correct parse action")
        @Test
        fun testDeserializeJsonActionWithAnalyticsNull() {
            // Given
            val json = makeActionWithAnalyticsNullJson()

            // When
            val actual = moshi.adapter(Action::class.java).fromJson(json)

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertTrue(actual is Navigate.PushView)
        }
    }

    @DisplayName("When try serialize object ActionAnalyticsConfig")
    @Nested
    inner class SerializeObjectActionAnalyticsConfigTest {

        @DisplayName("Then should create JSON with false string")
        @Test
        fun testSerializeJsonActionAnalyticsConfigDisabledReturnFalse() {
            val actionAnalyticsConfig = ActionAnalyticsConfig.Disabled()

            val json = moshi.adapter(ActionAnalyticsConfig::class.java).toJson(actionAnalyticsConfig)

            Assertions.assertNotNull(json)
            Assertions.assertEquals("false", json)
        }

        @DisplayName("Then should create JSON with true string")
        @Test
        fun testSerializeJsonActionAnalyticsConfigEnabledWithAnalyticsNullReturnFalse() {
            val actionAnalyticsConfig = ActionAnalyticsConfig.Enabled()

            val json = moshi.adapter(ActionAnalyticsConfig::class.java).toJson(actionAnalyticsConfig)

            Assertions.assertNotNull(json)
            Assertions.assertEquals("true", json)
        }

        @DisplayName("Then should create JSON with Attributes should return right string")
        @Test
        fun testSerializeJsonActionAnalyticsConfigEnabledWithAnalyticsReturnRightString() {
            val actionAnalyticsConfig = ActionAnalyticsConfig.Enabled(
                ActionAnalyticsProperties(attributes = listOf("attributes"))
            )

            val json = moshi.adapter(ActionAnalyticsConfig::class.java).toJson(actionAnalyticsConfig)

            Assertions.assertNotNull(json)
            Assertions.assertEquals(makeActionAnalyticsPropertiesWithAttributeJson(), json)
        }

        @DisplayName("Then should create JSON with AdditionalEntries should return right string")
        @Test
        fun testSerializeJsonActionAnalyticsConfigEnabledWithAdditionalEntriesReturnRightString() {
            val actionAnalyticsConfig = ActionAnalyticsConfig.Enabled(
                ActionAnalyticsProperties(additionalEntries = mapOf("attributes" to "test"))
            )

            val json = moshi.adapter(ActionAnalyticsConfig::class.java).toJson(actionAnalyticsConfig)

            Assertions.assertNotNull(json)
            Assertions.assertEquals(makeActionAnalyticsPropertiesWithAdditionalEntriesJson(), json)
        }

        @DisplayName("Then should create JSON with AdditionalEntries and attributes should return right string")
        @Test
        fun testSerializeJsonActionAnalyticsConfigEnabledWithAdditionalEntriesAndAttributesReturnRightString() {
            val actionAnalyticsConfig = ActionAnalyticsConfig.Enabled(
                ActionAnalyticsProperties(listOf("attributes"), mapOf("attributes" to "test"))
            )

            val json = moshi.adapter(ActionAnalyticsConfig::class.java).toJson(actionAnalyticsConfig)

            Assertions.assertNotNull(json)
            Assertions.assertEquals(makeActionAnalyticsPropertiesWithAttributesAndAdditionalEntriesJson(), json)
        }
    }

    private fun makeActionAnalyticsPropertiesWithAttributeJson() = """{"attributes":["attributes"]}"""

    private fun makeActionAnalyticsPropertiesWithAdditionalEntriesJson() =
        """{"additionalEntries":{"attributes":"test"}}"""

    private fun makeActionAnalyticsPropertiesWithAttributesAndAdditionalEntriesJson() =
        """{"attributes":["attributes"],"additionalEntries":{"attributes":"test"}}"""

    fun makeActionWithAnalyticsNullJson() = """
    {
      "_beagleAction_": "beagle:pushView",
      "route": {
        "url": "http://localhost:8080/test/example",
        "shouldPrefetch": false
      },
      "analytics": null
    }
"""

}