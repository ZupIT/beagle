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

package br.com.zup.beagle.android.components

import android.view.View
import androidx.lifecycle.ViewModelProvider
import br.com.zup.beagle.android.action.Navigate
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.context.ContextActionExecutor
import br.com.zup.beagle.android.testutil.getPrivateField
import br.com.zup.beagle.android.utils.generateViewModelInstance
import br.com.zup.beagle.android.utils.handleEvent
import br.com.zup.beagle.android.view.viewmodel.OnInitViewModel
import br.com.zup.beagle.android.widget.RootView
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.unmockkConstructor
import io.mockk.unmockkObject
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class OnInitiableComponentTest {

    private val rootView = mockk<RootView>(relaxed = true)
    private val onInitViewModel = spyk(OnInitViewModel())
    private val origin = mockk<View>(relaxed = true)
    private val listenerSlot = slot<View.OnAttachStateChangeListener>()
    private val id = 10

    @BeforeEach
    fun setUp() {
        mockkConstructor(ViewModelProvider::class)
        every { anyConstructed<ViewModelProvider>().get(OnInitViewModel::class.java) } returns onInitViewModel

        every { origin.id } returns id
        every { origin.addOnAttachStateChangeListener(capture(listenerSlot)) } just Runs
    }

    @AfterEach
    fun tearDown() {
        unmockkConstructor(ViewModelProvider::class)
    }

    @Test
    fun `GIVEN a initiableWidget without onInit actions WHEN handleOnInit THEN shouldn't call addOnAttachStateChangeListener`() {
        // Given
        val initiableWidget = Container(children = listOf())

        // When
        initiableWidget.handleOnInit(rootView, origin)

        // Then
        verify(exactly = 0) { origin.addOnAttachStateChangeListener(any()) }
    }

    @Test
    fun `GIVEN a initiableWidget WHEN handleOnInit THEN should add listener`() {
        // Given
        val initiableWidget = Container(children = listOf(), onInit = listOf(Navigate.PopView()))

        // When
        initiableWidget.handleOnInit(rootView, origin)

        // Then
        verify(exactly = 1) { origin.addOnAttachStateChangeListener(listenerSlot.captured) }
    }

    @Test
    fun `GIVEN a initiableWidget WHEN onViewAttachedToWindow THEN should setOnInitActionStatus true`() {
        // Given
        val action = Navigate.PopView()
        val initiableWidget = Container(children = listOf(), onInit = listOf(action))

        // When
        initiableWidget.handleOnInit(rootView, origin)
        listenerSlot.captured.onViewAttachedToWindow(origin)

        // Then
        verify(exactly = 1) { onInitViewModel.setOnInitActionStatus(id, true) }
    }

    @Test
    fun `GIVEN a initiableWidget WHEN onViewAttachedToWindow THEN should executeActions only once`() {
        // Given
        val action = Navigate.PopView()
        val initiableWidget = Container(children = listOf(), onInit = listOf(action))

        // When
        initiableWidget.handleOnInit(rootView, origin)
        listenerSlot.captured.onViewAttachedToWindow(origin)
        listenerSlot.captured.onViewAttachedToWindow(origin)

        // Then
        verify(exactly = 1) { action.handleEvent(rootView, origin, action) }
    }

    @Test
    fun `GIVEN a initiableWidget WHEN markToRerunOnInit THEN should setOnInitActionStatus false`() {
        // Given
        val action = Navigate.PopView()
        val initiableWidget = Container(children = listOf(), onInit = listOf(action))

        // When
        initiableWidget.handleOnInit(rootView, origin)
        initiableWidget.markToRerunOnInit()

        // Then
        verify(exactly = 1) { onInitViewModel.setOnInitActionStatus(id, false) }
    }

    @Test
    fun `GIVEN a initiableWidget witch already called onInit WHEN markToRerunOnInit THEN should be able to executeActions again`() {
        // Given
        val action = Navigate.PopView()
        val initiableWidget = Container(children = listOf(), onInit = listOf(action))

        // When
        initiableWidget.handleOnInit(rootView, origin)
        listenerSlot.captured.onViewAttachedToWindow(origin)
        initiableWidget.markToRerunOnInit()
        listenerSlot.captured.onViewAttachedToWindow(origin)

        // Then
        verify(exactly = 2) { action.handleEvent(rootView, origin, action)  }
    }
}
