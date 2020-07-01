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

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.android.engine.renderer.ViewRenderer
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import br.com.zup.beagle.android.widget.RootView
import io.mockk.Called
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

private val ROWS = listOf<ServerDrivenComponent>(mockk(), mockk())

class ListViewRecyclerAdapterTest {

    @MockK
    private lateinit var viewFactory: ViewFactory
    @MockK
    private lateinit var viewRendererMock: ViewRenderer<*>
    @MockK
    private lateinit var rootView: RootView
    @MockK
    private lateinit var context: Context
    @RelaxedMockK
    private lateinit var view: BeagleFlexView

    private lateinit var listViewRecyclerAdapter: ListViewRecyclerAdapter

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        listViewRecyclerAdapter = ListViewRecyclerAdapter(ROWS, viewFactory, RecyclerView.VERTICAL, rootView)

        every { viewFactory.makeBeagleFlexView(any()) } returns view
        every { rootView.getContext() } returns context
    }

    @Test
    fun getItemViewType_should_return_position() {
        // Given
        val position = 0

        // When
        val actual = listViewRecyclerAdapter.getItemViewType(position)

        // Then
        assertEquals(position, actual)
    }

    @Test
    fun onCreateViewHolder_should_return_ViewHolder_with_widget_view() {
        // Given
        val parent = mockk<ViewGroup>()
        val position = 0
        every { parent.context } returns context

        // When
        val actual = listViewRecyclerAdapter.onCreateViewHolder(parent, position)

        // Then
        assertEquals(view, actual.itemView)
    }

    @Test
    fun onBindViewHolder_should_do_nothing() {
        // Given
        val viewHolder = mockk<ViewHolder>()
        val position = 0

        // When
        listViewRecyclerAdapter.onBindViewHolder(viewHolder, position)

        // Then
        verify { viewHolder wasNot Called }
    }

    @Test
    fun getItemCount_should_return_rows_size() {
        val actual = listViewRecyclerAdapter.itemCount

        assertEquals(ROWS.size, actual)
    }
}