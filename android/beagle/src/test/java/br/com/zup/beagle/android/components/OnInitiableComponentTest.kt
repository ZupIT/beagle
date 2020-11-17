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
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.zup.beagle.android.action.AsyncActionStatus
import br.com.zup.beagle.android.action.Navigate
import br.com.zup.beagle.android.action.SendRequest
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.testutil.InstantExecutorExtension
import br.com.zup.beagle.android.utils.handleEvent
import br.com.zup.beagle.android.view.viewmodel.OnInitViewModel
import br.com.zup.beagle.android.widget.RootView
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.slot
import io.mockk.spyk
import io.mockk.unmockkConstructor
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@DisplayName("Given an OnInitiableComponent")
@ExtendWith(InstantExecutorExtension::class)
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

    @DisplayName("When handleOnInit")
    @Nested
    inner class HandleOnInit {

        @DisplayName("Then shouldn't call addOnAttachStateChangeListener without onInit")
        @Test
        fun handleEmptyOnInit() {
            // Given
            val initiableWidget = Container(children = listOf())

            // When
            initiableWidget.handleOnInit(rootView, origin)

            // Then
            verify(exactly = 0) { origin.addOnAttachStateChangeListener(any()) }
        }

        @DisplayName("Then should call addOnAttachStateChangeListener")
        @Test
        fun handleOnInit() {
            // Given
            val initiableWidget = Container(children = listOf(), onInit = listOf(Navigate.PopView()))

            // When
            initiableWidget.handleOnInit(rootView, origin)

            // Then
            verify(exactly = 1) { origin.addOnAttachStateChangeListener(listenerSlot.captured) }
        }
    }

    @DisplayName("When onViewAttachedToWindow")
    @Nested
    inner class OnViewAttached {

        @DisplayName("Then should setOnInitActionStatus true")
        @Test
        fun onViewAttachedToWindow() {
            // Given
            val action = Navigate.PopView()
            val initiableWidget = Container(children = listOf(), onInit = listOf(action))

            // When
            initiableWidget.handleOnInit(rootView, origin)
            listenerSlot.captured.onViewAttachedToWindow(origin)

            // Then
            verify(exactly = 1) { onInitViewModel.setOnInitCalled(id, true) }
        }

        @DisplayName("Then should observe the action")
        @Test
        fun onViewAttachedToWindowObserve() {
            // Given
            val status = mockk<LiveData<AsyncActionStatus>>(relaxed = true)
            val action = mockk<SendRequest>(relaxed = true)
            every { action.status } returns status
            val initiableWidget = Container(children = listOf(), onInit = listOf(action))

            // When
            initiableWidget.handleOnInit(rootView, origin)
            listenerSlot.captured.onViewAttachedToWindow(origin)

            // Then
            verify(exactly = 1) { status.observe(rootView.getLifecycleOwner(), any()) }
        }

        @DisplayName("Then should executeActions only once")
        @Test
        fun onViewAttachedToWindowExecute() {
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

        @DisplayName("Then should setOnInitFinished true to FINISHED AsyncAction")
        @Test
        fun onViewAttachedToWindowActionStatus() {
            // Given
            val status = mockk<LiveData<AsyncActionStatus>>(relaxed = true)
            val action = mockk<SendRequest>(relaxed = true)
            every { action.status } returns status
            val observerSlot = slot<Observer<AsyncActionStatus>>()
            every { status.observe(rootView.getLifecycleOwner(), capture(observerSlot)) } just Runs
            val initiableWidget = Container(children = listOf(), onInit = listOf(action))

            // When
            initiableWidget.handleOnInit(rootView, origin)
            listenerSlot.captured.onViewAttachedToWindow(origin)
            observerSlot.captured.onChanged(AsyncActionStatus.FINISHED)

            // Then
            verify(exactly = 1) { onInitViewModel.setOnInitFinished(id, true) }
        }
    }

    @DisplayName("When markToRerunOnInit")
    @Nested
    inner class MarkToRerun {

        @DisplayName("Then should setOnInitActionStatus false")
        @Test
        fun markToRerunOnInit() {
            // Given
            val action = Navigate.PopView()
            val initiableWidget = Container(children = listOf(), onInit = listOf(action))

            // When
            initiableWidget.handleOnInit(rootView, origin)
            initiableWidget.markToRerunOnInit()

            // Then
            verify(exactly = 1) { onInitViewModel.setOnInitCalled(id, false) }
        }

        @DisplayName("Then should be able to executeActions again")
        @Test
        fun markToRerunOnInitAgain() {
            // Given
            val action = Navigate.PopView()
            val initiableWidget = Container(children = listOf(), onInit = listOf(action))

            // When
            initiableWidget.handleOnInit(rootView, origin)
            listenerSlot.captured.onViewAttachedToWindow(origin)
            initiableWidget.markToRerunOnInit()
            listenerSlot.captured.onViewAttachedToWindow(origin)

            // Then
            verify(exactly = 2) { action.handleEvent(rootView, origin, action) }
        }
    }
}
