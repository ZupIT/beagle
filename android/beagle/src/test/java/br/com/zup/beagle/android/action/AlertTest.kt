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
import android.support.v7.app.AlertDialog
import android.view.View
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.utils.handleEvent
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.RootView
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class AlertTest {

    @RelaxedMockK
    private lateinit var rootView: RootView

    @MockK
    private lateinit var viewFactory: ViewFactory

    private val builder = mockk<AlertDialog.Builder>()
    private val dialog = mockk<AlertDialog>()
    private val titleSlot = slot<String>()
    private val messageSlot = slot<String>()
    private val buttonTextSlot = slot<String>()
    private val listenerSlot = slot<DialogInterface.OnClickListener>()
    private val view: View = mockk()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { viewFactory.makeAlertDialogBuilder(any()) } returns builder
        every { builder.setTitle(capture(titleSlot)) } returns builder
        every { builder.setMessage(capture(messageSlot)) } returns builder
        every { builder.setPositiveButton(capture(buttonTextSlot), capture(listenerSlot)) } returns builder
        every { builder.show() } returns mockk()
        every { dialog.dismiss() } just Runs
    }

    @Test
    fun `execute should create a AlertAction`() {
        // Given
        val action = Alert(
            title = RandomData.string(),
            message = RandomData.string(),
            labelOk = RandomData.string()
        )

        // When
        action.viewFactory = viewFactory
        action.execute(rootView, mockk())

        // Then
        assertEquals(action.title?.value, titleSlot.captured)
        assertEquals(action.message.value, messageSlot.captured)
        assertEquals(action.labelOk, buttonTextSlot.captured)
    }

    @Test
    fun `execute should create a AlertAction with text default`() {
        // Given
        val action = Alert(
            title = RandomData.string(),
            message = RandomData.string()
        )
        val randomLabel = RandomData.string()
        every { rootView.getContext().getString(android.R.string.ok) } returns randomLabel

        // When
        action.viewFactory = viewFactory
        action.execute(rootView, view)

        // Then
        assertEquals(action.title?.value, titleSlot.captured)
        assertEquals(action.message.value, messageSlot.captured)
        assertEquals(randomLabel, buttonTextSlot.captured)
    }

    @Test
    fun `click should dismiss dialog`() {
        // Given
        val action = Alert(
            title = RandomData.string(),
            message = RandomData.string(),
            labelOk = RandomData.string()
        )
        every { dialog.dismiss() } just Runs

        // When
        action.viewFactory = viewFactory
        action.execute(rootView, view)
        listenerSlot.captured.onClick(dialog, 0)

        // Then
        verify(exactly = once()) { dialog.dismiss() }
    }

    @Test
    fun `should handle onPressOk when click in button`() {
        // Given
        val onPressOk: Action = mockk(relaxed = true)
        val action = Alert(
            title = RandomData.string(),
            message = RandomData.string(),
            labelOk = RandomData.string(),
            onPressOk = onPressOk
        )
        every { dialog.dismiss() } just Runs

        // When
        action.viewFactory = viewFactory
        action.execute(rootView, view)
        listenerSlot.captured.onClick(dialog, 0)

        // Then
        verify(exactly = once()) { action.handleEvent(rootView, view, onPressOk) }
    }

}