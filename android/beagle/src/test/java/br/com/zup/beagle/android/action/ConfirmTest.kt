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

package br.com.zup.beagle.android.action

import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.utils.handleEvent
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.core.ServerDrivenComponent
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class ConfirmTest {

    @RelaxedMockK
    private lateinit var rootView: RootView

    @MockK
    private lateinit var viewFactory: ViewFactory

    private val builder = mockk<AlertDialog.Builder>()
    private val dialogBox = mockk<AlertDialog>()
    private val titleSlot = slot<String>()
    private val messageSlot = slot<String>()
    private val labelOkSlot = slot<String>()
    private val labelCancelSlot = slot<String>()
    private val listenerOkSlot = slot<DialogInterface.OnClickListener>()
    private val listenerCancelSlot = slot<DialogInterface.OnClickListener>()
    private val view: View = mockk()

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)

        every { viewFactory.makeAlertDialogBuilder(any()) } returns builder
        every { builder.setTitle(capture(titleSlot)) } returns builder
        every { builder.setMessage(capture(messageSlot)) } returns builder
        every { builder.setPositiveButton(capture(labelOkSlot), capture(listenerOkSlot)) } returns builder
        every { builder.setNegativeButton(capture(labelCancelSlot), capture(listenerCancelSlot)) } returns builder
        every { builder.show() } returns mockk()
    }

    @Test
    fun `execute should create a ConfirmAction`() {
        // Given
        val onPressOk: Action = mockk(relaxed = true)
        val onPressCancel: Action = mockk(relaxed = true)
        val action = Confirm(
            title = RandomData.string(),
            message = RandomData.string(),
            labelOk = RandomData.string(),
            labelCancel = RandomData.string(),
            onPressOk = onPressOk,
            onPressCancel = onPressCancel
        )

        // When
        action.viewFactory = viewFactory
        action.execute(rootView, view)

        // Then
        assertEquals(action.title?.value, titleSlot.captured)
        assertEquals(action.message.value, messageSlot.captured)
        assertEquals(action.labelOk, labelOkSlot.captured)
        assertEquals(action.labelCancel, labelCancelSlot.captured)
    }

    @Test
    fun `execute should create a ConfirmAction with text default`() {
        // Given
        val action = Confirm(
            title = RandomData.string(),
            message = RandomData.string()
        )
        val randomLabelOk = RandomData.string()
        val randomLabelCancel = RandomData.string()
        every { rootView.getContext().getString(android.R.string.ok) } returns randomLabelOk
        every { rootView.getContext().getString(android.R.string.cancel) } returns randomLabelCancel

        // When
        action.viewFactory = viewFactory
        action.execute(rootView, view)

        // Then
        assertEquals(action.title?.value, titleSlot.captured)
        assertEquals(action.message.value, messageSlot.captured)
        assertEquals(randomLabelOk, labelOkSlot.captured)
        assertEquals(randomLabelCancel, labelCancelSlot.captured)
    }

    @Test
    fun `click should dismiss dialog`() {
        // Given
        val action = Confirm(
            title = RandomData.string(),
            message = RandomData.string(),
            labelOk = RandomData.string()
        )
        every { dialogBox.dismiss() } just Runs

        // When
        action.viewFactory = viewFactory
        action.execute(rootView, view)
        listenerOkSlot.captured.onClick(dialogBox, 0)

        // Then
        verify(exactly = once()) { dialogBox.dismiss() }
    }

    @Test
    fun `should handle onPressOk when click in button`() {
        // Given
        val onPressOk: Action = mockk(relaxed = true)
        val action = Confirm(
            title = RandomData.string(),
            message = RandomData.string(),
            labelOk = RandomData.string(),
            labelCancel = RandomData.string(),
            onPressOk = onPressOk
        )
        every { dialogBox.dismiss() } just Runs
        // When
        action.viewFactory = viewFactory
        action.execute(rootView, view)
        listenerOkSlot.captured.onClick(dialogBox, 0)

        // Then
        verify(exactly = once()) { action.handleEvent(rootView, view, onPressOk, analyticsValue = "onPressOk") }
    }

    @Test
    fun `should handle onPressCancel when click in button`() {
        // Given
        val onPressCancel: Action = mockk(relaxed = true)
        val action = Confirm(
            title = RandomData.string(),
            message = RandomData.string(),
            labelOk = RandomData.string(),
            labelCancel = RandomData.string(),
            onPressCancel = onPressCancel
        )
        every { dialogBox.dismiss() } just Runs
        // When
        action.viewFactory = viewFactory
        action.execute(rootView, view)
        listenerCancelSlot.captured.onClick(dialogBox, 0)

        // Then
        verify(exactly = once()) { action.handleEvent(rootView, view, onPressCancel, analyticsValue = "onPressCancel") }
    }
}