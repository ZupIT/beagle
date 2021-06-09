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

package br.com.zup.beagle.android.data.serializer

import br.com.zup.beagle.newanalytics.ActionAnalyticsConfig
import br.com.zup.beagle.newanalytics.ActionAnalyticsProperties
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.action.AddChildren
import br.com.zup.beagle.android.action.Alert
import br.com.zup.beagle.android.action.Condition
import br.com.zup.beagle.android.action.Confirm
import br.com.zup.beagle.android.action.FormLocalAction
import br.com.zup.beagle.android.action.FormMethodType
import br.com.zup.beagle.android.action.FormRemoteAction
import br.com.zup.beagle.android.action.FormValidation
import br.com.zup.beagle.android.action.HttpAdditionalData
import br.com.zup.beagle.android.action.Navigate
import br.com.zup.beagle.android.action.Route
import br.com.zup.beagle.android.action.UndefinedAction
import br.com.zup.beagle.android.components.Button
import br.com.zup.beagle.android.components.GridView
import br.com.zup.beagle.android.components.Image
import br.com.zup.beagle.android.components.ImagePath
import br.com.zup.beagle.android.components.LazyComponent
import br.com.zup.beagle.android.components.ListView
import br.com.zup.beagle.android.components.TabView
import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.android.components.form.Form
import br.com.zup.beagle.android.components.form.FormInput
import br.com.zup.beagle.android.components.form.FormSubmit
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.components.layout.ScreenComponent
import br.com.zup.beagle.android.components.layout.ScrollView
import br.com.zup.beagle.android.components.page.PageIndicator
import br.com.zup.beagle.android.components.page.PageView
import br.com.zup.beagle.android.components.utils.Template
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.android.context.valueOf
import br.com.zup.beagle.android.mockdata.ComponentBinding
import br.com.zup.beagle.android.mockdata.CustomAndroidAction
import br.com.zup.beagle.android.mockdata.CustomInputWidget
import br.com.zup.beagle.android.mockdata.CustomWidget
import br.com.zup.beagle.android.mockdata.InternalObject
import br.com.zup.beagle.android.mockdata.Person
import br.com.zup.beagle.android.mockdata.TypeAdapterResolverImpl
import br.com.zup.beagle.android.networking.HttpMethod
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.widget.UndefinedWidget
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.core.ServerDrivenComponent
import com.squareup.moshi.Moshi
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested

@Suppress("UNCHECKED_CAST")
private val WIDGETS = listOf(
    CustomWidget::class.java as Class<WidgetView>,
    CustomInputWidget::class.java as Class<WidgetView>,
    ComponentBinding::class.java as Class<WidgetView>
)

@Suppress("UNCHECKED_CAST")
private val ACTIONS = listOf(
    CustomAndroidAction::class.java as Class<Action>
)

@DisplayName("Given a Moshi Adapter")
class BeagleMoshiTest : BaseTest() {

    private lateinit var moshi: Moshi

    @BeforeEach
    override fun setUp() {
        super.setUp()

        every { beagleSdk.formLocalActionHandler } returns mockk(relaxed = true)
//        every { beagleSdk.registeredWidgets() } returns WIDGETS
//        every { beagleSdk.registeredActions() } returns ACTIONS
        every { beagleSdk.typeAdapterResolver } returns TypeAdapterResolverImpl()

        moshi = BeagleMoshi.createMoshi()
    }

    @DisplayName("When try deserialize action with analytics null")
    @Nested
    inner class AnalyticsNullTest {

        @DisplayName("Then should return a correct parse action")
        @Test
        fun testActionWithAnalyticsNull() {
            // Given
            val json = makeActionWithAnalyticsNull()

            // When
            val actual = moshi.adapter(Action::class.java).fromJson(json)

            // Then
            assertNotNull(actual)
            assertTrue(actual is Navigate.PushView)
        }

    }

