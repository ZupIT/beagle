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

import androidx.appcompat.widget.AppCompatTextView
import br.com.zup.beagle.android.view.ViewFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach

private const val DEFAULT_TEXT = "Hello"
private const val DEFAULT_STYLE = "DummyStyle"

class TextTest : BaseComponentTest() {

    private val textView: AppCompatTextView = mockk(relaxed = true)

    private lateinit var text: Text

    @BeforeEach
    override fun setUp() {
        super.setUp()

        every { anyConstructed<ViewFactory>().makeTextView(any(), any()) } returns textView

        text = Text(DEFAULT_TEXT, DEFAULT_STYLE)
    }


    @Test
    fun build_should_return_a_TextView_instance_and_setTextWidget() {
        // When
        val view = text.buildView(rootView)

        // Then
        assertTrue(view is AppCompatTextView)
        verify(exactly = 1) { textView.text = DEFAULT_TEXT }
    }
}