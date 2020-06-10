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

import br.com.zup.beagle.android.Action
import br.com.zup.beagle.core.Bind
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.extensions.once
import br.com.zup.beagle.setup.BindingAdapter
import io.mockk.MockKAnnotations
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ActionBindingTest {
    lateinit var widgetBinding: MyActionBinding

    var stringValue: String = "DUMMY"

    var intValue: Int = 10

    val bindText: Bind<String> = Bind.Value(stringValue)
    val bindInt: Bind<Int> = Bind.Value(intValue)
    val rootView: RootView = mockk(relaxed = true)


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        widgetBinding = MyActionBinding(value = bindText, intValue = bindInt)

    }

    fun after() {
        unmockkAll()
    }

    @Test
    fun widget_should_be_instance_of_BindingAdapter() {
        assertTrue(widgetBinding is BindingAdapter)
        assertTrue(widgetBinding is Action)
    }

    @Test
    fun widget_should_have_2_elements_in_list() {
        val expectedSize = 2
        assertEquals(expectedSize, widgetBinding.getBindAttributes().size)
    }

    @Test
    fun widget_should_call_on_bind_at_least_once() {
        mockkConstructor(MyAction::class)
        //when
        widgetBinding.handle(rootView)

        //then
        verify(exactly = once()) { anyConstructed<MyAction>().handle(any()) }

    }

}