    @DisplayName("When call toJson")
    @Nested
    inner class SerializationTest {

        @DisplayName("Then should return an UndefinedWidget of type InputWidget JSONObject")
        @Test
        fun serializeUndefinedComponentOfTypeInputWidgetTest() {
            // Given
            val component = FormInput(
                name = RandomData.string(),
                child = UndefinedWidget()
            )

            // When
            val jsonComponent =
                moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

            // Then
            assertNotNull(JSONObject(jsonComponent))
        }

        @DisplayName("Then should return an UndefinedWidget of type PageIndicator JSONObject")
        @Test
        fun serializeUndefinedComponentOfTypePageIndicatorComponentTest() {
            // Given
            val component = PageView(
                children = listOf(),
                pageIndicator = UndefinedWidget()
            )

            // When
            val jsonComponent =
                moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

            // Then
            assertNotNull(JSONObject(jsonComponent))
        }

        @DisplayName("Then should create JSON with false string")
        @Test
        fun testActionAnalyticsConfigDisabledToJsonReturnFalse() {
            val actionAnalyticsConfig = ActionAnalyticsConfig.Disabled()

            val json = moshi.adapter(ActionAnalyticsConfig::class.java).toJson(actionAnalyticsConfig)

            assertNotNull(json)
            assertEquals("false", json)
        }

        @DisplayName("Then should create JSON with true string")
        @Test
        fun testActionAnalyticsConfigEnabledWithAnalyticsNullToJsonReturnFalse() {
            val actionAnalyticsConfig = ActionAnalyticsConfig.Enabled()

            val json = moshi.adapter(ActionAnalyticsConfig::class.java).toJson(actionAnalyticsConfig)

            assertNotNull(json)
            assertEquals("true", json)
        }

        @DisplayName("Then should create JSON with Attributes should return right string")
        @Test
        fun testActionAnalyticsConfigEnabledWithAnalyticsToJsonReturnRightString() {
            val actionAnalyticsConfig = ActionAnalyticsConfig.Enabled(ActionAnalyticsProperties(attributes = listOf("attributes")))

            val json = moshi.adapter(ActionAnalyticsConfig::class.java).toJson(actionAnalyticsConfig)

            assertNotNull(json)
            assertEquals(makeActionAnalyticsPropertiesWithAttribute(), json)
        }

        @DisplayName("Then should create JSON with AdditionalEntries should return right string")
        @Test
        fun testActionAnalyticsConfigEnabledWithAdditionalEntriesToJsonReturnRightString() {
            val actionAnalyticsConfig = ActionAnalyticsConfig.Enabled(ActionAnalyticsProperties(additionalEntries = mapOf("attributes" to "test")))

            val json = moshi.adapter(ActionAnalyticsConfig::class.java).toJson(actionAnalyticsConfig)

            assertNotNull(json)
            assertEquals(makeActionAnalyticsPropertiesWithAdditionalEntries(), json)
        }

        @DisplayName("Then should create JSON with AdditionalEntries and attributes should return right string")
        @Test
        fun testActionAnalyticsConfigEnabledWithAdditionalEntriesAndAttributesToJsonReturnRightString() {
            val actionAnalyticsConfig = ActionAnalyticsConfig.Enabled(ActionAnalyticsProperties(listOf("attributes"), mapOf("attributes" to "test")))

            val json = moshi.adapter(ActionAnalyticsConfig::class.java).toJson(actionAnalyticsConfig)

            assertNotNull(json)
            assertEquals(makeActionAnalyticsPropertiesWithAttributesAndAdditionalEntries(), json)
        }
    }

