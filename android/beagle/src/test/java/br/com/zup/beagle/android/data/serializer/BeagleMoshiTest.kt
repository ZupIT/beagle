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

import br.com.zup.beagle.action.CustomAction
import br.com.zup.beagle.action.FormValidation
import br.com.zup.beagle.action.Navigate
import br.com.zup.beagle.action.ShowNativeDialog
import br.com.zup.beagle.android.mockdata.ComponentBinding
import br.com.zup.beagle.android.mockdata.CustomAndroidAction
import br.com.zup.beagle.android.mockdata.CustomInputWidget
import br.com.zup.beagle.android.mockdata.CustomWidget
import br.com.zup.beagle.android.mockdata.InternalObject
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.widget.core.Bind
import br.com.zup.beagle.android.widget.core.ContextData
import br.com.zup.beagle.android.widget.core.WidgetView
import br.com.zup.beagle.android.widget.layout.ScreenComponent
import br.com.zup.beagle.android.widget.pager.PageIndicator
import br.com.zup.beagle.android.widget.ui.UndefinedWidget
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.core.Action
import br.com.zup.beagle.widget.form.Form
import br.com.zup.beagle.widget.form.FormInput
import br.com.zup.beagle.widget.form.FormMethodType
import br.com.zup.beagle.widget.form.FormRemoteAction
import br.com.zup.beagle.widget.form.FormSubmit
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.Horizontal
import br.com.zup.beagle.widget.layout.PageView
import br.com.zup.beagle.widget.layout.ScrollView
import br.com.zup.beagle.widget.layout.Spacer
import br.com.zup.beagle.widget.layout.Stack
import br.com.zup.beagle.widget.layout.Vertical
import br.com.zup.beagle.widget.lazy.LazyComponent
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.Image
import br.com.zup.beagle.widget.ui.ListView
import br.com.zup.beagle.widget.ui.NetworkImage
import br.com.zup.beagle.widget.ui.Text
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.json.JSONArray
import org.json.JSONObject
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import br.com.zup.beagle.android.Action as AndroidAction

@Suppress("UNCHECKED_CAST")
private val WIDGETS = listOf(
    CustomWidget::class.java as Class<WidgetView>,
    CustomInputWidget::class.java as Class<WidgetView>,
    ComponentBinding::class.java as Class<WidgetView>
)

@Suppress("UNCHECKED_CAST")
private val ACTIONS = listOf(
    CustomAndroidAction::class.java as Class<AndroidAction>
)

class BeagleMoshiTest {

    private lateinit var beagleMoshiFactory: BeagleMoshi

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        beagleMoshiFactory = BeagleMoshi

        mockkObject(BeagleEnvironment)

