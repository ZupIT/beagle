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

package br.com.zup.beagle.action

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import br.com.zup.beagle.extensions.once
import br.com.zup.beagle.testutil.RandomData
import br.com.zup.beagle.view.ViewFactory
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ShowNativeDialogActionHandlerTest {

    @MockK
    private lateinit var context: Context
    @MockK
    private lateinit var viewFactory: ViewFactory

    private val builder = mockk<AlertDialog.Builder>()
    private val dialog = mockk<AlertDialog>()
    private val titleSlot = slot<String>()
    private val messageSlot = slot<String>()
    private val buttonTextSlot = slot<String>()
    private val listenerSlot = slot<DialogInterface.OnClickListener>()

    private lateinit var showNativeDialogActionHandler: ShowNativeDialogActionHandler

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        showNativeDialogActionHandler = ShowNativeDialogActionHandler(viewFactory)

        every { viewFactory.makeAlertDialogBuilder(any()) } returns builder
        every { builder.setTitle(capture(titleSlot)) } returns builder
        every { builder.setMessage(capture(messageSlot)) } returns builder
        every { builder.setPositiveButton(capture(buttonTextSlot), capture(listenerSlot)) } returns builder
        every { builder.show() } returns mockk()
    }

    @Test
    fun handle_should_create_a_AlertDialog() {
        // Given
        val action = ShowNativeDialog(
            title = RandomData.string(),
            message = RandomData.string(),
            buttonText = RandomData.string()
        )

        // When
        showNativeDialogActionHandler.handle(context, action)

        // Then
        assertEquals(action.title, titleSlot.captured)
        assertEquals(action.message, messageSlot.captured)
        assertEquals(action.buttonText, buttonTextSlot.captured)
    }

    @Test
    fun handle_should_dismiss_dialog_when_clickListener_is_call() {
        // Given
        val action = ShowNativeDialog(
            title = RandomData.string(),
            message = RandomData.string(),
            buttonText = RandomData.string()
        )
        every { dialog.dismiss() } just Runs

        // When
        showNativeDialogActionHandler.handle(context, action)
        listenerSlot.captured.onClick(dialog, 0)

        // Then
        verify(exactly = once()) { dialog.dismiss() }
    }
}