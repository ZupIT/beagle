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

import android.content.Context
import android.support.v4.widget.TextViewCompat
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import br.com.zup.beagle.android.components.utils.styleManagerFactory
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.testutil.setPrivateField
import br.com.zup.beagle.android.utils.StyleManager
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.widget.core.TextInputType
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertTrue

const val VALUE = "Text Value"
const val PLACE_HOLDER = "Text Hint"
const val READ_ONLY = true
const val DISABLED = false
const val HIDDEN = true
const val STYLE_ID = "Style"
val TYPE = TextInputType.NUMBER

class TextInputTest : BaseComponentTest() {

    private val editText: EditText = mockk(relaxed = true, relaxUnitFun = true)
    private val styleManager: StyleManager = mockk(relaxed = true)
    private val context: Context = mockk()
    private val textWatcher: TextWatcher = mockk()

    private lateinit var textInput: TextInput

    override fun setUp() {
        super.setUp()

        mockkStatic(TextViewCompat::class)

        styleManagerFactory = styleManager

        every { anyConstructed<ViewFactory>().makeInputText(any(), any()) } returns editText
        every { TextViewCompat.setTextAppearance(any(), any()) } just Runs

        every { BeagleEnvironment.application } returns mockk(relaxed = true)

        every { editText.context } returns context

        textInput = TextInput(
            value = VALUE,
            placeholder = PLACE_HOLDER,
            readOnly = READ_ONLY,
            disabled = DISABLED,
            hidden = HIDDEN,
            type = TYPE,
            styleId = STYLE_ID
        )

        textInput.setPrivateField("textWatcher", textWatcher)
    }

    @Test
    fun `build should return a EditText instance`() {
        // When
        val view = textInput.buildView(rootView)

        // Then
        assertTrue(view is EditText)
    }

    @Test
    fun `verify setData when values is delivered`() {
        // When
        textInput.buildView(rootView)

        // Then
        verify(exactly = once()) { editText.setText(VALUE) }
        verify(exactly = once()) { editText.hint = PLACE_HOLDER }
        verify(exactly = once()) { editText.isEnabled = READ_ONLY }
        verify(exactly = once()) { editText.isEnabled = DISABLED }
        verify(exactly = once()) { editText.visibility = View.INVISIBLE }
        verify(exactly = once()) { editText.inputType = InputType.TYPE_CLASS_NUMBER }
        verify(exactly = once()) { editText.isFocusable = true }
        verify(exactly = once()) { editText.isFocusableInTouchMode = true }
    }
}
