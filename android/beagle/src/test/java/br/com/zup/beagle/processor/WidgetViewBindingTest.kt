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
import br.com.zup.beagle.widget.core.WidgetView
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

class FieldOnlyWidgetBindingTest {
    @InjectMockKs
    lateinit var widgetBinding: FieldOnlyWidgetBinding

    @RelaxedMockK
    lateinit var view: View

    @RelaxedMockK
    lateinit var fieldOnlyWidget: FieldOnlyWidget

    val expressionBoolean: Bind<Boolean> = mockk<Bind.Expression<Boolean>>(relaxed = true)
    val expressionLong: Bind<Long> = mockk<Bind.Expression<Long>>(relaxed = true)
    val expressionString: Bind<String> = mockk<Bind.Expression<String>>(relaxed = true)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        setInstanceField(widgetBinding, WIDGET_INSTANCE_PROPERTY, fieldOnlyWidget)
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
    fun fieldOnlyWidgetBindingAdapter_should_be_instance_of_BindingAdapter() {
        assertTrue(widgetBinding is BindingAdapter)
        assertTrue(widgetBinding is WidgetView)
    }

    @Test
    fun fieldOnlyWidgetBindingAdapter_should_have_3_elements_in_list() {
        val expectedSize = 3
        assertEquals(expectedSize, widgetBinding.getBindAttributes().size)
    }

    @Test
    fun fieldOnlyWidgetBindingAdapter_should_call_on_bind_at_least_once() {

        //when
        widgetBinding.bindModel()

        //then
        verify(atLeast = once()) { fieldOnlyWidget.onBind(any(), any()) }
    }

    @Test
    fun fieldOnlyWidgetBindingAdapter_should_call_observe_on_parameters() {

        //when
        widgetBinding.bindModel()

        //then
        verify(atLeast = once()) { widgetBinding.a.observes(any()) }
        verify(atLeast = once()) { widgetBinding.b.observes(any()) }
        verify(atLeast = once()) { widgetBinding.c.observes(any()) }
    }

    @Test
    fun fieldOnlyWidgetBindingAdapter_should_call_notify_widget_default_values() {

        //given
        val aPropertyValue = true
        val bPropertyValue = 25L
        val cPropertyValue = "DUMMY"

        every { fieldOnlyWidget.a } returns aPropertyValue
        every { fieldOnlyWidget.b } returns bPropertyValue
        every { fieldOnlyWidget.c } returns cPropertyValue

        val expected = FieldOnlyWidget(a = aPropertyValue, b = bPropertyValue, c = cPropertyValue)

        //when
        widgetBinding.bindModel()

        //then
        verify(exactly = once()) { fieldOnlyWidget.onBind(expected, view) }
    }
}