        every { BeagleEnvironment.beagleSdk.registeredWidgets() } returns WIDGETS
        every { BeagleEnvironment.beagleSdk.registeredActions() } returns ACTIONS
    }

    @After
    fun tearDown() {
        unmockkObject(BeagleEnvironment)
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_ScreenWidget() {
        // Given
        val json = makeScreenJson()

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is ScreenComponent)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_ScreenWidget() {
        // Given
        val component = ScreenComponent(child = UndefinedWidget())

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(actual))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_Container() {
        // Given
        val json = makeContainerJson()

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is Container)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_Container() {
        // Given
        val component = Container(listOf())

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(actual))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_Vertical() {
        // Given
        val json = makeVerticalJson()

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is Vertical)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_Vertical() {
        // Given
        val component = Vertical(listOf())

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(actual))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_Horizontal() {
        // Given
        val json = makeHorizontalJson()

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is Horizontal)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_Horizontal() {
        // Given
        val component = Horizontal(listOf())

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(actual))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_Stack() {
        // Given
        val json = makeStackJson()

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is Stack)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_Stack() {
        // Given
        val component = Stack(listOf())

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(actual))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_Spacer() {
        // Given
        val json = makeSpacerJson()

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is Spacer)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_Spacer() {
        // Given
        val component = Spacer(10.0)

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(actual))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_Text() {
        // Given
        val json = makeTextJson()

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is Text)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_Text() {
        // Given
        val component = Text(RandomData.string())

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(actual))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_Image() {
        // Given
        val json = makeImageJson()

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is Image)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_Image() {
        // Given
        val component = Image(RandomData.string())

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(actual))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_NetworkImage() {
        // Given
        val json = makeNetworkImageJson()

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is NetworkImage)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_NetworkImage() {
        // Given
        val component = NetworkImage(RandomData.string())

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(actual))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_Button() {
        // Given
        val json = makeButtonJson()

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is Button)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_Button() {
        // Given
        val component = Button("")

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(actual))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_ListView() {
        // Given
        val json = makeListViewJson()

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is ListView)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_ListView() {
        // Given
        val component = ListView(listOf())

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(actual))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_CustomWidget() {
        // Given
        val json = makeCustomJson()

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is CustomWidget)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_CustomWidget() {
        // Given
        val component = CustomWidget()

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(actual))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_LazyComponent() {
        // Given
        val json = makeLazyComponentJson()

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is LazyComponent)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_LazyComponent() {
        // Given
        val component = LazyComponent("", UndefinedWidget())

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(actual))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_ScrollView() {
        // Given
        val json = makeScrollViewJson()

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is ScrollView)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_ScrollView() {
        // Given
        val component = ScrollView(listOf())

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(actual))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_PageView() {
        // Given
        val json = makePageViewJson()

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is PageView)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_PageView() {
        // Given
        val component = PageView(listOf())

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(actual))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_PageIndicator() {
        // Given
        val json = makePageIndicatorJson()

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is PageIndicator)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_PageIndicator() {
        // Given
        val component = PageIndicator(RandomData.string(), RandomData.string())

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(actual))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_PushView_action() {
        // Given
        val json = makeNavigationActionJson()

        // When
        val actual = beagleMoshiFactory.moshi.adapter(Action::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is Navigate)
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_ShowNativeDialog() {
        // Given
        val json = makeShowNativeDialogJson()

        // When
        val actual = beagleMoshiFactory.moshi.adapter(Action::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is ShowNativeDialog)
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_CustomAction() {
        // Given
        val json = makeCustomActionJson()

        // When
        val actual = beagleMoshiFactory.moshi.adapter(Action::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is CustomAction)
    }


    @Test
    fun make_should_return_moshi_to_deserialize_a_CustomAndroidAction() {
        // Given
        val json = makeCustomAndroidActionJson()

        // When
        val actual = beagleMoshiFactory.moshi.adapter(AndroidAction::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is CustomAndroidAction)
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_FormValidation() {
        // Given
        val json = makeFormValidationJson()

        // When
        val actual = beagleMoshiFactory.moshi.adapter(Action::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is FormValidation)
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_FormInput() {
        // Given
        val json = makeFormInputJson()

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is FormInput)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_FormInput() {
        // Given
        val component = FormInput(
            name = RandomData.string(),
            child = UndefinedWidget()
        )

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(actual))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_FormSubmit() {
        // Given
        val json = makeFormSubmitJson()

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is FormSubmit)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_FormSubmit() {
        // Given
        val json = FormSubmit(UndefinedWidget())

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).toJson(json)

        // Then
        assertNotNull(JSONObject(actual))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_Form() {
        // Given
        val json = makeFormJson()

        // When
        val actual =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

        // Then
        assertNotNull(actual)
        assertTrue(actual is Form)
    }

    @Test
    fun make_should_return_moshi_to_serialize_a_Form() {
        // Given
        val component = Form(
            onSubmit = listOf(FormRemoteAction(
                RandomData.string(),
                FormMethodType.POST
            )
            ), child = UndefinedWidget()
        )

        // When
        val jsonComponent =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(jsonComponent))
    }

    @Test
    fun make_should_return_moshi_to_deserialize_a_UndefinedComponent() {
        // Given
        val json = makeUndefinedComponentJson()

        // When
        val component =
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

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
            beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(jsonComponent))
    }

    @Test
    fun moshi_should_deserialize_bindComponent() {
        // Given
        val jsonComponent = makeBindComponent()

        // When
        val component = beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).fromJson(jsonComponent)

        // Then
        val bindComponent = component as ComponentBinding
        assertNull(bindComponent.value1)
        assertEquals("Hello", bindComponent.value2.value)
        assertEquals(String::class.java, bindComponent.value2.type)
        assertEquals("@{hello}", bindComponent.value3.value)
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
        val bindComponent = beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).fromJson(jsonComponent) as ComponentBinding
        val internalObject = beagleMoshiFactory.moshi.adapter<Any>(bindComponent.value4.type).fromJson(internalObjectJson) as InternalObject

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
        val json = beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).toJson(component)

        // Then
        assertNotNull(JSONObject(json))
    }

    @Test
    fun make_should_create_contextData_with_jsonObject() {
        // Given
        val contextDataJson = makeContextWithJsonObject()

        // When
        val contextData = beagleMoshiFactory.moshi.adapter(ContextData::class.java).fromJson(contextDataJson)

        // Then
        assertTrue(contextData?.value is JSONObject)
    }

    @Test
    fun make_should_create_contextData_with_jsonArray() {
        // Given
        val contextDataJson = makeContextWithJsonArray()

        // When
        val contextData = beagleMoshiFactory.moshi.adapter(ContextData::class.java).fromJson(contextDataJson)

        // Then
        assertTrue(contextData?.value is JSONArray)
    }

    @Test
    fun make_should_create_contextData_with_primitive() {
        // Given
        val contextDataJson = makeContextWithPrimitive()

        // When
        val contextData = beagleMoshiFactory.moshi.adapter(ContextData::class.java).fromJson(contextDataJson)

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
        val json = beagleMoshiFactory.moshi.adapter(ContextData::class.java).toJson(contextData)

        // Then
        assertNotNull(JSONObject(json))
    }
}