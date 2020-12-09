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

import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.context.AsyncActionData
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
import io.mockk.verify
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ListAdapterTest : BaseTest() {

    private val orientation = RecyclerView.VERTICAL
    private val template = Container(children = listOf())
    private val iteratorName = "iteratorName"
    private val key = "id"
    private val generatedId = 10
    private val list = listOf("stub 1", "stub 2")
    private val viewFactory = mockk<ViewFactory>(relaxed = true)
    private val viewHolderItemView = mockk<BeagleFlexView>(relaxed = true)
    private val listViewModels = mockk<ListViewModels>()
    private val asyncActionViewModel = mockk<AsyncActionViewModel>(relaxed = true)
    private val contextViewModel = mockk<ScreenContextViewModel>()
    private val listViewIdViewModel = mockk<ListViewIdViewModel>()
    private val generateIdViewModel = mockk<GenerateIdViewModel>()
    private val observerSlot = slot<Observer<AsyncActionData>>()
    private val viewGroupMock = mockk<ViewGroup>()
    private val viewTypeMock = 0
    private val liveDataMock = mockk<LiveData<AsyncActionData>>(relaxed = true)

    private lateinit var listAdapter: ListAdapter

    @Before
    override fun setUp() {
        super.setUp()
        every { listViewModels.rootView } returns rootView
        every { listViewModels.asyncActionViewModel } returns asyncActionViewModel
        every { listViewModels.contextViewModel } returns contextViewModel
        every { listViewModels.listViewIdViewModel } returns listViewIdViewModel
        every { listViewModels.generateIdViewModel } returns generateIdViewModel
        every { liveDataMock.observe(rootView.getLifecycleOwner(), capture(observerSlot)) } just Runs
        every { asyncActionViewModel.asyncActionExecuted } returns liveDataMock
        //every { asyncActionViewModel.asyncActionExecuted.observe(rootView.getLifecycleOwner(), capture(observerSlot)) } just Runs
        every { listViewIdViewModel.createSingleManagerByListViewId(any(), any()) } just Runs
        every { generateIdViewModel.getViewId(rootView.getParentId()) } returns generatedId
        every { viewFactory.makeBeagleFlexView(rootView) } returns viewHolderItemView

        listAdapter =
            ListAdapter(
                orientation,
                template,
                iteratorName,
                key,
                viewFactory,
                listViewModels
            )

//        every { listAdapter.notifyDataSetChanged() } just Runs
    }


    @Test
    fun `Given a ListAdapter When ListAdapter is initialized Then it should register to observe all async actions executed`() {
        verify { asyncActionViewModel.asyncActionExecuted.observe(rootView.getLifecycleOwner(), any()) }
    }

    @Test
    fun `Given a ListAdapter When call onCreateViewHolder Then should create an itemView with autoGenerateIdEnabled false`() {
        val expectedAutoGenerateIdEnabled = false
        val autoGenerateIdEnabledSlot = slot<Boolean>()
        every { viewHolderItemView.setIsAutoGenerateIdEnabled(capture(autoGenerateIdEnabledSlot)) } just Runs

        listAdapter.onCreateViewHolder(viewGroupMock, viewTypeMock)

        assertEquals(expectedAutoGenerateIdEnabled, autoGenerateIdEnabledSlot.captured)
    }

    @Test
    fun `Given a ListAdapter with vertical orientation When call onCreateViewHolder Then should call setHeightAutoAndDirtyAllViews for the created itemView`() {
        listAdapter.onCreateViewHolder(viewGroupMock, viewTypeMock)

        verify { viewHolderItemView.setHeightAutoAndDirtyAllViews() }
    }

    @Test
    fun `Given a ListAdapter with vertical orientation When call onCreateViewHolder Then should set the correct layout params to view holder itemView`(){

        val expectedLayoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val layoutParamsSlot = slot<ViewGroup.LayoutParams>()
        every { viewHolderItemView.layoutParams = capture(layoutParamsSlot) } just Runs

        listAdapter.onCreateViewHolder(viewGroupMock, viewTypeMock)

        assertEquals(expectedLayoutParams.height, layoutParamsSlot.captured.height)
        assertEquals(expectedLayoutParams.width, layoutParamsSlot.captured.width)
    }

    @Test
    fun `Given a ListAdapter with horizontal orientation When call onCreateViewHolder Then should call setWidthAutoAndDirtyAllViews for the created itemView`() {

        val subject = ListAdapter(
            RecyclerView.HORIZONTAL,
            template,
            iteratorName,
            key,
            viewFactory,
            listViewModels
        )

        subject.onCreateViewHolder(viewGroupMock, viewTypeMock)

        verify { viewHolderItemView.setWidthAutoAndDirtyAllViews() }
    }

    @Test
    fun `Given a ListAdapter with horizontal orientationWhen call onCreateViewHolder Then should set the correct layout params to view holder itemView`(){
        val subject = ListAdapter(
            RecyclerView.HORIZONTAL,
            template,
            iteratorName,
            key,
            viewFactory,
            listViewModels
        )
        val expectedLayoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val layoutParamsSlot = slot<ViewGroup.LayoutParams>()
        every { viewHolderItemView.layoutParams = capture(layoutParamsSlot) } just Runs

        subject.onCreateViewHolder(viewGroupMock, viewTypeMock)

        assertEquals(expectedLayoutParams.height, layoutParamsSlot.captured.height)
        assertEquals(expectedLayoutParams.width, layoutParamsSlot.captured.width)
    }

    @Test
    fun `Given a ListAdapter When call onBindViewHolder Then should call viewHolder onBind`() {

        val viewHolderMock = mockk<ListViewHolder>(relaxed = true)
        val positionMock = 0
        val parentListViewSuffixMock = "parent"
        val recyclerIdMock = 10
        val listItemMock = ListItem(data = list[positionMock])
        listAdapter.setParentSuffix(parentListViewSuffixMock)
        listAdapter.setList(list, recyclerIdMock)

        listAdapter.onBindViewHolder(viewHolderMock, positionMock)

        verify(exactly = 1) { viewHolderMock.onBind(parentListViewSuffixMock, key, listItemMock, positionMock, recyclerIdMock) }
    }

    @Test
    fun `Given a ListAdapter When call onViewRecycled Then should call viewHolder onViewRecycled`() {
        val viewHolderMock = mockk<ListViewHolder>(relaxed = true)

        listAdapter.onViewRecycled(viewHolderMock)

        verify(exactly = 1) { viewHolderMock.onViewRecycled() }
    }

    @Test
    fun `Given a ListAdapter When call onViewAttachedToWindow Then should call viewHolder onViewAttachedToWindow`() {
        val viewHolderMock = mockk<ListViewHolder>(relaxed = true)

        listAdapter.onViewAttachedToWindow(viewHolderMock)

        verify(exactly = 1) { viewHolderMock.onViewAttachedToWindow() }
    }
/*
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
    */

}
