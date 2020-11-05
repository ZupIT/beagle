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
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.setup.BeagleSdk
import br.com.zup.beagle.android.utils.toView
import br.com.zup.beagle.android.utils.viewFactory
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.core.ServerDrivenComponent
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AddChildrenTest {

    private val serverDrivenComponent = mockk<ServerDrivenComponent>(relaxed = true)
    private val value = listOf(serverDrivenComponent)
    private val rootView = mockk<RootView>(relaxed = true)
    private val origin = mockk<View>(relaxed = true)
    private val viewGroup = mockk<ViewGroup>(relaxed = true)
    private val context = mockk<AppCompatActivity>(relaxed = true)
    private val view = mockk<BeagleFlexView>(relaxed = true)
    private val beagleSdk = mockk<BeagleSdk>(relaxed = true)
    private val id = "id"
    private val viewFactoryMock = mockk<ViewFactory>(relaxed = true)

    @BeforeEach
    fun setUp() {
        mockkObject(BeagleEnvironment)
        viewFactory = viewFactoryMock
        every { BeagleEnvironment.beagleSdk } returns beagleSdk
        every { beagleSdk.logger } returns mockk(relaxed = true)
        every { rootView.getContext() } returns context
        every { context.findViewById<ViewGroup>(any()) } returns viewGroup
        every { viewFactory.makeBeagleFlexView(rootView) } returns view
        every { viewGroup.addView(any()) } just Runs
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(BeagleEnvironment)
    }

    @Test
    fun addChildren_with_no_mode_should_append() {
        //GIVEN
        val action = AddChildren(id, value)

        //WHEN
        action.execute(rootView, origin)

        //THEN
        verify(exactly = 1) { viewGroup.addView(view) }
    }

    @Test
    fun addChildren_with_append_mode_should_call_view_group_add_view() {
        //GIVEN
        val action = AddChildren(id, value, Mode.APPEND)

        //WHEN
        action.execute(rootView, origin)

        //THEN
        verify(exactly = 1) { viewGroup.addView(view) }
    }

    @Test
    fun addChildren_with_replace_mode_should_call_view_group_remove_all_views_than_add_view() {
        //GIVEN
        val action = AddChildren(id, value, Mode.REPLACE)

        //WHEN
        action.execute(rootView, origin)

        //THEN
        verify(exactly = 1) { viewGroup.removeAllViews() }
        verify(exactly = 1) { viewGroup.addView(view) }
    }

    @Test
    fun addChildren_with_prepend_mode_should_call_view_group_add_view_index_zero() {
        //GIVEN
        val action = AddChildren(id, value, Mode.PREPEND)

        //WHEN
        action.execute(rootView, origin)

        //THEN
        verify(exactly = 1) { viewGroup.addView(view, 0) }
    }
}
