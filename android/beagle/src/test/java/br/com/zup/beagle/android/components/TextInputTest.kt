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
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.core.widget.TextViewCompat
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
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

const val VALUE = "Text Value"
const val PLACE_HOLDER = "Text Hint"
const val READ_ONLY = true
const val DISABLED = false
const val HIDDEN = true
const val STYLE_ID = "Style"
val TYPE = TextInputType.NUMBER

@DisplayName("Given a TextInput")
internal class TextInputTest : BaseComponentTest() {

    private val editText: EditText = mockk(relaxed = true, relaxUnitFun = true)
    private val styleManager: StyleManager = mockk(relaxed = true)
    private val context: Context = mockk()
    private val textWatcher: TextWatcher = mockk()

    private lateinit var textInput: TextInput

    @BeforeEach
    override fun setUp() {
        super.setUp()

        mockkStatic(TextViewCompat::class)

        styleManagerFactory = styleManager

        every { anyConstructed<ViewFactory>().makeInputText(any(), any()) } returns editText
        every { TextViewCompat.setTextAppearance(any(), any()) } just Runs

        every { BeagleEnvironment.application } returns mockk(relaxed = true)

        every { editText.context } returns context

        textInput = callTextInput(TYPE)

        textInput.setPrivateField("textWatcher", textWatcher)
    }

    private fun callTextInput(type: TextInputType) = TextInput(
        value = VALUE,
        placeholder = PLACE_HOLDER,
        readOnly = READ_ONLY,
        disabled = DISABLED,
        hidden = HIDDEN,
        type = type,
        styleId = STYLE_ID
    )

    @Test
    @DisplayName("Then should build a textView")
    fun buildEditTextInstance() {
        // When
        val view = textInput.buildView(rootView)

        // Then
        assertTrue(view is EditText)
        verify(exactly = once()) {
            editText.setText(VALUE)
            editText.hint = PLACE_HOLDER
            editText.isEnabled = READ_ONLY
            editText.isEnabled = DISABLED
            editText.visibility = View.INVISIBLE
            editText.isFocusable = true
            editText.isFocusableInTouchMode = true
        }
    }

    @Test
    @DisplayName("Then should get the value set for the text input component")
    fun getValueOfTextInput() {
        // Given
        textInput.buildView(rootView)

        // When
        val textInputValue = textInput.getValue()

        // Then
        assertEquals(textInputValue, editText.text.toString())
    }

    @Test
    @DisplayName("Then check if error message is set")
    fun checkErrorMessage() {
        // Given
        textInput.buildView(rootView)

        // When
        textInput.onErrorMessage("Error")

        // Then
        verify(exactly = once()) { editText.error = "Error" }
    }

//    @Test
//    @DisplayName("Then check if the text is changed")
//    fun checkSetUpOnTextChange() {
//    }

    @Test
    @DisplayName("Then check if text change was removed")
    fun checkRemovedTextChange() {
        // When
        textInput.buildView(rootView)

        // Then
        verify(exactly = once()) { editText.removeTextChangedListener(textWatcher) }
    }

    @Test
    @DisplayName("Then verify setData when values is delivered")
    fun verifySetDataValue() {
        // When
        textInput.buildView(rootView)

        // Then
        verify(exactly = once()) {
            editText.setText(VALUE)
            editText.hint = PLACE_HOLDER
            editText.isEnabled = READ_ONLY
            editText.isEnabled = DISABLED
            editText.visibility = View.INVISIBLE
            editText.isFocusable = true
            editText.isFocusableInTouchMode = true
        }
    }

    @Test
    @DisplayName("Then verify set enabled config of text input")
    fun verifyEnabledConfig() {
        // When
        textInput.buildView(rootView)

        // Then
        verify(exactly = once()) {
            editText.isEnabled = true
        }
    }

    @DisplayName("When passing input type")
    @Nested
    inner class InputTypeTest {

        @Test
        @DisplayName("Then should call setRawInputType with TYPE_CLASS_DATETIME")
        fun testInputTypeDate() {
            // Given
            val type = TextInputType.DATE

            // When
            val textInput = callTextInput(type)
            textInput.buildView(rootView)

            // Then
            verify(exactly = 1) { editText.setRawInputType(InputType.TYPE_CLASS_DATETIME) }
        }
    }

    @Test
    @DisplayName("Then should call setRawInputType with TYPE_TEXT_VARIATION_EMAIL_ADDRESS")
    fun setInputTypeEmail() {
        // Given
        val type = TextInputType.EMAIL

        // When
        val textInput = callTextInput(type)
        textInput.buildView(rootView)

        // Then
        verify(exactly = 1) { editText.setRawInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) }
    }

    @Test
    @DisplayName("Then should call TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_PASSWORD")
    fun setInputTypePassword() {
        // Given
        val type = TextInputType.PASSWORD

        // When
        val textInput = callTextInput(type)
        textInput.buildView(rootView)

        // Then
        verify(exactly = 1) { editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD }
    }

    @Test
    @DisplayName("Then should call setRawInputType with TYPE_CLASS_NUMBER")
    fun setInputTypeNumber() {
        // Given
        val type = TextInputType.NUMBER

        // When
        val textInput = callTextInput(type)
        textInput.buildView(rootView)

        // Then
        verify(exactly = 1) { editText.setRawInputType(InputType.TYPE_CLASS_NUMBER) }
    }

    @Test
    @DisplayName(" Then should call setRawInputType with TYPE_CLASS_TEXT or TYPE_TEXT_FLAG_CAP_SENTENCES")
    fun setInputTypeText() {
        // Given
        val type = TextInputType.TEXT

        // When
        val textInput = callTextInput(type)
        textInput.buildView(rootView)

        // Then
        verify(exactly = 1) { editText.setRawInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES) }
    }

}
