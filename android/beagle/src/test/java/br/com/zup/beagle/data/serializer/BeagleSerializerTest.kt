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

package br.com.zup.beagle.data.serializer

import br.com.zup.beagle.action.Action
import br.com.zup.beagle.action.Navigate
import br.com.zup.beagle.action.Route
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.exception.BeagleException
import br.com.zup.beagle.logger.BeagleMessageLogs
import br.com.zup.beagle.testutil.RandomData
import br.com.zup.beagle.widget.ui.Button
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

class BeagleSerializerTest {

    @MockK
    private lateinit var moshi: Moshi
    @MockK
    private lateinit var jsonAdapter: JsonAdapter<ServerDrivenComponent>
    @MockK
    private lateinit var actionJsonAdapter: JsonAdapter<Action>

    private lateinit var beagleSerializer: BeagleSerializer

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        beagleSerializer = BeagleSerializer(BeagleMoshi)

        mockkObject(BeagleMessageLogs)
        mockkObject(BeagleMoshi)

        every { BeagleMessageLogs.logDeserializationError(any(), any()) } just Runs
        every { BeagleMoshi.moshi } returns moshi
        every { moshi.adapter(ServerDrivenComponent::class.java) } returns jsonAdapter
        every { moshi.adapter(Action::class.java) } returns actionJsonAdapter
    }

    @After
    fun tearDown() {
        unmockkObject(BeagleMessageLogs)
        unmockkObject(BeagleMoshi)
    }

    @Test
    fun serializeWidget_should_return_a_String_when_pass_a_valid_Widget() {
        // Given
        val json = "{}"
        val button = Button(RandomData.string())
        every { jsonAdapter.toJson(button) } returns json

        // When
        val actual = beagleSerializer.serializeComponent(button)

        // Then
        assertEquals(json, actual)
    }

    @Test
    fun serializeWidget_should_return_a_BeagleException_when_toJson_returns_null() {
        // Given
        val button = Button(RandomData.string())
        every { jsonAdapter.toJson(button) } returns null

        // When
        val actual = assertFails{ beagleSerializer.serializeComponent(button) }

        // Then
        assertTrue(actual is BeagleException)
    }

    @Test
    fun serializeWidget_should_return_a_BeagleException_when_toJson_throws_exception() {
        // Given
        val exception = IOException()
        every { jsonAdapter.toJson(any()) } throws exception

        // When
        val button = Button(RandomData.string())
        val actual = assertFails { beagleSerializer.serializeComponent(button) }

        // Then
        assertTrue(actual is BeagleException)
    }

    @Test
    fun deserializeWidget_should_return_a_Widget_when_pass_a_valid_json_representation() {
        // Given
        val json = "{}"
        val button = Button(RandomData.string())
        every { jsonAdapter.fromJson(json) } returns button

        // When
        val actual = beagleSerializer.deserializeComponent(json)

        // Then
        assertEquals(button, actual)
    }

    @Test
    fun deserializeWidget_should_return_a_BeagleDeserializationException_when_fromJson_returns_null() {
        // Given
        val json = "{}"
        every { jsonAdapter.fromJson(json) } returns null

        // When
        val actual = assertFails{ beagleSerializer.deserializeComponent(json) }

        // Then
        assertTrue(actual is BeagleException)
    }

    @Test
    fun deserializeWidget_should_return_a_BeagleDeserializationException_when_fromJson_throws_exception() {
        // Given
        val exception = IOException()
        every { jsonAdapter.fromJson(any<String>()) } throws exception

        // When
        val actual = assertFails { beagleSerializer.deserializeComponent(RandomData.string()) }

        // Then
        assertTrue(actual is BeagleException)
    }

    @Test
    fun deserializeAction_should_return_a_Action_when_pass_a_valid_json_representation() {
        // Given
        val json = "{}"
        val navigate = Navigate.PushView(Route.Remote(""))
        every { actionJsonAdapter.fromJson(json) } returns navigate

        // When
        val actual = beagleSerializer.deserializeAction(json)

        // Then
        assertEquals(navigate, actual)
    }

    @Test
    fun deserializeAction_should_return_a_BeagleDeserializationException_when_fromJson_returns_null() {
        // Given
        val json = "{}"
        every { actionJsonAdapter.fromJson(json) } returns null

        // When
        val actual = assertFails{ beagleSerializer.deserializeAction(json) }

        // Then
        assertTrue(actual is BeagleException)
    }

    @Test
    fun deserializeAction_should_return_a_BeagleDeserializationException_when_fromJson_throws_exception() {
        // Given
        val exception = IOException()
        every { actionJsonAdapter.fromJson(any<String>()) } throws exception

        // When
        val actual = assertFails { beagleSerializer.deserializeAction(RandomData.string()) }

        // Then
        assertTrue(actual is BeagleException)
    }
}