    @DisplayName("When call fromJson")
    @Nested
    inner class DeserializationTest {

        @DisplayName("Then should create JSON with false string")
        @Test
        fun testActionAnalyticsConfigDisabledToJsonReturnFalse() {
            val actionAnalyticsConfigExpected = ActionAnalyticsConfig.Disabled()

            val actionAnalyticsConfigActual = moshi.adapter(ActionAnalyticsConfig::class.java).fromJson("false")

            assertNotNull(actionAnalyticsConfigActual)
            assertEquals(actionAnalyticsConfigExpected::class.java, actionAnalyticsConfigActual!!::class.java)
            assertFalse(actionAnalyticsConfigActual.value as Boolean)

        }

        @DisplayName("Then should create JSON with true string")
        @Test
        fun testActionAnalyticsConfigEnabledWithAnalyticsNullToJsonReturnFalse() {
            val actionAnalyticsConfigExpected = ActionAnalyticsConfig.Enabled()

            val actionAnalyticsConfigActual = moshi.adapter(ActionAnalyticsConfig::class.java).fromJson("true")

            assertNotNull(actionAnalyticsConfigActual)
            assertEquals(actionAnalyticsConfigExpected::class.java, actionAnalyticsConfigActual!!::class.java)
            assertEquals(null, actionAnalyticsConfigActual.value)
        }

        @DisplayName("Then should create JSON with Attributes should return right string")
        @Test
        fun testActionAnalyticsConfigEnabledWithAnalyticsToJsonReturnRightString() {
            val actionAnalyticsConfig = ActionAnalyticsConfig.Enabled(ActionAnalyticsProperties(attributes = listOf("attributes")))

            val actual = moshi.adapter(ActionAnalyticsConfig::class.java).fromJson(makeActionAnalyticsPropertiesWithAttribute())

            assertNotNull(actual)
            assertEquals(actionAnalyticsConfig::class.java, actual!!::class.java)
            assertEquals((actionAnalyticsConfig.value as ActionAnalyticsProperties).attributes, (actual.value as ActionAnalyticsProperties).attributes)
            assertEquals((actionAnalyticsConfig.value as ActionAnalyticsProperties).additionalEntries, (actual.value as ActionAnalyticsProperties).additionalEntries)

        }

        @DisplayName("Then should create JSON with AdditionalEntries should return right string")
        @Test
        fun testActionAnalyticsConfigEnabledWithAdditionalEntriesToJsonReturnRightString() {
            val actionAnalyticsConfig = ActionAnalyticsConfig.Enabled(ActionAnalyticsProperties(additionalEntries = mapOf("attributes" to "test")))

            val actual = moshi.adapter(ActionAnalyticsConfig::class.java).fromJson(makeActionAnalyticsPropertiesWithAdditionalEntries())

            assertNotNull(actual)
            assertEquals(actionAnalyticsConfig::class.java, actual!!::class.java)
            assertEquals((actionAnalyticsConfig.value as ActionAnalyticsProperties).attributes, (actual.value as ActionAnalyticsProperties).attributes)
            assertEquals(1, (actual.value as ActionAnalyticsProperties).additionalEntries?.size)
            assertEquals((actionAnalyticsConfig.value as ActionAnalyticsProperties).additionalEntries?.get("attributes"), (actual.value as ActionAnalyticsProperties).additionalEntries?.get("attributes"))
        }

        @DisplayName("Then should create JSON with AdditionalEntries and attributes should return right string")
        @Test
        fun testActionAnalyticsConfigEnabledWithAdditionalEntriesAndAttributesToJsonReturnRightString() {
            val actionAnalyticsConfig = ActionAnalyticsConfig.Enabled(ActionAnalyticsProperties(listOf("attributes"), mapOf("attributes" to "test")))

            val actual = moshi.adapter(ActionAnalyticsConfig::class.java).fromJson(makeActionAnalyticsPropertiesWithAttributesAndAdditionalEntries())

            assertNotNull(actual)
            assertEquals(actionAnalyticsConfig::class.java, actual!!::class.java)
            assertEquals((actionAnalyticsConfig.value as ActionAnalyticsProperties).attributes, (actual.value as ActionAnalyticsProperties).attributes)
            assertEquals((actionAnalyticsConfig.value as ActionAnalyticsProperties).additionalEntries, (actual.value as ActionAnalyticsProperties).additionalEntries)
        }
    }


}