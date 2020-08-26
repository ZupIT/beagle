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

package br.com.zup.beagle.android.action

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.utils.toView
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.core.ServerDrivenComponent
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test

class AddChildrenActionTest {

    private lateinit var value: List<ServerDrivenComponent>

    @MockK(relaxed = true, relaxUnitFun = true)
    private lateinit var serverDrivenComponent: ServerDrivenComponent

    @MockK(relaxed = true, relaxUnitFun = true)
    private lateinit var rootView: RootView

    @MockK(relaxed = true, relaxUnitFun = true)
    private lateinit var origin: View

    @MockK(relaxed = true, relaxUnitFun = true)
    private lateinit var viewGroup: ViewGroup

    @MockK(relaxed = true, relaxUnitFun = true)
    private lateinit var context: AppCompatActivity

    @MockK(relaxed = true, relaxUnitFun = true)
    private lateinit var view: View

    private val id = "id"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        every { rootView.getContext() } returns context
        every { context.findViewById<ViewGroup>(any()) } returns viewGroup
        mockkStatic("br.com.zup.beagle.android.utils.WidgetExtensionsKt")
        every { serverDrivenComponent.toView(rootView) } returns view
        every { viewGroup.addView(any()) } just runs
        value = listOf(serverDrivenComponent)
    }

    @Test
    fun addChildren_with_no_mode_should_append() {
        //GIVEN
        val action = AddChildrenAction(
            id,
            value
        )

        //WHEN
        action.execute(rootView, origin)

        //THEN
        verify(exactly = once()) { viewGroup.addView(view) }
    }

    @Test
    fun addChildren_with_append_mode_should_call_view_group_add_view() {
        //GIVEN
        val action = AddChildrenAction(
            id,
            value,
            Mode.APPEND
        )

        //WHEN
        action.execute(rootView, origin)

        //THEN
        verify(exactly = once()) { viewGroup.addView(view) }
    }

    @Test
    fun addChildren_with_replace_mode_should_call_view_group_remove_all_views_than_add_view() {
        //GIVEN
        val action = AddChildrenAction(
            id,
            value,
            Mode.REPLACE
        )

        //WHEN
        action.execute(rootView, origin)

        //THEN
        verify(exactly = once()) { viewGroup.removeAllViews() }
        verify(exactly = once()) { viewGroup.addView(view) }
    }

    @Test
    fun addChildren_with_prepend_mode_should_call_view_group_add_view_index_zero() {
        //GIVEN
        val action = AddChildrenAction(
            id,
            value,
            Mode.PREPEND
        )

        //WHEN
        action.execute(rootView, origin)

        //THEN
        verify(exactly = once()) { viewGroup.addView(view, 0) }
    }
}