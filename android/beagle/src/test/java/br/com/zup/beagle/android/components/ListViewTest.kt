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

package br.com.zup.beagle.android.components

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.widget.core.ListDirection
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ListViewTest : BaseComponentTest() {

    private val recyclerView: RecyclerView = mockk(relaxed = true)

    private val layoutManagerSlot = slot<LinearLayoutManager>()

    private lateinit var listView: ListView

    override fun setUp() {
        super.setUp()

        every { beagleFlexView.addView(any()) } just Runs
        every { anyConstructed<ViewFactory>().makeRecyclerView(rootView.getContext()) } returns recyclerView
        every { recyclerView.layoutManager = capture(layoutManagerSlot) } just Runs
        every { recyclerView.adapter = any() } just Runs

        listView = ListView(listOf(), ListDirection.VERTICAL)
    }

    @Test
    fun build_should_return_a_BeagleFlexView_instance() {
        val view = listView.buildView(rootView)

        assertTrue(view is RecyclerView)
    }

    @Test
    fun build_should_set_orientation_VERTICAL() {
        // Given
        listView = listView.copy(direction = ListDirection.VERTICAL)

        // When
        listView.buildView(rootView)

        // Then
        assertEquals(RecyclerView.VERTICAL, layoutManagerSlot.captured.orientation)
    }

    @Test
    fun build_should_set_orientation_HORIZONTAL() {
        // Given
        listView = listView.copy(direction = ListDirection.HORIZONTAL)

        // When
        listView.buildView(rootView)

        // Then
        assertEquals(RecyclerView.HORIZONTAL, layoutManagerSlot.captured.orientation)
    }
}