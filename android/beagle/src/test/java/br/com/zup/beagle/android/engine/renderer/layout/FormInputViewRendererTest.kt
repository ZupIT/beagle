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

package br.com.zup.beagle.android.engine.renderer.layout

import android.content.Context
import android.view.View
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.action.Navigate
import br.com.zup.beagle.android.action.ActionExecutor
import br.com.zup.beagle.android.engine.renderer.RootView
import br.com.zup.beagle.android.engine.renderer.ViewRendererFactory
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.widget.core.Action
import br.com.zup.beagle.widget.form.FormInput
import br.com.zup.beagle.android.widget.form.InputWidget
import br.com.zup.beagle.android.widget.form.InputWidgetWatcherActionType
import br.com.zup.beagle.android.widget.interfaces.Observer
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class FormInputViewRendererTest : BaseTest() {

    @MockK
    private lateinit var formInput: FormInput

    @MockK
    private lateinit var viewRendererFactory: ViewRendererFactory

    @MockK
    private lateinit var viewFactory: ViewFactory

    @InjectMockKs
    private lateinit var formInputViewRenderer: FormInputViewRenderer

    @MockK
    private lateinit var actionExecutor: ActionExecutor

    @MockK
    private lateinit var rootView: RootView

    @MockK
    private lateinit var view: View

    @RelaxedMockK
    private lateinit var inputWidget: InputWidget

    @MockK
    private lateinit var navigateAction: Navigate

    @MockK
    private lateinit var context: Context

    override fun setUp() {
        super.setUp()

        every { viewRendererFactory.make(any()).build(any()) } returns view
        every { rootView.getContext() } returns context
        every { view.context } returns context
        every { view.tag = any() } just Runs
        every { formInput.child } returns inputWidget
        every { actionExecutor.doAction(any(), any<List<Action>>()) } just Runs
    }

    @Test
    fun build_should_make_child() {
        // WHEN
        val actual = formInputViewRenderer.buildView(rootView)

        // THEN
        assertEquals(view, actual)
    }

    @Test
    fun build_should_set_widget_on_tag() {
        // WHEN
        formInputViewRenderer.buildView(rootView)

        // THEN
        verify(exactly = once()) { view.tag = formInput }
    }

    @Test
    fun action_observer_should_call_when_emit_event() {
        // GIVEN
        val observerSlot = slot<Observer<Pair<InputWidgetWatcherActionType, Any>>>()
        every { inputWidget.getAction().addObserver(capture(observerSlot)) } just Runs
        every { inputWidget.onFocus } returns listOf(navigateAction)

        // WHEN
        formInputViewRenderer.buildView(rootView)
        observerSlot.captured.update(mockk(), Pair(InputWidgetWatcherActionType.ON_FOCUS, ""))

        // THEN
        verify(exactly = once()) { view.tag = formInput }
        verify(exactly = once()) { actionExecutor.doAction(rootView, listOf(navigateAction)) }
    }
}