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
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.action.AsyncActionImpl
import br.com.zup.beagle.android.action.AsyncActionStatus
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
import io.mockk.mockkConstructor
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ListAdapterTest : BaseTest() {

    @get:Rule
    var executorTestRule = InstantTaskExecutorRule()

    private val viewHolderItemView = mockk<BeagleFlexView>(relaxed = true)
    private val viewGroupMock = mockk<ViewGroup>(relaxed = true)
    private val viewTypeMock = 0
    private val recyclerViewMock = mockk<RecyclerView>(relaxed = true)
    private val asyncActionMock = mockk<AsyncActionImpl>()
    private val asyncActionDataMock = AsyncActionData(
        viewHolderItemView, asyncActionMock
    )
    private val asyncActionDataSlot = slot<AsyncActionData>()
    private val asyncActionStatusObserverSlot = slot<Observer<AsyncActionStatus>>()
    private val asyncActionStatusSlot = slot<AsyncActionStatus>()
    private val orientation = RecyclerView.VERTICAL
    private val template = Container(children = listOf())
    private val iteratorName = "iteratorName"
    private val key = "id"
    private val generatedId = 10
    private val list = listOf("stub 1", "stub 2")
    private val viewFactory = mockk<ViewFactory>(relaxed = true)
    private val listViewModels = mockk<ListViewModels>()
    private val asyncActionViewModel = mockk<AsyncActionViewModel>()
    private val contextViewModel = mockk<ScreenContextViewModel>()
    private val listViewIdViewModel = mockk<ListViewIdViewModel>()
    private val generateIdViewModel = mockk<GenerateIdViewModel>()
    private val observerSlot = slot<Observer<AsyncActionData>>()

    private lateinit var listAdapter: ListAdapter

    @Before
    override fun setUp() {
        super.setUp()

        mockkConstructor(ListViewHolder::class)
        every { viewHolderItemView.parent } returns recyclerViewMock
        every { viewFactory.makeBeagleFlexView(rootView) } returns viewHolderItemView
        every { asyncActionViewModel.onAsyncActionExecuted(capture(asyncActionDataSlot)) } answers {
            observerSlot.captured.onChanged(asyncActionDataSlot.captured)
        }
        every { asyncActionMock.status.observe(any(), capture(asyncActionStatusObserverSlot)) } just Runs
        every { asyncActionMock.status.value = capture(asyncActionStatusSlot) } answers {
            asyncActionStatusObserverSlot.captured.onChanged(asyncActionStatusSlot.captured)
        }
        every { listViewModels.rootView } returns rootView
        every { listViewModels.asyncActionViewModel } returns asyncActionViewModel
        every { listViewModels.contextViewModel } returns contextViewModel
        every { listViewModels.listViewIdViewModel } returns listViewIdViewModel
        every { listViewModels.generateIdViewModel } returns generateIdViewModel
        every { asyncActionViewModel.asyncActionExecuted.observe(rootView.getLifecycleOwner(), capture(observerSlot)) } just Runs
        every { listViewIdViewModel.createSingleManagerByListViewId(any(), any()) } just Runs
        every { generateIdViewModel.getViewId(rootView.getParentId()) } returns generatedId

        listAdapter = ListAdapter(
            orientation,
            template,
            iteratorName,
            key,
            viewFactory,
            listViewModels
        )
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
    fun `Given a ListAdapter with vertical orientation When call onCreateViewHolder Then should set the correct layout params to view holder itemView`() {

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
    fun `Given a ListAdapter with horizontal orientationWhen call onCreateViewHolder Then should set the correct layout params to view holder itemView`() {
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
        listAdapter.setList(list)

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

    @Test
    fun `Given a ListAdapter When call setList with a null list Then adapter item count should be zero`() {
        val listMock = null
        val expectedItemCount = 0

        listAdapter.setList(listMock)
        val result = listAdapter.itemCount

        assertEquals(expectedItemCount, result)
    }

    @Test
    fun `Given a ListAdapter When call setList with a non null list Then adapter item count should return list size`() {
        val listMock = listOf(1, 2, 3)
        val expectedItemCount = 3

        listAdapter.setList(listMock)
        val result = listAdapter.itemCount

        assertEquals(expectedItemCount, result)
    }

    @Test
    fun `Given a ListAdapter When call setList and adapter recyclerId is -1 Then should generate a temporary id`() {
        val listMock = listOf(1, 2, 3)

        listAdapter.setList(listMock)

        verify(exactly = 1) {
            generateIdViewModel.getViewId(rootView.getParentId())
        }
    }

    @Test
    fun `Given a ListAdapter When call setList and adapter recyclerId is different of -1 Then should not generate a temporary id`() {
        val listMock = listOf(1, 2, 3)
        listAdapter.setRecyclerId(1)

        listAdapter.setList(listMock)

        verify(exactly = 0) {
            generateIdViewModel.getViewId(rootView.getParentId())
        }
    }

    @Test
    fun `Given a ListAdapter When call setList with a non null list Then should notify ListViewIdViewModel`() {
        val listMock = listOf(1, 2, 3)
        val recyclerIdMock = 1
        listAdapter.setRecyclerId(recyclerIdMock)

        listAdapter.setList(listMock)

        verify(exactly = 1) {
            listViewIdViewModel
                .createSingleManagerByListViewId(recyclerIdMock, true)
        }
    }

    @Test
    fun `Given a ListAdapter When call setRecyclerId with value different of -1 Then should change adapter recyclerId`() {
        val listMock = listOf(1, 2, 3)
        val recyclerIdMock = 1
        listAdapter.setRecyclerId(recyclerIdMock)

        listAdapter.setList(listMock)

        verify(exactly = 1) {
            listViewIdViewModel
                .createSingleManagerByListViewId(recyclerIdMock, true)
        }
    }

    @Test
    fun `Given a ListAdapter When call setRecyclerId with value -1 Then should not change adapter recyclerId`() {
        val listMock = listOf(1, 2, 3)
        val previousRecyclerIdMock = 1
        val newRecyclerIdMock = -1
        listAdapter.setRecyclerId(previousRecyclerIdMock)

        listAdapter.setRecyclerId(newRecyclerIdMock)
        listAdapter.setList(listMock)

        verify(exactly = 1) {
            listViewIdViewModel
                .createSingleManagerByListViewId(previousRecyclerIdMock, true)
        }
    }

    @Test
    fun `Given a ListAdapter When a view holder itemView call an async action and action has status started Then should make holder not recyclable`() {

        val holder = listAdapter.onCreateViewHolder(viewGroupMock, viewTypeMock)

        asyncActionViewModel.onAsyncActionExecuted(asyncActionDataMock)
        asyncActionMock.status.value = AsyncActionStatus.STARTED

        verify(exactly = 1) {
            holder.setIsRecyclable(false)
        }
    }

    @Test
    fun `Given a ListAdapter When a view holder itemView call an async action and action has status finished Then should make holder recyclable`() {
        val holder = listAdapter.onCreateViewHolder(viewGroupMock, viewTypeMock)

        asyncActionViewModel.onAsyncActionExecuted(asyncActionDataMock)
        asyncActionMock.status.value = AsyncActionStatus.STARTED
        asyncActionMock.status.value = AsyncActionStatus.FINISHED

        verify(exactly = 1) {
            holder.setIsRecyclable(false)
        }
    }
}