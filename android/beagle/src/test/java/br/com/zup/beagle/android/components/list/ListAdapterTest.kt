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

package br.com.zup.beagle.android.components.list

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.context.AsyncActionData
import br.com.zup.beagle.android.testutil.InstantExecutorExtension
import br.com.zup.beagle.android.utils.setIsAutoGenerateIdEnabled
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import br.com.zup.beagle.android.view.viewmodel.AsyncActionViewModel
import br.com.zup.beagle.android.view.viewmodel.GenerateIdViewModel
import br.com.zup.beagle.android.view.viewmodel.ListViewIdViewModel
import br.com.zup.beagle.android.view.viewmodel.ScreenContextViewModel
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
class ListAdapterTest : BaseTest() {

    private val orientation = RecyclerView.VERTICAL
    private val template = Container(children = listOf())
    private val iteratorName = "iteratorName"
    private val key = "id"
    private val generatedId = 10
    private val list = listOf("stub 1", "stub 2")
    private val viewFactory = mockk<ViewFactory>(relaxed = true)
    private val beagleFlexView = mockk<BeagleFlexView>(relaxed = true)
    private val listViewModels = mockk<ListViewModels>()
    private val asyncActionViewModel = mockk<AsyncActionViewModel>()
    private val contextViewModel = mockk<ScreenContextViewModel>()
    private val listViewIdViewModel = mockk<ListViewIdViewModel>()
    private val generateIdViewModel = mockk<GenerateIdViewModel>()
    private val observerSlot = slot<Observer<AsyncActionData>>()
    private val autoGenerateIdEnabledSlot = slot<Boolean>()

    private lateinit var listAdapter: ListAdapter

    @BeforeEach
    override fun setUp() {
        super.setUp()
        every { listViewModels.rootView } returns rootView
        every { listViewModels.asyncActionViewModel } returns asyncActionViewModel
        every { listViewModels.contextViewModel } returns contextViewModel
        every { listViewModels.listViewIdViewModel } returns listViewIdViewModel
        every { listViewModels.generateIdViewModel } returns generateIdViewModel
        every { asyncActionViewModel.asyncActionExecuted.observe(rootView.getLifecycleOwner(), capture(observerSlot)) } just Runs
        every { listViewIdViewModel.createSingleManagerByListViewId(any(), any()) } just Runs
        every { generateIdViewModel.getViewId(rootView.getParentId()) } returns generatedId
        every { viewFactory.makeBeagleFlexView(rootView) } returns beagleFlexView
        every { beagleFlexView.setIsAutoGenerateIdEnabled(capture(autoGenerateIdEnabledSlot)) } just Runs

        listAdapter = spyk(
            ListAdapter(
                orientation,
                template,
                iteratorName,
                key,
                viewFactory,
                listViewModels
            )
        )
        every { listAdapter.notifyDataSetChanged() } just Runs
    }

    @Test
    fun `GIVEN a listAdapter WHEN getItemViewType THEN should return 0`() {
        // Given
        val position = 1

        // When
        val actual = listAdapter.getItemViewType(position)

        // Then
        assertEquals(0, actual)
    }

    @Test
    fun `GIVEN a listAdapter WHEN init THEN should observe asyncActions`() {
        // Then
        verify { asyncActionViewModel.asyncActionExecuted.observe(rootView.getLifecycleOwner(), observerSlot.captured) }
    }

    @Test
    fun `GIVEN a listAdapter WHEN onCreateViewHolder THEN should create a ListViewHolder`() {
        // Given
        val parent = mockk<ViewGroup>()
        val viewType = 0

        // When
        val viewHolder = listAdapter.onCreateViewHolder(parent, viewType)

        // Then
        assertFalse(autoGenerateIdEnabledSlot.captured)
        assertTrue { viewHolder.itemView is BeagleFlexView }
        verify { beagleFlexView.setHeightAutoAndDirtyAllViews() }
    }

    @Test
    fun `GIVEN a listAdapter with horizontal recycler WHEN onCreateViewHolder THEN should setWidthAutoAndDirtyAllViews`() {
        // Given
        val parent = mockk<ViewGroup>()
        val viewType = 0

        listAdapter = spyk(
            ListAdapter(
                RecyclerView.HORIZONTAL,
                template,
                iteratorName,
                key,
                viewFactory,
                listViewModels
            )
        )

        // When
        val viewHolder = listAdapter.onCreateViewHolder(parent, viewType)
        val itemView = viewHolder.itemView

        // Then
        verify { (itemView as BeagleFlexView).setWidthAutoAndDirtyAllViews() }
        verify(exactly = 1) { beagleFlexView.addServerDrivenComponent(any(), false) }
    }

    @Test
    fun `GIVEN a loaded listAdapter WHEN onBindViewHolder THEN should call onBind from viewHolder`() {
        // Given
        val viewHolder = mockk<ListViewHolder>(relaxed = true)
        val parentListViewSuffix = "parent"
        val position = 0
        val recyclerId = 10
        val listItemSlot = slot<ListItem>()
        listAdapter.setParentSuffix(parentListViewSuffix)
        listAdapter.setList(list, recyclerId)

        // When
        listAdapter.onBindViewHolder(viewHolder, position)

        // Then
        verify(exactly = 1) { viewHolder.onBind(parentListViewSuffix, key, capture(listItemSlot), position, recyclerId) }
        assertEquals(list[0], listItemSlot.captured.data)
        assertFalse(listItemSlot.captured.isRecycled)
    }

    @Test
    fun `GIVEN a loaded listAdapter WHEN getItemCount THEN should use list and recyclerId`() {
        // Given
        val recyclerId = 10

        // When
        listAdapter.setList(list, recyclerId)

        // Then
        assertEquals(list.size, listAdapter.itemCount)
        verify(exactly = 1) { listViewIdViewModel.createSingleManagerByListViewId(recyclerId, true) }
    }

    @Test
    fun `GIVEN empty list WHEN setList THEN should itemCount be 0`() {
        // Given
        val recyclerId = 10
        listAdapter.setList(list, recyclerId)

        // When
        listAdapter.setList(emptyList(), recyclerId)

        // Then
        assertEquals(0, listAdapter.itemCount)
    }

    @Test
    fun `GIVEN recyclerId is -1 WHEN setList THEN should getViewId from generateIdViewModel`() {
        // Given
        val recyclerId = View.NO_ID

        // When
        listAdapter.setList(list, recyclerId)

        // Then
        verify(exactly = 1) { generateIdViewModel.getViewId(rootView.getParentId()) }
        verify(exactly = 1) { listViewIdViewModel.createSingleManagerByListViewId(generatedId, true) }
    }
}
