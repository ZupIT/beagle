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
import br.com.zup.beagle.android.action.Navigate
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.context.ContextActionExecutor
import br.com.zup.beagle.android.widget.RootView
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkObject
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class OnInitiableComponentTest {

    private val rootView = mockk<RootView>()
    private val origin = mockk<View>()
    private val listenerSlot = slot<View.OnAttachStateChangeListener>()

    @Before
    fun setUp() {
        mockkObject(ContextActionExecutor)

        every { origin.addOnAttachStateChangeListener(capture(listenerSlot)) } just Runs
        every { ContextActionExecutor.executeActions(rootView, origin, any()) } just Runs
    }

    @After
    fun tearDown() {
        unmockkObject(ContextActionExecutor)
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
    fun `GIVEN a initiableWidget WHEN onViewAttachedToWindow THEN should executeActions only once`() {
        // Given
        val actions = listOf(Navigate.PopView())
        val initiableWidget = Container(children = listOf(), onInit = actions)

        // When
        initiableWidget.handleOnInit(rootView, origin)
        listenerSlot.captured.onViewAttachedToWindow(origin)
        listenerSlot.captured.onViewAttachedToWindow(origin)

        // Then
        verify(exactly = 1) { ContextActionExecutor.executeActions(rootView, origin, actions) }
    }

    @Test
    fun `GIVEN a initiableWidget witch already called onInit WHEN markToRerunOnInit THEN should be able to executeActions again`() {
        // Given
        val actions = listOf(Navigate.PopView())
        val initiableWidget = Container(children = listOf(), onInit = actions)

        // When
        initiableWidget.handleOnInit(rootView, origin)
        listenerSlot.captured.onViewAttachedToWindow(origin)
        initiableWidget.markToRerunOnInit()
        listenerSlot.captured.onViewAttachedToWindow(origin)

        // Then
        verify(exactly = 2) { ContextActionExecutor.executeActions(rootView, origin, actions) }
    }
}
