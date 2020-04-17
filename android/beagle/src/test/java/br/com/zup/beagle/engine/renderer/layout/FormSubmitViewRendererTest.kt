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

package br.com.zup.beagle.engine.renderer.layout

import android.content.Context
import android.view.View
import br.com.zup.beagle.BaseTest
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.engine.renderer.ViewRendererFactory
import br.com.zup.beagle.extensions.once
import br.com.zup.beagle.view.ViewFactory
import br.com.zup.beagle.widget.form.FormSubmit
import br.com.zup.beagle.widget.form.InputWidget
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class FormSubmitViewRendererTest : BaseTest() {

    @MockK
    private lateinit var formSubmit: FormSubmit
    @MockK
    private lateinit var viewRendererFactory: ViewRendererFactory
    @MockK
    private lateinit var viewFactory: ViewFactory
    @InjectMockKs
    private lateinit var formSubmitViewRenderer: FormSubmitViewRenderer

    @MockK
    private lateinit var rootView: RootView
    @MockK
    private lateinit var inputWidget: InputWidget
    @MockK
    private lateinit var context: Context
    @MockK
    private lateinit var view: View

    override fun setUp() {
        super.setUp()
        every { viewRendererFactory.make(any()).build(any()) } returns view
        every { rootView.getContext() } returns context
        every { formSubmit.child } returns inputWidget
        every { view.tag = any() } just Runs
    }

    @Test
    fun build_should_make_child() {
        // WHEN
        val actual = formSubmitViewRenderer.build(rootView)

        // THEN
        assertEquals(view, actual)
    }

    @Test
    fun build_should_set_widget_on_tag() {
        // WHEN
        formSubmitViewRenderer.build(rootView)

        // THEN
        verify(exactly = once()) { view.tag = formSubmit }
    }
}