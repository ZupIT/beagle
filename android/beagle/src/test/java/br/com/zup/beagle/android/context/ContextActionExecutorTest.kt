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

package br.com.zup.beagle.android.context

import androidx.appcompat.app.AppCompatActivity
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.data.serializer.BeagleMoshi
import br.com.zup.beagle.android.engine.renderer.ActivityRootView
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.utils.ViewModelProviderFactory
import br.com.zup.beagle.android.view.viewmodel.ScreenContextViewModel
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.Test

import kotlin.test.assertEquals

class ContextActionExecutorTest : BaseTest() {

    private val rootView = mockk<ActivityRootView>()
    private val contextDataManager = mockk<ContextDataManager>(relaxed = true)

    private val action = mockk<Action>()
    private lateinit var contextActionExecutor: ContextActionExecutor

    private val contextDataSlot = slot<ContextData>()

    override fun setUp() {
        super.setUp()

        mockkObject(ViewModelProviderFactory)
        mockkObject(BeagleMoshi)

        contextActionExecutor = ContextActionExecutor()

        every { action.execute(any()) } just Runs
        every { beagleSdk.registeredWidgets() } returns listOf()
        every { rootView.activity } returns mockk()

        every { ViewModelProviderFactory.of(any<AppCompatActivity>())[ScreenContextViewModel::class.java]
            .contextDataManager } returns contextDataManager
        every { contextDataManager.addContext(capture(contextDataSlot)) } just Runs
    }

    override fun tearDown() {
        super.tearDown()

        unmockkAll()
    }

    @Test
    fun executeActions_should_create_implicit_context() {
        // Given
        val eventId = "onChange"
        val value = RandomData.string()

        // When
        contextActionExecutor.executeActions(rootView, listOf(action), eventId, value)

        // Then
        verifySequence {
            contextDataManager.addContext(any())
            action.execute(rootView)
            contextDataManager.removeContext(any())
        }
    }

    @Test
    fun executeActions_should_parse_primitive_value_to_JSONObject() {
        // Given
        val eventId = "onChange"
        val value = RandomData.string()

        // When
        contextActionExecutor.executeActions(rootView, listOf(action), eventId, value)

        // Then
        assertEquals(eventId, contextDataSlot.captured.id)
        assertEquals("{\"value\":\"$value\"}", contextDataSlot.captured.value.toString())
    }

    @Test
    fun executeActions_should_parse_object_value_to_JSONObject() {
        // Given
        val eventId = "onChange"
        val value = mockk<Container>()
        val jsonMock = "{}"
        every { BeagleMoshi.moshi.adapter<Container>(any<Class<*>>()).toJson(value) } returns jsonMock

        // When
        contextActionExecutor.executeActions(rootView, listOf(action), eventId, value)

        // Then
        assertEquals(eventId, contextDataSlot.captured.id)
        assertEquals(jsonMock, contextDataSlot.captured.value.toString())
    }

    @Test
    fun executeActions_should_not_create_implicit_context_when_value_is_null() {
        // Given
        val eventId = "onChange"
        val value = null

        // When
        contextActionExecutor.executeActions(rootView, listOf(action), eventId, value)

        // Then
        verify(exactly = once()) { action.execute(rootView) }
    }
}