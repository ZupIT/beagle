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

package br.com.zup.beagle.processor

import android.view.View
import br.com.zup.beagle.core.Bind
import br.com.zup.beagle.extensions.once
import br.com.zup.beagle.setup.BindingAdapter
import br.com.zup.beagle.widget.form.InputWidget
import io.mockk.MockKAnnotations
import io.mockk.every
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
    lateinit var widget: CustomInputWidget

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        setInstanceField(widgetBinding, WIDGET_INSTANCE_PROPERTY, widget)
        setInstanceField(widgetBinding, VIEW_PROPERTY, view)
    }

    fun after() {
        unmockkAll()
    }

    private fun setInstanceField(instanceObject: Any, propertyName: String, destinationObject: Any) {
        val widgetInstanceField = instanceObject::class.java.getDeclaredField(propertyName)
        widgetInstanceField.isAccessible = true
        widgetInstanceField.set(instanceObject, destinationObject)
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
        widgetBinding.bindModel()

        //then
        verify(atLeast = once()) { widget.onBind(any(), any()) }
    }

    @Test
    fun widget_should_call_observe_on_parameters() {

        //when
        widgetBinding.bindModel()

        //then
        verify(atLeast = once()) { widgetBinding.text.observes(any()) }
    }

    @Test
    fun widget_should_call_notify_widget_default_values() {

        //given
        val propertyValue = "DUMMY"

        every { widget.text } returns propertyValue

        val expected = CustomInputWidget(text = propertyValue)

        //when
        widgetBinding.bindModel()

        //then
        verify(exactly = once()) { widget.onBind(expected, view) }
    }

    @Test
    fun widget_should_call_on_get_value() {

        //when
        widgetBinding.getValue()

        //then
        verify(exactly = once()) { widget.getValue() }
    }

    @Test
    fun widget_should_call_on_error_message() {

        val message = "DUMMY"

        //when
        widgetBinding.onErrorMessage(message)

        //then
        verify(exactly = once()) { widget.onErrorMessage(message) }
    }
}