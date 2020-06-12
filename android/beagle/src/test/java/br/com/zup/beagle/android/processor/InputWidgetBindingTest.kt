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

package br.com.zup.beagle.android.processor

import android.content.Context
import android.view.View
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.setup.BindingAdapter
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.testutil.setPrivateField
import br.com.zup.beagle.android.widget.core.Bind
import br.com.zup.beagle.processor.CustomInputWidget
import br.com.zup.beagle.processor.CustomInputWidgetBinding
import br.com.zup.beagle.processor.VIEW_PROPERTY
import br.com.zup.beagle.processor.WIDGET_INSTANCE_PROPERTY
import br.com.zup.beagle.widget.form.InputWidget
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InputWidgetBindingTest {
    @InjectMockKs
    lateinit var widgetBinding: CustomInputWidgetBinding

    val text: Bind<String> = mockk<Bind.Expression<String>>(relaxed = true)

    @RelaxedMockK
    lateinit var view: View

    @RelaxedMockK
    lateinit var context: Context

    @RelaxedMockK
    lateinit var widget: CustomInputWidget

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        widgetBinding.setPrivateField(WIDGET_INSTANCE_PROPERTY, widget)
        widgetBinding.setPrivateField(VIEW_PROPERTY, view)
    }

    fun after() {
        unmockkAll()
    }

    @Test
    fun widget_should_be_instance_of_BindingAdapter() {
        assertTrue(widgetBinding is BindingAdapter)
        assertTrue(widgetBinding is InputWidget)
    }

    @Test
    fun widget_should_have_1_elements_in_list() {
        val expectedSize = 1
        assertEquals(expectedSize, widgetBinding.getBindAttributes().size)
    }

    @Test
    fun widget_should_call_on_bind_at_least_once() {

        //when
        widgetBinding.buildView(context)

        //then
        verify(atLeast = once()) { widget.onBind(any(), any()) }
    }

    @Test
    fun widget_should_call_observe_on_parameters() {

        //when
        widgetBinding.buildView(context)

        //then
        verify(atLeast = once()) { widgetBinding.text.observes(any()) }
    }

    @Test
    fun widget_should_call_on_get_value() {
        // Given When
        widgetBinding.getValue()

        //then
        verify(exactly = once()) { widget.getValue() }
    }

    @Test
    fun widget_should_call_on_error_message() {
        // Given
        val message = RandomData.string()

        //when
        widgetBinding.onErrorMessage(message)

        //then
        verify(exactly = once()) { widget.onErrorMessage(message) }
    }
}