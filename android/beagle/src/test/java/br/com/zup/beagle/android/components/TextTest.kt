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

import android.widget.TextView
import br.com.zup.beagle.android.utils.StyleManager
import br.com.zup.beagle.android.view.ViewFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

private const val DEFAULT_TEXT = "Hello"
private const val DEFAULT_STYLE = "DummyStyle"
private const val DEFAULT_STYLE_INTEGER = 123

@DisplayName("Given a Container")
class TextTest : BaseComponentTest() {

    private val textView: TextView = mockk(relaxed = true)

    private lateinit var text: Text

    @DisplayName("When build view without style id")
    @Nested
    inner class WithoutStyleIdTest {

        @Test
        @DisplayName("Then should create correct text")
        fun testBuildCorrectText() {
            // Given
            every { ViewFactory.makeTextView(any()) } returns textView

            text = Text(DEFAULT_TEXT)

            // When
            val view = text.buildView(rootView)

            // Then
            assertTrue(view is TextView)
            verify(exactly = 1) { ViewFactory.makeTextView(rootView.getContext()) }
        }

    }

    @DisplayName("When build view with style")
    @Nested
    inner class WithStyleIdTest {

        @Test
        @DisplayName("Then should create correct text")
        fun testBuildCorrectText() {
            // Given
            mockkConstructor(StyleManager::class)

            every { anyConstructed<StyleManager>().getTextStyle(DEFAULT_STYLE) } returns DEFAULT_STYLE_INTEGER
            every { ViewFactory.makeTextView(any(), any()) } returns textView

            text = Text(text = DEFAULT_TEXT, styleId = DEFAULT_STYLE)

            // When
            val view = text.buildView(rootView)

            // Then
            assertTrue(view is TextView)

            verify(exactly = 1) {
                ViewFactory.makeTextView(any(), any())
            }
        }

    }
}