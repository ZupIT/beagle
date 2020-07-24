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

import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.action.Alert
import br.com.zup.beagle.android.action.Confirm
import br.com.zup.beagle.android.action.FormLocalAction
import br.com.zup.beagle.android.action.Navigate
import br.com.zup.beagle.android.action.UndefinedAction
import br.com.zup.beagle.android.components.layout.ScreenComponent
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.fake.ComponentBinding
import br.com.zup.beagle.android.fake.CustomAndroidAction
import br.com.zup.beagle.android.fake.CustomInputWidget
import br.com.zup.beagle.android.fake.CustomWidget
import br.com.zup.beagle.android.fake.InternalObject
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.utils.BaseTest
import br.com.zup.beagle.android.widget.UndefinedWidget
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.core.ServerDrivenComponent
import com.squareup.moshi.Moshi
import io.mockk.every
import io.mockk.mockk
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

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

class BeagleMoshiTest : BaseTest() {

    private lateinit var moshi: Moshi

    override fun setUp() {
        super.setUp()

        every { beagleSdk.formLocalActionHandler } returns mockk(relaxed = true)
        every { beagleSdk.registeredWidgets() } returns WIDGETS
        every { beagleSdk.registeredActions() } returns ACTIONS

        moshi = BeagleMoshi.createMoshi()
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_ScreenWidget() {
        // Given
        val json = makeScreenJson()

        // When
        val actual = moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is ScreenComponent)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_ScreenWidget() {
        // Given
        val component = ScreenComponent(child = UndefinedWidget())

        // When
        val actual = moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(actual))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_CustomWidget() {
        // Given
        val json = makeCustomJson()

        // When
        val actual = moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is CustomWidget)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_CustomWidget() {
        // Given
        val component = CustomWidget()

        // When
        val actual = moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(actual))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_PushView_action() {
        // Given
        val json = makeNavigationActionJson()

        // When
        val actual = moshi.adapter(Action::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is Navigate)
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_AlertAction() {
        // Given
        val json = makeAlertActionJson()

        // When
        val actual = moshi.adapter(Action::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is Alert)
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_ConfirmAction() {
        // Given
        val json = makeConfirmActionJson()

        // When
        val actual = moshi.adapter(Action::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is Confirm)
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_FormLocalAction() {
        // Given
        val json = makeFormLocalActionJson()


        // When
        val actual = moshi.adapter(Action::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is FormLocalAction)
    }


    @Test
    fun make_should_return_moshi_to_deserialize_a_CustomAndroidAction() {
        // Given
        val json = makeFormLocalActionJson()

        // When
        val actual = moshi.adapter(Action::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is FormLocalAction)
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_UndefinedAction() {
        // Given
        val json = makeUndefinedActionJson()

        // When
        val actual = moshi.adapter(Action::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is UndefinedAction)
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_UndefinedComponent() {
        // Given
        val json = makeUndefinedComponentJson()

        // When
        val component =
            moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(component)
        assertTrue(component is UndefinedWidget)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_UndefinedComponent() {
        // Given
        val component = UndefinedWidget()

        // When
        val jsonComponent =
            moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(jsonComponent))
    }

    @Test
    fun moshi_should_deserialize_bindComponent() {
        // Given
        val jsonComponent = makeBindComponent()

        // When
        val component = moshi.adapter(ServerDrivenComponent::class.java).fromJson(jsonComponent)

        // Then
        val bindComponent = component as ComponentBinding
        assertNull(bindComponent.value1)
        assertEquals("Hello @{context.name}", bindComponent.value2.value)
        assertTrue(bindComponent.value2 is Bind.Expression<String>)
        assertEquals(String::class.java, bindComponent.value2.type)
        assertEquals("@{hello}", bindComponent.value3.value)
        assertTrue(bindComponent.value3 is Bind.Expression<Boolean>)
        assertEquals(Boolean::class.javaObjectType, bindComponent.value3.type)
        assertNotNull(bindComponent.value4.value)
        assertEquals(InternalObject::class.java, bindComponent.value4.type)
    }

    @Test
    fun moshi_should_deserialize_internalObject_using_component_type_attribute() {
        // Given
        val jsonComponent = makeBindComponent()
        val internalObjectJson = makeInternalObject()

        // When
        val bindComponent = moshi.adapter(ServerDrivenComponent::class.java).fromJson(jsonComponent) as ComponentBinding
        val internalObject = moshi.adapter<Any>(bindComponent.value4.type).fromJson(internalObjectJson) as InternalObject

        // Then
        assertEquals("hello", internalObject.value1)
        assertEquals(123, internalObject.value2)
    }

    @Test
    fun moshi_should_serialize_bindComponent() {
        // Given
        val component = ComponentBinding(
            value1 = null,
            value2 = Bind.Value("Hello"),
            value3 = Bind.Expression("@{hello}", Boolean::class.java),
            value4 = Bind.Value(
                InternalObject(
                    "",
                    1
                )
            )
        )

        // When
        val json = moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(json))
    }

    @Test
    fun make_should_create_contextData_with_jsonObject() {
        // Given
        val contextDataJson = makeContextWithJsonObject()

        // When
        val contextData = moshi.adapter(ContextData::class.java).fromJson(contextDataJson)

        // Then
        assertTrue(contextData?.value is JSONObject)
    }

    @Test
    fun make_should_create_contextData_with_jsonArray() {
        // Given
        val contextDataJson = makeContextWithJsonArray()

        // When
        val contextData = moshi.adapter(ContextData::class.java).fromJson(contextDataJson)

        // Then
        assertTrue(contextData?.value is JSONArray)
    }

    @Test
    fun make_should_create_contextData_with_primitive() {
        // Given
        val contextDataJson = makeContextWithPrimitive()

        // When
        val contextData = moshi.adapter(ContextData::class.java).fromJson(contextDataJson)

        // Then
        assertEquals("contextId", contextData?.id)
        assertEquals(true, contextData?.value)
    }

    @Test
    fun make_should_deserialize_contextData_with_jsonArray() {
        // Given
        val contextData = ContextData(
            id = RandomData.string(),
            value = JSONArray().apply {
                put(1)
                put(2)
            }
        )

        // When
        val json = moshi.adapter(ContextData::class.java).toJson(contextData)

        // Then
        assertNotNull(JSONObject(json))
    }
}
