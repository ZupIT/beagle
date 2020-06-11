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

package br.com.zup.beagle.android.form

import br.com.zup.beagle.android.components.form.core.FormSubmitter
import br.com.zup.beagle.android.data.serializer.BeagleSerializer
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.networking.HttpClient
import br.com.zup.beagle.android.networking.HttpMethod
import br.com.zup.beagle.android.networking.RequestData
import br.com.zup.beagle.android.networking.urlbuilder.UrlBuilder
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.widget.form.Form
import br.com.zup.beagle.widget.form.FormMethodType
import br.com.zup.beagle.widget.form.FormRemoteAction
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

private val FORMS_VALUE = mapOf<String, String>()
private val ACTION = RandomData.string()

class FormSubmitterTest {

    @MockK
    private lateinit var httpClient: HttpClient
    @MockK
    private lateinit var beagleSerializer: BeagleSerializer
    @MockK
    private lateinit var urlBuilder: UrlBuilder

    private val requestDataSlot = slot<RequestData>()
    private val urlSlot = slot<String>()

    @InjectMockKs
    private lateinit var formSubmitter: FormSubmitter

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        mockkObject(BeagleEnvironment)

        every { BeagleEnvironment.beagleSdk.config.baseUrl } returns RandomData.httpUrl()
        every { httpClient.execute(capture(requestDataSlot), any(), any()) } returns mockk()
        every { urlBuilder.format(any(), capture(urlSlot)) } returns ACTION
    }

    @After
    fun tearDown() {
        unmockkObject(BeagleEnvironment)
    }

    @Test
    fun submitForm_should_create_requestData_correctly() {
        // Given
        val action = createAction(FormMethodType.POST)
        val inputName = RandomData.string()
        val inputValue = RandomData.string()
        val formsValue = mapOf(inputName to inputValue)

        // When
        formSubmitter.submitForm(action, formsValue) {}

        // Then
        verify(exactly = once()) { httpClient.execute(any(), any(), any()) }

        val requestData = requestDataSlot.captured
        assertEquals(HttpMethod.POST, requestData.method)
        assertEquals("""{"$inputName":"$inputValue"}""", requestData.body)
        assertEquals(ACTION, requestData.uri.toString())
    }

    @Test
    fun submitForm_should_create_requestData_with_PUT_httpMethod() {
        // Given
        val action = createAction(FormMethodType.PUT)

        // When
        formSubmitter.submitForm(action, FORMS_VALUE) {}

        // Then
        assertEquals(HttpMethod.PUT, requestDataSlot.captured.method)
    }

    @Test
    fun submitForm_should_create_requestData_with_DELETE_httpMethod() {
        // Given
        val action = createAction(FormMethodType.DELETE)

        // When
        formSubmitter.submitForm(action, FORMS_VALUE) {}

        // Then
        assertEquals(HttpMethod.DELETE, requestDataSlot.captured.method)
    }

    @Test
    fun submitForm_should_create_requestData_with_GET_httpMethod() {
        // Given
        val action = createAction(FormMethodType.GET)

        // When
        formSubmitter.submitForm(action, FORMS_VALUE) {}

        // Then
        assertEquals(HttpMethod.GET, requestDataSlot.captured.method)
    }

    @Test
    fun submitForm_should_create_requestData_with_POST_httpMethod() {
        // Given
        val action = createAction(FormMethodType.POST)

        // When
        formSubmitter.submitForm(action, FORMS_VALUE) {}

        // Then
        assertEquals(HttpMethod.POST, requestDataSlot.captured.method)
    }

    @Test
    fun submitForm_should_set_form_action_as_url_on_requestData() {
        // Given
        val action = createAction(FormMethodType.POST)

        // When
        formSubmitter.submitForm(action, FORMS_VALUE) {}

        // Then
        assertEquals(ACTION, requestDataSlot.captured.uri.toString())
    }

    @Test
    fun submitForm_should_set_querystring_as_url_on_requestData_when_method_is_GET() {
        // Given
        val formsValue = mapOf(
            RandomData.string(3) to RandomData.string(3),
            RandomData.string(3) to RandomData.string(3)
        )
        val action = createAction(FormMethodType.GET)

        // When
        formSubmitter.submitForm(action, formsValue) {}

        // Then
        val formElements = formsValue.entries
        val element0 = formElements.elementAt(0)
        val element1 = formElements.elementAt(1)
        val expected = "$ACTION?${element0.key}=${element0.value}&${element1.key}=${element1.value}"
        assertEquals(expected, urlSlot.captured)
    }

    @Test
    fun submitForm_should_set_querystring_as_url_on_requestData_when_method_is_DELETE() {
        // Given
        val formsValue = mapOf(
            RandomData.string(3) to RandomData.string(3),
            RandomData.string(3) to RandomData.string(3)
        )
        val action = createAction(FormMethodType.GET)

        // When
        formSubmitter.submitForm(action, formsValue) {}

        // Then
        val formElements = formsValue.entries
        val element0 = formElements.elementAt(0)
        val element1 = formElements.elementAt(1)
        val expected = "$ACTION?${element0.key}=${element0.value}&${element1.key}=${element1.value}"
        assertEquals(expected, urlSlot.captured)
    }

    @Test
    fun submitForm_should_put_requestData_body_when_method_is_POST() {
        // Given
        val formsValue = mapOf(
            RandomData.string(3) to RandomData.string(3),
            RandomData.string(3) to RandomData.string(3)
        )
        val action = createAction(FormMethodType.POST)

        // When
        formSubmitter.submitForm(action, formsValue) {}

        // Then
        val formElements = formsValue.entries
        val element0 = formElements.elementAt(0)
        val element1 = formElements.elementAt(1)
        val expected =
            """{"${element0.key}":"${element0.value}", "${element1.key}":"${element1.value}"}"""
        assertEquals(expected, requestDataSlot.captured.body)
    }

    @Test
    fun submitForm_should_put_requestData_body_when_method_is_PUT() {
        // Given
        val formsValue = mapOf(
            RandomData.string(3) to RandomData.string(3),
            RandomData.string(3) to RandomData.string(3)
        )
        val action = createAction(FormMethodType.POST)

        // When
        formSubmitter.submitForm(action, formsValue) {}

        // Then
        val formElements = formsValue.entries
        val element0 = formElements.elementAt(0)
        val element1 = formElements.elementAt(1)
        val expected =
            """{"${element0.key}":"${element0.value}", "${element1.key}":"${element1.value}"}"""
        assertEquals(expected, requestDataSlot.captured.body)
    }

    private fun createAction(method: FormMethodType) = FormRemoteAction(
        path = ACTION,
        method = method
    )

    private fun createForm(action: FormRemoteAction) = Form(
        action = action,
        child = mockk()
    )
}