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

import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.action.Alert
import br.com.zup.beagle.android.action.Confirm
import br.com.zup.beagle.android.action.FormLocalAction
import br.com.zup.beagle.android.action.FormMethodType
import br.com.zup.beagle.android.action.FormRemoteAction
import br.com.zup.beagle.android.action.FormValidation
import br.com.zup.beagle.android.action.Navigate
import br.com.zup.beagle.android.action.UndefinedAction
import br.com.zup.beagle.android.components.Button
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
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.context.valueOf
import br.com.zup.beagle.android.mockdata.ComponentBinding
import br.com.zup.beagle.android.mockdata.CustomAndroidAction
import br.com.zup.beagle.android.mockdata.CustomInputWidget
import br.com.zup.beagle.android.mockdata.CustomWidget
import br.com.zup.beagle.android.mockdata.InternalObject
import br.com.zup.beagle.android.testutil.RandomData
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
    fun make_should_return_moshi_to_deserialize_a_Container() {
        // Given
        val json = makeContainerJson()

        // When
        val actual = moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is Container)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_Container() {
        // Given
        val component = Container(listOf())

        // When
        val actual = moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(actual))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_Text() {
        // Given
        val json = makeTextJson()

        // When
        val actual = moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is Text)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_Text() {
        // Given
        val component = Text(RandomData.string())

        // When
        val actual = moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(actual))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_Image_Local() {
        // Given
        val json = makeImageJson()

        // When
        val actual = moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is Image)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_Image_Local() {
        // Given
        val component = Image(ImagePath.Local(RandomData.string()))

        // When
        val actual = moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(actual))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_NetworkImage() {
        // Given
        val json = makeNetworkImageJson()

        // When
        val actual = moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is Image)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_NetworkImage() {
        // Given
        val component = Image(ImagePath.Remote(RandomData.string()))

        // When
        val actual = moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(actual))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_Button() {
        // Given
        val json = makeButtonJson()

        // When
        val actual = moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is Button)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_Button() {
        // Given
        val component = Button("")

        // When
        val actual = moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(actual))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_ListView() {
        // Given
        val json = makeListViewJson()

        // When
        val actual = moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is ListView)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_ListView() {
        // Given
        val component = ListView(listOf())

        // When
        val actual = moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(actual))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_TabView() {
        // Given
        val json = makeTabViewJson()

        //When
        val actual = moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is TabView)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_TabView() {
        // Given
        val component = TabView(listOf(), "")

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
    fun make_should_return_moshi_to_deserialize_a_LazyComponent() {
        // Given
        val json = makeLazyComponentJson()

        // When
        val actual = moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is LazyComponent)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_LazyComponent() {
        // Given
        val component = LazyComponent("", UndefinedWidget())

        // When
        val actual = moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(actual))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_ScrollView() {
        // Given
        val json = makeScrollViewJson()

        // When
        val actual = moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is ScrollView)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_ScrollView() {
        // Given
        val component = ScrollView(listOf())

        // When
        val actual = moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(actual))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_PageView() {
        // Given
        val json = makePageViewJson()

        // When
        val actual = moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is PageView)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_PageView() {
        // Given
        val component = PageView(listOf())

        // When
        val actual = moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(actual))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_PageIndicator() {
        // Given
        val json = makePageIndicatorJson()

        // When
        val actual = moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is PageIndicator)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_PageIndicator() {
        // Given
        val component = PageIndicator(RandomData.string(), RandomData.string())

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
    fun make_should_return_moshi_to_deserialize_a_FormValidation() {
        // Given
        val json = makeFormValidationJson()

        // When
        val actual = moshi.adapter(Action::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is FormValidation)
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
    fun make_should_return_moshi_to_serialize_a_UndefinedAction() {
        // Given
        val component = Button(
            text = "",
            onPress = listOf(
                UndefinedAction()
            )
        )

        // When
        val actual = moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        val expected = """{"_beagleComponent_":"beagle:button","text":"","onPress":[{"_beagleAction_":"beagle:undefinedaction"}]}"""
        assertEquals(expected, actual)
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_FormInput() {
        // Given
        val json = makeFormInputJson()

        // When
        val actual = moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is FormInput)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_FormInput() {
        // Given
        val component = FormInput(
            name = RandomData.string(),
            child = CustomInputWidget()
        )

        // When
        val actual = moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(actual))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_FormSubmit() {
        // Given
        val json = makeFormSubmitJson()

        // When
        val actual = moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is FormSubmit)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_FormSubmit() {
        // Given
        val json = FormSubmit(UndefinedWidget())

        // When
        val actual = moshi.adapter(ServerDrivenComponent::class.java).toJson(json)

        // Then
        assertNotNull(JSONObject(actual))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_Form() {
        // Given
        val json = makeFormJson()

        // When
        val actual = moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is Form)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_Form() {
        // Given
        val component = Form(
            onSubmit = listOf(
                FormRemoteAction(
                    RandomData.string(),
                    FormMethodType.POST
                )
            ),
            child = UndefinedWidget()
        )

        // When
        val jsonComponent =
            moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(jsonComponent))
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
    fun make_should_return_moshi_to_serialize_a_UndefinedComponent_of_type_InputWidget() {
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

    @Test
    fun make_should_return_moshi_to_serialize_a_UndefinedComponent_of_type_PageIndicatorComponent() {
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

    @Test
    fun moshi_should_deserialize_bindComponent() {
        // Given
        val jsonComponent = makeBindComponent()

        // When
        val component = moshi.adapter(ServerDrivenComponent::class.java).fromJson(jsonComponent)

        // Then
        val bindComponent = component as ComponentBinding
        assertNull(bindComponent.value1)
        assertEquals("Hello", bindComponent.value2.value)
        assertTrue(bindComponent.value2 is Bind.Value<String>)
        assertEquals(String::class.java, bindComponent.value2.type)
        assertEquals(true, bindComponent.value3.value)
        assertTrue(bindComponent.value3 is Bind.Value<Boolean>)
        assertEquals(Boolean::class.javaObjectType, bindComponent.value3.type)
        assertNotNull(bindComponent.value4.value)
        assertEquals(InternalObject::class.java, bindComponent.value4.type)
        assertEquals(mapOf("test1" to "a", "test2" to "b"), bindComponent.value5.value)
        assertEquals(listOf("test1", "test2"), bindComponent.value6.value)
    }

    @Test
    fun moshi_should_deserialize_bindComponent_with_expressions() {
        // Given
        val jsonComponent = makeBindComponentExpression()

        // When
        val component = moshi.adapter(ServerDrivenComponent::class.java).fromJson(jsonComponent)

        // Then
        val bindComponent = component as ComponentBinding
        assertEquals("@{intExpression}", bindComponent.value1?.value)
        assertTrue(bindComponent.value1 is Bind.Expression<Int>)
        assertEquals("Hello @{context.name}", bindComponent.value2.value)
        assertTrue(bindComponent.value2 is Bind.Expression<String>)
        assertEquals("@{booleanExpression}", bindComponent.value3.value)
        assertTrue(bindComponent.value3 is Bind.Expression<Boolean>)
        assertEquals("@{objectExpression}", bindComponent.value4.value)
        assertTrue(bindComponent.value4 is Bind.Expression<InternalObject>)
        assertEquals("@{mapExpression}", bindComponent.value5.value)
        assertTrue(bindComponent.value5 is Bind.Expression<Map<String, String>>)
        assertEquals("@{listExpression}", bindComponent.value6.value)
        assertTrue(bindComponent.value6 is Bind.Expression<List<String>>)
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
            value3 = Bind.Expression(listOf(), "@{hello}", Boolean::class.java),
            value4 = Bind.Value(
                InternalObject(
                    "",
                    1
                )
            ),
            value5 = valueOf(mapOf()),
            value6 = valueOf(listOf())
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
        val value = contextData?.value as? JSONObject
        assertEquals(true, value?.getBoolean("a"))
        assertEquals("a", value?.getString("b"))
    }

    @Test
    fun make_should_create_contextData_with_jsonArray() {
        // Given
        val contextDataJson = makeContextWithJsonArray()

        // When
        val contextData = moshi.adapter(ContextData::class.java).fromJson(contextDataJson)

        // Then
        val value = (contextData?.value as? JSONArray)?.getJSONObject(0)
        assertEquals(true, value?.getBoolean("a"))
        assertEquals("a", value?.getString("b"))
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
    fun moshi_should_serialize_contextData_with_integer() {
        // Given
        val contextData = ContextData(
            id = RandomData.string(),
            value = 2
        )

        // When
        val toJson = moshi.adapter(ContextData::class.java).toJson(contextData)

        // Then
        val jsonObject = JSONObject(toJson)
        assertNotNull(jsonObject)
        assertEquals(jsonObject.get("id"), contextData.id)
        assertEquals(jsonObject.get("value"), contextData.value)
    }

    @Test
    fun moshi_should_convert_and_revert_contextData_with_integer() {
        // Given
        val contextData = ContextData(
            id = RandomData.string(),
            value = 5
        )

        // When
        val toJson = moshi.adapter(ContextData::class.java).toJson(contextData)
        val fromJson = moshi.adapter(ContextData::class.java).fromJson(toJson)

        // Then
        assertEquals(contextData, fromJson)
    }

    @Test
    fun moshi_should_serialize_contextData_with_double() {
        // Given
        val contextData = ContextData(
            id = RandomData.string(),
            value = 2.5
        )

        // When
        val toJson = moshi.adapter(ContextData::class.java).toJson(contextData)

        // Then
        val jsonObject = JSONObject(toJson)
        assertNotNull(jsonObject)
        assertEquals(jsonObject.get("id"), contextData.id)
        assertEquals(jsonObject.get("value"), contextData.value)
    }

    @Test
    fun moshi_should_convert_and_revert_contextData_with_double() {
        // Given
        val contextData = ContextData(
            id = RandomData.string(),
            value = 4.7
        )

        // When
        val toJson = moshi.adapter(ContextData::class.java).toJson(contextData)
        val fromJson = moshi.adapter(ContextData::class.java).fromJson(toJson)

        // Then
        assertEquals(contextData, fromJson)
    }

    @Test
    fun make_should_create_contextData_with_integer() {
        // Given
        val testInt = 2
        val contextDataJson = makeContextWithNumber(testInt)

        // When
        val contextData = moshi.adapter(ContextData::class.java).fromJson(contextDataJson)

        // Then
        assertEquals("contextId", contextData?.id)
        assertEquals(testInt, contextData?.value)
    }

    @Test
    fun make_should_create_contextData_with_double() {
        // Given
        val testDouble = 2.5
        val contextDataJson = makeContextWithNumber(testDouble)

        // When
        val contextData = moshi.adapter(ContextData::class.java).fromJson(contextDataJson)

        // Then
        assertEquals("contextId", contextData?.id)
        assertEquals(testDouble, contextData?.value)
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