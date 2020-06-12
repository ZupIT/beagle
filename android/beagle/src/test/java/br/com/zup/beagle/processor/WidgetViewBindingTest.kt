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

import android.content.Context
import android.view.View
import br.com.zup.beagle.core.Bind
import br.com.zup.beagle.extensions.once
import br.com.zup.beagle.setup.BindingAdapter
import br.com.zup.beagle.testutil.setPrivateField
import br.com.zup.beagle.widget.core.WidgetView
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

class FieldOnlyWidgetBindingTest {
    @InjectMockKs
    lateinit var widgetBinding: FieldOnlyWidgetBinding

    @RelaxedMockK
    lateinit var view: View

    @RelaxedMockK
    lateinit var context: Context

    @RelaxedMockK
    lateinit var fieldOnlyWidget: FieldOnlyWidget

    val expressionBoolean: Bind<Boolean> = mockk<Bind.Expression<Boolean>>(relaxed = true)
    val expressionLong: Bind<Long> = mockk<Bind.Expression<Long>>(relaxed = true)
    val expressionString: Bind<String> = mockk<Bind.Expression<String>>(relaxed = true)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        widgetBinding.setPrivateField(WIDGET_INSTANCE_PROPERTY, fieldOnlyWidget)
        widgetBinding.setPrivateField(VIEW_PROPERTY, view)
    }

    fun after() {
        unmockkAll()
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
        // Given When
        widgetBinding.buildView(context)

        //then
        verify(atLeast = once()) { fieldOnlyWidget.onBind(any(), any()) }
    }

    @Test
    fun fieldOnlyWidgetBindingAdapter_should_call_observe_on_parameters() {
        // Given When
        widgetBinding.buildView(context)

        //then
        verify(atLeast = once()) { widgetBinding.a.observes(any()) }
        verify(atLeast = once()) { widgetBinding.b.observes(any()) }
        verify(atLeast = once()) { widgetBinding.c.observes(any()) }
    }
}