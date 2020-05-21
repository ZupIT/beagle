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
import br.com.zup.beagle.core.Binding
import br.com.zup.beagle.setup.BindingAdapter
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FieldOnlyWidgetBindingTest {
    @InjectMockKs
    lateinit var fieldOnlyWidgetBinding: FieldOnlyWidgetBinding

    @RelaxedMockK
    lateinit var widget: FieldOnlyWidget

    @RelaxedMockK
    lateinit var binding: FieldOnlyWidgetBinding

    @RelaxedMockK
    lateinit var view: View

    val expressionBoolean: Binding<Boolean> = mockk<Binding.Expression<Boolean>>(relaxed = true)
    val expressionLong: Binding<Long> = mockk<Binding.Expression<Long>>(relaxed = true)
    val expressionString: Binding<String> = mockk<Binding.Expression<String>>(relaxed = true)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { binding.a } returns expressionBoolean
        every { binding.b } returns expressionLong
        every { binding.c } returns expressionString
    }

    @Test
    fun fieldOnlyWidgetBindingAdapter_should_be_instance_of_BindingAdapter() {
        assertTrue(fieldOnlyWidgetBinding is BindingAdapter)
    }

    @Test
    fun fieldOnlyWidgetBindingAdapter_should_have_3_elements_in_list() {
        val expectedSize = 3
        assertEquals(expectedSize, fieldOnlyWidgetBinding.getBindAttributes().size)
    }

//    @Test
//    fun fieldOnlyWidgetBindingAdapter_should_call_on_bind_at_least_once() {
//
//        //when
//        fieldOnlyWidgetBinding.bindModel()
//
//        //then
//        verify(atLeast = once()) { widget.onBind(any(), any()) }
//    }
//
//    @Test
//    fun fieldOnlyWidgetBindingAdapter_should_call_observe_on_parameters() {
//
//        //when
//        fieldOnlyWidgetBinding.bindModel()
//
//        //then
//        verify(exactly = once()) { expressionBoolean.observes(any()) }
//        verify(exactly = once()) { expressionLong.observes(any()) }
//        verify(exactly = once()) { expressionString.observes(any()) }
//    }
//
//    @Test
//    fun fieldOnlyWidgetBindingAdapter_should_call_notify_widget_default_values() {
//
//        //given
//        val aPropertyValue = true
//        val bPropertyValue = 25L
//        val cPropertyValue = "DUMMY"
//
//        every { widget.a } returns aPropertyValue
//        every { widget.b } returns bPropertyValue
//        every { widget.c } returns cPropertyValue
//
//        val expected = FieldOnlyWidget(a = aPropertyValue, b = bPropertyValue, c = cPropertyValue)
//
//        //when
//        fieldOnlyWidgetBinding.bindModel()
//
//        //then
//        verify(exactly = once()) { widget.onBind(expected, view) }
//    }
}