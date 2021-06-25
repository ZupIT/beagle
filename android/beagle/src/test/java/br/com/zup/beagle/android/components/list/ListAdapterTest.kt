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
import android.widget.LinearLayout
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.action.AsyncActionImpl
import br.com.zup.beagle.android.action.AsyncActionStatus
import br.com.zup.beagle.android.components.Button
import br.com.zup.beagle.android.components.Image
import br.com.zup.beagle.android.components.ImagePath
import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.components.utils.Template
import br.com.zup.beagle.android.context.AsyncActionData
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.expressionOf
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
import io.mockk.mockkObject
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
    private val template by lazy { Container(children = listOf(Button(text = "test"))) }
    private val iteratorName = "iteratorName"
    private val key = "id"
    private val generatedId = 10
    private val list = listOf("stub 1", "stub 2")
    private val listViewModels = mockk<ListViewModels>()
    private val asyncActionViewModel = mockk<AsyncActionViewModel>()
    private val contextViewModel = mockk<ScreenContextViewModel>(relaxed = true)
    private val listViewIdViewModel = mockk<ListViewIdViewModel>()
    private val generateIdViewModel = mockk<GenerateIdViewModel>()
    private val observerSlot = slot<Observer<AsyncActionData>>()
    private val templateList by lazy {
        listOf(
            Template(
                case = expressionOf("@{eq(item, 'stub 1')}"),
                view = Container(children = listOf(Text(text = "test")))
            ),
            Template(
                case = expressionOf("@{eq(item, 'stub 2')}"),
                view = Text(text = "test")
            ),
            Template(
                case = null,
                view = Button(text = "button test")
            ),
            Template(
                case = expressionOf("@{eq(item, 'stub 5')}"),
                view = Image(ImagePath.Remote(url = "test"))
            ),
        )
    }

    private lateinit var listAdapter: ListAdapter

    @Before
    override fun setUp() {
        super.setUp()

        mockkConstructor(ListViewHolder::class)
        mockkObject(ViewFactory)
        every { viewHolderItemView.parent } returns recyclerViewMock
        every { ViewFactory.makeBeagleFlexView(rootView) } returns viewHolderItemView
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
            listViewModels,
            templateList,
            recyclerViewMock
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
            listViewModels,
            templateList,
            recyclerViewMock
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
            listViewModels,
            templateList,
            recyclerViewMock
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
    fun `Given a ListAdapter When call getItemViewType Then should evaluate context only once for each adapter position`() {
        val subject = ListAdapter(
            RecyclerView.VERTICAL,
            template,
            iteratorName,
            key,
            listViewModels,
            templateList,
            recyclerViewMock
        )

        every { listViewModels.contextViewModel.evaluateExpressionForGivenContext(recyclerViewMock, any(), templateList[0].case as Bind.Expression<*>) } returns true

        subject.setList(list)

        subject.getItemViewType(0)
        subject.getItemViewType(0)

        verify(exactly = 1) { listViewModels.contextViewModel.evaluateExpressionForGivenContext(recyclerViewMock, any(), templateList[0].case as Bind.Expression<*>) }
    }

    @Test
    fun `Given a ListAdapter When call getItemViewType after calling setList Then should evaluate context for each adapter position again`() {
        val newList = listOf("stub 3", "stub 4")
        val subject = ListAdapter(
            RecyclerView.VERTICAL,
            template,
            iteratorName,
            key,
            listViewModels,
            templateList,
            recyclerViewMock
        )

        every { listViewModels.contextViewModel.evaluateExpressionForGivenContext(recyclerViewMock, any(), templateList[0].case as Bind.Expression<*>) } returns true

        subject.setList(list)

        subject.getItemViewType(0)
        subject.setList(newList)
        subject.getItemViewType(0)

        verify(exactly = 2) { listViewModels.contextViewModel.evaluateExpressionForGivenContext(recyclerViewMock, any(), templateList[0].case as Bind.Expression<*>) }
    }

    @Test
    fun `Given a ListAdapter When call getItemViewType Then should get the correct default template index`() {
        val expectedIndex = 2
        val subject = ListAdapter(
            RecyclerView.VERTICAL,
            template,
            iteratorName,
            key,
            listViewModels,
            templateList,
            recyclerViewMock
        )

        every { listViewModels.contextViewModel.evaluateExpressionForGivenContext(recyclerViewMock, any(), any()) } returns false

        subject.setList(list)

        val result = subject.getItemViewType(0)

        assertEquals(expectedIndex, result)
    }

    @Test
    fun `Given a ListAdapter When call getItemViewType Then should return -1 when there is no default template`() {
        val expectedIndex = -1
        val templateList = listOf(
            Template(
                case = expressionOf("@{eq(item, 'stub 1')}"),
                view = Container(children = listOf())
            ),
            Template(
                case = expressionOf("@{eq(item, 'stub 2')}"),
                view = Container(children = listOf())
            ),
            Template(
                case = expressionOf("@{eq(item, 'stub 5')}"),
                view = Container(children = listOf())
            ),
        )
        val subject = ListAdapter(
            RecyclerView.VERTICAL,
            template,
            iteratorName,
            key,
            listViewModels,
            templateList,
            recyclerViewMock
        )

        every { listViewModels.contextViewModel.evaluateExpressionForGivenContext(recyclerViewMock, any(), any()) } returns false

        subject.setList(list)

        val result = subject.getItemViewType(0)

        assertEquals(expectedIndex, result)
    }

    @Test
    fun `Given a ListAdapter When call onCreateViewHolder Then should create the correct template`() {
        val expectedTemplate = Text(text = "test")
        val expectedViewType = 1
        val subject = ListAdapter(
            RecyclerView.VERTICAL,
            template,
            iteratorName,
            key,
            listViewModels,
            templateList,
            recyclerViewMock,
        )

        every { listViewModels.contextViewModel.evaluateExpressionForGivenContext(recyclerViewMock, any(), templateList[0].case as Bind.Expression<*>) } returns false
        every { listViewModels.contextViewModel.evaluateExpressionForGivenContext(recyclerViewMock, any(), templateList[1].case as Bind.Expression<*>) } returns true

        subject.setList(list)

        val viewType = subject.getItemViewType(0)
        val holder = subject.onCreateViewHolder(viewGroupMock, viewType)

        assertEquals(expectedViewType, viewType)
        assertEquals(expectedTemplate, holder.getTemplate())

    }

    @Test
    fun `Given a ListAdapter When call onCreateViewHolder Then should create the default template`() {
        val expectedTemplate = Button(text = "button test")
        val expectedViewType = 2
        val subject = ListAdapter(
            RecyclerView.VERTICAL,
            template,
            iteratorName,
            key,
            listViewModels,
            templateList,
            recyclerViewMock,
        )

        every { listViewModels.contextViewModel.evaluateExpressionForGivenContext(recyclerViewMock, any(), any()) } returns false

        subject.setList(list)
        val viewType = subject.getItemViewType(0)
        val holder = subject.onCreateViewHolder(viewGroupMock, viewType)

        assertEquals(expectedViewType, viewType)
        assertEquals(expectedTemplate, holder.getTemplate())
    }

    @Test
    fun `Given a ListAdapter with a null template list When call onCreateViewHolder Then should create the template`() {
        val expectedTemplate = Container(children = listOf(Button(text = "test")))
        val expectedViewType = -1
        val subject = ListAdapter(
            RecyclerView.VERTICAL,
            template,
            iteratorName,
            key,
            listViewModels,
            null,
            recyclerViewMock,
        )

        every { listViewModels.contextViewModel.evaluateExpressionForGivenContext(recyclerViewMock, any(), any()) } returns false

        subject.setList(list)
        val viewType = subject.getItemViewType(0)
        val holder = subject.onCreateViewHolder(viewGroupMock, viewType)

        assertEquals(expectedViewType, viewType)
        assertEquals(expectedTemplate, holder.getTemplate())
    }

    @Test
    fun `Given a ListAdapter When call onCreateViewHolder Then should create the non existent default template`() {
        val expectedTemplate = Container()
        val expectedViewType = -1
        val templateListWithNoDefault = listOf(
            Template(
                case = expressionOf("@{eq(item, 'stub 1')}"),
                view = Container(children = listOf(Text(text = "test")))
            ),
        )
        val subject = ListAdapter(
            RecyclerView.VERTICAL,
            null,
            iteratorName,
            key,
            listViewModels,
            templateListWithNoDefault,
            recyclerViewMock,
        )

        every { listViewModels.contextViewModel.evaluateExpressionForGivenContext(recyclerViewMock, any(), any()) } returns false

        subject.setList(list)
        val viewType = subject.getItemViewType(0)
        val holder = subject.onCreateViewHolder(viewGroupMock, viewType)

        assertEquals(expectedViewType, viewType)
        assertEquals(expectedTemplate, holder.getTemplate())
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

        verify(exactly = 1) {
            holder.setIsRecyclable(true)
        }
    }

    @Test
    fun `Given a ListAdapter When a view that is not inside a RecyclerView executes an async action Then holder should not observe that action`() {
        val viewGroup = LinearLayout(ApplicationProvider.getApplicationContext())
        val view = View(ApplicationProvider.getApplicationContext())
        viewGroup.addView(view)
        val asyncActionMock = mockk<AsyncActionImpl>(relaxed = true)
        val asyncActionDataMock = AsyncActionData(
            view, asyncActionMock
        )
        val holderObserverSlot = slot<Observer<AsyncActionStatus>>()
        val holder = listAdapter.onCreateViewHolder(viewGroupMock, viewTypeMock)
        every { holder.observer = capture(holderObserverSlot) } just Runs

        asyncActionViewModel.onAsyncActionExecuted(asyncActionDataMock)

        verify(exactly = 0) { holder.observer = capture(holderObserverSlot) }
    }

    @Test
    fun `Given a ListAdapter When a view with a null parent executes an async action Then holder should not observe that action`() {
        val viewMock = mockk<View>(relaxed = true)
        val asyncActionMock = mockk<AsyncActionImpl>(relaxed = true)
        val asyncActionDataMock = AsyncActionData(
            viewMock, asyncActionMock
        )
        val holderObserverSlot = slot<Observer<AsyncActionStatus>>()
        val holder = listAdapter.onCreateViewHolder(viewGroupMock, viewTypeMock)
        every { holder.observer = capture(holderObserverSlot) } just Runs
        every { viewMock.parent } returns null

        asyncActionViewModel.onAsyncActionExecuted(asyncActionDataMock)

        verify(exactly = 0) { holder.observer = capture(holderObserverSlot) }
    }

    @Test
    fun `Given a ListAdapter When a view executes an async action and there is no ViewHolder created for the adapter Then action must not be observed`() {
        val asyncActionMock = mockk<AsyncActionImpl>(relaxed = true)
        val asyncActionDataMock = AsyncActionData(
            viewHolderItemView, asyncActionMock
        )

        asyncActionViewModel.onAsyncActionExecuted(asyncActionDataMock)

        verify(exactly = 0) { asyncActionMock.status.observe(any(), any()) }
    }

    @Test
    fun `Given a ListAdapter When a view executes an async action and that view is not related to adapter ViewHolders Then action must not be observed`() {
        val viewMock = mockk<View>(relaxed = true)
        val asyncActionMock = mockk<AsyncActionImpl>(relaxed = true)
        val asyncActionDataMock = AsyncActionData(
            viewMock, asyncActionMock
        )
        every { viewMock.id } returns 999
        every { viewMock.parent } returns recyclerViewMock

        listAdapter.onCreateViewHolder(viewGroupMock, viewTypeMock)

        asyncActionViewModel.onAsyncActionExecuted(asyncActionDataMock)

        verify(exactly = 0) { asyncActionMock.status.observe(any(), any()) }
    }

    @Test
    fun `Given a ListAdapter When call clone Then cloned adapter should have exactly the same properties of source adapter`() {
        val adapterCopy = listAdapter.clone()

        assertEquals(listAdapter.orientation, adapterCopy.orientation)
        assertEquals(listAdapter.template, adapterCopy.template)
        assertEquals(listAdapter.iteratorName, adapterCopy.iteratorName)
        assertEquals(listAdapter.key, adapterCopy.key)
        assertEquals(listAdapter.listViewModels, adapterCopy.listViewModels)
        assertEquals(listAdapter.templateList, adapterCopy.templateList)
        assertEquals(listAdapter.originView, adapterCopy.originView)
    }
}