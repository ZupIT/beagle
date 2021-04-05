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

package br.com.zup.beagle.android.components.form

import android.view.ViewGroup
import android.widget.EditText
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.components.BaseComponentTest
import br.com.zup.beagle.android.components.TextInput
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import br.com.zup.beagle.core.ServerDrivenComponent
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given Simple Form")
internal class SimpleFormTest : BaseComponentTest() {

    private val simpleFormAction: Action = mockk()
    private val context: ContextData = mockk()
    private val onSubmit: List<Action> = listOf(simpleFormAction)
    private val children: List<ServerDrivenComponent> = listOf(TextInput("", error = "ddd"))

    private lateinit var simpleForm: SimpleForm

    @BeforeEach
    override fun setUp() {
        super.setUp()

        simpleForm = SimpleForm(context, onSubmit, children)
        every { simpleFormAction.execute(rootView, view) } just Runs
    }

    @DisplayName("When build view")
    @Nested
    inner class SimpleFormBuildView {

        @Test
        @DisplayName("Then should create beagle flex view")
        fun testConstructBeagleFlexView() {
            // When
            val view = simpleForm.buildView(rootView)

            // Then
            assertTrue(view is BeagleFlexView)
        }

        @Test
        @DisplayName("Then should add children in view")
        fun testAddChildrenInView() {
            // When
            simpleForm.buildView(rootView)

            // Then
            verify(exactly = once()) { beagleFlexView.addView(children) }
        }

    }

    @DisplayName("When call function submit")
    @Nested
    inner class SimpleFormSubmit {

        @Test
        @DisplayName("Then should call submit actions")
        fun testSubmitActionCall() {
            // When
            simpleForm.buildView(rootView)
            simpleForm.submit(rootView, view)

            // Then
            verify(exactly = once()) {
                simpleFormAction.execute(rootView, view)
            }
        }

    }

    @DisplayName("When call function submit")
    @Nested
    inner class SimpleFormSubmitError {

        private val text = mockk<TextInput>()
        private val editText = mockk<EditText>()
        private val errorAction: Action = mockk()
        private val onValidationError: List<Action> = listOf(errorAction)

        @BeforeEach
        fun setUp() {
            every { editText.getTag(any()) } returns text
            every { text.errorTextValuated } returns "error" andThen ""
            every { errorAction.execute(rootView, view) } just Runs
            every { beagleFlexView.childCount } returns 1

        }

        @Test
        @DisplayName("Then should call validation error actions")
        fun testValidationErrorCall() {
            // Given
            every { beagleFlexView.getChildAt(0) } returns editText

            simpleForm = SimpleForm(context, onSubmit, children, onValidationError)

            // When
            simpleForm.buildView(rootView)
            simpleForm.submit(rootView, view)

            // Then
            verify(exactly = once()) {
                errorAction.execute(rootView, view)
            }
        }

        @Test
        @DisplayName("Then should call validation error actions")
        fun testValidationErrorCallWithViewGroup() {
            // Given
            val viewGroup = mockk<ViewGroup>()

            every { beagleFlexView.getChildAt(0) } returns viewGroup
            every { beagleFlexView.getChildAt(1) } returns viewGroup
            every { viewGroup.childCount } returns 2
            every { viewGroup.getChildAt(any()) } returns editText

            simpleForm = SimpleForm(context, onSubmit, children, onValidationError)

            // When
            simpleForm.buildView(rootView)
            simpleForm.submit(rootView, view)

            // Then
            verify(exactly = once()) {
                errorAction.execute(rootView, view)
            }
        }

    }


}
